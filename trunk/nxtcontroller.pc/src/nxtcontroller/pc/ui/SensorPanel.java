package nxtcontroller.pc.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

/**
 * Panel which draw all sensor values.
 * @author Max Leuthäuser
 */
public class SensorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private int rotation = 90;
	private int front = 70;
	private int right = 70;
	private int left = 70;
	private int PANEL_SIZE_WIDTH;
	private int PANEL_SIZE_HEIGHT;
	private boolean distanceDrawingHasStarted = false;

	public SensorPanel() {
		setBackground(Color.white);
	}

	/**
	 * Calculate the angle and draw.
	 * @param degree
	 */
	public void drawRotation(int degree) {
		rotation = degree + 90;
		repaint();
	}

	/**
	 * Draw the distance values. 
	 * Parameter source: 0 means front, 1 means right, 2 means left
	 * @param source
	 * @param distance
	 */
	public void drawDistances(int source, int distance) {
		distanceDrawingHasStarted = true;
		switch (source) {
		case 1:
			front = distance;
			break;
		case 2:
			right = distance;
			break;
		case 3:
			left = distance;
			break;
		default:
			System.out.println("No valid distance.");
		}
		repaint();
	}

	/**
	 * @return the ratio from standard size to fullscreen size.
	 */
	private int getFullscreenModifier() {
		int result = (getSize().height * getSize().width) / (280 * 690);
		return result >= 4 ? 4 : result;
	}

	/**
	 * Draws all sensor values and takes care of the screen size.
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(java.awt.Graphics g) {
		PANEL_SIZE_HEIGHT = getSize().height;
		PANEL_SIZE_WIDTH = getSize().width;
		int fullScreenModifier = getFullscreenModifier();
		if (fullScreenModifier >= 1.1) {
			g.setFont(new Font("Verdana", Font.BOLD, 18));
		}
		super.paintComponent(g);
		g.setColor(Color.black);
		// warningLabel.setText("");

		// draw border for rotation compass
		g.drawRect(PANEL_SIZE_WIDTH / 3 * 2, PANEL_SIZE_HEIGHT / 2
				- (40 * fullScreenModifier), 80 * fullScreenModifier,
				80 * fullScreenModifier);
		g.drawString("Rotation", PANEL_SIZE_WIDTH / 3 * 2, PANEL_SIZE_HEIGHT
				/ 2 - (45 * fullScreenModifier));
		// draw the rotation arrow
		Point point2 = new Point(PANEL_SIZE_WIDTH / 3 * 2
				+ (40 * fullScreenModifier), PANEL_SIZE_HEIGHT / 2);
		Graphics2D graphics2D = (Graphics2D) g;
		AffineTransform at = AffineTransform.getTranslateInstance(point2.x,
				point2.y);
		int b = 8 * fullScreenModifier;
		double theta = Math.toRadians(45);
		// using a GeneralPath to create three lines that make up the arrow
		// (only) one time and then use AffineTransform to
		// rotate it.
		GeneralPath path = new GeneralPath();

		// Add the arrow shaft.
		path.moveTo(-20 * fullScreenModifier, 0);
		path.lineTo(20 * fullScreenModifier, 0);
		// Start a new line segment from the position of (15,0).
		path.moveTo(20 * fullScreenModifier, 0);
		// Create one of the two arrow head lines.
		int x = (int) (-b * Math.cos(theta));
		int y = (int) (b * Math.sin(theta));
		path.lineTo(x + (20 * fullScreenModifier), y);
		// Make the other arrow head line.
		x = (int) (-b * Math.cos(-theta));
		y = (int) (b * Math.sin(-theta));
		path.moveTo(20 * fullScreenModifier, 0);
		path.lineTo(x + (20 * fullScreenModifier), y);

		// theta was used for the angle between the arrow
		// shaft and each of its arrow head lines.
		// in addition we rotate the arrow
		// using rotation degree
		at.rotate(Math.toRadians(-rotation));
		Shape shape = at.createTransformedShape(path);
		graphics2D.setPaint(Color.green);
		graphics2D.draw(shape);

		g.setColor(Color.gray);
		// draw vehicle
		g.fillRect(PANEL_SIZE_WIDTH / 3 - (30 * fullScreenModifier),
				PANEL_SIZE_HEIGHT / 2 - (30 * fullScreenModifier),
				60 * fullScreenModifier, 65 * fullScreenModifier);
		g.setColor(Color.white);

		// distance values
		// front
		int modifier = 5;
		if (fullScreenModifier != 1) {
			modifier = 20;
		}
		if (distanceDrawingHasStarted) {
			g.drawString("" + front, PANEL_SIZE_WIDTH / 3 - modifier,
					PANEL_SIZE_HEIGHT / 2 - 10 * (fullScreenModifier * 2));
		}
		// right
		modifier = 10;
		if (fullScreenModifier != 1) {
			modifier = 40;
		}
		if (distanceDrawingHasStarted) {
			g.drawString("" + right, PANEL_SIZE_WIDTH / 3 + modifier,
					PANEL_SIZE_HEIGHT / 2 + (5 * fullScreenModifier));
		}
		// left
		modifier = 20;
		if (fullScreenModifier != 1) {
			modifier = 70;
		}
		if (distanceDrawingHasStarted) {
			g.drawString("" + left, PANEL_SIZE_WIDTH / 3 - modifier,
					PANEL_SIZE_HEIGHT / 2 + (5 * fullScreenModifier));
		}

		g.setColor(Color.black);

		// tires - front left
		g.fillRect(PANEL_SIZE_WIDTH / 3 - (35 * fullScreenModifier),
				PANEL_SIZE_HEIGHT / 2 - (25 * fullScreenModifier),
				5 * fullScreenModifier, 15 * fullScreenModifier);

		// tires - back left
		g.fillRect(PANEL_SIZE_WIDTH / 3 - (35 * fullScreenModifier),
				PANEL_SIZE_HEIGHT / 2 + (15 * fullScreenModifier),
				5 * fullScreenModifier, 15 * fullScreenModifier);

		// tires - front right
		g.fillRect(PANEL_SIZE_WIDTH / 3 + (30 * fullScreenModifier),
				PANEL_SIZE_HEIGHT / 2 - (25 * fullScreenModifier),
				5 * fullScreenModifier, 15 * fullScreenModifier);

		// tires - back right
		g.fillRect(PANEL_SIZE_WIDTH / 3 + (30 * fullScreenModifier),
				PANEL_SIZE_HEIGHT / 2 + (15 * fullScreenModifier),
				5 * fullScreenModifier, 15 * fullScreenModifier);

		// draw obstacles now
		// front
		int drawFront = 0;
		if (front < 61) {
			drawFront = 62 - front;
		}
		if (front <= 10) {
			g.setColor(Color.red);
			// warningLabel.setText("Collision warning!");
		}
		g.fillRect(PANEL_SIZE_WIDTH / 3 - (30 * fullScreenModifier),
				(40 * fullScreenModifier) + (drawFront * fullScreenModifier),
				60 * fullScreenModifier, 10 * fullScreenModifier);
		g.setColor(Color.black);

		// right
		int drawRight = 0;
		if (right < 61) {
			drawRight = 72 - right;
		}
		if (right <= 10) {
			g.setColor(Color.red);
			// warningLabel.setText("Collision warning!");
		}
		modifier = 1;
		if (fullScreenModifier != 1) {
			modifier = 70;
		}
		g.fillRect(PANEL_SIZE_WIDTH / 2 + modifier
				- (drawRight * fullScreenModifier), PANEL_SIZE_HEIGHT / 2
				- (30 * fullScreenModifier), 10 * fullScreenModifier,
				65 * fullScreenModifier);
		g.setColor(Color.black);

		// left
		@SuppressWarnings("unused")
		int drawLeft = 0;
		if (left < 61) {
			drawLeft = 72 - left;
		}
		if (left <= 10) {
			g.setColor(Color.red);
			// warningLabel.setText("Collision warning!");
		}
		modifier = 10;
		if (fullScreenModifier != 1) {
			modifier = 100;
		}
		g.fillRect(PANEL_SIZE_WIDTH / 6 - modifier
				+ (drawRight * fullScreenModifier), PANEL_SIZE_HEIGHT / 2
				- (30 * fullScreenModifier), 10 * fullScreenModifier,
				65 * fullScreenModifier);
	}
}
