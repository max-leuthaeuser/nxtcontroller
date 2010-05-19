package nxtcontroller.pc.controller;

/**
 * @author Max Leuthäuser
 * 
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import nxtcontroller.pc.ui.GUIBuilder;
import nxtcontroller.pc.ui.GUIController;
import nxtcontroller.pc.ui.MainFrame;

public class KeyboardHandler implements IHandler {
	private JRootPane rootPane;
	private MainFrame mainFrame;
	private KeyboardListener kl;

	public KeyboardHandler(MainFrame mainFrame, JRootPane rootPane) {
		this.rootPane = rootPane;
		this.mainFrame = mainFrame;
		this.kl = new KeyboardListener();
		init();
	}

	public void attach() {
		mainFrame.addKeyListener(kl);
		mainFrame.requestFocus();
	}

	public void destroy() {
		mainFrame.removeKeyListener(kl);
	}

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
			GUIBuilder.getInstance().getAppPanel().getGraphicsPanel().setIcon(
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.getKEYBOARD_NO_ACTION());
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// single events, use remote controller here
			if (lastKeyWasUp) {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setIcon(
								GUIBuilder.getInstance().getAppPanel()
										.getGraphicsPanel()
										.getKEYBOARD_POWER_UP());
			}
			if (lastKeyWasRight) {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setIcon(
								GUIBuilder.getInstance().getAppPanel()
										.getGraphicsPanel()
										.getKEYBOARD_DIR_RIGHT());

			}
			if (lastKeyWasDown) {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setIcon(
								GUIBuilder.getInstance().getAppPanel()
										.getGraphicsPanel()
										.getKEYBOARD_POWER_DOWN());

			}
			if (lastKeyWasLeft) {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setIcon(
								GUIBuilder.getInstance().getAppPanel()
										.getGraphicsPanel()
										.getKEYBOARD_DIR_LEFT());

			}
			if (e.getKeyChar() == 'i') {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setIcon(
								GUIBuilder.getInstance().getAppPanel()
										.getGraphicsPanel()
										.getKEYBOARD_SPEED_UP());

			}
			if (e.getKeyChar() == 'k') {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setIcon(
								GUIBuilder.getInstance().getAppPanel()
										.getGraphicsPanel()
										.getKEYBOARD_SPEED_DOWN());
			}

			// combinations, just for visualizing
			if (lastKeyWasDown && lastKeyWasLeft) {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setIcon(
								GUIBuilder.getInstance().getAppPanel()
										.getGraphicsPanel()
										.getKEYBOARD_POWER_DOWN_DIR_LEFT());
			}
			if (lastKeyWasDown && lastKeyWasRight) {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setIcon(
								GUIBuilder.getInstance().getAppPanel()
										.getGraphicsPanel()
										.getKEYBOARD_POWER_DOWN_DIR_RIGHT());
			}
			if (lastKeyWasUp && lastKeyWasLeft) {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setIcon(
								GUIBuilder.getInstance().getAppPanel()
										.getGraphicsPanel()
										.getKEYBOARD_POWER_UP_DIR_LEFT());
			}
			if (lastKeyWasUp && lastKeyWasRight) {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setIcon(
								GUIBuilder.getInstance().getAppPanel()
										.getGraphicsPanel()
										.getKEYBOARD_POWER_UP_DIR_RIGHT());
			}
		}
	}

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
}
