package ca.mcgill.ecse.flexibook.view;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class OwnerMenuPage extends JFrame {
  private static final long serialVersionUID = -4739464810558524924L;
  
  private JButton goToLandingPage;
  private JButton goToServices;
  private JButton goToBusinessInfo;
  private JButton goToAppointmentUpdate;
  private JButton goToViewCalendar;
  private JButton goToUserSettings;
  private JButton goToAppointmentManagement;

  public OwnerMenuPage() {
    initComponents();
  }
  
  private void initComponents() {
	  goToLandingPage = new JButton("Back to Landing Page");
    goToServices = new JButton();
    goToServices.setText("Go to Services");
    goToBusinessInfo = new JButton();
    goToBusinessInfo.setText("Go to Business Info");
    goToAppointmentUpdate = new JButton();
    goToAppointmentUpdate.setText("Go to Appointment Update");
    goToViewCalendar = new JButton();
    goToViewCalendar.setText("Go to View Calendar");
    goToUserSettings = new JButton("User Settings");
    goToAppointmentManagement = new JButton("Manage Appointments");
    
    JFrame that = this;
    goToLandingPage.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Utils.switchToFrame(that, new LandingPage());
      }
    });
    goToServices.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Utils.switchToFrame(that, new ServicesPage());
      }
    });
    goToBusinessInfo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Utils.switchToFrame(that, new BusinessInfoPage());
      }
    });
    goToAppointmentUpdate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Utils.switchToFrame(that, new AppointmentsPage());
      }
    });
    goToViewCalendar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Utils.switchToFrame(that, new ViewCalendarPage());
      }
    });
    goToUserSettings.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Utils.switchToFrame(that, new AccountSettingsPage());
      }
    });
    goToAppointmentManagement.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          Utils.switchToFrame(that, new AppointmentManagementPage());
        }
      });


    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("FlexiBook Appointment Management System");

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup()
        .addComponent(goToLandingPage)
        .addComponent(goToServices)
        .addComponent(goToBusinessInfo)
        .addComponent(goToAppointmentUpdate)
        .addComponent(goToViewCalendar)
        .addComponent(goToAppointmentManagement)
        .addComponent(goToUserSettings)
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
        .addComponent(goToLandingPage)
        .addComponent(goToServices)
        .addComponent(goToBusinessInfo)
        .addComponent(goToAppointmentUpdate)
        .addComponent(goToViewCalendar)
        .addComponent(goToAppointmentManagement)
        .addComponent(goToUserSettings)
      )
    );
    pack();
  }
}
