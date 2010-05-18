package nxtcontroller.pc.ui;
/**
 * @author Max Leuthäuser
 * 
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

public final class LogOperation {
	private static final String NEWLINE = "\n"; 
	
	private static String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"HH:mm:ss:S");
		Date currentTime = new Date();
		return "[" + formatter.format(currentTime) + "] ";
	}

	public static void writeLog(JTextArea log, String arg0) {
		log.append(getTime() + arg0 + NEWLINE);
	}
	
	public static void clearLog(JTextArea log) {
		log.setText("");
	}
}
