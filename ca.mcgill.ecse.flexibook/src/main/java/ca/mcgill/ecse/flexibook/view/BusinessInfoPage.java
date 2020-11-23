package ca.mcgill.ecse.flexibook.view;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOBusinessHour;

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
import java.util.Arrays;
import java.util.List;


public class BusinessInfoPage extends JFrame {
  private static final long serialVersionUID = -941637358529064014L;
  
  // ELEMENTS
  	// Panels
  private JPanel businessHoursPanel;
  private JPanel businessInfoPanel;
    // Titles
  private JTextField businessNameTextField;
  private JLabel addressLabel;
  private JLabel phoneNumberLabel;
  private JLabel emailLabel;
    // Edit buttons
  private JButton editBusinessHoursButton;
  private JButton editContactInfoButton;
    // Business Hour Elements
  private JLabel monLabel;
  private JLabel tuesLabel;
  private JLabel wedLabel;
  private JLabel thursLabel;
  private JLabel friLabel;
  private JLabel satLabel;
  private JLabel sunLabel;
  private JTextField monHoursTextField;
  private JTextField tuesHoursTextField;
  private JTextField wedHoursTextField;
  private JTextField thursHoursTextField;
  private JTextField friHoursTextField;
  private JTextField satHoursTextField;
  private JTextField sunHoursTextField;
    // Business address & contact
  private JTextField addressTextField;
  private JTextField phoneNumberTextField;
  private JTextField emailTextField;
  
  	// Error message
  private JLabel errorMessageLabel;
  private String errorMessage = null;
  
  
  public BusinessInfoPage() {
    initComponents();
    refreshData();
  }
 
  private void initComponents(){
// INTIALIZE
	// Errors
	errorMessageLabel = new JLabel();
	errorMessageLabel.setForeground(Color.RED);
    // Titles
    businessNameTextField = new JTextField("CLICK HERE TO SET BUSINESS NAME");
    businessNameTextField.setHorizontalAlignment(JTextField.CENTER);
    addressLabel = new JLabel("Address");
    phoneNumberLabel = new JLabel("Phone Number");
    emailLabel = new JLabel("Email");
    // Edit buttons
    editBusinessHoursButton = new JButton("Edit");
    editContactInfoButton = new JButton("Edit");
    // Business Info elements
    businessNameTextField = new JTextField();
    addressTextField = new JTextField();
    phoneNumberTextField = new JTextField();
    emailTextField = new JTextField();
    monLabel = new JLabel("Monday");
    tuesLabel = new JLabel("Tuesday");
    wedLabel = new JLabel("Wednesday");
    thursLabel = new JLabel("Thursday");
    friLabel = new JLabel("Friday");
    satLabel = new JLabel("Saturday");
    sunLabel = new JLabel("Sunday");
    monHoursTextField = new JTextField();
    tuesHoursTextField = new JTextField();
    wedHoursTextField = new JTextField();
    thursHoursTextField = new JTextField();
    friHoursTextField = new JTextField();
    satHoursTextField = new JTextField();
    sunHoursTextField = new JTextField();
    
    if (FlexiBookController.viewBusinessInfo() != null) {
    	// Contact info elements
        businessNameTextField.setText(FlexiBookController.viewBusinessInfo().getName().toString());
        addressTextField.setText(FlexiBookController.viewBusinessInfo().getAddress().toString());
        phoneNumberTextField.setText(FlexiBookController.viewBusinessInfo().getPhoneNumber().toString());
        emailTextField.setText(FlexiBookController.viewBusinessInfo().getEmail().toString());
        // Business hour elements
        for (TOBusinessHour bh : FlexiBookController.viewBusinessInfo().getBusinessHours()) {
            if (bh.getDayOfWeek().toString().equals("Monday")) {
                monHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
            }
            else if (bh.getDayOfWeek().toString().equals("Tuesday")) {
                tuesHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
            }
            else if (bh.getDayOfWeek().toString().equals("Wednesday")) {
                wedHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
            }
            else if (bh.getDayOfWeek().toString().equals("Thursday")) {
                thursHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
            }
            else if (bh.getDayOfWeek().toString().equals("Friday")) {
                friHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
            }
            else if (bh.getDayOfWeek().toString().equals("Saturday")) {
                satHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
            }
            else if (bh.getDayOfWeek().toString().equals("Sunday")) {
                sunHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
            }
        }

    } else {
    	businessNameTextField.setText("CLICK TO ADD BUSINESS NAME");
    	monHoursTextField.setText("ADD HOURS");
    	tuesHoursTextField.setText("ADD HOURS");
    	wedHoursTextField.setText("ADD HOURS");
    	thursHoursTextField.setText("ADD HOURS");
    	friHoursTextField.setText("ADD HOURS");
    	satHoursTextField.setText("ADD HOURS");
    	sunHoursTextField.setText("ADD HOURS");
        addressTextField.setText("ADD ADDRESS");
        phoneNumberTextField.setText("ADD PHONE NUMBER");
        emailTextField.setText("ADD EMAIL");
    }
    addressTextField.setEditable(false);
    phoneNumberTextField.setEditable(false);
    emailTextField.setEditable(false);
    monHoursTextField.setEditable(false);
    tuesHoursTextField.setEditable(false);
    wedHoursTextField.setEditable(false);
    thursHoursTextField.setEditable(false);
    friHoursTextField.setEditable(false);
    satHoursTextField.setEditable(false);
    sunHoursTextField.setEditable(false);

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Business Info Tab");
	setMinimumSize(new Dimension(600, 200));
    
    // LISTENERS
    editBusinessHoursButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
        	if (editBusinessHoursButton.getText().equals("Save")) {
        		saveBusinessHoursActionPerformed(evt);
        	} else {
            	editBusinessHoursButton.setText("Save");
                editBusinessHoursActionPerformed(evt);
        	}
        }
    });

    editContactInfoButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
        	if (editContactInfoButton.getText().equals("Save")) {
        		saveContactInfoActionPerformed(evt);
        	} else {
            	editContactInfoButton.setText("Save");
                editContactInfoActionPerformed(evt);
        	}
        }
    });
    
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
			.addGroup(
					bhLayout.createSequentialGroup()
					.addGroup(
							bhLayout.createParallelGroup()
							.addComponent(monLabel)
							.addComponent(tuesLabel)
							.addComponent(wedLabel)
							.addComponent(thursLabel)
							.addComponent(friLabel)
							.addComponent(satLabel)
							.addComponent(sunLabel)
					)
					.addGroup(
							bhLayout.createParallelGroup()
							.addComponent(monHoursTextField)
							.addComponent(tuesHoursTextField)
							.addComponent(wedHoursTextField)
							.addComponent(thursHoursTextField)
							.addComponent(friHoursTextField)
							.addComponent(satHoursTextField)
							.addComponent(sunHoursTextField)
					)
			)
			.addComponent(editBusinessHoursButton, Alignment.CENTER)
	);
	bhLayout.setVerticalGroup(
			bhLayout.createSequentialGroup()
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(monLabel)
					.addComponent(monHoursTextField)
			)
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(tuesLabel)
					.addComponent(tuesHoursTextField)
			)
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(wedLabel)
					.addComponent(wedHoursTextField)
			)
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(thursLabel)
					.addComponent(thursHoursTextField)
			)
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(friLabel)
					.addComponent(friHoursTextField)
			)
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(satLabel)
					.addComponent(satHoursTextField)
			)
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(sunLabel)
					.addComponent(sunHoursTextField)
			)
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

    layout.setHorizontalGroup(
    		layout.createParallelGroup()
    		.addComponent(businessNameTextField)
    		.addGroup(
    				layout.createSequentialGroup()
    				.addComponent(businessHoursPanel)
    				.addComponent(businessInfoPanel)
    		)
    		.addComponent(errorMessageLabel)
    );
    layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addComponent(businessNameTextField)
    		.addGroup(
    				layout.createParallelGroup()
    				.addComponent(businessHoursPanel)
    				.addComponent(businessInfoPanel)
    		)
    		.addComponent(errorMessageLabel)
    );
    pack();
  }

  private void refreshData() {
	  errorMessageLabel.setText(errorMessage);
	  addressTextField.setEditable(false);
	  phoneNumberTextField.setEditable(false);
	  emailTextField.setEditable(false);
	  monHoursTextField.setEditable(false);
	  tuesHoursTextField.setEditable(false);
	  wedHoursTextField.setEditable(false);
	  thursHoursTextField.setEditable(false);
	  friHoursTextField.setEditable(false);
	  satHoursTextField.setEditable(false);
      sunHoursTextField.setEditable(false);
	  if (errorMessage != null) {
		  errorMessageLabel.setText(errorMessage);
		  businessNameTextField.setText(FlexiBookController.viewBusinessInfo().getName());
		  addressTextField.setText(FlexiBookController.viewBusinessInfo().getAddress());;
		  phoneNumberTextField.setText(FlexiBookController.viewBusinessInfo().getAddress());
		  emailTextField.setText(FlexiBookController.viewBusinessInfo().getEmail());
	      for (TOBusinessHour bh : FlexiBookController.viewBusinessInfo().getBusinessHours()) {
	            if (bh.getDayOfWeek().toString().equals("Monday")) {
	                monHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
	            }
	            else if (bh.getDayOfWeek().toString().equals("Tuesday")) {
	                tuesHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
	            }
	            else if (bh.getDayOfWeek().toString().equals("Wednesday")) {
	                wedHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
	            }
	            else if (bh.getDayOfWeek().toString().equals("Thursday")) {
	                thursHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
	            }
	            else if (bh.getDayOfWeek().toString().equals("Friday")) {
	                friHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
	            }
	            else if (bh.getDayOfWeek().toString().equals("Saturday")) {
	                satHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
	            }
	            else if (bh.getDayOfWeek().toString().equals("Sunday")) {
	                sunHoursTextField.setText(bh.getStartTime().toString() + bh.getEndTime().toString());
	            }
	        }
	  }
	  
  }
  String[][] prevHours = new String[7][2];
  private void editBusinessHoursActionPerformed(java.awt.event.ActionEvent evt) {
	  errorMessage = null;
	  prevHours[0][0] = "Monday";
	  prevHours[1][0] = "Tuesday";
	  prevHours[2][0] = "Wednesday";
	  prevHours[3][0] = "Thursday";
	  prevHours[4][0] = "Friday";
	  prevHours[5][0] = "Saturday";
	  prevHours[6][0] = "Sunday";
	  prevHours[0][1] = monHoursTextField.getText();
	  prevHours[1][1] = tuesHoursTextField.getText();
	  prevHours[2][1] = wedHoursTextField.getText();
	  prevHours[3][1] = thursHoursTextField.getText();
	  prevHours[4][1] = friHoursTextField.getText();
	  prevHours[5][1] = satHoursTextField.getText();
	  prevHours[6][1] = sunHoursTextField.getText();
	  monHoursTextField.setEditable(true);
	  tuesHoursTextField.setEditable(true);
	  wedHoursTextField.setEditable(true);
	  thursHoursTextField.setEditable(true);
	  friHoursTextField.setEditable(true);
	  satHoursTextField.setEditable(true);
	  sunHoursTextField.setEditable(true);
  }
  String[][] currentHours= new String[7][2];
  private void saveBusinessHoursActionPerformed(java.awt.event.ActionEvent evt) {
	  errorMessage = null;
	  currentHours[0][0] = "Monday";
	  currentHours[1][0] = "Tuesday";
	  currentHours[2][0] = "Wednesday";
	  currentHours[3][0] = "Thursday";
	  currentHours[4][0] = "Friday";
	  currentHours[5][0] = "Saturday";
	  currentHours[6][0] = "Sunday";
	  currentHours[0][1] = monHoursTextField.getText();
	  currentHours[1][1] = tuesHoursTextField.getText();
	  currentHours[2][1] = wedHoursTextField.getText();
	  currentHours[3][1] = thursHoursTextField.getText();
	  currentHours[4][1] = friHoursTextField.getText();
	  currentHours[5][1] = satHoursTextField.getText();
	  currentHours[6][1] = sunHoursTextField.getText();
	  for (int i = 0; i <= 6; i = i + 1) {
		  if (prevHours[i][1] != currentHours[i][1]) {
			  if (prevHours[i][1] == "ADD HOURS") {
				  try {
					  String day = currentHours[i][0];
					  String startTime = currentHours[i][1].substring(0,5);
					  String endTime = currentHours[i][1].substring(6);
					  FlexiBookController.addNewBusinessHour(day, startTime, endTime);
				  } catch (InvalidInputException e) {
					  errorMessage = e.getMessage();
				  }
			  } else {
				  try {
					  String prevDay = prevHours[i][0];
					  String prevStartTime = prevHours[i][1].substring(0,5);
					  String newDay = prevDay;
					  String newStartTime = currentHours[i][1].substring(0,5);
					  String newEndTime = currentHours[i][1].substring(6);
					  FlexiBookController.updateBusinessHour(prevDay, prevStartTime, newDay, newStartTime, newEndTime);
				  } catch (InvalidInputException e) {
					  errorMessage = e.getMessage();
				  }
			  } 
		  }
	  }
	  errorMessageLabel.setText("Edit");
	  refreshData();
	  pack();
  }

  private void editContactInfoActionPerformed(java.awt.event.ActionEvent evt) {
	  errorMessage = null;
	  addressTextField.setEditable(true);
	  phoneNumberTextField.setEditable(true);
	  emailTextField.setEditable(true);
  }
  private void saveContactInfoActionPerformed(java.awt.event.ActionEvent evt) {
	  errorMessage = null;
	  String name = businessNameTextField.getText();
	  String address = addressTextField.getText();
	  String phoneNumber = phoneNumberTextField.getText();
	  String email = emailTextField.getText();
	  if (FlexiBookController.viewBusinessInfo() == null) {
		  try {
			  FlexiBookController.setUpBusinessInfo(name, address, phoneNumber, email);
		  } catch (InvalidInputException e) {
			  errorMessage = e.getMessage();
		  }
	  } else {
		  try {
			  FlexiBookController.updateBusinessInfo(name, address, phoneNumber, email);
		  } catch (InvalidInputException e) {
			  errorMessage = e.getMessage();
		  }
	  }
	  refreshData();
	  pack();
  }

}