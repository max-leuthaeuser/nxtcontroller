import javax.swing.*;
import java.awt.*;

public class ButtonsPanel extends JPanel
{
  // background colours for the textfields (game pad buttons)
  private static final Color OFF_COLOUR = Color.LIGHT_GRAY;
  private static final Color ON_COLOUR = Color.YELLOW;
  private final String[] labelTxt = {"faster:", "slower:"};
  private JTextField buttonTFs[];   // represents the game pad buttons


  public ButtonsPanel()
  /* Add the textfields to the panel and store references to
     them in a buttonTFs[] array. 

     Each textfield contains a number, is uneditable, and starts
     with an OFF_COLOUR background (meaning it's not pressed).
  */
  {
    setBackground(Color.white);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // initialize buttonTFs[]
    buttonTFs = new JTextField[2];
    for (int i=0; i < 2; i++) {
      buttonTFs[i] = new JTextField(""+(i+1), 2);
      buttonTFs[i].setEditable(false);
      buttonTFs[i].setBackground(OFF_COLOUR);
      buttonTFs[i].setHorizontalAlignment(JTextField.CENTER);
    }
    makeRow(0, 2/2);   // 1st row
    makeRow(2/2, 2);  // 2nd row
  }  // end of ButtonsPanel()


  private void makeRow(int start, int end)
  // a row of textfields from buttonTFs[start] to buttonTFs[end-1]
  {
    JPanel rowPanel = new JPanel();
    rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS)); // horiz box layout

    JPanel p;
    JLabel txtFaster = new JLabel(labelTxt[0]);
    JLabel txtSlower = new JLabel(labelTxt[1]);
    JLabel[] t = new JLabel[2];
    t[0] = txtFaster;
    t[1] = txtSlower;
    for (int i=start; i < end; i++) {
      p = new JPanel();
      p.setBackground(Color.white);
      p.add(t[i]);
      p.add( buttonTFs[i]);  // add button to its own panel p to stop resizing
      rowPanel.add(p);   // add panel p to row
    }
    add(rowPanel);
  }  // end of makeRow()


  public void setButtons(boolean bVals[])
  /* Use the bVals[] array to switch the buttonTFs on/off by
     changing the background colour of their textfields.
  */
  {
    if (bVals.length != GamePadController.NUM_BUTTONS)
      System.out.println("Writing number of button values");
    else {
      Color c;
      for (int i=0; i < 2; i++) {
        c = (bVals[i] == true) ? ON_COLOUR : OFF_COLOUR;
        buttonTFs[i].setBackground(c);
      }
      repaint();
    }
  } // end of setButtons()


} // end of ButtonsPanel class
