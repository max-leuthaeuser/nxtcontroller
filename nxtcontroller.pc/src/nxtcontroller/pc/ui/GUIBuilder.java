package nxtcontroller.pc.ui;
/**
 * @author Max Leuthäuser
 * 
 */

import java.awt.GridLayout;

import javax.swing.JTextArea;

public class GUIBuilder {
	private MainFrame mainFrame;
	private SensorPanel sensorPanel;
	private ApplicationControlPanel appPanel;

	private static GUIBuilder instance = new GUIBuilder();

	private GUIBuilder() {
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public SensorPanel getSensorPanel() {
		return sensorPanel;
	}

	public ApplicationControlPanel getAppPanel() {
		return appPanel;
	}

	public static GUIBuilder getInstance() {
		return instance;
	}
	
	public void removeAppControl() {
		this.mainFrame.remove(this.appPanel);
	}
	
	public void restoreAppControl() {
		this.mainFrame.add(this.appPanel);
	}

	public void buildInterface() {
		this.mainFrame = new MainFrame();
		this.mainFrame.setLayout(new GridLayout(2, 0));
		this.sensorPanel = new SensorPanel();
		this.mainFrame.add(this.sensorPanel);
		this.appPanel = new ApplicationControlPanel();
		this.mainFrame.add(this.appPanel);
		this.mainFrame.validate();
	}

	public JTextArea getLog() {
		return appPanel.getLog();
	}
}
