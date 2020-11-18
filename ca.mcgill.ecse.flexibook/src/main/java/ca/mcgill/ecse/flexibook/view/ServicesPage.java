package ca.mcgill.ecse.flexibook.view;

import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import ca.mcgill.ecse.flexibook.controller.TOBookableService;
import ca.mcgill.ecse.flexibook.controller.TOCalendar;

public class ServicesPage extends JFrame {
  private static final long serialVersionUID = 4990227802404187714L;
 
  private JLabel serviceLabel;
  
  private JLabel deleteServiceLabel;
  private JComboBox<String> deleteServiceList;
  private JButton deleteServiceButton;
 
  private JLabel updateServiceLabel;
  private JComboBox<String> updateServiceList;
  private JTextField updateServiceNameTextField;
  private JLabel updateServiceNameLabel;
  private JTextField updateServiceDurationTextField;
  private JLabel updateServiceDurationLabel;
  private JTextField updateServiceDownTimeTextField;
  private JLabel updateServiceDownTimeLabel;
  private JTextField updateServiceDTDurationTextField;
  private JLabel updateServiceDTDurationLabel;
  private JButton updateServiceButton;
  
  private JLabel addServiceLabel;
  private JTextField addServiceNameTextField;
  private JLabel addServiceNameLabel;
  private JTextField addServiceDurationTextField;
  private JLabel addServiceDurationLabel;
  private JTextField addServiceDownTimeTextField;
  private JLabel addServiceDownTimeLabel;
  private JTextField addServiceDTDurationTextField;
  private JLabel addServiceDTDurationLabel;
  private JButton addServiceButton;
 
 
  
  //data elements
  private List<TOBookableService> bookableServices;
  private TOCalendar calendar;
   

  public ServicesPage() {
      initComponents();
  }
  private void initComponents(){
   
	// DELETE SERVICE ELEMENTS
	deleteServiceLabel = new JLabel();
    deleteServiceLabel.setText("Delete Existing Service");
    deleteServiceList = new JComboBox<String>(new String[0]);
    deleteServiceButton = new JButton();
    deleteServiceButton.setText("Delete Service");
    
    
    // UPDATE SERVICE ELEMENTS
    updateServiceLabel.setText("Update Existing Service");
    updateServiceList = new JComboBox<String>(new String[0]);
    
    updateServiceNameTextField = new JTextField();
    updateServiceNameLabel = new JLabel();
    updateServiceNameLabel.setText("Name:");
    
    
    updateServiceDurationTextField = new JTextField();
    updateServiceDurationLabel = new JLabel();
    updateServiceDurationLabel.setText("Duration:");
    
    
    updateServiceDownTimeTextField = new JTextField();
    updateServiceDownTimeLabel = new JLabel();
    updateServiceDownTimeLabel.setText("Down Time:");
    
    updateServiceDTDurationTextField = new JTextField();
    updateServiceDTDurationLabel = new JLabel();
    updateServiceDTDurationLabel.setText("Down Time Duration:");
    
    updateServiceButton = new JButton();
    updateServiceButton.setText("Update Existing Service");
    
    
    
    //ADD SERVICE ELEMENTS
    addServiceLabel.setText("Create New Service");
    
    addServiceNameTextField = new JTextField();
    addServiceNameLabel = new JLabel();
    addServiceNameLabel.setText("Name:");
    
    
    addServiceDurationTextField = new JTextField();
    addServiceDurationLabel = new JLabel();
    addServiceDurationLabel.setText("Duration:");
    
    
    addServiceDownTimeTextField = new JTextField();
    addServiceDownTimeLabel = new JLabel();
    addServiceDownTimeLabel.setText("Down Time:");
    
    addServiceDTDurationTextField = new JTextField();
    addServiceDTDurationLabel = new JLabel();
    addServiceDTDurationLabel.setText("Down Time Duration:");
 
    addServiceButton = new JButton();
    addServiceButton.setText("Add Service");
    
    
    //Listeners
    deleteServiceButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            deleteServiceButtonActionPerformed(evt);
        }
    });
    
    updateServiceButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            updateServiceButtonActionPerformed(evt);
        }
    });
    
    addServiceButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            addServiceButtonActionPerformed(evt);
        }
    });
    
    JSeparator horizontalLineTop = new JSeparator();
    JSeparator horizontalLineBottom = new JSeparator();
    

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Services Tab");

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup()
          .addComponent(serviceLabel)
          )
        )
      )
    );
    
    
    layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {deleteServiceButton, deleteServiceList});
    layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {updateServiceButton, updateServiceList});
    layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addServiceButton, addServiceNameTextField});
    
    layout.setVerticalGroup(
      layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
        .addComponent(serviceLabel)
      )
    );
    pack();
  }
}
