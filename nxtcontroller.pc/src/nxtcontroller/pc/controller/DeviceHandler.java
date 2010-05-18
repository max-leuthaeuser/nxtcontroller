package nxtcontroller.pc.controller;
/**
 * @author Max Leuthäuser
 * 
 */

import javax.swing.JRootPane;

import nxtcontroller.pc.ui.MainFrame;

public class DeviceHandler {
	private JRootPane rootPane;
	private MainFrame mainFrame;
	private KeyboardHandler kh;
	private GamepadHandler gh;
	
	public DeviceHandler(MainFrame mainFrame, JRootPane rootPane) {
		this.rootPane = rootPane;
		this.mainFrame = mainFrame;
		kh = new KeyboardHandler(mainFrame, rootPane);
		gh = new GamepadHandler(mainFrame, rootPane);
	}
	
	public void setHandler(String device) {
		if (device.equals(ControllerTyp.types[0])) {
			gh.destroy();
			kh.attach();
		}
		if (device.equals(ControllerTyp.types[1])) {
			kh.destroy();
			gh.attach();
		}
	}
}
