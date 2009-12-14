package org.hhu.c2c.openlr.ui;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Views the sample database provided by TeleAtlas via <a
 * href="http://openlr.org/testdata.html">openlr.org/testdata</a>
 * 
 * Unfortunately one can not include the testdata in a release as it is only
 * available after conmfirming a licence agreement. Please obtain the data from
 * the link given above and put the database files in
 * <code>src/main/resources</code> directory and start the application with the
 * appropiate filename (preceded by a slash)
 * 
 * Usage
 * 
 * <pre>
 * 	$ TeleAtlasViewer /database.db3
 * </pre>
 * 
 * The application will render the given vendor map.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public class TeleAtlasViewer {

	private static final long serialVersionUID = 1L;

	private final JFrame frame;

	public TeleAtlasViewer(String relativePathToDatabase) {
		File path = new File(this.getClass()
				.getResource(relativePathToDatabase).getFile());
		if (!path.exists() || !path.canRead()) {
			System.err.println("Usage: TeleAtlasViewer /database.db3");
			System.exit(1);
		}
		frame = new MapFrame();
		PathManager pathManager = PathManager.getInstance();
		pathManager.connect(path);
		pathManager.readSource();
	}

	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
			}
		});
	}

	public static void main(String[] args) {
		if (args == null || args.length != 1) {
			System.err.println("Usage: TeleAtlasViewer /database.db3");
			System.exit(1);
		}

		new TeleAtlasViewer(args[0]).run();
	}
}