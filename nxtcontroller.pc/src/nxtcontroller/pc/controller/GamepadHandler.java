package nxtcontroller.pc.controller;
/**
 * @author Max Leuthäuser
 * 
 */

import javax.swing.JRootPane;

import nxtcontroller.pc.ui.MainFrame;

public class GamepadHandler implements IHandler{
	private JRootPane rootPane;
	private MainFrame mainFrame;

	public GamepadHandler(MainFrame mainFrame, JRootPane rootPane) {
		this.rootPane = rootPane;
		this.mainFrame = mainFrame;
	}
	
	public void attach() {
	}

	public void destroy() {
	}
}
