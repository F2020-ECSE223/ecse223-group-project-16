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
  	
  // Constants
  private static final int TEXT_FIELD_WIDTH = 300;
	
  // UI elements
  private JLabel errorMessageLabel;
  // username
  private JTextField userTextField;
  private JLabel userLabel;
  // password
  private JPasswordField passTextField;
  private JLabel passLabel;
  private JButton showPassButton;
  // login 
  private JButton loginButton;
  // page navigation
  private JButton signUpInsteadButton;
  
  // Data elements
  private String errorMessage = "";

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
    Utils.resizeTextFieldToWidth(userTextField, TEXT_FIELD_WIDTH);
    userLabel = new JLabel("Username");
    
    // elements for password field
    passTextField = new JPasswordField();
    passTextField.setEchoChar('*');
    Utils.resizeTextFieldToWidth(passTextField, TEXT_FIELD_WIDTH);
    passLabel = new JLabel("Password");
    showPassButton = new JButton("Show");
    
    // elements for login button
    loginButton = new JButton("Login");
    loginButton.setForeground(Color.WHITE);
    
    // elements for sign up instead button
    signUpInsteadButton = new JButton("Sign Up Instead");
    
    // action listeners
    loginButton.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			loginButtonActionPerformed(evt);
		}
	});
    
    showPassButton.addActionListener(new java.awt.event.ActionListener() {
  		public void actionPerformed(java.awt.event.ActionEvent evt) {
  			showPassButtonActionPerformed(evt);
  		}
  	});
    
    signUpInsteadButton.addActionListener(new java.awt.event.ActionListener() {
  		public void actionPerformed(java.awt.event.ActionEvent evt) {
  			signUpInsteadButtonActionPerformed(evt);
  		}
  	});
    
    
    // formatting
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Login");
    getRootPane().setDefaultButton(loginButton); 
    setResizable(false);

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup()
            .addComponent(userLabel)
            .addComponent(passLabel)
          )	  
          .addGroup(layout.createParallelGroup()
            .addComponent(userTextField)
            .addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup()
							.addComponent(passTextField)
							.addComponent(signUpInsteadButton)
							)
					.addGroup(layout.createParallelGroup()
							.addComponent(showPassButton)
							.addComponent(loginButton)
							)									
					)
            .addComponent(errorMessageLabel)
          )
        )
    );
    layout.setVerticalGroup(
      layout.createSequentialGroup() 
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup()
            .addComponent(userLabel)
            .addComponent(userTextField)
          )
          .addGroup(layout.createParallelGroup()
            .addComponent(passLabel)
            .addComponent(passTextField)
            .addComponent(showPassButton)
          )
          .addComponent(errorMessageLabel)
          .addGroup(layout.createParallelGroup()
        	.addComponent(signUpInsteadButton)
            .addComponent(loginButton)
          )
       )
    );
    pack();
  }
  
  private void refreshData() {
	  errorMessageLabel.setText(errorMessage);

	  if (errorMessage.length() != 0) {
		  userTextField.setText("");
		  passTextField.setText("");
			
		  pack();
	  }
  }
  
  private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// clear error message 
		errorMessage = "";
		
		// get username and password
		String username = userTextField.getText();
		String password = String.valueOf(passTextField.getPassword());
		
		// call the controller
		try {
      FlexiBookController.login(username, password);
      Utils.switchToFrame(this, new MenuPage());
			
		} catch (InvalidInputException e) {
			errorMessage = e.getMessage();
		} finally {
			refreshData();
		}
  }
  
  private void showPassButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (showPassButton.getText().equals("Show")) {
			passTextField.setEchoChar((char) 0);
			showPassButton.setText("Hide");
		}
		else {
			passTextField.setEchoChar('*');
			showPassButton.setText("Show");
		}
  }
  
  private void signUpInsteadButtonActionPerformed(java.awt.event.ActionEvent evt) {
	    Utils.switchToFrame(this, new SignUpPage());
  }
	
}
