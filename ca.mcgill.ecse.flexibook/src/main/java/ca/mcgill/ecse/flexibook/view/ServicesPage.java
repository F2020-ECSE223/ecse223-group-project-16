package ca.mcgill.ecse.flexibook.view;

import java.util.ArrayList;
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

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOBookableService;
import ca.mcgill.ecse.flexibook.model.BookableService;
import ca.mcgill.ecse.flexibook.model.Service;


public class ServicesPage extends JFrame {
  private static final long serialVersionUID = 4990227802404187714L;
 
  private JLabel errorMessage;

  private JLabel serviceLabel;
  

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
  private String error = null;
  private List<String> bookableServices;
   

  public ServicesPage() {
      initComponents();
      refreshData();
  }
  private void initComponents(){
	
	errorMessage = new JLabel();
	
	
	// DELETE SERVICE ELEMENTS
    deleteServiceButton = new JButton();
    deleteServiceButton.setText("Delete Service");
    
    
    // UPDATE SERVICE ELEMENTS
    updateServiceLabel = new JLabel();
    updateServiceLabel.setText("Update/Delete Existing Service");
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
    updateServiceButton.setText("Update Service");
    
    
    
    //ADD SERVICE ELEMENTS
    addServiceLabel = new JLabel();
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
    
    updateServiceList.addActionListener (new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            updateServiceListActionPerformed(evt);
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
    	  		  .addComponent(errorMessage)
    			  .addGroup(layout.createParallelGroup()
    					  .addComponent(addServiceLabel)
    					  .addComponent(updateServiceLabel)
    					  .addComponent(updateServiceList)
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
    
    layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {deleteServiceButton, updateServiceLabel});
    layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {updateServiceButton, updateServiceLabel});
    layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addServiceButton, addServiceLabel});
    
    layout.setVerticalGroup(
	      layout.createSequentialGroup()
	      .addComponent(errorMessage)
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
	    		.addComponent(updateServiceLabel)
	  			.addComponent(updateServiceNameLabel)
	  			.addComponent(updateServiceNameTextField)
	  			.addComponent(updateServiceDurationLabel)
	  			.addComponent(updateServiceDurationTextField)
	  			.addComponent(updateServiceDownTimeLabel)
	  			.addComponent(updateServiceDownTimeTextField)
	  			.addComponent(updateServiceDTDurationLabel)  
	  			.addComponent(updateServiceDTDurationTextField)
	  			.addComponent(updateServiceButton)
	  			
	    		)
	     .addGroup(layout.createParallelGroup()
	    		 .addComponent(updateServiceList)
	    		 .addComponent(deleteServiceButton)
	    		 )
    		);
    pack();
  }
  
  private void refreshData() {
	  
	  updateServiceList.removeAllItems();
	  bookableServices = new ArrayList<String>();
	  
	  Integer index = 0;
      for (TOBookableService service : FlexiBookController.getBookableServices()) {
          bookableServices.add(service.getName());
    	  updateServiceList.addItem(service.getName());
          index++;
      };
      updateServiceList.setSelectedIndex(-1);
      updateServiceNameTextField.setText("");
      updateServiceDurationTextField.setText("");
      updateServiceDownTimeTextField.setText("");
      updateServiceDTDurationTextField.setText("");
      
      addServiceNameTextField.setText("");
      addServiceDurationTextField.setText("");
      addServiceDownTimeTextField.setText("");
      addServiceDTDurationTextField.setText("");
      
  }
  
  private void addServiceButtonActionPerformed(java.awt.event.ActionEvent evt) {
	  try {
		  FlexiBookController.addService(addServiceNameTextField.getText(), addServiceDurationTextField.getText(), addServiceDownTimeTextField.getText(), addServiceDTDurationTextField.getText());
	  }catch (InvalidInputException e){
		  error = e.getMessage();
	  }
	  refreshData();
  }
  
  private void updateServiceButtonActionPerformed(java.awt.event.ActionEvent evt) {
	  try {
		  FlexiBookController.updateService(String.valueOf(updateServiceList.getSelectedItem()),updateServiceNameTextField.getText(), updateServiceDurationTextField.getText(), updateServiceDownTimeTextField.getText(), updateServiceDTDurationTextField.getText());
	  }catch (InvalidInputException e){
		  error = e.getMessage();
	  }
	  refreshData();
  }
  
  private void deleteServiceButtonActionPerformed(java.awt.event.ActionEvent evt) {
	  try {
		  FlexiBookController.deleteService(String.valueOf(updateServiceList.getSelectedItem()));
	  }catch (InvalidInputException e){
		  error = e.getMessage();
	  }
	  refreshData();
  }
  
  private void updateServiceListActionPerformed(java.awt.event.ActionEvent evt) {
	  for (BookableService bS : FlexiBookApplication.getFlexiBook().getBookableServices()){
		  if (bS.getName() == String.valueOf(updateServiceList.getSelectedItem())) {
			  Service serviceSelected = (Service) bS;
			  updateServiceNameTextField.setText(serviceSelected.getName());
		      updateServiceDurationTextField.setText(String.valueOf(serviceSelected.getDuration()));
		      updateServiceDownTimeTextField.setText(String.valueOf(serviceSelected.getDowntimeStart()));
		      updateServiceDTDurationTextField.setText(String.valueOf(serviceSelected.getDowntimeDuration()));
		  }
	  }
	 
  };
  
}
