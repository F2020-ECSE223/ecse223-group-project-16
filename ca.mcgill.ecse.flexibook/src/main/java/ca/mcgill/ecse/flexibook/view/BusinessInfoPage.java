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
  private JTextField businessNameTitle;
  private JLabel businessHoursTitle;
  private JLabel businessAddressTitle;
  private JLabel businessContactInfoTitle;
  	// Edit buttons
  private JButton editHoursButton;
  private JButton editAddressButton;
  private JButton editContactInfoButton;
  	// Business Hour Elements
  private JTable businessHoursContainer;
  	// Business address & contact
  private JTextField businessAddress;
  private JTextField businessPhone;
  private JTextField businessEmail;
  
  
  public BusinessInfoPage() {
    initComponents();
  }

  private void initComponents(){
// INTIALIZE
    // Titles
    businessNameTitle = new JTextField();
    	businessNameTitle.setText("CLICK HERE TO SET BUSINESS NAME");
    businessHoursTitle = new JLabel();
    	businessHoursTitle.setText("Business Hours");
    businessAddressTitle = new JLabel();
    	businessAddressTitle.setText("Address");
    businessContactInfoTitle = new JLabel();
    	businessContactInfoTitle.setText("Contact Information");
    // Edit buttons
    editHoursButton = new JButton();
    	editHoursButton.setText("Click to edit hours");
    editAddressButton = new JButton();
    	editAddressButton.setText("Click to change address");
    editContactInfoButton = new JButton();
    	editContactInfoButton.setText("Click to change contact information");
    
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
    
    // Business address and contact
    businessAddress = new JTextField();
    	businessAddress.setText("ADD ADDRESS");
    businessPhone = new JTextField();;
    	businessPhone.setText("ADD PHONE NUMBER");
    businessEmail = new JTextField();
    	businessEmail.setText("ADD EMAIL");
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
    layout.setHorizontalGroup(layout
    		.createParallelGroup()
    		.addComponent(businessNameTitle)
    		.addComponent(businessHoursTitle)
    		.addComponent(businessHoursContainer)
    		.addComponent(editHoursButton)
    		.addComponent(businessAddressTitle)
    		.addComponent(businessAddress)
    		.addComponent(businessContactInfoTitle)
    		.addComponent(businessPhone)
    		.addComponent(businessEmail)
    );
    layout.setVerticalGroup(layout
    		.createSequentialGroup()
    		.addComponent(businessNameTitle)
    		.addComponent(businessHoursTitle)
    		.addComponent(businessHoursContainer)
    		.addComponent(editHoursButton)
    		.addComponent(businessAddressTitle)
    		.addComponent(businessAddress)
    		.addComponent(businessContactInfoTitle)
    		.addComponent(businessPhone)
    		.addComponent(businessEmail)
    );
    /* fancier layout :( (not working currently)
    layout.setHorizontalGroup(layout
    		.createSequentialGroup()
    		.addGroup(layout
    	    		.createParallelGroup()
    	    		.addComponent(businessNameTitle)
    	    	    .addComponent(businessHoursTitle)
    	    	    .addComponent(businessHoursContainer)
    	    	    .addComponent(editHoursButton)
    				)
    		.addGroup(layout
    						.createParallelGroup()
    	    				.addComponent(businessAddressTitle)
    	    				.addComponent(businessAddress)
    	    				.addComponent(editAddressButton)
    	    				.addComponent(businessContactInfoTitle)
    	    				.addComponent(editContactInfoButton)
    						)
    );
    layout.setVerticalGroup(layout
    		.createSequentialGroup()
    		.addComponent(businessNameTitle)
    		.addGroup(layout
    				.createParallelGroup()
    				.addComponent(businessHoursTitle)
    				.addComponent(businessAddressTitle)
    				)
    		.addGroup(layout
    				.createParallelGroup()
    				.addComponent(businessHoursContainer)
    				.addGroup(layout
    						.createSequentialGroup()
    						.addComponent(businessAddress)
    						.addComponent(editAddressButton)
    						.addComponent(businessContactInfoTitle)
    						.addComponent(businessPhone)
    						.addComponent(businessEmail)
    						)
    				)
    		.addGroup(layout
    				.createParallelGroup()
    				.addComponent(editHoursButton)
    				.addComponent(editContactInfoButton)
    				)
    		);
    */
    pack();
    
    }
  }