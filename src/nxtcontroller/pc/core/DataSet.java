package nxtcontroller.pc.core;

/**
 * Simple model for the data received from the NXT. Contains the values of
 * ultrasonic sensor (left/front/right) the angle and the current battery level.
 * 
 * @author Martin Morgenstern <s4810525@mail.zih.tu-dresden.de>
 * @author Max Leuthäuser
 */
public final class DataSet {
	private final int left, front, right, angle, battery;

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
	 * @param battery
	 *            battery level of the NXT
	 */
	public DataSet(final int left, final int front, final int right,
			final int angle, final int battery) {
		this.left = left;
		this.right = right;
		this.front = front;
		this.angle = angle;
		this.battery = battery;
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

	/**
	 * @return the current battery level of the NXT
	 */
	public int getBattery() {
		return battery;
	}
}
