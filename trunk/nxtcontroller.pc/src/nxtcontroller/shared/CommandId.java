package nxtcontroller.shared;

/**
 * Identifiers for all commands that are sent from a remote controller to a
 * receiver. This class is shared between the NXT and PC implementations.
 * 
 * @author Martin Morgenstern <s4810525@mail.zih.tu-dresden.de>
 */
public final class CommandId {
	/*
	 * Command identifiers for motions.
	 */
	public static final byte STOP = 0x00;
	public static final byte FORWARD = 0x01;
	public static final byte BACKWARD = 0x02;
	public static final byte LEFT = 0x03;
	public static final byte RIGHT = 0x04;
	
	/*
	 * Command identifiers controlling the speed.
	 */
	public static final byte INCREASE_SPEED = 0x05;
	public static final byte DECREASE_SPEED = 0x06;
}
