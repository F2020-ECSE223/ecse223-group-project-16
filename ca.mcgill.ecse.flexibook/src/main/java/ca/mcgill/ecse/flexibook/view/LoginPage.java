package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;


public class LoginPage extends JFrame {
  private static final long serialVersionUID = 1307717424939065361L;
  
  // UI elements
  private JLabel errorMessageLabel;
  // username
  private JTextField userTextField;
  private JLabel userLabel;
  // password
  private JPasswordField passTextField;
  private JLabel passLabel;
  // login 
  private JButton loginButton;
  
  // Data elements
  private String errorMessage = null;

  public LoginPage() {
      initComponents();
  }
  
  public LoginPage(String username) {
	  initComponents();
	  userTextField.setText(username);
  }
  
  private void initComponents(){
    // elements for error message
 	errorMessageLabel = new JLabel();
 	errorMessageLabel.setForeground(Color.RED);
    
    // elements for username field
    userTextField = new JTextField();
    userLabel = new JLabel();
    userLabel.setText("Username");
    
    // elements for password field
    passTextField = new JPasswordField();
    passLabel = new JLabel();
    passLabel.setText("Password");
    
    // elements for login button
    loginButton = new JButton();
    loginButton.setText("Login");
    
    // action listeners
    loginButton.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			loginButtonActionPerformed(evt);
		}
	});
    
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Login Here");

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup()
          .addComponent(userLabel)
          .addComponent(passLabel))	  
          .addGroup(layout.createParallelGroup()
          .addComponent(errorMessageLabel)
          .addComponent(userTextField)
          .addComponent(passTextField)
          .addComponent(loginButton))
        )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup() 
        .addGroup(layout.createSequentialGroup()
          .addComponent(errorMessageLabel)
          .addGroup(layout.createParallelGroup()
        		.addComponent(userLabel)
        		.addComponent(userTextField))
          .addGroup(layout.createParallelGroup()
        		.addComponent(passLabel)
          		.addComponent(passTextField))
          .addComponent(loginButton)
       )
    );
    pack();
    setResizable(true);
  }
  
  private void refreshData() {
	  errorMessageLabel.setText(errorMessage);
	  
	  if (errorMessage != null || errorMessage.length() == 0) {
		  errorMessageLabel.setText(errorMessage);
		  userTextField.setText("");
		  passTextField.setText("");
			
		  pack();
	  }
  }
  
  private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// clear error message and basic input validation
		errorMessage = null;
		
		String username = userTextField.getText();
		String password = String.valueOf(passTextField.getPassword());
		
		// call the controller
		try {
			FlexiBookController.login(username, password);
			dispose();
		} catch (InvalidInputException e) {
			errorMessage = e.getMessage();
		} finally {
			refreshData();
		}
  }
	
}
