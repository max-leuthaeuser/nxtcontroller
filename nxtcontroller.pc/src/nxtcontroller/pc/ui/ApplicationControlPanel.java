package nxtcontroller.pc.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.*;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import lejos.pc.comm.NXTConnector;

import nxtcontroller.pc.controller.ControllerTyp;
import nxtcontroller.pc.controller.GamepadHandler;
import nxtcontroller.pc.core.RemoteController;

/**
 * Panel which holds all control elements.
 * 
 * @author Max Leuthäuser
 */
public class ApplicationControlPanel extends JPanel {
	private static final long serialVersionUID = 2963071726378133381L;

	private JSplitPane vSplitPane;
	private JPanel logPanel, clearLogPanel, clearBttPanel, bttPanel;
	private GraphicsPanel graphicsPanel;
	private JEditorPane log, btt;
	private JButton clearLog, clearBTT, saveLog, saveBTT;
	private MouseHandler bh;
	private JComboBox controllerBox;
	private JButton connect, searchID;

	/**
	 * Constructor. Builds all control elements and attach mouse listener.
	 */
	public ApplicationControlPanel() {
		super();
		bh = new MouseHandler();
		setLayout(new GridLayout(2, 0));
		controllerBox = new JComboBox();
		controllerBox.addItem(ControllerTyp.types[0]);
		controllerBox.addItem(ControllerTyp.types[1]);
		controllerBox.setEditable(false);
		controllerBox.addActionListener(bh);
		controllerBox.setToolTipText(UILanguage.OPTION_HINT);

		connect = new JButton(UILanguage.CONNECT);
		connect.setToolTipText(UILanguage.CONNECT_HINT);
		connect.addMouseListener(bh);
		graphicsPanel = new GraphicsPanel();

		searchID = new JButton(UILanguage.SEARCH);
		searchID.setToolTipText(UILanguage.SEARCH_HINT);
		searchID.addMouseListener(bh);
		searchID.setToolTipText(UILanguage.SEARCH_HINT);

		JPanel bPanel = new JPanel();
		bPanel.setLayout(new GridLayout(2, 0));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		bPanel.add(searchID);
		bPanel.add(controllerBox);
		buttonPanel.add(bPanel, BorderLayout.NORTH);
		buttonPanel.add(connect, BorderLayout.SOUTH);

		vSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				graphicsPanel, buttonPanel);
		vSplitPane.setBorder(BorderFactory
				.createTitledBorder(UILanguage.CONTROLPANEL));

		vSplitPane.setPreferredSize(new Dimension(
				StaticSizes.APPLICATION_SIZE_WIDTH - 30,
				StaticSizes.APPLICATION_SIZE_HEIGTH / 2 - 20));
		add(vSplitPane);
		vSplitPane
				.setDividerLocation(StaticSizes.APPLICATION_SIZE_STANDARD_SPACER);

		buildLogPanel();
		buildBTTracePanel();

		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(0, 2));

		JPanel t1 = new JPanel();
		JPanel logP = new JPanel();
		logP.setLayout(new GridLayout(1, 1));
		logP.add(new JScrollPane(log));
		JSplitPane logPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				logP, clearLogPanel);
		t1.setBorder(BorderFactory.createTitledBorder(UILanguage.LOG));
		logPane.setPreferredSize(new Dimension(
				StaticSizes.APPLICATION_SIZE_WIDTH / 2 - 11,
				StaticSizes.APPLICATION_SIZE_HEIGTH / 3 - 90));
		logPane.setDividerLocation(255);
		t1.add(logPane);

		JPanel t2 = new JPanel();
		JPanel bttP = new JPanel();
		bttP.setLayout(new GridLayout(1, 1));
		bttP.add(new JScrollPane(btt));
		JSplitPane logPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				bttP, clearBttPanel);
		t2.setBorder(BorderFactory.createTitledBorder(UILanguage.BTT));
		logPane2.setPreferredSize(new Dimension(
				StaticSizes.APPLICATION_SIZE_WIDTH / 2 - 11,
				StaticSizes.APPLICATION_SIZE_HEIGTH / 3 - 90));
		logPane2.setDividerLocation(255);
		t2.add(logPane2);

		textPanel.add(t1);
		textPanel.add(t2);

		add(textPanel);
	}

	/**
	 * Create the application log.
	 */
	private void buildLogPanel() {
		saveLog = new JButton(UILanguage.SAVE);
		saveLog.addMouseListener(bh);
		saveLog.setToolTipText(UILanguage.SAVE_HINT);
		clearLog = new JButton(UILanguage.CLEAR);
		clearLog.addMouseListener(bh);
		clearLog.setToolTipText(UILanguage.CLEAR_HINT);
		logPanel = new JPanel();
		logPanel.setLayout(new GridLayout(1, 0));
		log = new JEditorPane();
		log.setEditable(false);
		log.setContentType("text/html");
		logPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.gray), UILanguage.LOG));
		clearLogPanel = new JPanel();
		clearLogPanel.setLayout(new BorderLayout());
		clearLogPanel.add(saveLog, BorderLayout.NORTH);
		clearLogPanel.add(clearLog, BorderLayout.SOUTH);
		LogOperation.writeLog(log, UILanguage.RUNNING_ON
				+ System.getProperty("os.name"));
	}

	/**
	 * Create the Bluetooth trace log.
	 */
	private void buildBTTracePanel() {
		saveBTT = new JButton(UILanguage.SAVE);
		saveBTT.addMouseListener(bh);
		saveBTT.setToolTipText(UILanguage.SAVE_HINT);
		clearBTT = new JButton(UILanguage.CLEAR);
		clearBTT.addMouseListener(bh);
		clearBTT.setToolTipText(UILanguage.CLEAR_HINT);
		bttPanel = new JPanel();
		bttPanel.setLayout(new GridLayout(1, 0));
		btt = new JEditorPane();
		btt.setEditable(false);
		btt.setContentType("text/html");
		bttPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.gray), UILanguage.BTT));
		clearBttPanel = new JPanel();
		clearBttPanel.setLayout(new BorderLayout());
		clearBttPanel.add(saveBTT, BorderLayout.NORTH);
		clearBttPanel.add(clearBTT, BorderLayout.SOUTH);
	}

	/**
	 * @return the application log.
	 */
	public JEditorPane getLog() {
		return this.log;
	}

	/**
	 * @return the Bluetooth trace log
	 */
	public JEditorPane getBTT() {
		return this.btt;
	}

	/**
	 * Write a String to a file.
	 * 
	 * @param path
	 * @param text
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeFile(String path, String text)
			throws FileNotFoundException, IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(path));
		out.write(text);
		out.close();
	}

	/**
	 * @return the graphics panel
	 */
	public GraphicsPanel getGraphicsPanel() {
		return graphicsPanel;
	}

	/**
	 * Search for available NXT Bricks via Bluetooth and connect if the user
	 * selects a specific device.
	 */
	private void connect() {
		LogOperation.writeLog(GUIBuilder.getInstance().getLog(),
				UILanguage.CONNECT_SEARCHING);
		
		NXTInfo[] availableNXTs = null;
		NXTConnector nxtConn = new NXTConnector();

		availableNXTs = nxtConn.search("*", null, NXTCommFactory.BLUETOOTH);
		if (availableNXTs.length == 0) {
			LogOperation.writeLog(GUIBuilder.getInstance().getLog(),
					UILanguage.CONNECT_NO_NXT_AVAILABLE);
			return;
		}

		String[] result = new String[availableNXTs.length];
		int i = 0;
		for (NXTInfo n : availableNXTs) {
			result[i] = n.name + "@" + n.deviceAddress;
			i++;
		}

		String s = (String) JOptionPane.showInputDialog(GUIBuilder
				.getInstance().getMainFrame(), UILanguage.CONNECT_DIALOG_HINT,
				UILanguage.CONNECT_DIALOG_HEADLINE, JOptionPane.PLAIN_MESSAGE,
				null, result, "");

		if ((s != null) && (s.length() > 0)) {
			nxtConn.connectTo(s.substring(0, s.indexOf("@")), s.substring(s
					.indexOf("@") + 1, s.length()), NXTCommFactory.BLUETOOTH);
			LogOperation.writeLog(GUIBuilder.getInstance().getLog(),
					UILanguage.CONNECT_READY + s);
			OutputStream out = nxtConn.getOutputStream();
			RemoteController rc = new RemoteController(out);
			GUIController.getInstance().getDeviceHandler().getKeyboardHandler().setRemoteController(rc);
			GUIController.getInstance().getDeviceHandler().getGamepadHandler().setRemoteController(rc);
			return;
		}

		LogOperation.writeLog(GUIBuilder.getInstance().getLog(),
				UILanguage.CONNECT_ABORT);
	}

	/**
	 * @return the controller box which holds the available controller devices.
	 */
	public JComboBox getControllerBox() {
		return controllerBox;
	}

	/**
	 * Inner class which handles all incoming mouse events and control the
	 * application.
	 */
	private class MouseHandler implements MouseListener, ActionListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (arg0.getSource() == connect) {
				connect();
				GUIBuilder.getInstance().getMainFrame().requestFocus();
			}
			if (arg0.getSource() == searchID) {
				if (!GamepadHandler.search()) {
					controllerBox.setSelectedIndex(0);
				} else
					LogOperation.writeLog(log, UILanguage.GAMEPAD_AVAILABLE);
				GUIBuilder.getInstance().getMainFrame().requestFocus();
			}
			if (arg0.getSource() == clearLog) {
				LogOperation.clearLog(log);
				GUIBuilder.getInstance().getMainFrame().requestFocus();
			}
			if (arg0.getSource() == clearBTT) {
				LogOperation.clearLog(btt);
				GUIBuilder.getInstance().getMainFrame().requestFocus();
			}
			if (arg0.getSource() == saveLog) {
				GUIBuilder.getInstance().getMainFrame().requestFocus();
				if (!log.getText().isEmpty()) {
					JFileChooser fc = new JFileChooser();
					int returnVal = fc.showSaveDialog(logPanel);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						try {
							writeFile(file.getAbsolutePath(), log.getText());
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							LogOperation.writeLog(log,
									UILanguage.ERR_FILE_NOT_FOUND);
						} catch (IOException e) {
							e.printStackTrace();
							LogOperation.writeLog(log, UILanguage.ERR_IO);
						}
						LogOperation.writeLog(log, UILanguage.LOG_SAVED
								+ file.getName() + ".\n");
					} else {
						LogOperation.writeLog(log,
								UILanguage.LOG_SAVE_CANCELLED);
					}
				}
			}
			if (arg0.getSource() == saveBTT) {
				GUIBuilder.getInstance().getMainFrame().requestFocus();
				if (!btt.getText().isEmpty()) {
					JFileChooser fc = new JFileChooser();
					int returnVal = fc.showSaveDialog(bttPanel);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						try {
							writeFile(file.getAbsolutePath(), btt.getText());
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							LogOperation.writeLog(log,
									UILanguage.ERR_FILE_NOT_FOUND);
						} catch (IOException e) {
							e.printStackTrace();
							LogOperation.writeLog(log, UILanguage.ERR_IO);
						}
						LogOperation.writeLog(log, UILanguage.BTT_SAVED
								+ file.getName() + ".\n");
					} else {
						LogOperation.writeLog(log,
								UILanguage.LOG_SAVE_CANCELLED);
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * Used to switch the input devices.
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JComboBox cb = (JComboBox) arg0.getSource();
			String selected = (String) cb.getSelectedItem();
			if (selected.equals(ControllerTyp.types[0])) {
				GUIBuilder.getInstance().getMainFrame().requestFocus();
				LogOperation.writeLog(log, UILanguage.USING_KEYBOARD);
				graphicsPanel.setKeyboardDefaultIcon();
				GUIController.getInstance().getDeviceHandler().setHandler(
						ControllerTyp.types[0]);
			}
			if (selected.equals(ControllerTyp.types[1])) {
				GUIBuilder.getInstance().getMainFrame().requestFocus();
				LogOperation.writeLog(log, UILanguage.USING_GAMEPAD);
				GUIController.getInstance().getDeviceHandler().setHandler(
						ControllerTyp.types[1]);
			}
		}
	}
}
