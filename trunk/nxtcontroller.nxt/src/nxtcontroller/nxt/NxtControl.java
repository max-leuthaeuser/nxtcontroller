package nxtcontroller.nxt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nxtcontroller.shared.CommandId;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Motor;
import lejos.nxt.NXT;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.navigation.TachoPilot;
import lejos.util.Delay;

// TODO javadoc
public final class NxtControl implements ButtonListener {
    private final UltrasonicSensor left = new UltrasonicSensor(SensorPort.S3);
    private final UltrasonicSensor front = new UltrasonicSensor(SensorPort.S1);
    private final UltrasonicSensor right = new UltrasonicSensor(SensorPort.S2);

    private final TachoPilot pilot = new TachoPilot(3.0f, 2.0f, Motor.A,
            Motor.C);

    private final OutputStream out;
    private final InputStream in;

    private final SenderThread senderThread = new SenderThread();

    // TODO fail-safe bluetooth connection
    private NxtControl() {
        System.out.println("Wait for BT...");
        BTConnection con = Bluetooth.waitForConnection();
        System.out.println("Go...");

        out = con.openOutputStream();
        in = con.openInputStream();

        Button.ESCAPE.addButtonListener(this);
    }

    public void start() {
        senderThread.start();
        dispatchCommands();
    }

    private void dispatchCommands() {
        for (;;) {
            switch (receive()) {
            case CommandId.BACKWARD:
                pilot.backward();
                break;
            case CommandId.FORWARD:
                pilot.forward();
                break;
            case CommandId.LEFT:
                // TODO not implemented
                break;
            case CommandId.RIGHT:
                // TODO not implemented
                break;
            case CommandId.STOP:
                pilot.stop();
                break;
            case CommandId.INCREASE_SPEED:
                increaseSpeed();
                break;
            case CommandId.DECREASE_SPEED:
                decreaseSpeed();
                break;
            default:
                // TODO better error reporting
                throw new RuntimeException();
            }
        }
    }

    private void decreaseSpeed() {
        // TODO Auto-generated method stub
    }

    private void increaseSpeed() {
        // TODO Auto-generated method stub
    }

    private int receive() {
        int result;

        try {
            result = in.read();
        } catch (IOException e) {
            result = -1;
        }

        return result;
    }

    public void shutdown() {
        pilot.stop();

        try {
            out.close();
            in.close();
        } catch (IOException e) {
            // TODO handle exception
        }

        NXT.shutDown();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        NxtControl nxt = new NxtControl();
        nxt.start();
    }

    private final class SenderThread extends Thread {
        private static final int DELAY = 250;

        @Override
        public void run() {
            for (;;) {
                sendValues();
                Delay.msDelay(DELAY);
            }
        }

        private void sendValues() {
            try {
                out.write(left.getDistance());
                out.write(front.getDistance());
                out.write(right.getDistance());

                int angle = Math.round(pilot.getAngle());

                out.write(angle);
                out.write(angle >> 8);
                out.flush();
            } catch (IOException e) {
                // TODO better error reporting
                throw new RuntimeException();
            }
        }
    }

    @Override
    public void buttonPressed(Button b) {
    }

    @Override
    public void buttonReleased(Button b) {
        shutdown();
    }
}
