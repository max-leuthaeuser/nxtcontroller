package nxtcontroller.pc.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.JEditorPane;

/**
 * Class which is used to write log data.
 * 
 * @author Max Leuthäuser
 */
public final class LogOperation {
	private static final String NEWLINE = "<br>";
	private static LinkedList<String> logList = new LinkedList<String>();

	/**
	 * @return the current time HTML formatted.
	 */
	private static String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:S");
		Date currentTime = new Date();
		return "<font color=\"blue\">[" + formatter.format(currentTime)
				+ "]</font>";
	}

	/**
	 * Write a HTML formatted log.
	 * @param log
	 * @param arg0
	 */
	public static void writeLog(JEditorPane log, String arg0) {
		String result = "<html><font size='2' face='Verdana'>";
		logList.add(getTime() + " " + arg0);
		for (String s : logList) {
			result += s + NEWLINE;
		}
		result += "</html>";
		log.setText(result);
	}

	/**
	 * Clear a log.
	 * @param log
	 */
	public static void clearLog(JEditorPane log) {
		log.setText("");
		logList.clear();
	}
}