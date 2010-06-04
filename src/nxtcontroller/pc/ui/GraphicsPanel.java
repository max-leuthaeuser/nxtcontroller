package nxtcontroller.pc.ui;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel which shows the current state of the selected input device.
 * 
 * @author Max Leuthäuser
 */
public class GraphicsPanel extends JPanel {
	private JLabel icon;
	private static final long serialVersionUID = 8071893825743554944L;

	private ImageIcon GAMEPAD_DIR_LEFT = new ImageIcon(
			Graphics.GAMEPAD_DIR_LEFT);
	private ImageIcon GAMEPAD_POWER_UP_DIR_LEFT = new ImageIcon(
			Graphics.GAMEPAD_POWER_UP_DIR_LEFT);
	private ImageIcon GAMEPAD_DIR_RIGHT = new ImageIcon(
			Graphics.GAMEPAD_DIR_RIGHT);
	private ImageIcon GAMEPAD_POWER_UP_DIR_RIGHT = new ImageIcon(
			Graphics.GAMEPAD_POWER_UP_DIR_RIGHT);
	private ImageIcon GAMEPAD_NO_ACTION = new ImageIcon(
			Graphics.GAMEPAD_NO_ACTION);
	private ImageIcon GAMEPAD_POWER_UP = new ImageIcon(
			Graphics.GAMEPAD_POWER_UP);
	private ImageIcon GAMEPAD_POWER_DOWN_DIR_LEFT = new ImageIcon(
			Graphics.GAMEPAD_POWER_DOWN_DIR_LEFT);
	private ImageIcon GAMEPAD_SPEED_DOWN = new ImageIcon(
			Graphics.GAMEPAD_SPEED_DOWN);
	private ImageIcon GAMEPAD_POWER_DOWN_DIR_RIGHT = new ImageIcon(
			Graphics.GAMEPAD_POWER_DOWN_DIR_RIGHT);
	private ImageIcon GAMEPAD_SPEED_UP = new ImageIcon(
			Graphics.GAMEPAD_SPEED_UP);
	private ImageIcon GAMEPAD_POWER_DOWN = new ImageIcon(
			Graphics.GAMEPAD_POWER_DOWN);

	private ImageIcon KEYBOARD_DIR_LEFT = new ImageIcon(
			Graphics.KEYBOARD_DIR_LEFT);
	private ImageIcon KEYBOARD_POWER_UP_DIR_LEFT = new ImageIcon(
			Graphics.KEYBOARD_POWER_UP_DIR_LEFT);
	private ImageIcon KEYBOARD_DIR_RIGHT = new ImageIcon(
			Graphics.KEYBOARD_DIR_RIGHT);
	private ImageIcon KEYBOARD_POWER_UP_DIR_RIGHT = new ImageIcon(
			Graphics.KEYBOARD_POWER_UP_DIR_RIGHT);
	private ImageIcon KEYBOARD_NO_ACTION = new ImageIcon(
			Graphics.KEYBOARD_NO_ACTION);
	private ImageIcon KEYBOARD_POWER_UP = new ImageIcon(
			Graphics.KEYBOARD_POWER_UP);
	private ImageIcon KEYBOARD_POWER_DOWN_DIR_LEFT = new ImageIcon(
			Graphics.KEYBOARD_POWER_DOWN_DIR_LEFT);
	private ImageIcon KEYBOARD_SPEED_DOWN = new ImageIcon(
			Graphics.KEYBOARD_SPEED_DOWN);
	private ImageIcon KEYBOARD_POWER_DOWN_DIR_RIGHT = new ImageIcon(
			Graphics.KEYBOARD_POWER_DOWN_DIR_RIGHT);
	private ImageIcon KEYBOARD_SPEED_UP = new ImageIcon(
			Graphics.KEYBOARD_SPEED_UP);
	private ImageIcon KEYBOARD_POWER_DOWN = new ImageIcon(
			Graphics.KEYBOARD_POWER_DOWN);

	public GraphicsPanel() {
		super();
		setBackground(Color.WHITE);
		icon = new JLabel();
		// default icon at startup
		setIcon(this.KEYBOARD_NO_ACTION);
		add(icon);
	}

	/**
	 * Sets a new image to the GraphicsPanel
	 * @param newIcon
	 */
	private void setIcon(ImageIcon newIcon) {
		icon.setIcon(newIcon);
	}

	public void setGamepadDefaultIcon() {
		setIcon(GAMEPAD_NO_ACTION);
	}

	public void setGamepadDirLeft() {
		setIcon(GAMEPAD_DIR_LEFT);
	}

	public void setGamepadDirRight() {
		setIcon(GAMEPAD_DIR_RIGHT);
	}

	public void setGamepadPowerUp() {
		setIcon(GAMEPAD_POWER_UP);
	}

	public void setGamepadPowerDown() {
		setIcon(GAMEPAD_POWER_DOWN);
	}

	public void setGamepadSpeedUp() {
		setIcon(GAMEPAD_SPEED_UP);
	}

	public void setGamepadSpeedDown() {
		setIcon(GAMEPAD_SPEED_DOWN);
	}

	public void setGamepadPowerUpDirLeft() {
		setIcon(GAMEPAD_POWER_UP_DIR_LEFT);
	}

	public void setGamepadPowerDownDirLeft() {
		setIcon(GAMEPAD_POWER_DOWN_DIR_LEFT);
	}

	public void setGamepadPowerUpDirRight() {
		setIcon(GAMEPAD_POWER_UP_DIR_RIGHT);
	}

	public void setGamepadPowerDownDirRight() {
		setIcon(GAMEPAD_POWER_DOWN_DIR_RIGHT);
	}

	public void setKeyboardDefaultIcon() {
		setIcon(KEYBOARD_NO_ACTION);
	}

	public void setKeyboardDirLeft() {
		setIcon(KEYBOARD_DIR_LEFT);
	}

	public void setKeyboardDirRight() {
		setIcon(KEYBOARD_DIR_RIGHT);
	}

	public void setKeyboardPowerUp() {
		setIcon(KEYBOARD_POWER_UP);
	}

	public void setKeyboardPowerDown() {
		setIcon(KEYBOARD_POWER_DOWN);
	}

	public void setKeyboardSpeedUp() {
		setIcon(KEYBOARD_SPEED_UP);
	}

	public void setKeyboardSpeedDown() {
		setIcon(KEYBOARD_SPEED_DOWN);
	}

	public void setKeyboardPowerUpDirLeft() {
		setIcon(KEYBOARD_POWER_UP_DIR_LEFT);
	}

	public void setKeyboardPowerDownDirLeft() {
		setIcon(KEYBOARD_POWER_DOWN_DIR_LEFT);
	}

	public void setKeyboardPowerUpDirRight() {
		setIcon(KEYBOARD_POWER_UP_DIR_RIGHT);
	}

	public void setKeyboardPowerDownDirRight() {
		setIcon(KEYBOARD_POWER_DOWN_DIR_RIGHT);
	}
}
