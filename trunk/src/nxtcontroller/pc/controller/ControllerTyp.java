package nxtcontroller.pc.controller;

import nxtcontroller.pc.ui.UILanguage;

/**
 * This class holds the supported controller devices as simple string
 * representation.
 * 
 * @author Max Leuthäuser
 */
public class ControllerTyp {
	/**
	 * @return an array with 2 elements. <b>0</b> means keyboard, <b>1</b>
	 *         Gamepad.
	 */
	public static String[] getTypes() {
		String[] result = { UILanguage.getKeyboard(), UILanguage.getGamepad() };
		return result;
	}
}
