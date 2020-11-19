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
	private void resizeTextField(JTextField textField) {
		Dimension textFieldDimension = new Dimension(TEXT_FIELD_WIDTH, textField.getPreferredSize().height);
		textField.setPreferredSize(textFieldDimension);
//		textField.setMaximumSize(textFieldDimension);
	}

	private void makePlaceholder(JTextField textField, String placeholderText) {
		textField.setText(placeholderText);
		textField.setForeground(Color.GRAY);
		textField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField.getText().equals(placeholderText)) {
					textField.setText("");
					textField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().isEmpty()) {
					textField.setForeground(Color.GRAY);
					textField.setText(placeholderText);
				}
			}
		});
	}

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
	private JLabel confirmPasswordLabel;
	private JTextField confirmPasswordTextField;
	private JButton passwordVisibilityButton;
	// onboarding flow
	private JButton signUpButton;
	private JButton goToLoginButton;

	// data elements
	private String errorMessage = "";

	public SignUpPage() {
		initComponents();
		refreshData();
	}

	private void initComponents() {
		// UI elements
		// elements for error message
	    errorMessageTextArea = new JTextArea();
	    errorMessageTextArea.setForeground(Color.RED);
	    errorMessageTextArea.setWrapStyleWord(true);
	    // mimic a JLabel
	    errorMessageTextArea.setLineWrap(true);
	    errorMessageTextArea.setOpaque(false);
	    errorMessageTextArea.setEditable(false);
//	    errorMessageTextArea.setFocusable(false);
	    errorMessageTextArea.setBackground(UIManager.getColor("Label.background"));
	    errorMessageTextArea.setFont(UIManager.getFont("Label.font"));
	    errorMessageTextArea.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
	    
		// elements for username field
		usernameLabel = new JLabel("Username");
		usernameTextField = new JTextField(); // placeholder
		resizeTextField(usernameTextField);
		// elements for password field
		passwordLabel = new JLabel("Password");
		passwordField = new JPasswordField(); // placeholder
		resizeTextField(passwordField);
		passwordField.setEchoChar('*');
		passwordVisibilityButton = new JButton("Show");
		// elements for Sign Up button
		signUpButton = new JButton("Sign Up");
		signUpButton.setForeground(Color.WHITE);
		// elements for go to Login button
		goToLoginButton = new JButton("Login instead");
		goToLoginButton.setForeground(Color.DARK_GRAY);

		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // JFrame.DISPOSE_ON_CLOSE ??
		setTitle("Create your FlexiBook Account");

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
		
		goToLoginButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				goToLoginButtonActionPerformed(event);
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
//		layout.setHorizontalGroup(
//				layout.createParallelGroup()
//				.addGroup(
//						layout.createSequentialGroup()
//						.addGroup(
//								layout.createParallelGroup()
//								.addComponent(usernameLabel, Alignment.TRAILING)
//								.addComponent(passwordLabel, Alignment.TRAILING)
//								)
//						.addGroup(
//								layout.createParallelGroup()
//								.addComponent(usernameTextField)
//								.addGroup(
//										layout.createSequentialGroup()
//										.addComponent(passwordField)
//										.addComponent(passwordVisibilityButton)
//										)
//								.addGroup(
//										Alignment.TRAILING,
//										layout.createSequentialGroup()
//										.addComponent(goToLoginButton)
//										.addComponent(signUpButton)
//										)
//								)
//						)
//				.addComponent(errorMessageTextArea, Alignment.CENTER)
//				);
//
//		layout.setVerticalGroup(layout.createSequentialGroup()
//				.addGroup(
//						layout.createParallelGroup(Alignment.CENTER)
//						.addComponent(usernameLabel)
//						.addComponent(usernameTextField)
//						)
//				.addGroup(
//						layout.createParallelGroup(Alignment.CENTER)
//						.addComponent(passwordLabel)
//						.addComponent(passwordField)
//						.addComponent(passwordVisibilityButton)
//						)
//				.addComponent(errorMessageTextArea)
//				.addGroup(
//						layout.createParallelGroup()
//						.addComponent(goToLoginButton, Alignment.LEADING)
//						.addComponent(signUpButton, Alignment.TRAILING)
//						)
//				);
		
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
												.addComponent(goToLoginButton, Alignment.LEADING)
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
						.addComponent(goToLoginButton, Alignment.LEADING)
						.addComponent(signUpButton, Alignment.LEADING)
						)
				);
		//@formatter:on
		
		pack();
		setResizable(true);
		
		getRootPane().setDefaultButton(signUpButton); // Enter key wired to signUpButton
	}

	private void refreshData() {
		errorMessageTextArea.setText(errorMessage);
		if (errorMessage == null || errorMessage.trim().length() == 0) {
			// populate
		}

		// this is needed because the size of the window changes depending on whether an error message is shown or not
		pack();
	}
	
	private void passwordVisibilityButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (passwordVisibilityButton.getText().equals("Show")) {
			passwordVisibilityButton.setText("Hide");
			passwordField.setEchoChar((char) 0);
		} else {
			passwordVisibilityButton.setText("Show");
			passwordField.setEchoChar('*');
		}
	}
	
	private void goToLogin() {
		LoginPage loginPage = new LoginPage(); // would like to be able to pass in a username here into the login constructor, to use as a default value
		loginPage.setVisible(true);
		loginPage.setLocationRelativeTo(this); // center spawned login page on own center
		dispose(); // self-destruct
	}
	
	private void signUpButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// clear error message
		errorMessage = null;
		try {
			FlexiBookController.createCustomerAccount(usernameTextField.getText(), String.valueOf(passwordField.getPassword()));
			goToLogin();
		} catch (InvalidInputException e) {
			errorMessage = e.getMessage();
		} finally {
			refreshData();
		}
	}
	
	private void goToLoginButtonActionPerformed(java.awt.event.ActionEvent evt) {
		goToLogin();
	}
}
