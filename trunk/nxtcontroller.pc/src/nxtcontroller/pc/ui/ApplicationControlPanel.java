package nxtcontroller.pc.ui;
/**
 * @author Max Leuthäuser
 * 
 */

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
import javax.swing.*;

public class ApplicationControlPanel extends JPanel {
	private static final long serialVersionUID = 2963071726378133381L;

	private JSplitPane vSplitPane;
	private JPanel logPanel, clearLogPanel, clearBttPanel, bttPanel;
	private GraphicsPanel graphicsPanel;
	private JTextArea log, btt;
	private JButton clearLog, clearBTT, saveLog, saveBTT;
	private MouseHandler bh;
	private JComboBox controllerBox;
	private JButton connect;
	private final String[] useableController = { "Keyboard", "Gamepad" };

	public ApplicationControlPanel() {
		super();
		bh = new MouseHandler();
		setLayout(new GridLayout(2, 0));
		controllerBox = new JComboBox(useableController);
		controllerBox.setEditable(false);

		controllerBox.addActionListener(bh);
		connect = new JButton("Connect");
		connect.addMouseListener(bh);
		graphicsPanel = new GraphicsPanel();

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.add(controllerBox, BorderLayout.NORTH);
		buttonPanel.add(connect, BorderLayout.SOUTH);

		vSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				graphicsPanel, buttonPanel);
		vSplitPane.setBorder(BorderFactory.createTitledBorder("Control options"));

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
		JSplitPane logPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				new JScrollPane(log), clearLogPanel);
		t1.setBorder(BorderFactory.createTitledBorder("Application Log"));
		logPane.setPreferredSize(new Dimension(
				StaticSizes.APPLICATION_SIZE_WIDTH / 2 - 11,
				StaticSizes.APPLICATION_SIZE_HEIGTH / 3 - 90));
		logPane.setDividerLocation(230);
		t1.add(logPane);

		JPanel t2 = new JPanel();
		JSplitPane logPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				new JScrollPane(btt), clearBttPanel);
		t2.setBorder(BorderFactory.createTitledBorder("Bluetooth Trace Log"));
		logPane2.setPreferredSize(new Dimension(
				StaticSizes.APPLICATION_SIZE_WIDTH / 2 - 11,
				StaticSizes.APPLICATION_SIZE_HEIGTH / 3 - 90));
		logPane2.setDividerLocation(230);
		t2.add(logPane2);

		textPanel.add(t1);
		textPanel.add(t2);

		add(textPanel);
		textPanel.setBackground(Color.RED);
	}

	private void buildLogPanel() {
		saveLog = new JButton("Save");
		saveLog.addMouseListener(bh);
		clearLog = new JButton("Clear");
		clearLog.addMouseListener(bh);
		logPanel = new JPanel();
		logPanel.setLayout(new GridLayout(1, 0));
		log = new JTextArea();
		log.setEditable(false);
		logPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.gray), "Log"));
		clearLogPanel = new JPanel();
		clearLogPanel.setLayout(new BorderLayout());
		clearLogPanel.add(saveLog, BorderLayout.NORTH);
		clearLogPanel.add(clearLog, BorderLayout.SOUTH);

	}

	private void buildBTTracePanel() {
		saveBTT = new JButton("Save");
		saveBTT.addMouseListener(bh);
		clearBTT = new JButton("Clear");
		clearBTT.addMouseListener(bh);
		bttPanel = new JPanel();
		bttPanel.setLayout(new GridLayout(1, 0));
		btt = new JTextArea();
		btt.setEditable(false);
		bttPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.gray), "Log"));
		clearBttPanel = new JPanel();
		clearBttPanel.setLayout(new BorderLayout());
		clearBttPanel.add(saveBTT, BorderLayout.NORTH);
		clearBttPanel.add(clearBTT, BorderLayout.SOUTH);
	}

	public JTextArea getLog() {
		return this.log;
	}

	public JTextArea getBTT() {
		return this.btt;
	}

	private void writeFile(String path, String text)
			throws FileNotFoundException, IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(path));
		out.write(text);
		out.close();
	}

	private class MouseHandler implements MouseListener, ActionListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub

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
			if (arg0.getSource() == clearLog) {
				log.setText("");
			}
			if (arg0.getSource() == clearBTT) {
				btt.setText("");
			}
			if (arg0.getSource() == saveLog) {
				if (!log.getText().isEmpty()) {
					JFileChooser fc = new JFileChooser();
					int returnVal = fc.showSaveDialog(logPanel);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						try {
							writeFile(file.getAbsolutePath(), log.getText());
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							log.append("Error: File not found!");
						} catch (IOException e) {
							e.printStackTrace();
							log.append("I/O Error!");
						}
						log.append("Log saved to: " + file.getName() + ".\n");
					} else {
						log.append("Save command cancelled by user.\n");
					}
				}
			}
			if (arg0.getSource() == saveBTT) {
				if (!btt.getText().isEmpty()) {
					JFileChooser fc = new JFileChooser();
					int returnVal = fc.showSaveDialog(bttPanel);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						try {
							writeFile(file.getAbsolutePath(), btt.getText());
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							log.append("Error: File not found!");
						} catch (IOException e) {
							e.printStackTrace();
							log.append("I/O Error!");
						}
						log.append("Bluetooth Trace saved to: "
								+ file.getName() + ".\n");
					} else {
						log.append("Save command cancelled by user.\n");
					}
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JComboBox cb = (JComboBox) arg0.getSource();
			String selected = (String) cb.getSelectedItem();
			if (selected.equals(useableController[0])) {
				GUIController.getInstance().writeLog("Using Keyboard now ...");
				graphicsPanel.setIcon(graphicsPanel.getKEYBOARD_NO_ACTION());
			}
			if (selected.equals(useableController[1])) {
				GUIController.getInstance().writeLog("Using Gamepad now ...");
				graphicsPanel.setIcon(graphicsPanel.getGAMEPAD_NO_ACTION());
			}
		}
	}
}
