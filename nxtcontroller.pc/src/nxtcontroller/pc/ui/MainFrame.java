package nxtcontroller.pc.ui;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import javax.swing.*;

/**
 * This class is the main frame of the application. It contains all GUI
 * elements.
 * 
 * @author Max Leuthäuser
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 3083232085671490610L;

	public MainFrame() {
		super();
		setTitle("NXTController - Lange Nacht der Wissenschaften (TU-Dresden)");
		Dimension dimension = new Dimension(StaticSizes.APPLICATION_SIZE_WIDTH,
				StaticSizes.APPLICATION_SIZE_HEIGTH);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		restore();
	}

	/**
	 * @return the standard position at the center of the screen.
	 */
	private Point getStandardPosition() {
		return new Point(GUIController.getInstance().getDisplaySize().x / 2
				- (StaticSizes.APPLICATION_SIZE_WIDTH / 2), GUIController
				.getInstance().getDisplaySize().y
				/ 2 - (StaticSizes.APPLICATION_SIZE_HEIGTH / 2));
	}

	/**
	 * Set the standard size and position.
	 */
	public void restore() {
		dispose();
		setBounds(this.getStandardPosition().x, this.getStandardPosition().y,
				StaticSizes.APPLICATION_SIZE_WIDTH,
				StaticSizes.APPLICATION_SIZE_HEIGTH);
		setUndecorated(false);
		setVisible(true);
		repaint();
	}

	/**
	 * Sets this frame to fullscreen.
	 */
	public void setMaximized() {
		GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getScreenDevices()[0];
		dispose();
		setUndecorated(true);
		if (device.isFullScreenSupported()) {
			// Full-screen mode
			device.setFullScreenWindow(this);
			validate();
			repaint();
			setVisible(true);
		}
	}
}