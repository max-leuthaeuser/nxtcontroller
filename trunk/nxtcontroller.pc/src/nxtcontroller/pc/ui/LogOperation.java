package nxtcontroller.pc.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Class which is used to write log data.
 * 
 * @author Max Leuthäuser
 */
public final class LogOperation {
	private static final String NEWLINE = "<br>";
	private static LinkedList<String> appLogList = new LinkedList<String>();
	private static LinkedList<String> bttLogList = new LinkedList<String>();

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
	 * Write the HTML formatted application log.
	 * 
	 * @param arg0
	 *            Text to write in the log
	 */
	public static void writeAppLog(String arg0) {
		String result = "<html><font size='2' face='Verdana'>";
		appLogList.add(getTime() + " " + arg0);
		for (String s : appLogList) {
			result += s + NEWLINE;
		}
		result += "</html>";
		GUIBuilder.getInstance().getLog().setText(result);
	}

	/**
	 * Clear the application log.
	 * 
	 * @param log
	 */
	public static void clearAppLog() {
		GUIBuilder.getInstance().getLog().setText("");
		appLogList.clear();
	}
	
	/**
	 * Write the HTML formatted bluetooth log.
	 * 
	 * @param arg0
	 *            Text to write in the log
	 */
	public static void writeBttLog(String arg0) {
		String result = "<html><font size='2' face='Verdana'>";
		bttLogList.add(getTime() + " " + arg0);
		for (String s : bttLogList) {
			result += s + NEWLINE;
		}
		result += "</html>";
		GUIBuilder.getInstance().getBtt().setText(result);
	}

	/**
	 * Clear the application log.
	 * 
	 * @param log
	 */
	public static void clearBttLog() {
		GUIBuilder.getInstance().getBtt().setText("");
		bttLogList.clear();
	}
}
