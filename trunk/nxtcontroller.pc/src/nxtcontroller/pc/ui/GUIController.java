package nxtcontroller.pc.ui;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import nxtcontroller.pc.controller.ControllerTyp;
import nxtcontroller.pc.controller.DeviceHandler;

/**
 * Class which controls all GUI elements and allows to toggle the fullscreen
 * mode.
 * 
 * @author Max Leuthäuser
 */
public class GUIController {
	private DeviceHandler dh;
	private GUIBuilder guiBuilder = GUIBuilder.getInstance();
	private boolean isInFullScreenMode = false;

	private static GUIController instance = new GUIController();

	private GUIController() {
	}

	/**
	 * @return an instance of the class {@link GUIController}
	 */
	public static GUIController getInstance() {
		return instance;
	}

	/**
	 * Use the {@link GUIBuilder} to create the interface and set the default
	 * input device handler. (keyboard)
	 */
	public void init() {
		guiBuilder.buildInterface();
		dh = new DeviceHandler(guiBuilder.getMainFrame(), guiBuilder
				.getMainFrame().getRootPane());
		dh.setHandler(ControllerTyp.getTypes()[0]);
	}

	/**
	 * @return the {@link DeviceHandler}
	 */
	public DeviceHandler getDeviceHandler() {
		return dh;
	}

	/**
	 * @return <b>true</b> if the application is in fullscreen mode,
	 *         <b>false</b> otherwise.
	 */
	public boolean isInFullScreenMode() {
		return isInFullScreenMode;
	}

	/**
	 * @return the display size of the main screen at this computer.
	 */
	public Point getDisplaySize() {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		DisplayMode dm = gs.getDisplayMode();
		return new Point(dm.getWidth(), dm.getHeight());
	}

	/**
	 * Toggle the fullscreen mode. The {@link ApplicationControlPanel} is
	 * removed while staying in fullscreen.
	 */
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
