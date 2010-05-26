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
     * Try to set the Swing look and feel that is the default for the system. In
     * case of an error, a detailed report is printed to stderr.
     */
    private static void setSystemLookAndFeel() {
        final String lafClass = UIManager.getSystemLookAndFeelClassName();

        try {
            UIManager.setLookAndFeel(lafClass);
        } catch (Exception e) {
            // TODO this message should also be printed in the log panel
            System.err.println("Could not set the look and feel to '"
                    + lafClass + "', reverting to the default one:");
            System.err.println(e.getMessage());
        }
    }
}
