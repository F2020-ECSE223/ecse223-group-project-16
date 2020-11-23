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
    //refreshData();
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
    
	/*
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
  /*
  private void refreshData() {
	  errorMessageLabel.setText(errorMessage);
	  
	  if (errorMessage != null || errorMessage.length() == 0) {
		  errorMessageLabel.setText(errorMessage);
		  businessNameTextField.setText("");
		  addressTextField.setText("");;
		  phoneNumberTextField.setText("");
		  emailTextField.setText("");
		  
		  String[] columnNames = {"Day", "Hours"};
		  String[][] hours = new String[7][2];
		  businessHoursContainer = new JTable(hours, columnNames);
		  pack();
	  }
  }
  private void editBusinessHoursActionPerformed(java.awt.event.ActionEvent evt) {
	  errorMessage = null;
	  // set textfield editable to true
  }
  private void saveBusinessHoursActionPerformed(java.awt.event.ActionEvent evt) {
	  errorMessage = null;
	  try {
		  FlexiBookController.updateBusinessHour(prevDay, prevStartTime, newDay, newStartTime, newEndTime);
	  } catch (InvalidInputException e) {
		  errorMessage = e.getMessage();
	  } finally {
		  refreshData();
	  }
  }
  
  private void editContactInfoActionPerformed(java.awt.event.ActionEvent evt) {
	  errorMessage = null;
	  // set textfield editable to true
  }
  private void saveContactInfoActionPerformed(java.awt.event.ActionEvent evt) {
	  errorMessage = null;
	  try {
		  FlexiBookController.updateBusinessInfo(name, address, phoneNumber, email);
	  } catch (InvalidInputException e) {
		  errorMessage = e.getMessage();
	  } finally {
		  refreshData();
	  }
  }
  */
}