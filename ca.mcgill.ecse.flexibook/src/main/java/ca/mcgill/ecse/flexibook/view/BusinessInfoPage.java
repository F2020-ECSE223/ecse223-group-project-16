package ca.mcgill.ecse.flexibook.view;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
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
  	// Panels
  private JPanel businessHoursPanel;
  private JPanel businessInfoPanel;
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
    businessNameTextField.setHorizontalAlignment(JTextField.CENTER);
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
    hours[5][0] = "Saturday"; hours[5][1] = "ADD HOURS";
    hours[6][0] = "Sunday"; hours[6][1] = "ADD HOURS";
    businessHoursContainer = new JTable(hours, columnNames);
    businessHoursContainer.setRowHeight(19);
    
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
	setMinimumSize(new Dimension(600, 200));
    
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
 
    // PANEL LAYOUT
    Utils.resizeTextFieldToWidth(addressTextField, 20);
    Utils.resizeTextFieldToWidth(businessNameTextField, 20);
    Utils.resizeTextFieldToWidth(emailTextField, 20);
    Utils.resizeTextFieldToWidth(phoneNumberTextField, 20);
	businessHoursPanel = new JPanel(true);
	GroupLayout bhLayout = new GroupLayout(businessHoursPanel);
	bhLayout.setAutoCreateGaps(true);
	bhLayout.setAutoCreateContainerGaps(true);
	businessHoursPanel.setLayout(bhLayout);
	businessHoursPanel.setBorder(
			BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(), 
						"Business Hours"),
					new EmptyBorder(5, 5, 5, 5)
					)
				);
	bhLayout.setHorizontalGroup(
			bhLayout.createParallelGroup()
			.addComponent(businessHoursLabel, Alignment.CENTER)
			.addComponent(businessHoursContainer)
			.addComponent(editBusinessHoursButton, Alignment.CENTER)
	);
	bhLayout.setVerticalGroup(
			bhLayout.createSequentialGroup()
			.addComponent(businessHoursLabel)
			.addComponent(businessHoursContainer)
			.addComponent(editBusinessHoursButton)
	);
	
	businessInfoPanel = new JPanel(true);
	GroupLayout biLayout = new GroupLayout(businessInfoPanel);
	biLayout.setAutoCreateGaps(true);
	biLayout.setAutoCreateContainerGaps(true);
	businessInfoPanel.setLayout(biLayout);
	businessInfoPanel.setBorder(
			BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(), 
						"Contact Information"),
					new EmptyBorder(5, 5, 5, 5)
					)
				);
	biLayout.setHorizontalGroup(
			biLayout.createParallelGroup()
			.addComponent(addressLabel, Alignment.CENTER)
			.addComponent(addressTextField)
			.addComponent(phoneNumberLabel, Alignment.CENTER)
			.addComponent(phoneNumberTextField)
			.addComponent(emailLabel, Alignment.CENTER)
			.addComponent(emailTextField)
			.addComponent(editContactInfoButton, Alignment.CENTER)
	);
	biLayout.setVerticalGroup(
			biLayout.createSequentialGroup()
			.addComponent(addressLabel)
			.addComponent(addressTextField)
			.addComponent(phoneNumberLabel)
			.addComponent(phoneNumberTextField)
			.addComponent(emailLabel)
			.addComponent(emailTextField)
			.addComponent(editContactInfoButton)
	);
	
	// GROUP LAYOUT
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    Utils.resizeTextFieldToWidth(addressTextField, 50);
    Utils.resizeTextFieldToWidth(businessNameTextField, 50);
    Utils.resizeTextFieldToWidth(emailTextField, 50);
    Utils.resizeTextFieldToWidth(phoneNumberTextField, 50);

    layout.setHorizontalGroup(
    		layout.createParallelGroup()
    		.addComponent(businessNameTextField)
    		.addGroup(
    				layout.createSequentialGroup()
    				.addComponent(businessHoursPanel)
    				.addComponent(businessInfoPanel)
    		)		
    );
    layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addComponent(businessNameTextField)
    		.addGroup(
    				layout.createParallelGroup()
    				.addComponent(businessHoursPanel)
    				.addComponent(businessInfoPanel)
    		)
    );
    pack();
  }
}