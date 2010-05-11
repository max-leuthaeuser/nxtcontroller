
import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;

public class NXTtr {
    private static DataOutputStream dataOut;
    private static DataInputStream dataIn;
    private static BTConnection BTLink;
    private static int speed = 400, turnSpeed = 270, speedBuffer, speedControl;
    private static int commandValue, transmitReceived;
    private static boolean[] control = new boolean[6];
    private static boolean[] command = new boolean[6];
    private static UltrasonicSensor frontSonic = new UltrasonicSensor(SensorPort.S1);
    private static UltrasonicSensor rightSonic = new UltrasonicSensor(SensorPort.S2);
    private static UltrasonicSensor leftSonic = new UltrasonicSensor(SensorPort.S3);
    private static GetDistances g;

    public static void main(String[] args) {
        connect();
        g = new GetDistances();
        g.start();
        while (!Button.ESCAPE.isPressed()) {
            control = checkCommand();
            speedControl = getSpeed(control);
            move(control, speedControl);
        }
    }//End main

    public static boolean[] checkCommand()//check input data
    {

        try {
            transmitReceived = dataIn.readInt();

            if (transmitReceived == 1) {
                command[0] = true;
            }//forward
            if (transmitReceived == 10) {
                command[0] = false;
            }
            if (transmitReceived == 2) {
                command[1] = true;
            }//backward
            if (transmitReceived == 20) {
                command[1] = false;
            }
            if (transmitReceived == 3) {
                command[2] = true;
            }//leftTurn
            if (transmitReceived == 30) {
                command[2] = false;
            }
            if (transmitReceived == 4) {
                command[3] = true;
            }//rightTurn
            if (transmitReceived == 40) {
                command[3] = false;
            }
            if (transmitReceived == 5) {
                command[0] = false;//stop
                command[1] = false;
                command[2] = false;
                command[3] = false;
            }
            if (transmitReceived == 6) {
                command[4] = true;
            }//speed up
            if (transmitReceived == 60) {
                command[4] = false;
            }
            if (transmitReceived == 7) {
                command[5] = true;
            }//slow down
            if (transmitReceived == 70) {
                command[5] = false;
            } else {
            }
        } catch (IOException ioe) {
            System.out.println("IO Exception readInt");
        }
        return command;

    }//End checkCommand

    public static void move(boolean[] D, int S) {
        int movingSpeed;
        boolean[] direction = new boolean[4];

        direction[0] = D[0];
        direction[1] = D[1];
        direction[2] = D[2];
        direction[3] = D[3];

        movingSpeed = S;

        Motor.A.setSpeed(movingSpeed);
        Motor.C.setSpeed(movingSpeed);

        // forward
        if (direction[0]) {
            LCD.clear();
            Motor.A.forward();
            Motor.C.forward();
        }

        // backward
        if (direction[1]) {
            LCD.clear();
            Motor.A.backward();
            Motor.C.backward();
        }

        // Rotate at place left
        if (direction[2]) {
            LCD.clear();
            Motor.A.setSpeed(turnSpeed);
            Motor.C.setSpeed(turnSpeed);
            Motor.A.forward();
            Motor.C.backward();
        }

        // Rotate at place right
        if (direction[3]) {
            LCD.clear();
            Motor.A.setSpeed(turnSpeed);
            Motor.C.setSpeed(turnSpeed);
            Motor.A.backward();
            Motor.C.forward();
        }

        // Forward and left curve
        if (direction[0] && direction[2]) {
            speedBuffer = movingSpeed + turnSpeed;
            LCD.clear();
            Motor.A.setSpeed(speedBuffer);
            Motor.C.forward();
            Motor.A.forward();
        }

        // Forward and right curve
        if (direction[0] && direction[3]) {
            speedBuffer = movingSpeed + turnSpeed;
            LCD.clear();
            Motor.C.setSpeed(speedBuffer);
            Motor.C.forward();
            Motor.A.forward();
        }
        // Full stop
        if (!direction[0] && !direction[1] && !direction[2] && !direction[3]) {
            Motor.A.stop();
            Motor.C.stop();
        }
    }//End move

    public static void connect() {
        System.out.println("Listening for BT");
        BTLink = Bluetooth.waitForConnection();
        Sound.twoBeeps();
        dataOut = BTLink.openDataOutputStream();
        dataIn = BTLink.openDataInputStream();

    }//End connect

    public static int getSpeed(boolean[] D) {
        boolean accelerate, decelerate;

        accelerate = D[4];
        decelerate = D[5];

        if (accelerate == true) {
            speed += 50;
            command[4] = false;
        }

        if (decelerate == true) {
            speed -= 50;
            command[5] = false;
        }
        return speed;
    }//End getSpeed

    static class GetDistances extends Thread {
        private int getSonicNormalized(UltrasonicSensor s) {
            /*int result = s.getDistance();
            if (result >= 1 && result <= 250) {
                return result;
            }
            // zero means there is no obstacle
            return 0;*/
            return s.getDistance();
        }

        @Override
        public void run() {
            while (!Button.ESCAPE.isPressed()) {
                int front = getSonicNormalized(frontSonic);
                int right = getSonicNormalized(rightSonic);
                int left = getSonicNormalized(leftSonic);
                try {
                    dataOut.writeInt(Integer.parseInt("1111" + front));
                    dataOut.writeInt(Integer.parseInt("2222" + right));
                    dataOut.writeInt(Integer.parseInt("3333" + left));
                    dataOut.flush();
                } catch (IOException ioe) {
                    System.out.println("\nIO Exception writeInt");
                }
                try {
                    this.sleep(50);
                } catch (Exception e) {
                    System.out.print("Sleep Interupt Exception.");
                }
            }
        }
    }
}//NXTtr Class

