package nxtcontroller.pc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import nxtcontroller.pc.ui.GUIController;
import nxtcontroller.pc.ui.LogOperation;

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
				setConsoleOutputToLog();
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

	/**
	 * Pipe the console output to our Bluetooth log using {@link LogOperation}
	 * to show the Bluetooth output from Bluecove.
	 */
	private static void setConsoleOutputToLog() {
		System.setOut(new PrintStream(new OutputStream() {

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				LogOperation.writeBttLog(new String(b, off, len));
			}

			@Override
			public void write(byte[] b) throws IOException {
				this.write(b, 0, b.length);
			}

			@Override
			public void write(int b) throws IOException {
				this.write(new byte[] { (byte) b });
			}
		}));
	}
}
