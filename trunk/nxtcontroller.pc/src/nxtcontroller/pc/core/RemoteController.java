package nxtcontroller.pc.core;

import java.io.IOException;
import java.io.OutputStream;

import nxtcontroller.shared.CommandId;

/**
 * Simple remote controller interface for basically every kind of output stream.
 * It is intended to be used as a remote controller for the corresponding NXT
 * receiver class that actually implement the commands that are given to an
 * instance of this class.
 * 
 * @author Martin Morgenstern <s4810525@mail.zih.tu-dresden.de>
 * @see org.softfreaks.lejos.shared.CommandId
 * @see org.softfreaks.lejos.nxt.Receiver
 */
public class RemoteController {
	/**
	 * Used to write the commands to the receiver.
	 */
	private final OutputStream out;

	/**
	 * Initialize the remote controller.
	 * 
	 * @param out
	 *            An already initialized output stream that is ready to write
	 *            bytes
	 * @throws NullPointerException
	 *             if the parameter out is null
	 */
	public RemoteController(final OutputStream out) {
		if (null == out) {
			throw new NullPointerException();
		}

		this.out = out;
	}

	/**
	 * Command the receiver to drive forward. The receiver will not stop until
	 * the method stop is called.
	 */
	public void driveForward() {
		send(CommandId.FORWARD);
	}

	/**
	 * Command the receiver to drive backward. The receiver will not stop until
	 * the method stop is called.
	 */
	public void driveBackward() {
		send(CommandId.BACKWARD);
	}

	/**
	 * Command the receiver to stop.
	 */
	public void stop() {
		send(CommandId.STOP);
	}

	/**
	 * Command the receiver to increase the speed.
	 */
	public void increaseSpeed() {
		send(CommandId.INCREASE_SPEED);
	}

	/**
	 * Command the receiver to decrease the speed.
	 */
	public void decreaseSpeed() {
		send(CommandId.DECREASE_SPEED);
	}

	/**
	 * Send a command to the receiver using the output stream.
	 * 
	 * @param commandId
	 *            command identifier
	 */
	private void send(final byte commandId) {
		try {
			out.write(commandId);
			out.flush();
		} catch (IOException ex) {
			// TODO: how to handle this exception? should program be closed?
		}
	}
}
