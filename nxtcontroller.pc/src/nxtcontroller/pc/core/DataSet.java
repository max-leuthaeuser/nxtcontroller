package nxtcontroller.pc.core;

/**
 * Simple model for the data received from the NXT. Contains the values of
 * ultrasonic sensor (left/front/right) and the angle.
 * 
 * @author Martin Morgenstern <s4810525@mail.zih.tu-dresden.de>
 */
public final class DataSet {
	private final int left, front, right, angle;

	/**
	 * Create a new data set with given values.
	 * 
	 * @param left
	 *            left sonic sensor distance
	 * @param front
	 *            front sonic sensor distance
	 * @param right
	 *            right sonic sensor distance
	 * @param angle
	 *            angle of the car
	 */
	public DataSet(int left, int front, int right, int angle) {
		this.left = left;
		this.right = right;
		this.front = front;
		this.angle = angle;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	public int getFront() {
		return front;
	}

	public int getAngle() {
		return angle;
	}
}
