package ca.mcgill.ecse.flexibook.view;

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
  private JTextField monday;
  private JTextField tuesday;
  private JTextField wednesday;
  private JTextField thursday;
  private JTextField friday;
  private JTextField saturday;
  private JTextField sunday;
  private JTextField mondayHours;
  private JTextField tuesdayHours;
  private JTextField wednesdayHours;
  private JTextField thursdayHours;
  private JTextField fridayHours;
  private JTextField saturdayHours;
  private JTextField sundayHours;
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
