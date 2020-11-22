package ca.mcgill.ecse.flexibook.view;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class BusinessInfoPage extends JFrame {
  private static final long serialVersionUID = -941637358529064014L;
  
  // ELEMENTS
    // Titles
  private JTextField businessNameTextField;
  private JLabel businessHoursLabel;
  private JLabel contactInfoLabel;
  private JLabel addressLabel;
  private JLabel phoneNumberLabel;
  private JLabel emailLabel;
    // Edit buttons
  private JButton editBusinessHoursButton;
  private JButton editContactInfoButton;
    // Business Hour Elements
  private JTable businessHoursContainer;
    // Business address & contact
  private JTextField addressTextField;
  private JTextField phoneNumberTextField;
  private JTextField emailTextField;
  
  
  public BusinessInfoPage() {
    initComponents();
  }
 
  private void initComponents(){
// INTIALIZE
    // Titles
    businessNameTextField = new JTextField("CLICK HERE TO SET BUSINESS NAME");
    businessHoursLabel = new JLabel("Business Hours");
    contactInfoLabel = new JLabel("Contact Information");
    addressLabel = new JLabel("Address");
    phoneNumberLabel = new JLabel("Phone Number");
    emailLabel = new JLabel("Email");
    // Edit buttons
    editBusinessHoursButton = new JButton("Edit");
    editContactInfoButton = new JButton("Edit");
    
    // Business Hour elements
    String[] columnNames = {"Day", "Hours"};
    String[][] hours = new String[7][2];
    hours[0][0] = "Monday"; hours[0][1] = "ADD HOURS";
    hours[1][0] = "Tuesday"; hours[1][1] = "ADD HOURS";
    hours[2][0] = "Wednesday"; hours[2][1] = "ADD HOURS";
    hours[3][0] = "Thursday"; hours[3][1] = "ADD HOURS";
    hours[4][0] = "Friday"; hours[4][1] = "ADD HOURS";
    hours[5][0] = "Satruday"; hours[5][1] = "ADD HOURS";
    hours[6][0] = "Sundayy"; hours[6][1] = "ADD HOURS";
    businessHoursContainer = new JTable(hours, columnNames);
    
    // Business address and contact
    addressTextField = new JTextField();
    addressTextField.setText("ADD ADDRESS");
    phoneNumberTextField = new JTextField();;
    phoneNumberTextField.setText("ADD PHONE NUMBER");
    emailTextField = new JTextField();
    emailTextField.setText("ADD EMAIL");
/*
    if (!FlexiBookController.viewBusinessInfo().equals(null)) {
        businessNameTitle.setText(FlexiBookController.viewBusinessInfo().getName().toString());
        businessAddress.setText(FlexiBookController.viewBusinessInfo().getAddress().toString());
        businessPhone.setText(FlexiBookController.viewBusinessInfo().getPhoneNumber().toString());
        businessEmail.setText(FlexiBookController.viewBusinessInfo().getEmail().toString());
        */
          /*
        String[][] hours = new String[7][2];
        for (BusinessHour bh : FlexiBookController.viewBusinessHours()) {
            if (bh.getDayOfWeek().toString().equals("Monday")) {
                hours[0][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
            }
            else if (bh.getDayOfWeek().toString().equals("Tuesday")) {
                hours[1][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
            }
            else if (bh.getDayOfWeek().toString().equals("Wednesday")) {
                hours[2][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
            }
            else if (bh.getDayOfWeek().toString().equals("Thursday")) {
                hours[3][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
            }
            else if (bh.getDayOfWeek().toString().equals("Friday")) {
                hours[4][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
            }
            else if (bh.getDayOfWeek().toString().equals("Saturday")) {
                hours [5][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
            }
            else if (bh.getDayOfWeek().toString().equals("Sunday")) {
                hours[6][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
            }
        }
        */
    //}
 
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Business Info Tab");
    
    // LISTENERS
    /*
    editHoursButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            editHoursActionPerformed(evt);
        }
    });
    editAddressButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            editAddressActionPerformed(evt);
        }
    });
    editContactInfoButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            editContactInfoActionPerformed(evt);
        }
    });
    */
 
    // LAYOUT
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    
    layout.setHorizontalGroup(
            layout.createParallelGroup()
            .addComponent(businessNameTextField)
            .addGroup(
                    layout.createSequentialGroup()
                    .addGroup(
                            layout.createParallelGroup()
                            .addComponent(businessHoursLabel, Alignment.CENTER)
                            .addComponent(businessHoursContainer)
                            .addComponent(editBusinessHoursButton)
                            )
                    .addGroup(
                            layout.createParallelGroup()
                            .addComponent(contactInfoLabel, Alignment.CENTER)
                            .addComponent(addressLabel)
                            .addComponent(addressTextField)
                            .addComponent(phoneNumberLabel)
                            .addComponent(phoneNumberTextField)
                            .addComponent(emailLabel)
                            .addComponent(emailTextField)
                            .addComponent(editContactInfoButton)
                            )
                    )
            );
    
    layout.setVerticalGroup(
            layout.createSequentialGroup()
            .addComponent(businessNameTextField)
            .addGroup(
                    layout.createParallelGroup()
                    .addGroup(
                            layout.createSequentialGroup()
                            .addComponent(businessHoursLabel)
                            .addComponent(businessHoursContainer)
                            .addComponent(editBusinessHoursButton)
                            )
                    .addGroup(
                            layout.createSequentialGroup()
                            .addComponent(contactInfoLabel)
                            .addComponent(addressLabel)
                            .addComponent(addressTextField)
                            .addComponent(phoneNumberLabel)
                            .addComponent(phoneNumberTextField)
                            .addComponent(emailLabel)
                            .addComponent(emailTextField)
                            .addComponent(editContactInfoButton)
                            )
                    )
            );
    pack();
  }
}