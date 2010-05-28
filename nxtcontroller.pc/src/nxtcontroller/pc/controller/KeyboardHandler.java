package nxtcontroller.pc.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

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
		return true;
	}

	/**
	 * Remove the keyboard as current input device.
	 */
	public void destroy() {
		mainFrame.removeKeyListener(kl);
	}

	/**
	 * Inner class which handle all incoming key events and control the NXT.
	 */
	private class KeyboardListener implements KeyListener {
		private boolean lastKeyWasUp = false;
		private boolean lastKeyWasRight = false;
		private boolean lastKeyWasDown = false;
		private boolean lastKeyWasLeft = false;

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == 'w') {
				lastKeyWasUp = true;
			}
			if (e.getKeyChar() == 'd') {
				lastKeyWasRight = true;
			}
			if (e.getKeyChar() == 's') {
				lastKeyWasDown = true;
			}
			if (e.getKeyChar() == 'a') {
				lastKeyWasLeft = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyChar() == 'w') {
				lastKeyWasUp = false;
			}
			if (e.getKeyChar() == 'd') {
				lastKeyWasRight = false;
			}
			if (e.getKeyChar() == 's') {
				lastKeyWasDown = false;
			}
			if (e.getKeyChar() == 'a') {
				lastKeyWasLeft = false;
			}
			GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
					.setKeyboardDefaultIcon();
			if (remoteController != null) {
				remoteController.stop();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// single events, use remote controller here
			if (lastKeyWasUp) {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setKeyboardPowerUp();
				if (remoteController != null) {
					remoteController.driveForward();
				}
			}
			if (lastKeyWasRight) {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setKeyboardDirRight();
				if (remoteController != null) {
					remoteController.driveRight();
				}
			}
			if (lastKeyWasDown) {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setKeyboardPowerDown();
				if (remoteController != null) {
					remoteController.driveBackward();
				}
			}
			if (lastKeyWasLeft) {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setKeyboardDirLeft();
				if (remoteController != null) {
					remoteController.driveLeft();
				}
			}
			if (e.getKeyChar() == 'i') {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setKeyboardSpeedUp();
				if (remoteController != null) {
					remoteController.increaseSpeed();
				}
			}
			if (e.getKeyChar() == 'k') {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setKeyboardSpeedDown();
				if (remoteController != null) {
					remoteController.decreaseSpeed();
				}
			}

			// combinations
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
}
