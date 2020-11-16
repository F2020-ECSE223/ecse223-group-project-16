package ca.mcgill.ecse.flexibook.view;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class ViewCalendarPage extends JFrame {
  private static final long serialVersionUID = 1704467229218861611L;
  private JLabel viewCalendarLabel;

  public ViewCalendarPage() {
      initComponents();
  }
  private void initComponents(){
    viewCalendarLabel = new JLabel();
    viewCalendarLabel.setText("View Calendar Tab here");

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("ViewCalendar Tab");

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup()
          .addComponent(viewCalendarLabel)
          )
        )
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
        .addComponent(viewCalendarLabel)
      )
    );
    pack();
  }
}
