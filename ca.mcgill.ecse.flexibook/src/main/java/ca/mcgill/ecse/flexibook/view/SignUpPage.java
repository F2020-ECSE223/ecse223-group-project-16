package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class SignUpPage extends JFrame {
	private static final long serialVersionUID = -6027711256830801450L;

	// constants
	private static final int TEXT_FIELD_WIDTH = 300;

	// UI elements
	// error
	private JTextArea errorMessageTextArea; // for line wrapping
	// username
	private JLabel usernameLabel;
	private JTextField usernameTextField;
	// password
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private JButton passwordVisibilityButton;
	// onboarding flow
	private JButton signUpButton;
	private JButton switchToLoginButton;

	// data elements
	private String errorMessage = "";

	public SignUpPage() {
		initComponents();
		refreshData();
	}

	private void initComponents() {
		// UI elements
		// error
	    errorMessageTextArea = new JTextArea();
	    errorMessageTextArea.setForeground(Color.RED);
	    errorMessageTextArea.setWrapStyleWord(true);
	    // mimic a JLabel
	    errorMessageTextArea.setLineWrap(true);
	    errorMessageTextArea.setOpaque(false);
	    errorMessageTextArea.setEditable(false);
	    errorMessageTextArea.setFocusable(false);
	    errorMessageTextArea.setBackground(UIManager.getColor("Label.background"));
	    errorMessageTextArea.setFont(UIManager.getFont("Label.font"));
	    errorMessageTextArea.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
	    
		// username
		usernameLabel = new JLabel("Username");
		usernameTextField = new JTextField();
		Utils.resizeTextFieldToWidth(usernameTextField, TEXT_FIELD_WIDTH);
		// password
		passwordLabel = new JLabel("Password");
		passwordField = new JPasswordField();
		Utils.resizeTextFieldToWidth(passwordField, TEXT_FIELD_WIDTH);
		passwordField.setEchoChar('*');
		passwordVisibilityButton = new JButton("Show");
		// sign up
		signUpButton = new JButton("Sign Up");
		signUpButton.setForeground(Color.WHITE);
		// switch to login
		switchToLoginButton = new JButton("Login instead");
		switchToLoginButton.setForeground(Color.DARK_GRAY);

		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Create your FlexiBook Account");
		getRootPane().setDefaultButton(signUpButton); // Wire enter key to sign up button
		setResizable(false);

		// listeners
		passwordVisibilityButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				passwordVisibilityButtonActionPerformed(event);
			}
		});
		
		signUpButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				signUpButtonActionPerformed(event);
			}
		});
		
		switchToLoginButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				switchToLoginButtonActionPerformed(event);
			}
		});
		
		addWindowFocusListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent event) {
		    	signUpButton.setForeground(Color.WHITE);
		    }
		    
		    public void windowLostFocus(WindowEvent event) {
		    	signUpButton.setForeground(Color.BLACK);
		    }
		});

		// layout
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		//@formatter:off
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(
						layout.createSequentialGroup()
						.addGroup(
								layout.createParallelGroup()
								.addComponent(usernameLabel, Alignment.TRAILING)
								.addComponent(passwordLabel, Alignment.TRAILING)
								)
						.addGroup(
								layout.createParallelGroup()
								.addComponent(usernameTextField)
								.addGroup(
										layout.createSequentialGroup()
										.addGroup(
												layout.createParallelGroup()
												.addComponent(passwordField)
												.addComponent(switchToLoginButton, Alignment.LEADING)
												)
										.addGroup(
												layout.createParallelGroup(Alignment.TRAILING)
												.addComponent(passwordVisibilityButton)
												.addComponent(signUpButton)
												)									
										)
								)
						)
				.addComponent(errorMessageTextArea, Alignment.CENTER)
				);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(Alignment.CENTER)
						.addComponent(usernameLabel)
						.addComponent(usernameTextField)
						)
				.addGroup(
						layout.createParallelGroup(Alignment.CENTER)
						.addComponent(passwordLabel)
						.addComponent(passwordField)
						.addComponent(passwordVisibilityButton)
						)
				.addComponent(errorMessageTextArea)
				.addGroup(
						layout.createParallelGroup()
						.addComponent(switchToLoginButton, Alignment.LEADING)
						.addComponent(signUpButton, Alignment.LEADING)
						)
				);
		//@formatter:on
		
		pack();
	}

	private void refreshData() {
		errorMessageTextArea.setText(errorMessage);
		if (errorMessage == null || errorMessage.trim().length() == 0) {
			// no-op
		}

		pack();
	}
	
	private void passwordVisibilityButtonActionPerformed(java.awt.event.ActionEvent evt) {
		Utils.togglePasswordFieldVisibility(passwordField, passwordVisibilityButton);
	}
	
	private void switchToLogin() {
		Utils.switchToFrame(this, new LoginPage()); // would like to be able to pass in a username here into the login constructor, to use as a default value
	}
	
	private void signUpButtonActionPerformed(java.awt.event.ActionEvent evt) {
		errorMessage = null;
		try {
			FlexiBookController.createCustomerAccount(usernameTextField.getText(), String.valueOf(passwordField.getPassword()));
			switchToLogin();
		} catch (InvalidInputException e) {
			errorMessage = e.getMessage();
		} finally {
			refreshData();
		}
	}
	
	private void switchToLoginButtonActionPerformed(java.awt.event.ActionEvent evt) {
		switchToLogin();
	}
}
