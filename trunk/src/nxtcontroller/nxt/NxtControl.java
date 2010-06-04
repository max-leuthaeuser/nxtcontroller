package nxtcontroller.nxt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Float;
import lejos.nxt.Battery;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Motor;
import lejos.nxt.NXT;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.util.Delay;
import nxtcontroller.shared.CommandId;

/**
 * Client side application running on the NXT. Handles incoming commands to
 * control the direction and power to drive and sends distance values from 3
 * UltraSonic Sensors the angle the NXT is rotated to and the current battery
 * level periodically.
 * 
 * @author Max Leuthäuser
 * @author Martin Morgenstern
 */
public final class NxtControl implements ButtonListener {
	private final UltrasonicSensor left = new UltrasonicSensor(SensorPort.S3);
	private final UltrasonicSensor front = new UltrasonicSensor(SensorPort.S1);
	private final UltrasonicSensor right = new UltrasonicSensor(SensorPort.S2);

	private final Motor motorLeft = Motor.A;
	private final Motor motorRight = Motor.C;

	/**
	 * Standard speed at start.
	 */
	private int speed = 400;
	/**
	 * Maximum speed at the moment (600).
	 * <p>
	 * <i>800 is possible with full charged batteries.</i>
	 * </p>
	 */
	private final int MAX_SPEED = 600;
	/**
	 * Minimum speed at the moment (200).
	 */
	private final int MIN_SPEED = 200;
	/**
	 * Value which should be use to increase or decrease the speed of the NXT.
	 * 
	 * @see NxtControl#decreaseSpeed()
	 * @see NxtControl#increaseSpeed()
	 */
	private final int SPEED_INC_VALUE = 50;
	private final int ROTATE_VALUE = 200;

	/**
	 * Distance between the two wheels of the NXT.
	 */
	private final float trackWidth = 65.0f;
	/**
	 * Diameter of the wheels which are used.
	 */
	private final float wheelDiameter = 52.0f;

	private final OutputStream out;
	private final InputStream in;

	private final SenderThread senderThread = new SenderThread();

	/**
	 * Waits for an incoming bluetooth connection at start and retrieves its
	 * input- and outputstream.
	 * 
	 * <p>
	 * TODO: fail-safe bluetooth connection
	 * </p>
	 */
	private NxtControl() {
		Button.ESCAPE.addButtonListener(this);

		System.out.println("Wait for BT...");
		BTConnection con = Bluetooth.waitForConnection();
		System.out.println("Go...");

		out = con.openOutputStream();
		in = con.openInputStream();

	}

	/**
	 * Starts sending the sensor values and dispatches incoming commands.
	 * 
	 * @see SenderThread
	 * @see NxtControl#dispatchCommands()
	 */
	public void start() {
		senderThread.start();
		dispatchCommands();
	}

	/**
	 * NXT moves backward continuously. To stop the NXT use
	 * {@link NxtControl#stop()}
	 */
	private void backward() {
		normalize();
		motorLeft.backward();
		motorRight.backward();
	}

	/**
	 * NXT moves forward continuously. To stop the NXT use
	 * {@link NxtControl#stop()}
	 */
	private void forward() {
		normalize();
		motorLeft.forward();
		motorRight.forward();
	}

	/**
	 * NXT turns left on the spot continuously. To stop the NXT use
	 * {@link NxtControl#stop()}
	 */
	private void left() {
		normalize();
		motorLeft.forward();
		motorRight.backward();
	}

	/**
	 * NXT turns right on the spot continuously. To stop the NXT use
	 * {@link NxtControl#stop()}
	 */
	private void right() {
		normalize();
		motorLeft.backward();
		motorRight.forward();
	}

	/**
	 * NXT moves along a curve forward and left continuously. To stop the NXT
	 * use {@link NxtControl#stop()}
	 */
	private void forwardLeft() {
		motorLeft.setSpeed(speed + ROTATE_VALUE);
		motorLeft.forward();
		motorRight.forward();
	}

	/**
	 * NXT moves along a curve forward and right continuously. To stop the NXT
	 * use {@link NxtControl#stop()}
	 */
	private void forwardRight() {
		motorRight.setSpeed(speed + ROTATE_VALUE);
		motorLeft.forward();
		motorRight.forward();
	}

	/**
	 * NXT moves along a curve backward and left continuously. To stop the NXT
	 * use {@link NxtControl#stop()}
	 */
	private void backwardLeft() {
		motorLeft.setSpeed(speed + ROTATE_VALUE);
		motorLeft.backward();
		motorRight.backward();
	}

	/**
	 * NXT moves along a curve backward and right continuously. To stop the NXT
	 * use {@link NxtControl#stop()}
	 */
	private void backwardRight() {
		motorRight.setSpeed(speed + ROTATE_VALUE);
		motorLeft.backward();
		motorRight.backward();
	}

	/**
	 * Stop the NXT immediately.
	 */
	private void stop() {
		motorLeft.stop();
		motorRight.stop();
	}

	/**
	 * Recover the base speed.
	 */
	private void normalize() {
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

	/**
	 * Decrease the speed between {@link NxtControl#MAX_SPEED} and
	 * {@link NxtControl#MIN_SPEED} using {@link NxtControl#SPEED_INC_VALUE}
	 */
	private void decreaseSpeed() {
		if (speed >= MIN_SPEED) {
			speed -= SPEED_INC_VALUE;
		}
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

	/**
	 * Increase the speed between {@link NxtControl#MAX_SPEED} and
	 * {@link NxtControl#MIN_SPEED} using {@link NxtControl#SPEED_INC_VALUE}
	 */
	private void increaseSpeed() {
		if (speed <= MAX_SPEED) {
			speed += SPEED_INC_VALUE;
		}
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

	/**
	 * Check commands via bluetooth continuously an decide which move should be
	 * performed.
	 * 
	 * @see NxtControl#receive()
	 */
	private void dispatchCommands() {
		for (;;) {
			switch (receive()) {
			case CommandId.BACKWARD:
				backward();
				break;
			case CommandId.BACKWARD_LEFT:
				backwardLeft();
				break;
			case CommandId.BACKWARD_RIGHT:
				backwardRight();
				break;
			case CommandId.FORWARD:
				forward();
				break;
			case CommandId.FORWARD_LEFT:
				forwardLeft();
				break;
			case CommandId.FORWARD_RIGHT:
				forwardRight();
				break;
			case CommandId.LEFT:
				left();
				break;
			case CommandId.RIGHT:
				right();
				break;
			case CommandId.STOP:
				stop();
				break;
			case CommandId.INCREASE_SPEED:
				increaseSpeed();
				break;
			case CommandId.DECREASE_SPEED:
				decreaseSpeed();
				break;
			default:
				// TODO better error reporting
				throw new RuntimeException();
			}
		}
	}

	/**
	 * @return the current command from the bluetooth input stream.
	 */
	private int receive() {
		int result;

		try {
			result = in.read();
		} catch (IOException e) {
			result = -1;
		}

		return result;
	}

	/**
	 * Shutdown the NXT and close all bluetooth streams.
	 */
	public void shutdown() {
		stop();

		try {
			out.close();
			in.close();
		} catch (IOException e) {
			// TODO handle exception
		}

		NXT.shutDown();
	}

	/**
	 * Entry point of the application.
	 * 
	 * @param args
	 *            ignored here
	 */
	public static void main(String[] args) {
		NxtControl nxt = new NxtControl();
		nxt.start();
	}

	/**
	 * Thread which sends UltraSonic Sensor values and the current rotation
	 * value via bluetooth continuously.
	 */
	private final class SenderThread extends Thread {
		/**
		 * Delay between sending new values to the server.
		 */
		private static final int DELAY = 50;
		/**
		 * Delay in seconds between a new battery status value is send.
		 */
		private static final int BATTERY_DELAY = 60;
		private int batteryCounter = 0;

		/**
		 * Actual battery value in mV.
		 */
		private int batteryValue = Math.round(getBattery());
		/**
		 * Standard battery value in mV.
		 */
		private final float BATTERY_DEFAULT_VALUE = 8200;

		/**
		 * Start this thread and send values continuously. Uses
		 * {@link SenderThread#DELAY} to wait between sending a set of new
		 * values.
		 * 
		 * @see SenderThread#sendValues()
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			for (;;) {
				sendValues();
				Delay.msDelay(DELAY);
			}
		}

		/**
		 * @return the angle the NXT is rotated to.
		 * 
		 * @see NxtControl#trackWidth
		 * @see NxtControl#wheelDiameter
		 */
		private float getAngle() {
			float turnRatio = trackWidth / wheelDiameter;
			return ((motorRight.getTachoCount() / turnRatio) - (motorLeft
					.getTachoCount() / turnRatio)) / 2.0f;
		}

		private float getBattery() {
			return (new Float(Battery.getVoltageMilliVolt()) / BATTERY_DEFAULT_VALUE) * 100;
		}

		/**
		 * Sends a set of UltraSonic sensor values the current angle and the
		 * current battery level.
		 * 
		 * @see SenderThread#getAngle()
		 * @see SenderThread#getBattery()
		 */
		private void sendValues() {
			try {
				out.write(left.getDistance());
				out.write(front.getDistance());
				out.write(right.getDistance());

				int angle = Math.round(getAngle());

				out.write(angle);
				out.write(angle >> 8);

				// calculate the actual battery value as percentage only after
				// the given BATTERY_DELAY
				if (batteryCounter >= (BATTERY_DELAY * 20)) {
					batteryCounter = 0;
					batteryValue = Math.round(getBattery());
				} else {
					batteryCounter++;
				}

				out.write(batteryValue);
				out.write(batteryValue >> 8);

				out.flush();
			} catch (IOException e) {
				// TODO better error reporting
				throw new RuntimeException();
			}
		}
	}

	@Override
	public void buttonPressed(Button b) {
	}

	@Override
	public void buttonReleased(Button b) {
		shutdown();
	}
}
