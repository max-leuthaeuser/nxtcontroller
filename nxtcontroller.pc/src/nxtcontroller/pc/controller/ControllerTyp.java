package nxtcontroller.pc.controller;

import nxtcontroller.pc.ui.UILanguage;

/**
 * This class holds the supported
 * controller devices as simple string representation.
 * 
 * @author Max Leuthäuser
 */
public final class ControllerTyp {
	/**
	 * Contains the supported controller devices.
	 * <p>
	 * types[0] means 'keyboard' / types[1] means 'gamepad'
	 * </p>
	 */
	public final static String[] types = { UILanguage.KEYBOARD,
			UILanguage.GAMEPAD };
}
