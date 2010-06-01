package nxtcontroller.pc.controller;

import javax.swing.JRootPane;

import nxtcontroller.pc.ui.MainFrame;

/**
 * Class which allows to switch to another input device.
 * 
 * @author Max Leuthäuser
 */
public class DeviceHandler {
	private KeyboardHandler kh;
	private GamepadHandler gh;

	public DeviceHandler(MainFrame mainFrame, JRootPane rootPane) {
		kh = new KeyboardHandler(mainFrame, rootPane);
		gh = new GamepadHandler(mainFrame, rootPane);
	}

	/**
	 * 
	 * @param device
	 *            which represents the new device which will control the NXT
	 *            after switching.
	 */
	public void setHandler(final String device) {
		if (device.equals(ControllerTyp.getTypes()[0])) {
			if (kh.attach()) {
				gh.destroy();
			}
		}
		if (device.equals(ControllerTyp.getTypes()[1])) {
			if (gh.attach()) {
				kh.destroy();
			}
		}
	}
	
	/**
	 * @return {@link KeyboardHandler}
	 */
	public KeyboardHandler getKeyboardHandler() {
		return this.kh;
	}
	
	/**
	 * @return {@link GamepadHandler}
	 */
	public GamepadHandler getGamepadHandler() {
		return this.gh;
	}
}
