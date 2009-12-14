package org.hhu.c2c.openlr.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * This panel shows the drawn map.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public class MapPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final PathManager pathManager;

	protected MapPanel() {
		pathManager = PathManager.getInstance();
		pathManager.setParent(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		pathManager.transform();
		g2d.draw(pathManager.getPath());

	}
}