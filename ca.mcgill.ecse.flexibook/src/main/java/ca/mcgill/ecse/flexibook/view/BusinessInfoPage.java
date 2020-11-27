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
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
  private List<JLabel> dayLabels;
  private List<JTextField> dayTextFields;
    // Business address & contact
  private JTextField addressTextField;
  private JTextField phoneNumberTextField;
  private JTextField emailTextField;
  
  	// Error message
  private JLabel errorMessageLabel;
  private String errorMessage;
  
  
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
    dayLabels = new ArrayList<>();
    for (int i=0; i<7; i++) {
	    String day  = DateFormatSymbols.getInstance().getWeekdays()[i + 1]; 
	    dayLabels.add(new JLabel(day));
	}
    dayTextFields = new ArrayList<>();
    for (int i=0; i<7; i++) {
	    String day  = DateFormatSymbols.getInstance().getWeekdays()[i + 1]; 
	    dayTextFields.add(new JTextField());
	}
    // Setting to non-editable textfields
    addressTextField.setEditable(false);
    phoneNumberTextField.setEditable(false);
    emailTextField.setEditable(false);
    for (JTextField tf : dayTextFields) {
    	tf.setEditable(false);
    }
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Business Info Tab");
	setMinimumSize(new Dimension(600, 500));
    
    // LISTENERS
    editBusinessHoursButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
        	if (editBusinessHoursButton.getText().equals("Save")) {
        		saveBusinessHoursActionPerformed(evt);
        	} else {
                editBusinessHoursActionPerformed(evt);
        	}
        }
    });

    editContactInfoButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
        	if (editContactInfoButton.getText().equals("Save")) {
        		System.out.println("save contact");
        		saveContactInfoActionPerformed(evt);
        	} else {
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
							.addComponent(dayLabels.get(0))
							.addComponent(dayLabels.get(1))
							.addComponent(dayLabels.get(2))
							.addComponent(dayLabels.get(3))
							.addComponent(dayLabels.get(4))
							.addComponent(dayLabels.get(5))
							.addComponent(dayLabels.get(6))
					)
					.addGroup(
							bhLayout.createParallelGroup()
							.addComponent(dayTextFields.get(0))
							.addComponent(dayTextFields.get(1))
							.addComponent(dayTextFields.get(2))
							.addComponent(dayTextFields.get(3))
							.addComponent(dayTextFields.get(4))
							.addComponent(dayTextFields.get(5))
							.addComponent(dayTextFields.get(6))
					)
			)
			.addComponent(editBusinessHoursButton, Alignment.CENTER)
	);
	bhLayout.setVerticalGroup(
			bhLayout.createSequentialGroup()
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(dayLabels.get(0))
					.addComponent(dayTextFields.get(0))
			)
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(dayLabels.get(1))
					.addComponent(dayTextFields.get(1))
			)
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(dayLabels.get(2))
					.addComponent(dayTextFields.get(2))
			)
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(dayLabels.get(3))
					.addComponent(dayTextFields.get(3))
			)
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(dayLabels.get(4))
					.addComponent(dayTextFields.get(4))
			)
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(dayLabels.get(5))
					.addComponent(dayTextFields.get(5))
			)
			.addGroup(
					bhLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(dayLabels.get(6))
					.addComponent(dayTextFields.get(6))
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
	  // Setting to non-editable textfields
      addressTextField.setEditable(false);
      phoneNumberTextField.setEditable(false);
      emailTextField.setEditable(false);
      for (JTextField tf : dayTextFields) {
    	  tf.setEditable(false);
      }
	  editBusinessHoursButton.setText("Edit");
	  editContactInfoButton.setText("Edit");
	  errorMessageLabel.setText(errorMessage);
	  if (FlexiBookController.viewBusinessInfo() != null) {
		  errorMessageLabel.setText(errorMessage);
		  businessNameTextField.setText(FlexiBookController.viewBusinessInfo().getName());
		  addressTextField.setText(FlexiBookController.viewBusinessInfo().getAddress());;
		  phoneNumberTextField.setText(FlexiBookController.viewBusinessInfo().getPhoneNumber());
		  emailTextField.setText(FlexiBookController.viewBusinessInfo().getEmail());
	      for (TOBusinessHour bh : FlexiBookController.viewBusinessInfo().getBusinessHours()) {
	    	  String pattern = "HH:mm";
	            if (bh.getDayOfWeek().toString().equals("Sunday")) {
	            	dayTextFields.get(0).setText(Utils.formatTime(bh.getStartTime(), pattern) + '-' + Utils.formatTime(bh.getEndTime(), pattern));
	                if (dayTextFields.get(0) == null) {
	                	dayTextFields.get(0).setText("ADD HOURS");
	                }
	            }
	            else if (bh.getDayOfWeek().toString().equals("Monday")) {
	            	dayTextFields.get(1).setText(Utils.formatTime(bh.getStartTime(), pattern) + '-' + Utils.formatTime(bh.getEndTime(), pattern));
	                if (dayTextFields.get(1).getText() == null) {
	                	dayTextFields.get(1).setText("ADD HOURS");
	                }
	            }
	            else if (bh.getDayOfWeek().toString().equals("Tuesday")) {
	            	dayTextFields.get(2).setText(Utils.formatTime(bh.getStartTime(), pattern) + '-' + Utils.formatTime(bh.getEndTime(), pattern));
	                if (dayTextFields.get(2).getText() == null) {
	                	dayTextFields.get(2).setText("ADD HOURS");
	                }
	            }
	            else if (bh.getDayOfWeek().toString().equals("Wednesday")) {
	            	dayTextFields.get(3).setText(Utils.formatTime(bh.getStartTime(), pattern) + '-' + Utils.formatTime(bh.getEndTime(), pattern));
	                if (dayTextFields.get(3).getText() == null) {
	                	dayTextFields.get(3).setText("ADD HOURS");
	                }
	            }
	            else if (bh.getDayOfWeek().toString().equals("Thursday")) {
	            	dayTextFields.get(4).setText(Utils.formatTime(bh.getStartTime(), pattern) + '-' + Utils.formatTime(bh.getEndTime(), pattern));
	                if (dayTextFields.get(4).getText() == null) {
	                	dayTextFields.get(4).setText("ADD HOURS");
	                }
	            }
	            else if (bh.getDayOfWeek().toString().equals("Friday")) {
	            	dayTextFields.get(5).setText(Utils.formatTime(bh.getStartTime(), pattern) + '-' + Utils.formatTime(bh.getEndTime(), pattern));
	                if (dayTextFields.get(5).getText() == null) {
	                	dayTextFields.get(5).setText("ADD HOURS");
	                }
	            }
	            else if (bh.getDayOfWeek().toString().equals("Saturday")) {
	            	dayTextFields.get(6).setText(Utils.formatTime(bh.getStartTime(), pattern) + '-' + Utils.formatTime(bh.getEndTime(), pattern));
	                if (dayTextFields.get(6).getText() == null) {
	                	dayTextFields.get(6).setText("ADD HOURS");
	                }
	            }
	        }
	  } else {
	    	businessNameTextField.setText("CLICK TO ADD BUSINESS NAME");
	        for (JTextField tf : dayTextFields) {
	        	tf.setText("ADD HOURS");
	        }
	        addressTextField.setText("ADD ADDRESS");
	        phoneNumberTextField.setText("ADD PHONE NUMBER");
	        emailTextField.setText("ADD EMAIL");
	        errorMessageLabel.setText(errorMessage);
	  }
	  
  }
  String[] prevHours = new String[7];
  private void editBusinessHoursActionPerformed(java.awt.event.ActionEvent evt) {
	  if (FlexiBookController.viewBusinessInfo() == null) {
		  errorMessage = "Please assign your business a name, address, phone number, and email";
		  errorMessageLabel.setText(errorMessage);
	  } else {
		  for (int i=0; i<7; i++) {
			  prevHours[i] = dayTextFields.get(i).getText();
		  }
	    for (JTextField tf : dayTextFields) {
	    	tf.setEditable(true);
	    }
	  editBusinessHoursButton.setText("Save");
	  }
  }
  String[] currentHours= new String[7];
  private void saveBusinessHoursActionPerformed(java.awt.event.ActionEvent evt) {
	  errorMessage = null;
	  for (int i=0; i<7; i++) {
		  currentHours[i] = dayTextFields.get(i).getText();
	  }
	  for (int i = 0; i<7; i++) {
		  if (!prevHours[i].equals(currentHours[i])) {
			  if (notValidBusinessHour(currentHours[i])) {
				  errorMessage = "Business hours must be in 24 hour time. (i.e., 07:00-18:30)";
			  }
			  else if (prevHours[i] == "ADD HOURS") {
				  try {
					  String day = dayLabels.get(i).getText();
					  String startTime = currentHours[i].substring(0,5);
					  String endTime = currentHours[i].substring(6);
					  FlexiBookController.addNewBusinessHour(day, startTime, endTime);
				  } catch (InvalidInputException e) {
					  errorMessage = e.getMessage();
				  }
			  } else {
				  try {
					  String prevDay = dayLabels.get(i).getText();
					  String prevStartTime = prevHours[i].substring(0,5);
					  String newDay = prevDay;
					  String newStartTime = currentHours[i].substring(0,5);
					  String newEndTime = currentHours[i].substring(6);
					  FlexiBookController.updateBusinessHour(prevDay, prevStartTime, newDay, newStartTime, newEndTime);
				  } catch (InvalidInputException e) {
					  errorMessage = e.getMessage();
				  }
			  } 
		  }
	  }
	  if (errorMessage == null) {
		  refreshData();
		  pack();
	  } else {
		  errorMessageLabel.setText(errorMessage);
	  }
  }
  private void editContactInfoActionPerformed(java.awt.event.ActionEvent evt) {
	  errorMessage = null;
	  editContactInfoButton.setText("Save");
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
	  if (errorMessage == null) {
		  System.out.println("null");
		  refreshData();
		  pack();
	  } else {
		  System.out.println(errorMessage);
		  errorMessageLabel.setText(errorMessage);
	  }
  }
	/**
	 * @author Julie
	 */
	private static boolean notValidBusinessHour(String businessHour) {
		Pattern pattern = Pattern.compile("(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]-([01]?[0-9]|2[0-3]):[0-5][0-9]");
		Matcher matcher = pattern.matcher(businessHour);
		if (matcher.matches()) {
			return false;
		} else {
			return true;
		}
	}

}