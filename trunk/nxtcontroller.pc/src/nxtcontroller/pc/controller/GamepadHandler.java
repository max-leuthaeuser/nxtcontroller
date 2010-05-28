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
	private Timer pollTimer;
	private GamePadController gpController;
	private int lastSpeedWas = 4;
	private int lastDirWas = 4;
	@SuppressWarnings("unused")
	private int lastButtonWas = 0;
	@SuppressWarnings("unused")
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
							.writeAppLog(UILanguage.GAMEPAD_CONNECTION_LOST);
					GUIBuilder.getInstance().getAppPanel().getControllerBox()
							.setSelectedIndex(0);
					GUIController.getInstance().getDeviceHandler().setHandler(
							ControllerTyp.types[0]);
					return;
				}
				// get directions from analog sticks
				int speedDir = gpController.getXYStickDir();
				int compassDir = gpController.getZRZStickDir();
				// hatPanel.setCompass(speedDir);
				// zPanel.setCompass(compassDir);
				boolean[] buttons = gpController.getButtons();
				// buttonsPanel.setButtons(buttons);

				// Recover Speed
				if (speedDir == 4) {
					if (lastSpeedWas == 7) {
						GUIBuilder.getInstance().getAppPanel()
								.getGraphicsPanel().setGamepadDefaultIcon();
						// remotecontroller forward release
					}
					if (lastSpeedWas == 1) {
						GUIBuilder.getInstance().getAppPanel()
								.getGraphicsPanel().setGamepadDefaultIcon();
						// remotecontroller backward release
					}

				}
				// Recover direction
				if (compassDir == 4) {
					if (lastDirWas == 5) {
						GUIBuilder.getInstance().getAppPanel()
								.getGraphicsPanel().setGamepadDefaultIcon();
						// remotecontroller right release
					}
					if (lastDirWas == 3) {
						GUIBuilder.getInstance().getAppPanel()
								.getGraphicsPanel().setGamepadDefaultIcon();
						// remotecontroller left release
					}

				}

				// Control Speed
				if (speedDir != 4) {
					if (speedDir == 1) {
						lastSpeedWas = 1;
						GUIBuilder.getInstance().getAppPanel()
								.getGraphicsPanel().setGamepadPowerUp();
						// remotecontroller forward
					}
					if (speedDir == 7) {
						lastSpeedWas = 7;
						GUIBuilder.getInstance().getAppPanel()
								.getGraphicsPanel().setGamepadPowerDown();
						// remotecontroller backward
					}
				}
				// Control direction
				if (compassDir != 4) {
					if (compassDir == 3) {
						lastDirWas = 3;
						GUIBuilder.getInstance().getAppPanel()
								.getGraphicsPanel().setGamepadDirLeft();
						// remotecontroller left
					}
					if (compassDir == 5) {
						lastDirWas = 5;
						GUIBuilder.getInstance().getAppPanel()
								.getGraphicsPanel().setGamepadDirRight();
						// remotecontroller right
					}
				}
				// Control acceleration
				if (buttons[0]) {
					lastButtonWas = 10;
					// remotecontroller speed up
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setGamepadSpeedUp();
				}
				if (buttons[1]) {
					lastButtonWas = 11;
					// remotecontroller speed down
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setGamepadSpeedDown();
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
	public void setRemoteController(RemoteController remoteController) {
		this.remoteController = remoteController;
	}
}
