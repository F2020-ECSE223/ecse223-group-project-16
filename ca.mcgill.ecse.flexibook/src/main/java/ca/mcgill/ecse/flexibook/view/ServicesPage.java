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
           // deleteServiceButtonActionPerformed(evt);
        }
    });
    
    updateServiceButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
           // updateServiceButtonActionPerformed(evt);
        }
    });
    
    addServiceButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
           // addServiceButtonActionPerformed(evt);
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
    	  layout.createParallelGroup()
    	  .addComponent(serviceLabel)
    	  .addComponent(horizontalLineTop)
    	  .addComponent(horizontalLineBottom)
    	  .addGroup(layout.createSequentialGroup()
    			  .addGroup(layout.createParallelGroup()
    					  .addComponent(addServiceLabel)
    					  .addComponent(updateServiceLabel)
    					  .addComponent(updateServiceList)
    					  .addComponent(deleteServiceLabel)
    					  .addComponent(deleteServiceList)
    					  )
    			  )
    	  		.addGroup(layout.createParallelGroup()
    	  				.addComponent(addServiceNameLabel)
    	  				.addComponent(updateServiceNameLabel)
    	  				)
    	  		.addGroup(layout.createParallelGroup()
    	  				.addComponent(addServiceNameTextField)
    	  				.addComponent(updateServiceNameTextField)
    	  				)
    	  		.addGroup(layout.createParallelGroup()
    	  				.addComponent(addServiceDurationLabel)
    	  				.addComponent(updateServiceDurationLabel)
    	  				)
    	  		.addGroup(layout.createParallelGroup()
    	  				.addComponent(addServiceDurationTextField)
    	  				.addComponent(updateServiceDurationTextField)
    	  				)
    	  		.addGroup(layout.createParallelGroup()
    	  				.addComponent(addServiceDownTimeLabel)
    	  				.addComponent(updateServiceDownTimeLabel)
    	  				)
    	  		.addGroup(layout.createParallelGroup()
    	  				.addComponent(addServiceDownTimeTextField)
    	  				.addComponent(updateServiceDownTimeTextField)
    	  				)
    	  		.addGroup(layout.createParallelGroup()
    	  				.addComponent(addServiceDTDurationLabel)
    	  				.addComponent(updateServiceDTDurationLabel)
    	  				)
    	  		.addGroup(layout.createParallelGroup()
    	  				.addComponent(addServiceDTDurationTextField)
    	  				.addComponent(updateServiceDTDurationTextField)
    	  				)
    	  		.addGroup(layout.createParallelGroup()
    	  				.addComponent(addServiceButton)
    	  				.addComponent(updateServiceButton)
    	  				.addComponent(deleteServiceButton)
    	  				)
    );
    
    
    layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {deleteServiceButton, deleteServiceList});
    layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {updateServiceButton, updateServiceList});
    layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addServiceButton, addServiceNameTextField});
    
    layout.setVerticalGroup(
	      layout.createSequentialGroup()
	      .addGroup(layout.createParallelGroup()
				.addComponent(addServiceLabel)
				.addComponent(addServiceNameLabel)
				.addComponent(addServiceNameTextField)
				.addComponent(addServiceDurationLabel)
				.addComponent(addServiceDurationTextField)
				.addComponent(addServiceDownTimeLabel)
				.addComponent(addServiceDownTimeTextField)
				.addComponent(addServiceDTDurationLabel)  
				.addComponent(addServiceDTDurationTextField)
				)
	      .addComponent(addServiceButton)
	      .addGroup(layout.createParallelGroup()
	    		.addComponent(horizontalLineTop) 
	    		)
	      .addGroup(layout.createParallelGroup()
	    		.addComponent(updateServiceLabel)
	  			.addComponent(updateServiceNameLabel)
	  			.addComponent(updateServiceNameTextField)
	  			.addComponent(updateServiceDurationLabel)
	  			.addComponent(updateServiceDurationTextField)
	  			.addComponent(updateServiceDownTimeLabel)
	  			.addComponent(updateServiceDownTimeTextField)
	  			.addComponent(updateServiceDTDurationLabel)  
	  			.addComponent(updateServiceDTDurationTextField)
	    		)
	     .addGroup(layout.createParallelGroup()
	    		 .addComponent(updateServiceList)
	    		 .addComponent(updateServiceButton)
	    		 )
	     .addGroup(layout.createParallelGroup()
	    		.addComponent(horizontalLineBottom)
	    		)
	     .addGroup(layout.createParallelGroup()
	    		.addComponent(deleteServiceLabel)
	    		)
		.addGroup(layout.createParallelGroup()
				.addComponent(deleteServiceList)
				.addComponent(deleteServiceButton)	
	  	    )
    );
    pack();
  }
}
