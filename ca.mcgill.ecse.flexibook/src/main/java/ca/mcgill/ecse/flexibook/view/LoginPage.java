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
  private JLabel loginLabel;
  private JLabel errorMessage;
  private JTextField userTextField;
  private JLabel userLabel;
  private JPasswordField passTextField;
  private JLabel passLabel;
  private JButton loginButton;
  
  // Data elements
  private String error = null;

  public LoginPage() {
      initComponents();
  }
  private void initComponents(){
    loginLabel = new JLabel();
    loginLabel.setText("Login Tab here");
    
    // elements for error message
 	errorMessage = new JLabel();
 	errorMessage.setForeground(Color.RED);
    
    // username text field and label
    userTextField = new JTextField();
    userLabel = new JLabel();
    userLabel.setText("Username");
    
    // password text field and label
    passTextField = new JPasswordField();
    passLabel = new JLabel();
    passLabel.setText("Password");
    
    // login button
    loginButton = new JButton();
    loginButton.setText("Login");
    
    loginButton.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			loginButtonActionPerformed(evt);
		}
	});
    
    
    

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Login Tab");

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup()
          .addComponent(loginLabel)
          .addComponent(errorMessage)
          .addComponent(userTextField)
          .addComponent(passTextField)
          .addComponent(userLabel)
          .addComponent(passLabel)
          .addComponent(loginButton)
          )
        )
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
        .addComponent(loginLabel)
        .addComponent(errorMessage)
        .addComponent(userTextField)
        .addComponent(passTextField)
        .addComponent(userLabel)
        .addComponent(passLabel)
        .addComponent(loginButton)
      )
    );
    pack();
  }
  
  private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// clear error message and basic input validation
		error = null;
		
		String username = userTextField.getText();
		String password = String.valueOf(passTextField.getPassword());
		
		// call the controller
		try {
			FlexiBookController.login(username, password);
			this.setVisible(false);
		} catch (InvalidInputException e) {
			error = e.getMessage();
			
			errorMessage.setText(error);
			userTextField.setText("");
			passTextField.setText("");
			
			pack();
		} 
  }
	
}
