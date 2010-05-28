package nxtcontroller.nxt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

// TODO javadoc
public final class NxtControl implements ButtonListener {
	private final UltrasonicSensor left = new UltrasonicSensor(SensorPort.S3);
	private final UltrasonicSensor front = new UltrasonicSensor(SensorPort.S1);
	private final UltrasonicSensor right = new UltrasonicSensor(SensorPort.S2);

	private final Motor motorLeft = Motor.A;
	private final Motor motorRight = Motor.C;

	private int speed = 400;
	private final int MAX_SPEED = 600;
	private final int MIN_SPEED = 200;
	private final int SPEED_INC_VALUE = 50;
	private final int ROTATE_VALUE = 200;

	private final float trackWidth = 65.0f;
	private final float wheelDiameter = 52.0f;

	private final OutputStream out;
	private final InputStream in;

	private final SenderThread senderThread = new SenderThread();

	// TODO fail-safe bluetooth connection
	private NxtControl() {
		Button.ESCAPE.addButtonListener(this);

		System.out.println("Wait for BT...");
		BTConnection con = Bluetooth.waitForConnection();
		System.out.println("Go...");

		out = con.openOutputStream();
		in = con.openInputStream();

	}

	public void start() {
		senderThread.start();
		dispatchCommands();
	}

	private void backward() {
		normalize();
		motorLeft.backward();
		motorRight.backward();
	}

	private void forward() {
		normalize();
		motorLeft.forward();
		motorRight.forward();
	}

	private void left() {
		normalize();
		motorLeft.forward();
		motorRight.backward();
	}

	private void right() {
		normalize();
		motorLeft.backward();
		motorRight.forward();
	}

	private void forwardLeft() {
		motorLeft.setSpeed(speed + ROTATE_VALUE);
		motorLeft.forward();
		motorRight.forward();
	}

	private void forwardRight() {
		motorRight.setSpeed(speed + ROTATE_VALUE);
		motorLeft.forward();
		motorRight.forward();
	}

	private void backwardLeft() {
		motorLeft.setSpeed(speed + ROTATE_VALUE);
		motorLeft.backward();
		motorRight.backward();
	}

	private void backwardRight() {
		motorRight.setSpeed(speed + ROTATE_VALUE);
		motorLeft.backward();
		motorRight.backward();
	}

	private void stop() {
		motorLeft.stop();
		motorRight.stop();
	}

	private void normalize() {
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

	private void decreaseSpeed() {
		if (speed >= MIN_SPEED) {
			speed -= SPEED_INC_VALUE;
		}
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

	private void increaseSpeed() {
		if (speed <= MAX_SPEED) {
			speed += SPEED_INC_VALUE;
		}
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

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

	private int receive() {
		int result;

		try {
			result = in.read();
		} catch (IOException e) {
			result = -1;
		}

		return result;
	}

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
	 * @param args
	 */
	public static void main(String[] args) {
		NxtControl nxt = new NxtControl();
		nxt.start();
	}

	private final class SenderThread extends Thread {
		private static final int DELAY = 250;

		@Override
		public void run() {
			for (;;) {
				sendValues();
				Delay.msDelay(DELAY);
			}
		}

		private float getAngle() {
			float turnRatio = trackWidth / wheelDiameter;
			return ((motorRight.getTachoCount() / turnRatio) - (motorLeft
					.getTachoCount() / turnRatio)) / 2.0f;
		}

		private void sendValues() {
			try {
				out.write(left.getDistance());
				out.write(front.getDistance());
				out.write(right.getDistance());

				int angle = Math.round(getAngle());

				out.write(angle);
				out.write(angle >> 8);
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
