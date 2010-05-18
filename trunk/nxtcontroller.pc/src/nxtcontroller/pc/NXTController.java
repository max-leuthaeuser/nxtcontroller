package nxtcontroller.pc;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import nxtcontroller.pc.ui.GUIController;

/**
 * Main class and application entry point.
 * 
 * @author Max Leuthäuser
 * @author Martin Morgenstern
 */
public class NXTController {
    /**
     * Setup and start the GUI inside the AWT event dispatching thread.
     * 
     * @param args
     *            cmdline args (ignored)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setSystemLookAndFeel();
                GUIController.getInstance().init();
            }
        });
    }

    /**
     * Try to set the Swing look and feel that is the default for the system.
     * Exit gracefully.
     */
    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // this exception is intentionally ignored
        }
    }
}
