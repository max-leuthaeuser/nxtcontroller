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
	public DataSet(final int left, final int front, final int right,
			final int angle) {
		this.left = left;
		this.right = right;
		this.front = front;
		this.angle = angle;
	}

	/**
	 * @return the value for the left UltraSonic Sensor
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * @return the value for the right UltraSonic Sensor
	 */
	public int getRight() {
		return right;
	}

	/**
	 * @return the value for the UltraSonic Sensor at the front
	 */
	public int getFront() {
		return front;
	}

	/**
	 * @return the angle the NXT rotated to
	 */
	public int getAngle() {
		return angle;
	}
}
