package nxtcontroller.pc.ui;
/**
 * @author Max Leuthäuser
 * 
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.JEditorPane;
import javax.swing.JTextArea;

public final class LogOperation {
	private static final String NEWLINE = "<br>"; 
	private static LinkedList<String> logList = new LinkedList<String>();
	
	private static String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"HH:mm:ss:S");
		Date currentTime = new Date();
		return "<font color=\"blue\">[" + formatter.format(currentTime) + "]</font>";
	}

	public static void writeLog(JEditorPane log, String arg0) {
		String result = "<html><font size='2' face='Verdana'>";
		logList.add(arg0);
		for (String s : logList) {
			result += getTime() + " " + s + NEWLINE;
		}
		result += "</html>";		
		log.setText(result);
	}
	
	public static void clearLog(JEditorPane log) {
		log.setText("");
		logList.clear();
	}
}
