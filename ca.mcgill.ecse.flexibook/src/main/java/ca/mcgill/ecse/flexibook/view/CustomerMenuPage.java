package ca.mcgill.ecse.flexibook.view;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;


public class CustomerMenuPage extends JFrame {
  private static final long serialVersionUID = -4739464810558524924L;
  
  private JButton goToLandingPage;
  private JButton goToBusinessInfo;
  private JButton goToAppointmentMake;
  private JButton goToViewCalendar;
  private JButton goToUserSettings;

  public CustomerMenuPage() {
    initComponents();
  }
  
  private void initComponents() {
	  goToLandingPage = new JButton("Back to Landing Page");
    goToBusinessInfo = new JButton("Go to Business Info");
    goToAppointmentMake = new JButton("Go to Appointment Make");
    goToViewCalendar = new JButton("Go to View Calendar");
    goToUserSettings = new JButton("User Settings");
    
    JFrame that = this;
    goToLandingPage.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Utils.switchToFrame(that, new LandingPage());
          }
        });
    goToBusinessInfo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Utils.switchToFrame(that, new BusinessInfoPage());
      }
    });
    goToAppointmentMake.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Utils.switchToFrame(that, new AppointmentsPage());
      }
    });
    goToViewCalendar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Utils.switchToFrame(that, new ViewCalendarPage());
        dispose();
      }
    });
    goToUserSettings.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Utils.switchToFrame(that, new AccountSettingsPage());
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
        .addComponent(goToLandingPage)
        .addComponent(goToBusinessInfo)
        .addComponent(goToAppointmentMake)
        .addComponent(goToViewCalendar)
        .addComponent(goToUserSettings)
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
        .addComponent(goToLandingPage)
        .addComponent(goToBusinessInfo)
        .addComponent(goToAppointmentMake)
        .addComponent(goToViewCalendar)
        .addComponent(goToUserSettings)
      )
    );
    pack();
  }
}
