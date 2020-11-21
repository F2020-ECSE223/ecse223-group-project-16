package ca.mcgill.ecse.flexibook.view;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOBusiness;
import ca.mcgill.ecse.flexibook.model.Business; //?

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
  private JLabel businessInfoLabel;
  private JLabel businessNameTitle;
  private JLabel businessHoursTitle;
  private JLabel businessAddressTitle;
  private JLabel businessContactInfoTitle;
  	// Edit buttons
  private JButton editHoursButton;
  private JButton editAddressButton;
  private JButton editContactInfoButton;
  	// Business Hour Elements
  private JTable businessHoursContainer;
  /*
  private JLabel mon;
  private JLabel tues;
  private JLabel wed;
  private JLabel thurs;
  private JLabel fri;
  private JLabel sat;
  private JLabel sun;
  private JTextField monHours;
  private JTextField tuesHours;
  private JTextField wedHours;
  private JTextField thursHours;
  private JTextField friHours;
  private JTextField satHours;
  private JTextField sunHours;
  */
  	// Business address & contact
  private JTextField businessAddress;
  private JTextField businessPhone;
  private JTextField businessEmail;
  
  
  public BusinessInfoPage() {
    initComponents();
  }

  private void initComponents(){
// INTIALIZE
    businessInfoLabel = new JLabel("Business Info Tab here");
    // Titles
    businessNameTitle = new JLabel(FlexiBookApplication.getFlexiBook().getBusiness().getName());
    businessHoursTitle = new JLabel("Business Hours");
    businessAddressTitle = new JLabel("Address");
    businessContactInfoTitle = new JLabel("Contact Information");
    // Edit buttons
    editHoursButton = new JButton("Click to edit hours");
    editAddressButton = new JButton("Click to change address");
    editContactInfoButton = new JButton("Click to change contact information");
    // Edit button listeners
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
    // Business Hour elements
   /*  mon = new JLabel("Monday");
    tues = new JLabel("Tuesday");
    wed = new JLabel("Wednesday");
    thurs = new JLabel("Thursday");
    fri = new JLabel("Friday");
    sat = new JLabel("Saturday");
    sun = new JLabel("Sunday");
    */
    String[][] hours = new String[7][2];
    for (BusinessHour bh : FlexiBookApplication.getFlexiBook().getBusiness().getBusinessHours()) {
    	if (bh.getDayOfWeek().toString().equals("Monday")) {
    		//monHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    		hours[0][0] = "Monday"; hours[0][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
    	}
    	else if (bh.getDayOfWeek().toString().equals("Tuesday")) {
       		//tuesHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    		hours[1][0] = "Tuesday"; hours[1][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
    	}
    	else if (bh.getDayOfWeek().toString().equals("Wednesday")) {
       		//wedHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    		hours[2][0] = "Wednesday"; hours[2][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
    	}
    	else if (bh.getDayOfWeek().toString().equals("Thursday")) {
       		//thursHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    		hours[3][0] = "Thursday"; hours[3][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
    	}
    	else if (bh.getDayOfWeek().toString().equals("Friday")) {
       		//friHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    		hours[4][0] = "Friday"; hours[4][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
    	}
    	else if (bh.getDayOfWeek().toString().equals("Saturday")) {
       		//satHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    		hours[5][0] = "Saturday"; hours [5][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
    	}
    	else if (bh.getDayOfWeek().toString().equals("Sunday")) {
       		//sunHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    		hours[6][0] = "Sunday"; hours[6][1] = bh.getStartTime().toString() + bh.getEndTime().toString();
    	}
    }
    String[] columnNames = {"Day", "Hours"};
    businessHoursContainer = new JTable(hours, columnNames);
    // Business address and contact
    businessAddress = new JTextField(FlexiBookApplication.getFlexiBook().getBusiness().getAddress().toString());
    businessPhone = new JTextField(FlexiBookApplication.getFlexiBook().getBusiness().getPhoneNumber().toString());;
    businessEmail = new JTextField(FlexiBookApplication.getFlexiBook().getBusiness().getEmail().toString());

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Business Info Tab");

    // LAYOUT
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
    		  .addComponent(businessNameTitle)
    		  .addComponent(businessHoursTitle)
    		  .addComponent(businessHoursContainer)
    		  .addComponent(editHoursButton)
      )
      .addGroup(layout.createParallelGroup()
    		  .addComponent(businessAddressTitle)
    		  .addComponent(businessAddress)
    		  .addComponent(editAddressButton)
    		  .addComponent(businessContactInfoTitle)
    		  .addComponent(businessPhone)
    		  .addComponent(businessEmail)
    		  .addComponent(editContactInfoButton)
      )
    );
    layout.setVerticalGroup(
      layout.createSequentialGroup()
      .addComponent(businessNameTitle)
      .addGroup(layout.createParallelGroup()
    		  .addComponent(businessHoursTitle)
        	  .addComponent(businessAddressTitle)
      )
      .addGroup(layout.createParallelGroup()
    		  .addComponent(businessHoursContainer)
    		  .addGroup(layout.createSequentialGroup())
    		  		.addComponent(businessAddress)
    		  		.addComponent(editAddressButton)
    		  		.addComponent(businessContactInfoTitle)
    		  		.addComponent(businessPhone)
    		  		.addComponent(businessEmail)
      )
      .addGroup(layout.createParallelGroup()
    		.addComponent(editHoursButton)
		  	.addComponent(editContactInfoButton)
      )
    );
    pack();
  }
}