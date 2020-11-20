package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

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
    // below is testing
    File f = new File("data.flexibook");
		f.delete();
    FlexiBookApplication.getFlexiBook();

    try{
      FlexiBookController.login("owner", "password");
      FlexiBookController.setUpBusinessInfo("Jimmy G", "WallStreet", "0123456789", "hq@wang.com");
      FlexiBookController.addNewBusinessHour("Monday", "09:00", "20:00");
      FlexiBookController.addNewBusinessHour("Tuesday", "09:00", "20:00");
      FlexiBookController.addNewBusinessHour("Wednesday", "09:00", "20:00");
      FlexiBookController.addNewBusinessHour("Thursday", "09:00", "20:00");
      FlexiBookController.addNewBusinessHour("Friday", "09:00", "20:00");
      FlexiBookController.addNewBusinessHour("Saturday", "09:00", "20:00");
      FlexiBookController.addNewBusinessHour("Sunday", "09:00", "20:00");
      FlexiBookController.addService("cut", "10", "0", "0");
      FlexiBookController.addService("trim", "10", "0", "0");

      FlexiBookController.logout();

      FlexiBookController.createCustomerAccount("test", "password");
      FlexiBookController.login("test", "password");
    }
    catch(InvalidInputException e){

    }
    // everything above is to be removed
    initComponents();
    refreshData();
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
    makeStartDateLabel.setText("Appointment start date (yyyy-mm-dd)");
    makeStartDateTextField = new JTextField();

    makeStartTimeLabel = new JLabel();
    makeStartTimeLabel.setText("Appointment start time (hh-mm)");
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
    updateStartDateLabel.setText("New Appointment start date (yyyy-mm-dd)");
    updateStartDateTextField = new JTextField();

    updateStartTimeLabel = new JLabel();
    updateStartTimeLabel.setText("New Appointment start time (hh-mm)");
    updateStartTimeTextField = new JTextField();

    cancelAppointmentButton = new JButton();
    cancelAppointmentButton.setText("Cancel Appointment");

    makeServicesList = new JComboBox<String>(new String[0]);

    updateAppointmentList = new JComboBox<String>(new String[0]);

    cancelAppointmentListLabel = new JLabel();
    cancelAppointmentListLabel.setText("List of appointments");
    cancelAppointmentList = new JComboBox<String>(new String[0]);

    makeAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        makeAppointmentActionPerformed(evt);
        // new FlexiBookPage().setVisible(true);
        // dispose();
        // we'll add another button to return to home later
      }
    });
    updateAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        updateAppointmentActionPerformed(evt);
        // new FlexiBookPage().setVisible(true);
        // dispose();
        // we'll add another button to return to home later
      }
    });
    cancelAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelAppointmentActionPerformed(evt);
        // new FlexiBookPage().setVisible(true);
        // dispose();
        // we'll add another button to return to home later
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
          FlexiBookApplication.getCurrentUser().getUsername(), 
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
          FlexiBookApplication.getCurrentUser().getUsername(), 
          appData[0], 
          appData[1], 
          appData[2],
          updateStartDateTextField.getText(),
          updateStartTimeTextField.getText()
        );
      }
      catch(InvalidInputException e){
        error = FlexiBookApplication.getCurrentUser().getUsername();
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
          FlexiBookApplication.getCurrentUser().getUsername(), 
          appData[0], 
          appData[1], 
          appData[2]
        );
      }
      catch(InvalidInputException e){
        error = FlexiBookApplication.getCurrentUser().getUsername();
      }
    }
    refreshData();
    pack();
  }
}
