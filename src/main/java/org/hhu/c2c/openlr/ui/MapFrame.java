package org.hhu.c2c.openlr.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Creates a simple frame containing the {@link MapPanel}.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public class MapFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public MapFrame() {
		setSize(700, 700);
		setTitle("Map");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 2 - getWidth() / 2, size.height / 2
				- getHeight() / 2);

		JPanel panel = new MapPanel();

		MouseMotionListener mouseMotionListener = new MapMouseMotionListener();
		panel.addMouseMotionListener(mouseMotionListener);
		getContentPane().add(panel);
	}
}
