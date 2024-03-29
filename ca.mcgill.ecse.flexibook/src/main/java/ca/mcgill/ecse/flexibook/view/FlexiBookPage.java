package ca.mcgill.ecse.flexibook.view;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import ca.mcgill.ecse.flexibook.controller.FlexiBookController;

public class FlexiBookPage extends JFrame {
  private static final long serialVersionUID = -4739464810558524924L;
  
  
  private JButton goToLandingPage;
  private JButton goToSignUp;
  private JButton goToLogin;
  private JButton goToServices;
  private JButton goToBusinessInfo;
  private JButton goToAppointmentMake;
  private JButton goToAppointmentUpdate;
  private JButton goToViewCalendar;
  private JButton goToAppointmentManagement;
  private JButton goToUserSettings;
  
  
  public FlexiBookPage() {
    initComponents();
  }
  
  private void initComponents() {
	  goToLandingPage = new JButton("Back to Landing Page");
	  goToSignUp = new JButton();
	  goToSignUp.setText("Go to SignUP");
	  goToLogin = new JButton();
	  goToLogin.setText("Go to Login");
	  goToServices = new JButton();
	  goToServices.setText("Go to Services");
	  goToBusinessInfo = new JButton();
	  goToBusinessInfo.setText("Go to Business Info");
	  goToAppointmentMake = new JButton();
	  goToAppointmentMake.setText("Go to Appointment Make");
	  goToAppointmentUpdate = new JButton();
	  goToAppointmentUpdate.setText("Go to Appointment Update");
	  goToViewCalendar = new JButton();
	  goToViewCalendar.setText("Go to View Calendar");
	  goToUserSettings = new JButton("User Settings");
	  goToAppointmentManagement = new JButton("Go to Appointment Management");

    
    JFrame that = this;
    goToLandingPage.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Utils.switchToFrame(that, new LandingPage());

          }
        });
    goToSignUp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        new SignUpPage().setVisible(true);
        dispose();
      }
    });
    goToLogin.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        new LoginPage().setVisible(true);
        dispose();
      }
    });
    goToServices.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        new ServicesPage().setVisible(true);
        dispose();
      }
    });
    goToBusinessInfo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        new BusinessInfoPage().setVisible(true);
      }
    });
    goToAppointmentMake.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        new AppointmentsPage().setVisible(true);
      }
    });
    goToAppointmentUpdate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        new AppointmentsPage().setVisible(true);  
      }
    });
    goToViewCalendar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        new ViewCalendarPage().setVisible(true);
        dispose();
      }
    });
    goToUserSettings.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        new AccountSettingsPage().setVisible(true);
      }
    });

    goToAppointmentManagement.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          new AppointmentManagementPage().setVisible(true);
        }
      });
    
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("FlexiBook Appointment Booking System");

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup()
        	.addComponent(goToLandingPage)
            .addComponent(goToSignUp)
            .addComponent(goToLogin)
            .addComponent(goToServices)
            .addComponent(goToBusinessInfo)
            .addComponent(goToAppointmentMake)
            .addComponent(goToAppointmentUpdate)
            .addComponent(goToAppointmentManagement)
            .addComponent(goToViewCalendar)
            .addComponent(goToUserSettings)
          )
        )
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
        .addComponent(goToLandingPage)
        .addComponent(goToSignUp)
        .addComponent(goToLogin)
        .addComponent(goToServices)
        .addComponent(goToBusinessInfo)
        .addComponent(goToAppointmentMake)
        .addComponent(goToAppointmentUpdate)
        .addComponent(goToAppointmentManagement)
        .addComponent(goToViewCalendar)
        .addComponent(goToUserSettings)
      )
    );
    pack();
  }
}
