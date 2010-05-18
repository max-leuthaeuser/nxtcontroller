package nxtcontroller.pc;

/**
 * @author Max Leuthäuser
 * 
 */

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import nxtcontroller.pc.ui.GUIController;

public class NXTController {
	public static void main(String[] args) {
		try {
		    // Set System L&F
	        UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       // handle exception
	    }
	    catch (ClassNotFoundException e) {
	       // handle exception
	    }
	    catch (InstantiationException e) {
	       // handle exception
	    }
	    catch (IllegalAccessException e) {
	       // handle exception
	    }
		GUIController.getInstance().init();
	}
}
