package org.hhu.c2c.openlr.ui;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;

import org.hhu.c2c.openlr.gis.LineStringHelper;

import com.vividsolutions.jts.geom.LineString;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public class PathManager {

	private static final int VIRTUAL_MOUSE_POINTER_HEIGHT = 4;

	private static final int VIRTUAL_MOUSE_POINTER_WIDTH = 4;

	private static PathManager instance;

	private Connection connection;

	private GeneralPath path;

	private JPanel parent;

	private PathManager() {
		path = new GeneralPath();
	}

	public void transform() {
		transform(path, parent.getWidth(), parent.getHeight());
	}

	private void transform(GeneralPath path, int windowWidth, int windowHeight) {
		Rectangle2D bounds = path.getBounds2D();

		AffineTransform translateTransform = getTranslation(-bounds.getMinX(),
				-bounds.getMinY());

		path.transform(translateTransform);

		AffineTransform scaleTransform = getScale(windowWidth
				/ bounds.getWidth(), windowHeight / bounds.getHeight());
		path.transform(scaleTransform);
	}

	public void readSource() {
		this.path = readLines();
	}

	private GeneralPath readLines() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		GeneralPath path = new GeneralPath();
		LineString lineString = null;

		try {
			ps = connection.prepareStatement("SELECT GEOM FROM LINE");
			rs = ps.executeQuery();

			while (rs.next()) {
				lineString = LineStringHelper.lineStringFromWKB(rs
						.getBytes("GEOM"));
				path.append(LineStringHelper.getPath2D(lineString), false);
			}

			return path;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// ignore
			}
		}

		// should not happen
		return new GeneralPath();
	}

	private Connection getDatabaseConnection(String db)
			throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		final String url = String.format("jdbc:sqlite:%s", db);
		final Connection connection = DriverManager.getConnection(url);
		connection.setReadOnly(true);
		return connection;
	}

	public static PathManager getInstance() {
		if (instance == null) {
			instance = new PathManager();
		}
		return instance;
	}

	public Shape getPath() {
		return path;
	}

	public AffineTransform getTranslation(double tx, double ty) {
		AffineTransform at = new AffineTransform();
		at.translate(tx, ty);
		return at;
	}

	public AffineTransform getScale(double tx, double ty) {
		AffineTransform at = new AffineTransform();
		at.scale(tx, ty);
		return at;
	}

	public void connect(File database) {

		try {
			this.connection = getDatabaseConnection(database.toString());
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Unable to load database driver.",
					e);
		} catch (SQLException e) {
			throw new IllegalStateException(
					"Unable to open database connection.", e);
		}
	}

	public void setParent(JPanel panel) {
		this.parent = panel;
	}

	public boolean intersect(int x, int y) {
		return path.intersects(getRectangle(x, y));
	}

	public Rectangle2D getRectangle(int x, int y) {
		Rectangle2D r = new Rectangle2D.Double();
		r.setFrame(x - VIRTUAL_MOUSE_POINTER_WIDTH / 2, y
				- VIRTUAL_MOUSE_POINTER_HEIGHT / 2,
				VIRTUAL_MOUSE_POINTER_WIDTH, VIRTUAL_MOUSE_POINTER_HEIGHT);

		return r;
	}

}