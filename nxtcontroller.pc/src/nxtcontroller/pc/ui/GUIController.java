package nxtcontroller.pc.ui;

/**
 * @author Max Leuthäuser
 * 
 */

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;

import nxtcontroller.pc.controller.ControllerTyp;
import nxtcontroller.pc.controller.DeviceHandler;

public class GUIController {
	private DeviceHandler dh;
	private GUIBuilder guiBuilder = GUIBuilder.getInstance();
	private boolean isInFullScreenMode = false;

	private static GUIController instance = new GUIController();

	private GUIController() {
	}

	public static GUIController getInstance() {
		return instance;
	}

	public void init() {
		guiBuilder.buildInterface();
		dh = new DeviceHandler(guiBuilder.getMainFrame(), guiBuilder
				.getMainFrame().getRootPane());
		dh.setHandler(ControllerTyp.types[0]);
	}
	
	public DeviceHandler getDeviceHandler() {
		return dh;
	}
	
	public Point getDisplaySize() {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		DisplayMode dm = gs.getDisplayMode();
		return new Point(dm.getWidth(), dm.getHeight());
	}

	public void toggleFullScreen() {
		if (isInFullScreenMode) {
			isInFullScreenMode = false;
			guiBuilder.restoreAppControl();
			guiBuilder.getMainFrame().setLayout(new GridLayout(2, 0));
			guiBuilder.getMainFrame().restore();
		} else {
			isInFullScreenMode = true;
			guiBuilder.removeAppControl();
			guiBuilder.getMainFrame().setLayout(new GridLayout(1, 0));
			guiBuilder.getMainFrame().setMaximized();
		}
	}
}
