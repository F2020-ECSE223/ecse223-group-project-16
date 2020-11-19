package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class AppointmentsPage extends JFrame {
  private static final long serialVersionUID = 5454788004189244344L;
  private JLabel makeAppointmentLabel;
  private JLabel updateAppointmentLabel;
  private JLabel cancelAppointmentLabel;


  private JLabel makeStartDateLabel;
  private JTextField makeStartDateTextField;
  
  private JLabel makeStartTimeLabel;
  private JTextField makeStartTimeTextField;

  private JLabel makeServiceToBookLabel;
  private JComboBox<String> makeServicesList;
  
  private JLabel updateAppointmentListLabel;

  private JComboBox<String> updateAppointmentList;

  private JLabel updateStartDateLabel;
  private JTextField updateStartDateTextField;
  
  private JLabel updateStartTimeLabel;
  private JTextField updateStartTimeTextField;

  private JLabel updateServiceToBookLabel;
  private JComboBox<String> updateServicesList;

  private JLabel cancelAppointmentListLabel;
  private JComboBox<String> cancelAppointmentList;
  
  private JButton makeAppointmentButton;

  private JButton updateAppointmentButton;

  private JButton cancelAppointmentButton;

  private JLabel errorMessage;
  
  public AppointmentsPage() {
    refreshData();
    initComponents();
  }

  private void initComponents(){
    errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);

    makeAppointmentLabel = new JLabel();
    makeAppointmentLabel.setText("Make Appointment");
    updateAppointmentLabel = new JLabel();
    updateAppointmentLabel.setText("Update Appointment");
    cancelAppointmentLabel = new JLabel();
    cancelAppointmentLabel.setText("Cancel Appointment");

    makeStartDateLabel = new JLabel();
    makeStartDateLabel.setText("Appointment start date");
    makeStartDateTextField = new JTextField();

    makeStartTimeLabel = new JLabel();
    makeStartTimeLabel.setText("Appointment start time");
    makeStartTimeTextField = new JTextField();

    makeServiceToBookLabel = new JLabel();
    makeServiceToBookLabel.setText("Appointment service");

    makeAppointmentButton = new JButton();
    makeAppointmentButton.setText("Make Appointment");

    updateAppointmentButton = new JButton();
    updateAppointmentButton.setText("Update Appointment");

    updateAppointmentListLabel = new JLabel();
    updateAppointmentListLabel.setText("List of appointments");

    updateStartDateLabel = new JLabel();
    updateStartDateLabel.setText("New Appointment start date");
    updateStartDateTextField = new JTextField();

    updateStartTimeLabel = new JLabel();
    updateStartTimeLabel.setText("New Appointment start time");
    updateStartTimeTextField = new JTextField();

    updateServiceToBookLabel = new JLabel();
    updateServiceToBookLabel.setText("New Appointment service");

    cancelAppointmentButton = new JButton();
    cancelAppointmentButton.setText("Cancel Appointment");

    makeServicesList = new JComboBox<String>(new String[0]);
    updateServicesList = new JComboBox<String>(new String[0]);

    updateAppointmentList = new JComboBox<String>(new String[0]);

    cancelAppointmentListLabel = new JLabel();
    cancelAppointmentListLabel.setText("List of appointments");
    cancelAppointmentList = new JComboBox<String>(new String[0]);

    makeAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        makeAppointActionPerformed(evt);
        new FlexiBookPage().setVisible(true);
        dispose();
      }
    });
    updateAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        updateAppointActionPerformed(evt);
        new FlexiBookPage().setVisible(true);
        dispose();
      }
    });
    cancelAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelAppointActionPerformed(evt);
        new FlexiBookPage().setVisible(true);
        dispose();
      }
    });

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Appointment Tab");

    GroupLayout layout = new GroupLayout(getContentPane());

    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
        .addComponent(errorMessage)
        .addGroup(layout.createParallelGroup()
          .addComponent(makeAppointmentLabel)
          .addComponent(makeStartDateLabel)
          .addComponent(makeStartDateTextField)
          .addComponent(updateAppointmentLabel)
          .addComponent(cancelAppointmentLabel)
          .addComponent(updateAppointmentList)
          .addComponent(updateAppointmentListLabel)
          .addComponent(cancelAppointmentList)
          .addComponent(cancelAppointmentListLabel)
        )
        .addGroup(layout.createParallelGroup()
          .addComponent(makeStartTimeLabel)
          .addComponent(makeStartTimeTextField)
          .addComponent(updateStartDateLabel)
          .addComponent(updateStartDateTextField)
        )
        .addGroup(layout.createParallelGroup()
          .addComponent(makeServiceToBookLabel)
          .addComponent(makeServicesList)
          .addComponent(updateStartTimeLabel)
          .addComponent(updateStartTimeTextField)
        )
        .addGroup(layout.createParallelGroup()
          .addComponent(updateServiceToBookLabel)
          .addComponent(updateServicesList)
        )
        .addGroup(layout.createParallelGroup()
          .addComponent(makeAppointmentButton)
          .addComponent(updateAppointmentButton)
          .addComponent(cancelAppointmentButton)
        )
    );
    layout.setVerticalGroup(
      layout.createSequentialGroup()
        .addComponent(errorMessage)
        .addComponent(makeAppointmentLabel)
        .addGroup(layout.createParallelGroup()
          .addComponent(makeStartDateLabel)
          .addComponent(makeStartTimeLabel)
          .addComponent(makeServiceToBookLabel)
        )
        .addGroup(layout.createParallelGroup()
          .addComponent(makeStartDateTextField)
          .addComponent(makeStartTimeTextField)
          .addComponent(makeServicesList)
          .addComponent(makeAppointmentButton)
        )
        .addComponent(updateAppointmentLabel)
        .addGroup(layout.createParallelGroup()
          .addComponent(updateAppointmentListLabel)
          .addComponent(updateStartDateLabel)
          .addComponent(updateStartTimeLabel)
          .addComponent(updateServiceToBookLabel)
        )
        .addGroup(layout.createParallelGroup()
          .addComponent(updateAppointmentList)
          .addComponent(updateStartDateTextField)
          .addComponent(updateStartTimeTextField)
          .addComponent(updateServicesList)
          .addComponent(updateAppointmentButton)
        )
        .addComponent(cancelAppointmentLabel)
        .addGroup(layout.createParallelGroup()
          .addComponent(cancelAppointmentListLabel)
        )
        .addGroup(layout.createParallelGroup()
          .addComponent(cancelAppointmentList)
          .addComponent(cancelAppointmentButton)
        )
    );
    pack();
  }

  private void refreshData() {
    // refresh the TOAppointments that will be used
    // refresh the services list
  }

  private void makeAppointActionPerformed(java.awt.event.ActionEvent evt){

  }

  private void updateAppointActionPerformed(java.awt.event.ActionEvent evt){
    
  }

  private void cancelAppointActionPerformed(java.awt.event.ActionEvent evt){
    
  }
}
