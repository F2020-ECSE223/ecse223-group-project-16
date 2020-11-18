package ca.mcgill.ecse.flexibook.view;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class AppointmentsPage extends JFrame {
  private static final long serialVersionUID = 5454788004189244344L;
  private JLabel makeAppointmentLabel;
  private JLabel updateAppointmentLabel;
  private JLabel cancelAppointmentLabel;


  private JLabel startDateLabel;
  private JTextField startDateTextField;
  
  private JLabel startTimeLabel;
  private JTextField startTimeTextField;

  private JLabel serviceToBookLabel;

  private JComboBox<String> servicesList;
  
  public AppointmentsPage() {
    initComponents();
  }
  private void initComponents(){
    makeAppointmentLabel = new JLabel();
    makeAppointmentLabel.setText("Make Appointment");
    updateAppointmentLabel = new JLabel();
    updateAppointmentLabel.setText("Update Appointment");
    cancelAppointmentLabel = new JLabel();
    cancelAppointmentLabel.setText("Cancel Appointment");

    startDateLabel = new JLabel();
    startDateLabel.setText("Appointment start date");
    startDateTextField = new JTextField();

    startTimeLabel = new JLabel();
    startTimeLabel.setText("Appointment start time");
    startTimeTextField = new JTextField();

    serviceToBookLabel = new JLabel();
    serviceToBookLabel.setText("Appointment service");

    servicesList = new JComboBox<String>(new String[0]);

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Appointment Tab");

    GroupLayout layout = new GroupLayout(getContentPane());

    JSeparator horizontalLineTop = new JSeparator();
    JSeparator horizontalLineMiddle = new JSeparator();
    JSeparator horizontalLineBottom = new JSeparator();

    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup()
          .addComponent(makeAppointmentLabel)
          .addComponent(startDateLabel)
          .addComponent(startDateTextField)
          .addComponent(updateAppointmentLabel)
          .addComponent(cancelAppointmentLabel)
        )
        .addGroup(layout.createParallelGroup()
          .addComponent(startTimeLabel)
          .addComponent(startTimeTextField)
        )
        .addGroup(layout.createParallelGroup()
          .addComponent(serviceToBookLabel)
          .addComponent(servicesList)
        )
    );
    layout.setVerticalGroup(
      layout.createSequentialGroup()
        .addComponent(makeAppointmentLabel)
        .addGroup(layout.createParallelGroup()
          .addComponent(startDateLabel)
          .addComponent(startTimeLabel)
          .addComponent(serviceToBookLabel)
        )
        .addGroup(layout.createParallelGroup()
          .addComponent(startDateTextField)
          .addComponent(startTimeTextField)
          .addComponent(servicesList)
        )
        .addComponent(updateAppointmentLabel)
        .addComponent(cancelAppointmentLabel)
    );
    pack();
  }
}
