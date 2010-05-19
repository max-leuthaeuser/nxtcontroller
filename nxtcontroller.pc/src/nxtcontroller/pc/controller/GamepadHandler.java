package nxtcontroller.pc.controller;

/**
 * @author Max Leuthäuser
 * 
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRootPane;
import javax.swing.Timer;

import nxtcontroller.pc.ui.GUIBuilder;
import nxtcontroller.pc.ui.MainFrame;

public class GamepadHandler implements IHandler {
	private JRootPane rootPane;
	private MainFrame mainFrame;
	private static final int DELAY = 40;
	private Timer pollTimer;
	private GamePadController gpController;
	private int lastSpeedWas = 4;
	private int lastDirWas = 4;
	private int lastButtonWas = 0;

	public GamepadHandler(MainFrame mainFrame, JRootPane rootPane) {
		this.rootPane = rootPane;
		this.mainFrame = mainFrame;
	}

	public void attach() {
		gpController = new GamePadController();
		if (gpController.gamePadIsAvailable) {
			setDefaultIcon();
			startPolling();
		} else
			GUIBuilder.getInstance().getAppPanel().getGraphicsPanel().setIcon(
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.getKEYBOARD_NO_ACTION());
	}

	public void destroy() {
		if (gpController != null && gpController.gamePadIsAvailable) {
			pollTimer.stop(); // stop the timer
		}
	}

	private void setDefaultIcon() {
		GUIBuilder.getInstance().getAppPanel().getGraphicsPanel().setIcon(
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.getGAMEPAD_NO_ACTION());
	}
	
	private void startPolling() {
		ActionListener pollPerformer = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				gpController.poll();
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
						setDefaultIcon();
						// remotecontroller forward release
					}
					if (lastSpeedWas == 1) {
						setDefaultIcon();
						// remotecontroller backward release
					}

				}
				// Recover direction
				if (compassDir == 4) {
					if (lastDirWas == 5) {
						setDefaultIcon();
						// remotecontroller right release
					}
					if (lastDirWas == 3) {
						setDefaultIcon();
						// remotecontroller left release
					}

				}

				// Control Speed
				if (speedDir != 4) {
					if (speedDir == 1) {
						lastSpeedWas = 1;
						GUIBuilder.getInstance().getAppPanel().getGraphicsPanel().setIcon(
								GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
										.getGAMEPAD_POWER_UP());
						// remotecontroller forward
					}
					if (speedDir == 7) {
						lastSpeedWas = 7;
						GUIBuilder.getInstance().getAppPanel().getGraphicsPanel().setIcon(
								GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
										.getGAMEPAD_POWER_DOWN());
						// remotecontroller backward
					}
				}
				// Control direction
				if (compassDir != 4) {
					if (compassDir == 3) {
						lastDirWas = 3;
						GUIBuilder.getInstance().getAppPanel().getGraphicsPanel().setIcon(
								GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
										.getGAMEPAD_DIR_LEFT());
						// remotecontroller left
					}
					if (compassDir == 5) {
						lastDirWas = 5;
						GUIBuilder.getInstance().getAppPanel().getGraphicsPanel().setIcon(
								GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
										.getGAMEPAD_DIR_RIGHT());
						// remotecontroller right
					}
				}
				// Control acceleration
				if (buttons[0]) {
					lastButtonWas = 10;
					// remotecontroller speed up
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel().setIcon(
							GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
									.getGAMEPAD_SPEED_UP());
				}
				if (buttons[1]) {
					lastButtonWas = 11;
					// remotecontroller speed down
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel().setIcon(
							GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
									.getGAMEPAD_SPEED_DOWN());
				}
			}
		};
		pollTimer = new Timer(DELAY, pollPerformer);
		pollTimer.start();
	}
}
