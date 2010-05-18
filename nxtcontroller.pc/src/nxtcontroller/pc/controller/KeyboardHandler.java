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
		@Override
		public void keyPressed(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {
			GUIBuilder.getInstance().getAppPanel().getGraphicsPanel().setIcon(
					GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
							.getKEYBOARD_NO_ACTION());
		}

		@Override
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == 'w') {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setIcon(
								GUIBuilder.getInstance().getAppPanel()
										.getGraphicsPanel()
										.getKEYBOARD_POWER_UP());
			}
			if (e.getKeyChar() == 'd') {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setIcon(
								GUIBuilder.getInstance().getAppPanel()
										.getGraphicsPanel()
										.getKEYBOARD_DIR_RIGHT());

			}
			if (e.getKeyChar() == 's') {
				GUIBuilder.getInstance().getAppPanel().getGraphicsPanel()
						.setIcon(
								GUIBuilder.getInstance().getAppPanel()
										.getGraphicsPanel()
										.getKEYBOARD_POWER_DOWN());

			}
			if (e.getKeyChar() == 'a') {
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
