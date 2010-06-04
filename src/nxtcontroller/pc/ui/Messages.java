package nxtcontroller.pc.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	
	private static final int DEFAULT = 0;
	private static final int GERMAN = 1;
	private static final String DEFAULT_NL = "nxtcontroller.pc.messages_default"; //$NON-NLS-1$
	private static final String GERMAN_NL = "nxtcontroller.pc.messages_german"; //$NON-NLS-1$
	private static String BUNDLE_NAME = DEFAULT_NL;
	private static int currentNL = DEFAULT;

	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public static void toggleNl() {
		if (currentNL == DEFAULT) {
			currentNL = GERMAN;
			BUNDLE_NAME = GERMAN_NL;
		} else {
			currentNL = DEFAULT;
			BUNDLE_NAME = DEFAULT_NL;
		}
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
