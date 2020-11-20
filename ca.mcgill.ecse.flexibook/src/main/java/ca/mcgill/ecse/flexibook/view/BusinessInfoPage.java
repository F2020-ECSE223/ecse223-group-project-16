package ca.mcgill.ecse.flexibook.view;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.model.BusinessHour;

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
  
  // UI Elements
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
  	// Business address & contact
  private JTextField businessAddress;
  private JTextField businessPhone;
  private JTextField businessEmail;
  
  
  public BusinessInfoPage() {
    initComponents();
  }
  private void initComponents(){
    businessInfoLabel = new JLabel();
    businessInfoLabel.setText("Business Info Tab here");
    // Titles
    businessNameTitle = new JLabel(FlexiBookApplication.getFlexiBook().getBusiness().getName());
    businessHoursTitle = new JLabel("Business Hours");
    businessAddressTitle = new JLabel("Address");
    businessContactInfoTitle = new JLabel("Contact Information");
    // Edit buttons
    editHoursButton = new JButton("Click to edit hours");
    editAddressButton = new JButton("Click to change address");
    editContactInfoButton = new JButton("Click to change contact information");
    // Business Hour elements
    mon = new JLabel("Monday");
    tues = new JLabel("Tuesday");
    wed = new JLabel("Wednesday");
    thurs = new JLabel("Thursday");
    fri = new JLabel("Friday");
    sat = new JLabel("Saturday");
    sun = new JLabel("Sunday");
    for (BusinessHour bh : FlexiBookApplication.getFlexiBook().getBusiness().getBusinessHours()) {
    	if (bh.getDayOfWeek().toString().equals("Monday")) {
    		monHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    	}
    	else if (bh.getDayOfWeek().toString().equals("Tuesday")) {
       		tuesHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    	}
    	else if (bh.getDayOfWeek().toString().equals("Wednesday")) {
       		wedHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    	}
    	else if (bh.getDayOfWeek().toString().equals("Thursday")) {
       		thursHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    	}
    	else if (bh.getDayOfWeek().toString().equals("Friday")) {
       		friHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    	}
    	else if (bh.getDayOfWeek().toString().equals("Saturday")) {
       		satHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    	}
    	else if (bh.getDayOfWeek().toString().equals("Sunday")) {
       		sunHours = new JTextField(bh.getStartTime().toString() + bh.getEndTime().toString());
    	}
    }
    // Business address and contact
    

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Business Info Tab");

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup()
          .addComponent(businessInfoLabel)
          )
        )
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
        .addComponent(businessInfoLabel)
      )
    );
    pack();
  }
}
