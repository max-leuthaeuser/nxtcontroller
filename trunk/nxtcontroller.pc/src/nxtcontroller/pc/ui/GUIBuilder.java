package nxtcontroller.pc.ui;

import java.awt.GridLayout;
import javax.swing.JEditorPane;

/**
 * Class which builds the complete GUI with all its elements. Implementation is
 * characterized through the singleton pattern. You could use this class to gain
 * access to the most important GUI elements.
 * 
 * @author Max Leuthäuser
 */
public class GUIBuilder {
	private MainFrame mainFrame;
	private SensorPanel sensorPanel;
	private ApplicationControlPanel appPanel;

	private static GUIBuilder instance = new GUIBuilder();

	private GUIBuilder() {
	}

	/**
	 * @return the {@link MainFrame}
	 */
	public MainFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * @return the {@link SensorPanel}
	 */
	public SensorPanel getSensorPanel() {
		return sensorPanel;
	}

	/**
	 * @return the {@link ApplicationControlPanel}
	 */
	public ApplicationControlPanel getAppPanel() {
		return appPanel;
	}

	/**
	 * @return an instance of the class {@link GUIBuilder}
	 */
	public static GUIBuilder getInstance() {
		return instance;
	}

	/**
	 * remove the {@link ApplicationControlPanel}. Uses while resizing to
	 * fullscreen.
	 */
	public void removeAppControl() {
		this.mainFrame.remove(this.appPanel);
	}

	/**
	 * restore the {@link ApplicationControlPanel}. Uses while resizing to
	 * standard size.
	 */
	public void restoreAppControl() {
		this.mainFrame.add(this.appPanel);
	}

	/**
	 * Build the interface of this application. Uses the following elements: <li>
	 * {@link MainFrame}</li> <li>{@link SensorPanel}</li> <li>
	 * {@link ApplicationControlPanel}</li>
	 */
	public void buildInterface() {
		this.mainFrame = new MainFrame();
		this.mainFrame.setLayout(new GridLayout(2, 0));
		this.sensorPanel = new SensorPanel();
		this.mainFrame.add(this.sensorPanel);
		this.appPanel = new ApplicationControlPanel();
		this.mainFrame.add(this.appPanel);
		this.mainFrame.validate();
		
		LogOperation.writeAppLog(UILanguage.RUNNING_ON
				+ System.getProperty("os.name"));
	}

	/**
	 * @return the application log
	 */
	public JEditorPane getLog() {
		return appPanel.getLog();
	}

	/**
	 * @return the bluetooth log
	 */
	public JEditorPane getBtt() {
		return appPanel.getBTT();
	}
}
