
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;
import lejos.pc.comm.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GamePadViewer extends JFrame {

    private static final int DELAY = 40;   // ms (polling interval)
    // needs to be fast to catch fast button pressing
    private GamePadController gpController;
    private DistancesPanel distancesView;
    private CompassPanel hatPanel, zPanel;
    private ButtonsPanel buttonsPanel;   // shows which buttons are pressed
    private Timer pollTimer;   // timer which triggers the polling
    public static JButton quit, connect;
    public static JButton forward, reverse, leftTurn, rightTurn, stop, speedUp, slowDown;
    public static JLabel L1, L2, L3, L4, L5, L6, L7, L8, L9, L10;
    public static StatusBar status;
    public ButtonHandler bh = new ButtonHandler();
    public static DataOutputStream outData;
    public static DataInputStream inData;
    public static NXTConnector link;
    private static boolean isConnected = false;
    private Container c;
    private int lastSpeedWas = 4;
    private int lastDirWas = 4;
    private int lastButtonWas = 0;
    private int rotation = 0;

    public GamePadViewer() {
        super("NXT BT Gamepad Controller");

        gpController = new GamePadController();

        makeGUI();

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                if (gpController.gamePadIsAvailable) {
                    pollTimer.stop();   // stop the timer
                }
                System.exit(0);
            }
        });

        pack();
        setResizable(false);
        setVisible(true);
        if (gpController.gamePadIsAvailable) {
            startPolling();
        } else {
            status.setMessage(status.getText()
                    + " (No gamepad found.)");
        }
    } // end of GamePadViewer()

    public class StatusBar extends JLabel {

        /** Creates a new instance of StatusBar */
        public StatusBar() {
            super();
            super.setPreferredSize(new Dimension(100, 16));
            setMessage("Ready");
        }

        public void setMessage(String message) {
            setText(" " + message);
        }
    }

    private void makeGUI() {
        c = getContentPane();
        c.setLayout(new BorderLayout());
        // Main panel which will hold all the other components
        JPanel main = new JPanel(new BorderLayout());

        // DistancesView panel which shows the current values
        // of the 3 uSonic sensors
        distancesView = new DistancesPanel();
        c.add(distancesView, BorderLayout.NORTH);

        // ControllerView panel which displays the status
        // of the gamepad input device
        JPanel controllerView = new JPanel();
        buttonsPanel = new ButtonsPanel();
        controllerView.add(buttonsPanel);
        controllerView.setLayout(new BoxLayout(controllerView, BoxLayout.X_AXIS));
        // three CompassPanels in a row
        hatPanel = new CompassPanel("+ / -");
        controllerView.add(hatPanel);
        zPanel = new CompassPanel("< / >");
        controllerView.add(zPanel);
        main.add(controllerView, BorderLayout.NORTH);

        // Button panel which holds alle the buttons
        // to controll die NXT via mouse
        JPanel pButtons = new JPanel();
        pButtons.setLayout(new GridLayout(4, 5));

        L1 = new JLabel("");
        pButtons.add(L1);
        forward = new JButton("Forward");
        forward.addActionListener(bh);
        forward.addMouseListener(bh);
        forward.addKeyListener(bh);
        pButtons.add(forward);
        L2 = new JLabel("");
        pButtons.add(L2);
        L3 = new JLabel("");
        pButtons.add(L3);
        speedUp = new JButton("Accelerate");
        speedUp.addActionListener(bh);
        speedUp.addMouseListener(bh);
        speedUp.addKeyListener(bh);
        pButtons.add(speedUp);

        leftTurn = new JButton("Left");
        leftTurn.addActionListener(bh);
        leftTurn.addMouseListener(bh);
        leftTurn.addKeyListener(bh);
        pButtons.add(leftTurn);
        stop = new JButton("Stop");
        stop.addActionListener(bh);
        stop.addMouseListener(bh);
        stop.addKeyListener(bh);
        pButtons.add(stop);

        rightTurn = new JButton("Right");
        rightTurn.addActionListener(bh);
        rightTurn.addMouseListener(bh);
        rightTurn.addKeyListener(bh);
        pButtons.add(rightTurn);
        L4 = new JLabel("");
        pButtons.add(L4);
        slowDown = new JButton("Decelerate");
        slowDown.addActionListener(bh);
        slowDown.addMouseListener(bh);
        slowDown.addKeyListener(bh);
        pButtons.add(slowDown);

        L5 = new JLabel("");
        pButtons.add(L5);
        reverse = new JButton("Reverse");
        reverse.addActionListener(bh);
        reverse.addMouseListener(bh);
        reverse.addKeyListener(bh);
        pButtons.add(reverse);

        L6 = new JLabel("");
        pButtons.add(L6);
        L7 = new JLabel("");
        pButtons.add(L7);
        L8 = new JLabel("");
        pButtons.add(L8);

        connect = new JButton(" Connect ");
        connect.addActionListener(bh);
        connect.addKeyListener(bh);
        pButtons.add(connect);

        L9 = new JLabel("");
        pButtons.add(L9);
        L10 = new JLabel("");
        pButtons.add(L10);

        quit = new JButton("Quit");
        quit.addActionListener(bh);
        pButtons.add(quit);

        main.add(pButtons, BorderLayout.CENTER);
        status = new StatusBar();
        status.setFont(new Font("Calibri", Font.TRUETYPE_FONT, 11));
        status.setForeground(Color.DARK_GRAY);
        status.setMessage("Click on connect to use Bluetooth with the NXT.");
        status.setBorder(BorderFactory.createEmptyBorder());
        main.add(status, BorderLayout.SOUTH);

        // adding main panel finally
        c.add(main, BorderLayout.SOUTH);
    }  // end of makeGUI()

    private void startPolling() {
        ActionListener pollPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                gpController.poll();
                // update the GUI:
                // get directions from analog sticks
                int speedDir = gpController.getXYStickDir();
                int compassDir = gpController.getZRZStickDir();
                // visualize analog stick status
                hatPanel.setCompass(speedDir);
                zPanel.setCompass(compassDir);
                boolean[] buttons = gpController.getButtons();
                // visualize button status
                buttonsPanel.setButtons(buttons);

                // Recover Speed
                if (isConnected && speedDir == 4) {
                    if (lastSpeedWas == 7) {
                        bh.keyManReleased('s');
                    }
                    if (lastSpeedWas == 1) {
                        bh.keyManReleased('w');
                    }

                }
                // Recover direction
                if (isConnected && compassDir == 4) {
                    if (lastDirWas == 5) {
                        bh.keyManReleased('d');
                    }
                    if (lastDirWas == 3) {
                        bh.keyManReleased('a');
                    }

                }

                //Controll Speed
                if (isConnected && speedDir != 4) {
                    if (speedDir == 1) {
                        lastSpeedWas = 1;
                        bh.keyManTyped('w');
                    }
                    if (speedDir == 7) {
                        lastSpeedWas = 7;
                        bh.keyManTyped('s');
                    }
                }
                // Controll direction
                if (isConnected && compassDir != 4) {
                    if (compassDir == 3) {
                        lastDirWas = 3;
                        bh.keyManTyped('a');
                    }
                    if (compassDir == 5) {
                        lastDirWas = 5;
                        bh.keyManTyped('d');
                    }
                }
                // Controll acceleration
                if (isConnected && buttons[0]) {
                    lastButtonWas = 10;
                    bh.keyManTyped('i');
                }
                if (isConnected && buttons[1]) {
                    lastButtonWas = 11;
                    bh.keyManTyped('k');
                }
            }
        };
        pollTimer = new Timer(DELAY, pollPerformer);

        pollTimer.start();
    }

    private class ButtonHandler implements ActionListener, MouseListener, KeyListener {
//***********************************************************************
        //Buttons action

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == quit) {
                System.exit(0);
            }
            if (ae.getSource() == connect) {
                connect();
            }
        }//End ActionEvent(for buttons)

//***********************************************************************
//Mouse actions
        public void mouseClicked(MouseEvent arg0) {
        }

        public void mouseEntered(MouseEvent arg0) {
        }

        public void mouseExited(MouseEvent arg0) {
        }

        public void mousePressed(MouseEvent moe) {
            try {
                if (moe.getSource() == forward) {
                    outData.writeInt(1);
                }
                if (moe.getSource() == reverse) {
                    outData.writeInt(2);
                }
                if (moe.getSource() == leftTurn) {
                    outData.writeInt(3);
                    if (rotation > 360) {
                        rotation = 0;
                    }
                    rotation += 15;
                    distancesView.drawRotation(rotation);
                }
                if (moe.getSource() == rightTurn) {
                    outData.writeInt(4);
                    if (rotation < -360) {
                        rotation = 0;
                    }
                    rotation -= 15;
                    distancesView.drawRotation(rotation);
                }
                if (moe.getSource() == speedUp) {
                    outData.writeInt(6);
                }
                if (moe.getSource() == slowDown) {
                    outData.writeInt(7);
                }

                outData.flush();
            } catch (IOException ioe) {
                System.out.println("\nIO Exception writeInt");
            }

        }//End mousePressed

        public void mouseReleased(MouseEvent moe) {
            try {
                if (moe.getSource() == forward
                        || moe.getSource() == reverse
                        || moe.getSource() == leftTurn
                        || moe.getSource() == rightTurn) {
                    outData.writeInt(5);
                }
                if (moe.getSource() == slowDown) {
                    outData.writeInt(60);
                }
                if (moe.getSource() == speedUp) {
                    outData.writeInt(70);
                }

                outData.flush();
            } catch (IOException ioe) {
                System.out.println("\nIO Exception writeInt");
            }

        }//End mouseReleased

//***********************************************************************
//Keyboard action
        public void keyPressed(KeyEvent ke) {
        }//End keyPressed

        public void keyManTyped(char ke) {
            try {
                if (ke == 'w') {
                    outData.writeInt(1);
                }
                if (ke == 's') {
                    outData.writeInt(2);
                }
                if (ke == 'a') {
                    outData.writeInt(3);
                    if (rotation > 360) {
                        rotation = 0;
                    }
                    rotation += 15;
                    distancesView.drawRotation(rotation);
                }
                if (ke == 'd') {
                    outData.writeInt(4);
                    if (rotation < -360) {
                        rotation = 0;
                    }
                    rotation -= 15;
                    distancesView.drawRotation(rotation);
                }
                if (ke == 'i') {
                    outData.writeInt(6);
                }
                if (ke == 'k') {
                    outData.writeInt(7);
                }
                outData.flush();
            } catch (IOException ioe) {
                System.out.println("\nIO Exception writeInt");
            }
        }

        public void keyTyped(KeyEvent ke) {
            try {
                if (ke.getKeyChar() == 'w') {
                    outData.writeInt(1);
                }
                if (ke.getKeyChar() == 's') {
                    outData.writeInt(2);
                }
                if (ke.getKeyChar() == 'a') {
                    outData.writeInt(3);
                    if (rotation > 360) {
                        rotation = 0;
                    }
                    rotation += 15;
                    distancesView.drawRotation(rotation);
                }
                if (ke.getKeyChar() == 'd') {
                    outData.writeInt(4);
                    if (rotation < -360) {
                        rotation = 0;
                    }
                    rotation -= 15;
                    distancesView.drawRotation(rotation);
                }
                if (ke.getKeyChar() == 'i') {
                    outData.writeInt(6);
                }
                if (ke.getKeyChar() == 'k') {
                    outData.writeInt(7);
                }

                outData.flush();
            } catch (IOException ioe) {
                System.out.println("\nIO Exception writeInt");
            }

        }//End keyTyped

        public void keyReleased(KeyEvent ke) {
            try {
                if (ke.getKeyChar() == 'w') {
                    outData.writeInt(10);
                }
                if (ke.getKeyChar() == 's') {
                    outData.writeInt(20);
                }
                if (ke.getKeyChar() == 'a') {
                    outData.writeInt(30);
                }
                if (ke.getKeyChar() == 'd') {
                    outData.writeInt(40);
                }
                if (ke.getKeyChar() == 'i') {
                    outData.writeInt(60);
                }
                if (ke.getKeyChar() == 'k') {
                    outData.writeInt(70);
                }
                if (ke.getKeyChar() == 'q') {
                    System.exit(0);
                }

                outData.flush();
            } catch (IOException ioe) {
                System.out.println("\nIO Exception writeInt");
            }
        }//End keyReleased

        public void keyManReleased(char ke) {
            try {
                if (ke == 'w') {
                    outData.writeInt(10);
                }
                if (ke == 's') {
                    outData.writeInt(20);
                }
                if (ke == 'a') {
                    outData.writeInt(30);
                }
                if (ke == 'd') {
                    outData.writeInt(40);
                }
                if (ke == 'i') {
                    outData.writeInt(60);
                }
                if (ke == 'k') {
                    outData.writeInt(70);
                }
                if (ke == 'q') {
                    System.exit(0);
                }
                outData.flush();
            } catch (IOException ioe) {
                System.out.println("\nIO Exception writeInt");
            }
        }//End keyReleased
    }//End ButtonHandler

    private void connect() {
        status.setMessage("Connecting... ");
        link = new NXTConnector();
        try {
            if (!link.connectTo("btspp://")) {
                System.out.println("\nNo NXT available with BT");
                status.setMessage("Connection failed. No NXT available via Bluetooth.");
            } else {
                outData = link.getDataOut();
                inData = link.getDataIn();
                System.out.println("\nNXT is Connected");
                isConnected = true;
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                ScheduledFuture<?> fut = scheduler.scheduleAtFixedRate(
                        new Runnable() {

                            public void run() {
                                status.setMessage(link.getNXTInfo().connectionState.name() + " on "
                                        + link.getNXTInfo().name
                                        + "@" + link.getNXTInfo().deviceAddress);
                            }
                        }, 1, 3, TimeUnit.SECONDS);
                DrawDistances d = new DrawDistances();
                d.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//End connect

    private class DrawDistances extends Thread {

        @Override
        public void run() {
            LinkedList<Integer> frontLastFiveValues = new LinkedList<Integer>();
            LinkedList<Integer> rightLastFiveValues = new LinkedList<Integer>();
            LinkedList<Integer> leftLastFiveValues = new LinkedList<Integer>();
            int currentNumber = 0;
            int result = 0;
            int dataInFromNXT = 0;
            while (true) {
                result = 0;
                dataInFromNXT = 0;
                try {
                    dataInFromNXT = inData.readInt();
                } catch (IOException ioe) {
                    System.out.println("\nIO Exception readUTF");
                }
                if (("" + dataInFromNXT).startsWith("1111")) {
                    currentNumber = Integer.parseInt(("" + dataInFromNXT).substring(4));
                    if (frontLastFiveValues.size() == 3) {
                        int counter = 0;
                        for (int i : frontLastFiveValues) {
                            if (i >= 200) {
                                counter++;
                            }
                        }
                        if (counter <= 1) {
                            counter = 0;
                            for (int i : frontLastFiveValues) {
                                if (i < 200) {
                                    result += i;
                                    counter++;
                                }
                            }
                            distancesView.drawDistances(1, result / counter);
                        } else {
                            distancesView.drawDistances(1, 255);
                        }
                        frontLastFiveValues.clear();
                    } else {
                        frontLastFiveValues.add(currentNumber);
                    }
                }
                if (("" + dataInFromNXT).startsWith("2222")) {
                    currentNumber = Integer.parseInt(("" + dataInFromNXT).substring(4));
                    if (rightLastFiveValues.size() == 3) {
                        int counter = 0;
                        for (int i : rightLastFiveValues) {
                            if (i >= 200) {
                                counter++;
                            }
                        }
                        if (counter <= 1) {
                            counter = 0;
                            for (int i : rightLastFiveValues) {
                                if (i < 200) {
                                    result += i;
                                    counter++;
                                }
                            }
                            distancesView.drawDistances(2, result / counter);
                        } else {
                            distancesView.drawDistances(2, 255);
                        }
                        rightLastFiveValues.clear();
                    } else {
                        rightLastFiveValues.add(currentNumber);
                    }
                }
                if (("" + dataInFromNXT).startsWith("3333")) {
                    currentNumber = Integer.parseInt(("" + dataInFromNXT).substring(4));
                    if (leftLastFiveValues.size() == 3) {
                        int counter = 0;
                        for (int i : leftLastFiveValues) {
                            if (i >= 200) {
                                counter++;
                            }
                        }
                        if (counter <= 1) {
                            counter = 0;
                            for (int i : leftLastFiveValues) {
                                if (i < 200) {
                                    result += i;
                                    counter++;
                                }
                            }
                            distancesView.drawDistances(3, result / counter);
                        } else {
                            distancesView.drawDistances(3, 255);
                        }
                        leftLastFiveValues.clear();
                    } else {
                        leftLastFiveValues.add(currentNumber);
                    }
                }
            }
        }
    }

    public static void main(String args[]) {
        new GamePadViewer();
    }
}  // end of GamePadViewer class

