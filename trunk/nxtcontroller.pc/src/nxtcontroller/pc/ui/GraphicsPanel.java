package nxtcontroller.pc.ui;
/**
 * @author Max Leuth�user
 * 
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
			Graphics.KEYBOARD_POWER_UP_DIR_LEFT);
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
		setIcon(this.KEYBOARD_NO_ACTION, Graphics.KEYBOARD);
		add(icon);
	}

	private ImageIcon scale(Image src, double scale) {
		int w = (int) (scale * src.getWidth(this));
		int h = (int) (scale * src.getHeight(this));
		int type = BufferedImage.TYPE_INT_RGB;
		BufferedImage dst = new BufferedImage(w, h, type);
		Graphics2D g2 = dst.createGraphics();
		g2.drawImage(src, 0, 0, w, h, this);
		g2.dispose();
		return new ImageIcon(dst);
	}

	public void setIcon(ImageIcon newIcon, int type) {
		if (type == Graphics.KEYBOARD) {
			icon.setIcon(scale(newIcon.getImage(), 0.90));
		} else
			icon.setIcon(scale(newIcon.getImage(), 0.3));
	}

	public ImageIcon getGAMEPAD_DIR_LEFT() {
		return GAMEPAD_DIR_LEFT;
	}

	public ImageIcon getGAMEPAD_POWER_UP_DIR_LEFT() {
		return GAMEPAD_POWER_UP_DIR_LEFT;
	}

	public ImageIcon getGAMEPAD_DIR_RIGHT() {
		return GAMEPAD_DIR_RIGHT;
	}

	public ImageIcon getGAMEPAD_POWER_UP_DIR_RIGHT() {
		return GAMEPAD_POWER_UP_DIR_RIGHT;
	}

	public ImageIcon getGAMEPAD_NO_ACTION() {
		return GAMEPAD_NO_ACTION;
	}

	public ImageIcon getGAMEPAD_POWER_UP() {
		return GAMEPAD_POWER_UP;
	}

	public ImageIcon getGAMEPAD_POWER_DOWN_DIR_LEFT() {
		return GAMEPAD_POWER_DOWN_DIR_LEFT;
	}

	public ImageIcon getGAMEPAD_SPEED_DOWN() {
		return GAMEPAD_SPEED_DOWN;
	}

	public ImageIcon getGAMEPAD_POWER_DOWN_DIR_RIGHT() {
		return GAMEPAD_POWER_DOWN_DIR_RIGHT;
	}

	public ImageIcon getGAMEPAD_SPEED_UP() {
		return GAMEPAD_SPEED_UP;
	}

	public ImageIcon getGAMEPAD_POWER_DOWN() {
		return GAMEPAD_POWER_DOWN;
	}

	public ImageIcon getKEYBOARD_DIR_LEFT() {
		return KEYBOARD_DIR_LEFT;
	}

	public ImageIcon getKEYBOARD_POWER_UP_DIR_LEFT() {
		return KEYBOARD_POWER_UP_DIR_LEFT;
	}

	public ImageIcon getKEYBOARD_DIR_RIGHT() {
		return KEYBOARD_DIR_RIGHT;
	}

	public ImageIcon getKEYBOARD_POWER_UP_DIR_RIGHT() {
		return KEYBOARD_POWER_UP_DIR_RIGHT;
	}

	public ImageIcon getKEYBOARD_NO_ACTION() {
		return KEYBOARD_NO_ACTION;
	}

	public ImageIcon getKEYBOARD_POWER_UP() {
		return KEYBOARD_POWER_UP;
	}

	public ImageIcon getKEYBOARD_POWER_DOWN_DIR_LEFT() {
		return KEYBOARD_POWER_DOWN_DIR_LEFT;
	}

	public ImageIcon getKEYBOARD_SPEED_DOWN() {
		return KEYBOARD_SPEED_DOWN;
	}

	public ImageIcon getKEYBOARD_POWER_DOWN_DIR_RIGHT() {
		return KEYBOARD_POWER_DOWN_DIR_RIGHT;
	}

	public ImageIcon getKEYBOARD_SPEED_UP() {
		return KEYBOARD_SPEED_UP;
	}

	public ImageIcon getKEYBOARD_POWER_DOWN() {
		return KEYBOARD_POWER_DOWN;
	}
}