package nxtcontroller.pc.ui;

/**
 * @author Max Leuthäuser
 *
 */

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.*;

import nxtcontroller.pc.controller.ControllerTyp;
import nxtcontroller.pc.controller.DeviceHandler;

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

	private Point getStandardPosition() {
		return new Point(GUIController.getInstance().getDisplaySize().x / 2
				- (StaticSizes.APPLICATION_SIZE_WIDTH / 2), GUIController
				.getInstance().getDisplaySize().y
				/ 2 - (StaticSizes.APPLICATION_SIZE_HEIGTH / 2));
	}

	public void restore() {
		setVisible(false);
		// we need dispose() on linux only
		if (System.getProperty("os.name").startsWith("Linux")) {
			dispose();
		}
		setBounds(this.getStandardPosition().x, this.getStandardPosition().y,
				StaticSizes.APPLICATION_SIZE_WIDTH,
				StaticSizes.APPLICATION_SIZE_HEIGTH);

		repaint();
		setVisible(true);
	}

	public void setMaximized() {
		setVisible(false);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		GraphicsDevice device;
		device = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getScreenDevices()[0];
		device.setFullScreenWindow(this);
		setBounds(-1, -1, device.getDisplayMode().getWidth(), device
				.getDisplayMode().getWidth());
		repaint();
		setVisible(true);
	}
}