package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOAppointment;
import ca.mcgill.ecse.flexibook.controller.TOBookableService;

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

  private JLabel cancelAppointmentListLabel;
  private JComboBox<String> cancelAppointmentList;
  
  private JButton makeAppointmentButton;

  private JButton updateAppointmentButton;

  private JButton cancelAppointmentButton;

  private JLabel errorMessage;
  private String error = null;

  public AppointmentsPage() {
    initComponents();
    refreshData();
  }

  private void initComponents(){
    errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);

    makeAppointmentLabel = new JLabel("Make Appointment");
    updateAppointmentLabel = new JLabel("Update Appointment");
    cancelAppointmentLabel = new JLabel("Cancel Appointment");

    makeStartDateLabel = new JLabel("Appointment start date (yyyy-mm-dd)");
    makeStartDateTextField = new JTextField();

    makeStartTimeLabel = new JLabel("Appointment start time (hh-mm)");
    makeStartTimeTextField = new JTextField();

    makeServiceToBookLabel = new JLabel("Appointment service");

    makeAppointmentButton = new JButton("Make Appointment");

    updateAppointmentButton = new JButton("Update Appointment");

    updateAppointmentListLabel = new JLabel("List of appointments");

    updateStartDateLabel = new JLabel("New Appointment start date (yyyy-mm-dd)");
    updateStartDateTextField = new JTextField();

    updateStartTimeLabel = new JLabel("New Appointment start time (hh-mm)");
    updateStartTimeTextField = new JTextField();

    cancelAppointmentButton = new JButton("Cancel Appointment");

    makeServicesList = new JComboBox<String>(new String[0]);

    updateAppointmentList = new JComboBox<String>(new String[0]);

    cancelAppointmentListLabel = new JLabel("List of appointments");
    cancelAppointmentList = new JComboBox<String>(new String[0]);

    makeAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        makeAppointmentActionPerformed(evt);
      }
    });
    updateAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        updateAppointmentActionPerformed(evt);
      }
    });
    cancelAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelAppointmentActionPerformed(evt);
      }
    });

	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setTitle("Appointments");

    GroupLayout layout = new GroupLayout(getContentPane());

    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup()
          .addComponent(errorMessage)
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
        )
        .addGroup(layout.createParallelGroup()
          .addComponent(updateAppointmentList)
          .addComponent(updateStartDateTextField)
          .addComponent(updateStartTimeTextField)
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
    
    setJMenuBar(new FlexiBookMenuBar(this, "Appointments"));
    pack();
  }

  private void refreshData() {
    // refresh the TOAppointments that will be used
    // refresh the services list
    errorMessage.setText(error);
    if(error == null || error.isEmpty()){
      makeStartDateTextField.setText("");
      makeStartTimeTextField.setText("");
      updateStartDateTextField.setText("");
      updateStartTimeTextField.setText("");

      makeServicesList.removeAllItems();
      makeServicesList.addItem("");
      for(TOBookableService service: FlexiBookController.getBookableServices()){
        makeServicesList.addItem(service.getName());
      }

      updateAppointmentList.removeAllItems();
      updateAppointmentList.addItem("");
      try{
        for(TOAppointment app : FlexiBookController.getAppointments(FlexiBookApplication.getCurrentUser().getUsername())){
          updateAppointmentList.addItem(
            String.join(" ", 
              app.getBookableServiceName(), 
              app.getStartDate().toString(), 
              app.getStartTime().toString()
            )
          );
        }
      }
      catch(InvalidInputException e){
        errorMessage.setText(e.getMessage());
      }
      
      cancelAppointmentList.removeAllItems();
      cancelAppointmentList.addItem("");
      try{
        for(TOAppointment app : FlexiBookController.getAppointments(FlexiBookApplication.getCurrentUser().getUsername())){
          cancelAppointmentList.addItem(
            String.join(" ", 
              app.getBookableServiceName(), 
              app.getStartDate().toString(), 
              app.getStartTime().toString()
            )
          );
        }
      }
      catch(InvalidInputException e){
        errorMessage.setText(e.getMessage());
      }
      pack();
    }
  }

  private void makeAppointmentActionPerformed(java.awt.event.ActionEvent evt){
    error = null;
    if(makeServicesList.getSelectedItem().toString().isEmpty()){
      error = "Please select a service for your appointment.";
    }
    else{
      try{
        FlexiBookController.makeAppointment(
          FlexiBookController.getCurrentUser().getUsername(), 
          makeStartDateTextField.getText(), 
          makeServicesList.getSelectedItem().toString(), 
          makeStartTimeTextField.getText()
        );
      }
      catch(InvalidInputException e){
        error = e.getMessage();
      }
    }
    refreshData();
    pack();
  }

  private void updateAppointmentActionPerformed(java.awt.event.ActionEvent evt){
    error = null;
    if(updateAppointmentList.getSelectedItem().toString().isEmpty()){
      error = "Please select an appointment to update.";
    }
    else{
      try{
        String[] appData = updateAppointmentList.getSelectedItem().toString().split(" ");
        FlexiBookController.updateAppointment(
          FlexiBookController.getCurrentUser().getUsername(), 
          appData[0], 
          appData[1], 
          appData[2],
          updateStartDateTextField.getText(),
          updateStartTimeTextField.getText()
        );
      }
      catch(InvalidInputException e){
        error = e.getMessage();
      }
    }
    refreshData();
    pack();
  }

  private void cancelAppointmentActionPerformed(java.awt.event.ActionEvent evt){
    error = null;
    if(cancelAppointmentList.getSelectedItem().toString().isEmpty()){
      error = "Please select an appointment to cancel.";
    }
    else{
      try{
        String[] appData = cancelAppointmentList.getSelectedItem().toString().split(" ");
        FlexiBookController.cancelAppointment(
          FlexiBookController.getCurrentUser().getUsername(), 
          appData[0], 
          appData[1], 
          appData[2]
        );
      }
      catch(InvalidInputException e){
        error = e.getMessage();
      }
    }
    refreshData();
    pack();
  }
}
