package nxtcontroller.pc.ui;
/**
 * @author Max Leuthäuser
 * 
 */

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;

public class GUIController {
	private GUIBuilder guiBuilder = GUIBuilder.getInstance();
	private boolean isInFullScreenMode = false;
	
	private static GUIController instance = new GUIController();

	private GUIController() {
	}
	
	public static GUIController getInstance() {
		return instance;
	}
	
	public void init() {
		this.guiBuilder.buildInterface();
	}
	
	public Point getDisplaySize() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		DisplayMode dm=gs.getDisplayMode();
		return new Point(dm.getWidth(), dm.getHeight());
	}
	
	public void toggleFullScreen() {
		if (this.isInFullScreenMode) {
			this.isInFullScreenMode = false;
			this.guiBuilder.restoreAppControl();
			this.guiBuilder.getMainFrame().setLayout(new GridLayout(2, 0));
			this.guiBuilder.getMainFrame().restore();
		} else {
			this.isInFullScreenMode = true;
			this.guiBuilder.removeAppControl();
			this.guiBuilder.getMainFrame().setLayout(new GridLayout(1, 0));
			this.guiBuilder.getMainFrame().setMaximized();
		}	
	}
}
