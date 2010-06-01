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
import nxtcontroller.pc.core.InputStreamListener;
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
	private NXTInfo[] availableNXTs = null;
	private NXTConnector nxtConn;
	private JPanel t2, t1;
	private JPanel bttP, logP;

	/**
	 * Builds all control elements and attach mouse listener.
	 */
	public ApplicationControlPanel() {
		super();
		bh = new MouseHandler();
		setLayout(new GridLayout(2, 0));
		controllerBox = new JComboBox();
		controllerBox.setEditable(false);
		connect = new JButton();
		connect.addMouseListener(bh);
		graphicsPanel = new GraphicsPanel();
		searchID = new JButton();
		searchID.addMouseListener(bh);
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
		t1 = new JPanel();
		logP = new JPanel();
		logP.setLayout(new GridLayout(1, 1));
		logP.add(new JScrollPane(log));
		JSplitPane logPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				logP, clearLogPanel);
		logPane.setPreferredSize(new Dimension(
				StaticSizes.APPLICATION_SIZE_WIDTH / 2 - 11,
				StaticSizes.APPLICATION_SIZE_HEIGTH / 3 - 90));
		logPane.setDividerLocation(255);
		t1.add(logPane);
		t2 = new JPanel();
		bttP = new JPanel();
		bttP.setLayout(new GridLayout(1, 1));
		bttP.add(new JScrollPane(btt));
		JSplitPane logPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				bttP, clearBttPanel);
		logPane2.setPreferredSize(new Dimension(
				StaticSizes.APPLICATION_SIZE_WIDTH / 2 - 11,
				StaticSizes.APPLICATION_SIZE_HEIGTH / 3 - 90));
		logPane2.setDividerLocation(255);
		t2.add(logPane2);
		textPanel.add(t1);
		textPanel.add(t2);
		add(textPanel);
		setUIText();
	}

	/**
	 * Create the application log.
	 */
	private void buildLogPanel() {
		saveLog = new JButton();
		saveLog.addMouseListener(bh);
		clearLog = new JButton();
		clearLog.addMouseListener(bh);
		logPanel = new JPanel();
		logPanel.setLayout(new GridLayout(1, 0));
		log = new JEditorPane();
		log.setEditable(false);
		log.setContentType("text/html");
		clearLogPanel = new JPanel();
		clearLogPanel.setLayout(new BorderLayout());
		clearLogPanel.add(saveLog, BorderLayout.NORTH);
		clearLogPanel.add(clearLog, BorderLayout.SOUTH);
	}

	/**
	 * Create the Bluetooth trace log.
	 */
	private void buildBTTracePanel() {
		saveBTT = new JButton();
		saveBTT.addMouseListener(bh);
		clearBTT = new JButton();
		clearBTT.addMouseListener(bh);
		bttPanel = new JPanel();
		bttPanel.setLayout(new GridLayout(1, 0));
		btt = new JEditorPane();
		btt.setEditable(false);
		btt.setContentType("text/html");
		clearBttPanel = new JPanel();
		clearBttPanel.setLayout(new BorderLayout());
		clearBttPanel.add(saveBTT, BorderLayout.NORTH);
		clearBttPanel.add(clearBTT, BorderLayout.SOUTH);
	}

	/**
	 * Sets every GUI text in the application.
	 */
	public void setUIText() {
		saveLog.setToolTipText(UILanguage.getSaveHint());
		saveLog.setText(UILanguage.getSave());
		clearLog.setText(UILanguage.getClear());
		saveBTT.setText(UILanguage.getSave());
		clearLog.setToolTipText(UILanguage.getClearHint());
		clearBTT.setToolTipText(UILanguage.getClearHint());
		clearBTT.setText(UILanguage.getClear());
		saveBTT.setToolTipText(UILanguage.getSaveHint());
		bttPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.gray), UILanguage.getBtt()));
		logPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.gray), UILanguage.getLog()));
		t2.setBorder(BorderFactory.createTitledBorder(UILanguage.getBtt()));
		t1.setBorder(BorderFactory.createTitledBorder(UILanguage.getLog()));
		vSplitPane.setBorder(BorderFactory.createTitledBorder(UILanguage
				.getControlpanel()));
		searchID.setToolTipText(UILanguage.getSearchHint());
		connect.setToolTipText(UILanguage.getConnectHint());
		searchID.setText(UILanguage.getSearch());
		connect.setText(UILanguage.getConnect());
		controllerBox.setToolTipText(UILanguage.getOptionHint());

		controllerBox.removeActionListener(bh);
		controllerBox.removeAllItems();
		controllerBox.addItem(ControllerTyp.getTypes()[0]);
		controllerBox.addItem(ControllerTyp.getTypes()[1]);
		controllerBox.addActionListener(bh);
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
	 * Write a String to a local file.
	 * 
	 * @param path
	 * @param text
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeFile(final String path, final String text)
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
		LogOperation.writeBttLog(UILanguage.getConnectSearching());

		availableNXTs = null;
		nxtConn = new NXTConnector();

		Thread searchThread = new Thread() {
			@Override
			public void run() {
				availableNXTs = nxtConn.search("*", null,
						NXTCommFactory.BLUETOOTH);

				if (availableNXTs.length == 0) {
					LogOperation.writeBttLog(UILanguage
							.getConnectNoNxtAvailable());
					return;
				}

				String[] result = new String[availableNXTs.length];
				int i = 0;
				for (NXTInfo n : availableNXTs) {
					result[i] = n.name + "@" + n.deviceAddress;
					i++;
				}

				String s = (String) JOptionPane.showInputDialog(GUIBuilder
						.getInstance().getMainFrame(), UILanguage
						.getConnectDialogHint(), UILanguage
						.getConnectDialogHeadline(), JOptionPane.PLAIN_MESSAGE,
						null, result, "");

				if ((s != null) && (s.length() > 0)) {
					nxtConn.connectTo(s.substring(0, s.indexOf("@")), s
							.substring(s.indexOf("@") + 1, s.length()),
							NXTCommFactory.BLUETOOTH);

					OutputStream out = nxtConn.getOutputStream();

					if (out != null) {
						LogOperation.writeBttLog(UILanguage.getConnectReady()
								+ s);
						RemoteController rc = new RemoteController(out);
						GUIController.getInstance().getDeviceHandler()
								.getKeyboardHandler().setRemoteController(rc);
						GUIController.getInstance().getDeviceHandler()
								.getGamepadHandler().setRemoteController(rc);

						InputStreamListener isListener = new InputStreamListener(
								nxtConn.getInputStream());
						isListener.register(GUIBuilder.getInstance()
								.getSensorPanel());
						new Thread(isListener).start();
					} else {
						LogOperation.writeBttLog(UILanguage.getConnectAbort()
								+ ": " + s);
					}
					return;
				} else
					LogOperation.writeBttLog(UILanguage.getConnectAbort());
			}
		};
		searchThread.start();
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
					LogOperation.writeAppLog(UILanguage.getGamepadAvailable());
				GUIBuilder.getInstance().getMainFrame().requestFocus();
			}
			if (arg0.getSource() == clearLog) {
				LogOperation.clearAppLog();
				GUIBuilder.getInstance().getMainFrame().requestFocus();
			}
			if (arg0.getSource() == clearBTT) {
				LogOperation.clearBttLog();
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
							LogOperation.writeAppLog(UILanguage
									.getErrFileNotFound());
						} catch (IOException e) {
							e.printStackTrace();
							LogOperation.writeAppLog(UILanguage.getErrIo());
						}
						LogOperation.writeAppLog(UILanguage.getLogSaved()
								+ file.getName() + ".\n");
					} else {
						LogOperation.writeAppLog(UILanguage
								.getLogSaveCancelled());
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
							LogOperation.writeAppLog(UILanguage
									.getErrFileNotFound());
						} catch (IOException e) {
							e.printStackTrace();
							LogOperation.writeAppLog(UILanguage.getErrIo());
						}
						LogOperation.writeAppLog(UILanguage.getBttSaved()
								+ file.getName() + ".\n");
					} else {
						LogOperation.writeAppLog(UILanguage
								.getLogSaveCancelled());
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
			if (selected.equals(ControllerTyp.getTypes()[0])) {
				GUIBuilder.getInstance().getMainFrame().requestFocus();
				LogOperation.writeAppLog(UILanguage.getUsingKeyboard());
				graphicsPanel.setKeyboardDefaultIcon();
				GUIController.getInstance().getDeviceHandler().setHandler(
						ControllerTyp.getTypes()[0]);
			}
			if (selected.equals(ControllerTyp.getTypes()[1])) {
				GUIBuilder.getInstance().getMainFrame().requestFocus();
				LogOperation.writeAppLog(UILanguage.getUsingGamepad());
				GUIController.getInstance().getDeviceHandler().setHandler(
						ControllerTyp.getTypes()[1]);
			}
		}
	}
}
