
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public class DistancesPanel extends JPanel {

    private JLabel distancesFront;
    private JLabel distancesRight;
    private JLabel distancesLeft;
    private JLabel warningLabel;
    private int rotation = 90;
    private int front = 70;
    private int right = 70;
    private int left = 70;
    private final int PANEL_SIZE = 200;

    public DistancesPanel() {
        setBackground(Color.white);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        //this.drdrawRect(4,1,1,3);
        distancesFront = new JLabel();
        distancesRight = new JLabel();
        distancesLeft = new JLabel();
        warningLabel = new JLabel();
        add(distancesFront);
        add(distancesRight);
        add(distancesLeft);
        add(warningLabel);
        warningLabel.setForeground(Color.red);
    }

    public void drawRotation(int degree) {
        rotation = degree + 90;
        repaint();
    }

    public void drawDistances(int source, int distance) {
        switch (source) {
            case 1:
                front = distance;
                distancesFront.setText("Front: " + distance);
                break;
            case 2:
                right = distance;
                distancesRight.setText("Right: " + distance);
                break;
            case 3:
                left = distance;
                distancesLeft.setText("Left: " + distance);
                break;
            default:
                System.out.println("No valid distance.");
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        warningLabel.setText("");
        // draw border for collision box
        g.drawRect(120, 1, 250, PANEL_SIZE - 5);
        // draw border for rotation compass
        g.drawRect(390, 55, 80, 80);
        g.drawString("Rotation", 390, 48);
        g.drawString("0°", 427, 70);
        g.drawString("90°", 450, 100);
        g.drawString("180°", 420, 130);
        g.drawString("270°", 392, 100);

        // draw the rotation arrow
        Point point2 = new Point(430,95);
        Graphics2D graphics2D = (Graphics2D) g;
        AffineTransform at = AffineTransform.getTranslateInstance(point2.x, point2.y);
        int b = 8;
        double theta = Math.toRadians(45);
        // using a GeneralPath to create three lines that make up the arrow
        // (only) one time and then use AffineTransform to
        // rotate it.
        GeneralPath path = new GeneralPath();

        // Add the arrow shaft.
        path.moveTo(-15, 0);
        path.lineTo(15, 0);
        // Start a new line segment from the position of (15,0).
        path.moveTo(15, 0);
        // Create one of the two arrow head lines.
        int x = (int) (-b * Math.cos(theta));
        int y = (int) (b * Math.sin(theta));
        path.lineTo(x+15, y);
        // Make the other arrow head line.
        x = (int) (-b * Math.cos(-theta));
        y = (int) (b * Math.sin(-theta));
        path.moveTo(15, 0);
        path.lineTo(x+15, y);

        // theta was used for the angle between the arrow
        // shaft and each of its arrow head lines.
        // in addition we rotate the arrow
        // using rotation degree
        at.rotate(Math.toRadians(-rotation));
        Shape shape = at.createTransformedShape(path);
        graphics2D.setPaint(Color.green);
        graphics2D.draw(shape);

        g.setColor(Color.black);
        // draw vehicle
        g.drawRect(217, 80, 60, 65);
        // tires - front left
        g.fillRect(212, 85, 5, 15);
        // tires - back left
        g.fillRect(212, 125, 5, 15);
        // tires - front right
        g.fillRect(277, 85, 5, 15);
        // tires - back right
        g.fillRect(277, 125, 5, 15);

        // draw obstacles now
        // front
        int drawFront = 0;
        if (front < 61) {
            drawFront = 62 - front;
        }
        if (front <= 10) {
            g.setColor(Color.red);
            warningLabel.setText("Collision warning!");
        }
        g.fillRect(217, 5 + drawFront, 60, 10);
        g.setColor(Color.black);
        // right
        int drawRight = 0;
        if (right < 61) {
            drawRight = 72 - right;
        }
        if (right <= 10) {
            g.setColor(Color.red);
            warningLabel.setText("Collision warning!");
        }
        g.fillRect(356 - drawRight, 80, 10, 65);
        g.setColor(Color.black);
        // left
        int drawLeft = 0;
        if (left < 61) {
            drawLeft = 72 - left;
        }
        if (left <= 10) {
            g.setColor(Color.red);
            warningLabel.setText("Collision warning!");
        }
        g.fillRect(124 + drawLeft, 80, 10, 65);
    }
}
