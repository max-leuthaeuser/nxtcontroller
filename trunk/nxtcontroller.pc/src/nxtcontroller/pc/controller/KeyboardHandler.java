package nxtcontroller.pc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import nxtcontroller.pc.core.RemoteController;
import nxtcontroller.pc.ui.GUIBuilder;
import nxtcontroller.pc.ui.GUIController;
import nxtcontroller.pc.ui.MainFrame;

/**
 * Class which handle all input events from keyboard and control the NXT.
 * 
 * @author Max Leuthäuser
 */
public class KeyboardHandler implements IHandler {
	private JRootPane rootPane;
	private MainFrame mainFrame;
	private KeyboardListener kl;
	private RemoteController remoteController;
	private static final int DELAY = 50;
	private Timer pollTimer;

	private boolean lastKeyWasUp = false;
	private boolean lastKeyWasRight = false;
	private boolean lastKeyWasDown = false;
	private boolean lastKeyWasLeft = false;
	private boolean lastKeyWasSpeedUp = false;
	private boolean lastKeyWasSpeedDown = false;

	public KeyboardHandler(MainFrame mainFrame, JRootPane rootPane) {
		this.rootPane = rootPane;
		this.mainFrame = mainFrame;
		this.kl = new KeyboardListener();
		init();
	}

	/**
	 * Attach the keyboard to the application and use it as new input device
	 * from now on.
	 */
	public boolean attach() {
		mainFrame.addKeyListener(kl);
		mainFrame.requestFocus();
		startPolling();
		return true;
	}

	/**
	 * Remove the keyboard as current input device.
	 */
	public void destroy() {
		pollTimer.stop();
		mainFrame.removeKeyListener(kl);
	}

	/**
	 * Inner class which handle all incoming key events and control the NXT.
	 */
	private class KeyboardListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				lastKeyWasLeft = true;
				break;
			case KeyEvent.VK_D:
				lastKeyWasRight = true;
				break;
			case KeyEvent.VK_W:
				lastKeyWasUp = true;
				break;
			case KeyEvent.VK_S:
				lastKeyWasDown = true;
				break;
			case KeyEvent.VK_I:
				lastKeyWasSpeedUp = true;
				break;
			case KeyEvent.VK_K:
				lastKeyWasSpeedDown = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				lastKeyWasLeft = false;
				break;
			case KeyEvent.VK_D:
				lastKeyWasRight = false;
				break;
			case KeyEvent.VK_W:
				lastKeyWasUp = false;
				break;
			case KeyEvent.VK_S:
				lastKeyWasDown = false;
				break;
			case KeyEvent.VK_I:
				lastKeyWasSpeedUp = false;
				break;
			case KeyEvent.VK_K:
				lastKeyWasSpeedDown = false;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}
	}

	/**
	 * Attach a global key listener to handle fullscreen switching.
	 */
	public void init() {
		rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke("F12"), "fullscreen");
		rootPane.getActionMap().put("fullscreen", new AbstractAction() {
			private static final long serialVersionUID = 1309752715414833276L;

			public void actionPerformed(java.awt.event.ActionEvent e) {
				GUIController.getInstance().toggleFullScreen();
			}
		});
	}

	/**
	 * Set a new {@link RemoteController} to control the NXT via Bluetooth.
	 */
	@Override
	public void setRemoteController(RemoteController remoteController) {
		this.remoteController = remoteController;
	}

	/**
	 * Starts a timer and poll for new input values from the currently attached
	 * keyboard. See DELAY in this class for polling interval.
	 */
	@Override
	public void startPolling() {
		ActionListener pollPerformer = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// single events
				if (lastKeyWasUp && (!lastKeyWasLeft && !lastKeyWasRight)) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setKeyboardPowerUp();
					if (remoteController != null) {
						remoteController.driveForward();
					}
				}
				if (lastKeyWasRight && (!lastKeyWasDown && !lastKeyWasUp)) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setKeyboardDirRight();
					if (remoteController != null) {
						remoteController.driveRight();
					}
				}
				if (lastKeyWasDown && (!lastKeyWasLeft && !lastKeyWasRight)) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setKeyboardPowerDown();
					if (remoteController != null) {
						remoteController.driveBackward();
					}
				}
				if (lastKeyWasLeft && (!lastKeyWasDown && !lastKeyWasUp)) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setKeyboardDirLeft();
					if (remoteController != null) {
						remoteController.driveLeft();
					}
				}
				if (lastKeyWasSpeedUp) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setKeyboardSpeedUp();
					if (remoteController != null) {
						remoteController.increaseSpeed();
					}
				}
				if (lastKeyWasSpeedDown) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setKeyboardSpeedDown();
					if (remoteController != null) {
						remoteController.decreaseSpeed();
					}
				}
				
				// combinations
				if (lastKeyWasUp && lastKeyWasLeft) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setKeyboardPowerUpDirLeft();
					if (remoteController != null) {
						remoteController.driveForwardLeft();
					}
				}
				if (lastKeyWasUp && lastKeyWasRight) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setKeyboardPowerUpDirRight();
					if (remoteController != null) {
						remoteController.driveForwardRight();
					}
				}
				if (lastKeyWasDown && lastKeyWasLeft) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setKeyboardPowerDownDirLeft();
					if (remoteController != null) {
						remoteController.driveBackwardLeft();
					}
				}
				if (lastKeyWasDown && lastKeyWasRight) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setKeyboardPowerDownDirRight();
					if (remoteController != null) {
						remoteController.driveBackwardRight();
					}
				}

				if (!lastKeyWasUp && !lastKeyWasDown && !lastKeyWasRight
						&& !lastKeyWasLeft && !lastKeyWasSpeedDown
						&& !lastKeyWasSpeedUp) {
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.setKeyboardDefaultIcon();
					if (remoteController != null) {
						remoteController.stop();
					}
				}
				/*System.out.println("up: " + lastKeyWasUp);
				System.out.println("right: " + lastKeyWasRight);
				System.out.println("left: " + lastKeyWasLeft);
				System.out.println("down: " + lastKeyWasDown);
				System.out.println("---");*/
			}
		};
		pollTimer = new Timer(DELAY, pollPerformer);
		pollTimer.start();
	}
}
