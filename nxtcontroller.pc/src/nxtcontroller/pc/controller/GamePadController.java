package nxtcontroller.pc.controller;

import net.java.games.input.*;
import net.java.games.input.Component.POV;
import nxtcontroller.pc.ui.LogOperation;
import nxtcontroller.pc.ui.UILanguage;

/**
 * Class which interact with any USB device which is connected. In this
 * application its used to get gamepads only.
 * 
 * @author Max Leuthäuser
 */
public class GamePadController {

	public static final int NUM_BUTTONS = 12;
	// public stick and hat compass positions
	public static final int NUM_COMPASS_DIRS = 9;
	public static final int NW = 0;
	public static final int NORTH = 1;
	public static final int NE = 2;
	public static final int WEST = 3;
	public static final int NONE = 4; // default value
	public static final int EAST = 5;
	public static final int SW = 6;
	public static final int SOUTH = 7;
	public static final int SE = 8;
	private Controller controller;
	private Component[] comps; // holds the components
	// comps[] indices for specific components
	private int xAxisIdx, yAxisIdx, zAxisIdx, rzAxisIdx;
	// indices for the analog sticks axes
	private int povIdx; // index for the POV hat
	private int buttonsIdx[]; // indices for the buttons
	private Rumbler[] rumblers;
	private int rumblerIdx; // index for the rumbler being used
	private boolean rumblerOn = false; // whether rumbler is on or off
	public boolean gamePadIsAvailable = true;

	public GamePadController() {
		// get the controllers
		ControllerEnvironment ce = ControllerEnvironment
				.getDefaultEnvironment();
		Controller[] cs = ce.getControllers();
		// get the game pad controller
		controller = findGamePad(cs);
		if (controller != null) {
			LogOperation.writeAppLog(UILanguage.getGamepad() + ": "
					+ controller.getName() + ", " + controller.getType());

			// collect indices for the required game pad components
			findCompIndices(controller);

			findRumblers(controller);
		} else
			this.gamePadIsAvailable = false;
	} // end of GamePadController()

	/*
	 * Search the array of controllers until a suitable game pad controller is
	 * found (eith of type GAMEPAD or STICK).
	 */
	private Controller findGamePad(Controller[] cs) {
		Controller.Type type;
		int i = 0;
		while (i < cs.length) {
			type = cs[i].getType();
			if ((type == Controller.Type.GAMEPAD)
					|| (type == Controller.Type.STICK)) {
				break;
			}
			i++;
		}

		if (i == cs.length) {
			LogOperation.writeAppLog(UILanguage.getNoGamepadFound());
			return null;
		} else {
			LogOperation.writeAppLog(UILanguage.getGamepadChoosen() + i);
		}

		return cs[i];
	} // end of findGamePad()

	/*
	 * Store the indices for the analog sticks axes (x,y) and (z,rz), POV hat,
	 * and button components of the controller.
	 */
	private void findCompIndices(Controller controller) {
		comps = controller.getComponents();
		// get the indices for the axes of the analog sticks: (x,y) and (z,rz)
		xAxisIdx = findCompIndex(comps, Component.Identifier.Axis.X, "x-axis");
		yAxisIdx = findCompIndex(comps, Component.Identifier.Axis.Y, "y-axis");

		zAxisIdx = findCompIndex(comps, Component.Identifier.Axis.Z, "z-axis");
		rzAxisIdx = findCompIndex(comps, Component.Identifier.Axis.RZ,
				"rz-axis");

		// get POV hat index
		povIdx = findCompIndex(comps, Component.Identifier.Axis.POV, "POV hat");

		findButtons(comps);
	} // end of findCompIndices()

	/*
	 * Search through comps[] for id, returning the corresponding array index,
	 * or -1
	 */
	private int findCompIndex(Component[] comps, Component.Identifier id,
			String nm) {
		Component c;
		for (int i = 0; i < comps.length; i++) {
			c = comps[i];
			if ((c.getIdentifier() == id) && !c.isRelative()) {
				return i;
			}
		}
		return -1;
	} // end of findCompIndex()

	/*
	 * Search through comps[] for NUM_BUTTONS buttons, storing their indices in
	 * buttonsIdx[]. Ignore excessive buttons. If there aren't enough buttons,
	 * then fill the empty spots in buttonsIdx[] with -1's.
	 */
	private void findButtons(Component[] comps) {
		buttonsIdx = new int[NUM_BUTTONS];
		int numButtons = 0;
		Component c;

		for (int i = 0; i < comps.length; i++) {
			c = comps[i];
			if (isButton(c)) { // deal with a button
				if (numButtons != NUM_BUTTONS) {
					buttonsIdx[numButtons] = i; // store button index
					numButtons++;
				}
			}
		}

		// fill empty spots in buttonsIdx[] with -1's
		if (numButtons < NUM_BUTTONS) {
			while (numButtons < NUM_BUTTONS) {
				buttonsIdx[numButtons] = -1;
				numButtons++;
			}
		}
	} // end of findButtons()

	/*
	 * Return true if the component is a digital/absolute button, and its
	 * identifier name ends with "Button" (i.e. the identifier class is
	 * Component.Identifier.Button).
	 */
	private boolean isButton(Component c) {
		if (!c.isAnalog() && !c.isRelative()) { // digital and absolute
			String className = c.getIdentifier().getClass().getName();
			// System.out.println(c.getName() + " identifier: " + className);
			if (className.endsWith("Button")) {
				return true;
			}
		}
		return false;
	} // end of isButton()

	/*
	 * Find the rumblers. Use the last rumbler for making vibrations, an
	 * arbitrary decision.
	 */
	private void findRumblers(Controller controller) {
		// get the game pad's rumblers
		rumblers = controller.getRumblers();
		if (rumblers.length == 0) {
			rumblerIdx = -1;
		} else {
			rumblerIdx = rumblers.length - 1; // use last rumbler
		}
	} // end of findRumblers()

	// ----------------- polling and getting data ------------------
	public boolean poll() // update the component values in the controller
	{
		return controller.poll();
	}

	public int getXYStickDir() // return the (x,y) analog stick compass
	// direction
	{
		if ((xAxisIdx == -1) || (yAxisIdx == -1)) {
			LogOperation
					.writeAppLog(UILanguage.getGamepadDirectionDataUnavailable());
			return NONE;
		} else {
			return getCompassDir(xAxisIdx, yAxisIdx);
		}
	} // end of getXYStickDir()

	public int getZRZStickDir() // return the (z,rz) analog stick compass
	// direction
	{
		if ((zAxisIdx == -1) || (rzAxisIdx == -1)) {
			LogOperation
					.writeAppLog(UILanguage.getGamepadDirectionDataUnavailable());
			return NONE;
		} else {
			return getCompassDir(zAxisIdx, rzAxisIdx);
		}
	} // end of getXYStickDir()

	private int getCompassDir(int xA, int yA) // Return the axes as a single
	// compass value
	{
		float xCoord = comps[xA].getPollData();
		float yCoord = comps[yA].getPollData();
		// System.out.println("(x,y): (" + xCoord + "," + yCoord + ")");

		int xc = Math.round(xCoord);
		int yc = Math.round(yCoord);
		// System.out.println("Rounded (x,y): (" + xc + "," + yc + ")");

		if ((yc == -1) && (xc == -1)) // (y,x)
		{
			return NW;
		} else if ((yc == -1) && (xc == 0)) {
			return NORTH;
		} else if ((yc == -1) && (xc == 1)) {
			return NE;
		} else if ((yc == 0) && (xc == -1)) {
			return WEST;
		} else if ((yc == 0) && (xc == 0)) {
			return NONE;
		} else if ((yc == 0) && (xc == 1)) {
			return EAST;
		} else if ((yc == 1) && (xc == -1)) {
			return SW;
		} else if ((yc == 1) && (xc == 0)) {
			return SOUTH;
		} else if ((yc == 1) && (xc == 1)) {
			return SE;
		} else {
			return NONE;
		}
	} // end of getCompassDir()

	public int getHatDir() // Return the POV hat's direction as a compass
	// direction
	{
		if (povIdx == -1) {
			LogOperation
					.writeAppLog(UILanguage.getGamepadDirectionDataUnavailable());
			return NONE;
		} else {
			float povDir = comps[povIdx].getPollData();
			if (povDir == POV.CENTER) // 0.0f
			{
				return NONE;
			} else if (povDir == POV.DOWN) // 0.75f
			{
				return SOUTH;
			} else if (povDir == POV.DOWN_LEFT) // 0.875f
			{
				return SW;
			} else if (povDir == POV.DOWN_RIGHT) // 0.625f
			{
				return SE;
			} else if (povDir == POV.LEFT) // 1.0f
			{
				return WEST;
			} else if (povDir == POV.RIGHT) // 0.5f
			{
				return EAST;
			} else if (povDir == POV.UP) // 0.25f
			{
				return NORTH;
			} else if (povDir == POV.UP_LEFT) // 0.125f
			{
				return NW;
			} else if (povDir == POV.UP_RIGHT) // 0.375f
			{
				return NE;
			} else { // assume center
				LogOperation
						.writeAppLog(UILanguage.getGamepadDirectionDataUnavailable());
				return NONE;
			}
		}
	} // end of getHatDir()

	/*
	 * Return all the buttons in a single array. Each button value is a boolean.
	 */
	public boolean[] getButtons() {
		boolean[] buttons = new boolean[NUM_BUTTONS];
		float value;
		for (int i = 0; i < NUM_BUTTONS; i++) {
			value = comps[buttonsIdx[i]].getPollData();
			buttons[i] = ((value == 0.0f) ? false : true);
		}
		return buttons;
	} // end of getButtons()

	/*
	 * Return the button value (a boolean) for button number 'pos'. pos is in
	 * the range 1-NUM_BUTTONS to match the game pad button labels.
	 */
	public boolean isButtonPressed(int pos) {
		if ((pos < 1) || (pos > NUM_BUTTONS)) {
			return false;
		}

		if (buttonsIdx[pos - 1] == -1) // no button found at that pos
		{
			return false;
		}

		float value = comps[buttonsIdx[pos - 1]].getPollData();
		// array range is 0-NUM_BUTTONS-1
		return ((value == 0.0f) ? false : true);
	} // end of isButtonPressed()

	// ------------------- trigger a rumbler -------------------
	public void setRumbler(boolean switchOn) // turn the rumbler on or off
	{
		if (rumblerIdx != -1) {
			if (switchOn) {
				rumblers[rumblerIdx].rumble(0.8f); // almost full on for last
				// rumbler
			} else // switch off
			{
				rumblers[rumblerIdx].rumble(0.0f);
			}
			rumblerOn = switchOn; // record rumbler's new status
		}
	} // end of setRumbler()

	public boolean isRumblerOn() {
		return rumblerOn;
	}
} // end of GamePadController class

