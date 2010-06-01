package nxtcontroller.pc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRootPane;
import javax.swing.Timer;

import nxtcontroller.pc.core.RemoteController;
import nxtcontroller.pc.ui.GUIBuilder;
import nxtcontroller.pc.ui.GUIController;
import nxtcontroller.pc.ui.LogOperation;
import nxtcontroller.pc.ui.MainFrame;
import nxtcontroller.pc.ui.UILanguage;

/**
 * Class which handle all input events from a gamepad and control the NXT.
 * 
 * @author Max Leuthäuser
 */
public class GamepadHandler implements IHandler {
	private static final int DELAY = 40;
	private static final int STOP = 4;
	private static final int FORWARD = 1;
	private static final int BACKWARD = 7;
	private static final int RIGHT = 5;
	private static final int LEFT = 3;
	private Timer pollTimer;
	private GamePadController gpController;
	private RemoteController remoteController;

	public GamepadHandler(MainFrame mainFrame, JRootPane rootPane) {
	}

	/**
	 * @return true of a available gamepad was found, false otherwise.
	 */
	public static boolean search() {
		try {
			GamePadController controller = new GamePadController();
			return controller.gamePadIsAvailable;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Attach the gamepad to the application and use it as new input device from
	 * now on.
	 */
	public boolean attach() {
		gpController = new GamePadController();
		if (gpController.gamePadIsAvailable) {
			startPolling();
			GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
					.setGamepadDefaultIcon();
			return true;
		} else {
			GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
					.setKeyboardDefaultIcon();
			return false;
		}
	}

	/**
	 * Remove the gamepad as current input device.
	 */
	public void destroy() {
		if (gpController != null && gpController.gamePadIsAvailable) {
			pollTimer.stop(); // stop the timer
		}
	}

	/**
	 * Starts a timer and poll for new input values from the currently attached
	 * gamepad. See DELAY in this class for polling interval.
	 */
	@Override
	public void startPolling() {
		ActionListener pollPerformer = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!gpController.poll()) {
					pollTimer.stop();
					LogOperation
							.writeAppLog(UILanguage.getGamepadConnectionLost());
					GUIBuilder.getInstance().getAppPanel().getControllerBox()
							.setSelectedIndex(0);
					GUIController.getInstance().getDeviceHandler().setHandler(
							ControllerTyp.getTypes()[0]);
					return;
				}
				// get directions from analog sticks
				int speedDir = gpController.getXYStickDir();
				int compassDir = gpController.getZRZStickDir();
				boolean[] buttons = gpController.getButtons();

				// stop
				if (speedDir == STOP && compassDir == STOP) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setGamepadDefaultIcon();
					if (remoteController != null) {
						remoteController.stop();
					}
				}

				// forward
				if (speedDir == FORWARD && compassDir == STOP) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setGamepadPowerUp();
					if (remoteController != null) {
						remoteController.driveForward();
					}
				}

				// forward right
				if (speedDir == FORWARD && compassDir == RIGHT) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setGamepadPowerUpDirRight();
					if (remoteController != null) {
						remoteController.driveForwardRight();
					}
				}

				// forward left
				if (speedDir == FORWARD && compassDir == LEFT) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setGamepadPowerUpDirLeft();
					if (remoteController != null) {
						remoteController.driveForwardLeft();
					}
				}

				// backward
				if (speedDir == BACKWARD && compassDir == STOP) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setGamepadPowerDown();
					if (remoteController != null) {
						remoteController.driveBackward();
					}
				}

				// backward right
				if (speedDir == BACKWARD && compassDir == RIGHT) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setGamepadPowerDownDirRight();
					if (remoteController != null) {
						remoteController.driveBackwardRight();
					}
				}

				// backward left
				if (speedDir == BACKWARD && compassDir == LEFT) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setGamepadPowerDownDirLeft();
					if (remoteController != null) {
						remoteController.driveBackwardLeft();
					}
				}
				
				// left
				if (speedDir == STOP && compassDir == LEFT) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setGamepadDirLeft();
					if (remoteController != null) {
						remoteController.driveLeft();
					}
				}
				
				// right
				if (speedDir == STOP && compassDir == RIGHT) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setGamepadDirRight();
					if (remoteController != null) {
						remoteController.driveRight();
					}
				}

				// Control acceleration
				if (buttons[0]) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setGamepadSpeedUp();
					if (remoteController != null) {
						remoteController.increaseSpeed();
					}
				}
				if (buttons[1]) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setGamepadSpeedDown();
					if (remoteController != null) {
						remoteController.decreaseSpeed();
					}
				}
			}
		};
		pollTimer = new Timer(DELAY, pollPerformer);
		pollTimer.start();
	}

	/**
	 * Set a new {@link RemoteController} to control the NXT via Bluetooth.
	 */
	@Override
	public void setRemoteController(final RemoteController remoteController) {
		this.remoteController = remoteController;
	}
}
