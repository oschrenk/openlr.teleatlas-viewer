package org.hhu.c2c.openlr.ui;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public class MapMouseMotionListener implements MouseMotionListener {

	private PathManager pathManager;

	public MapMouseMotionListener() {
		this.pathManager = PathManager.getInstance();
	}

	public void mouseDragged(MouseEvent e) {
		showMousePos(e);
	}

	public void mouseMoved(MouseEvent e) {
		showMousePos(e);
	}

	private void showMousePos(MouseEvent e) {
		PointerInfo pointerInfo = MouseInfo.getPointerInfo();
		Point mousePoint = pointerInfo.getLocation();
		JPanel panel = (JPanel) e.getComponent();

		Point panelLeftUpperCorner = panel.getLocationOnScreen();

		if (pathManager.intersect(mousePoint.x - panelLeftUpperCorner.x,
				mousePoint.y - panelLeftUpperCorner.y)) {
			System.out.println("x=" + (mousePoint.x - panelLeftUpperCorner.x)
					+ ",y=" + (mousePoint.y - panelLeftUpperCorner.y));
		}

	}
}
