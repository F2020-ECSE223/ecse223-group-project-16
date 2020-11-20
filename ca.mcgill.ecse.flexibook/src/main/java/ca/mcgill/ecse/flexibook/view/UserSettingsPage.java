package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOUser;


@SuppressWarnings("serial")
public class UserSettingsPage extends JFrame {
	private static final int TEXT_FIELD_WIDTH = 300;
	
	private void resizeTextField(JTextField textField) {
		Dimension textFieldDimension = new Dimension(TEXT_FIELD_WIDTH, textField.getPreferredSize().height);
		textField.setPreferredSize(textFieldDimension);
//		textField.setMaximumSize(textFieldDimension);
	}
	
	// UI elements
	// edit
	private JPanel editPanel;
	// error
	private JTextArea editErrorMessageTextArea;
	// username
	private JLabel usernameLabel;
	private JTextField usernameTextField;
	// password
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private JButton passwordVisibilityButton;
	// controls
	private JPanel editControlsPanel;
	private JButton editButton;
	private JButton confirmEditButton;
	private JButton cancelEditButton;
	
	// delete
	private JPanel deletePanel;
	private JButton deleteButton;
	
	// data elements
	private boolean isCurrentUserOwner;
	private String editErrorMessage = "";
	
	public UserSettingsPage() {
		TOUser currentUser = FlexiBookController.getCurrentUser();
		isCurrentUserOwner = currentUser != null && currentUser.getUsername().equals("owner");
//		isCurrentUserOwner = true; // debug
		initComponents();
		refreshData();
	}
	
	private void initComponents() {
		// UI elements
		// edit
		// error
	    editErrorMessageTextArea = new JTextArea();
	    editErrorMessageTextArea.setForeground(Color.RED);
	    editErrorMessageTextArea.setWrapStyleWord(true);
	    // mimic a JLabel
	    editErrorMessageTextArea.setLineWrap(true);
	    editErrorMessageTextArea.setOpaque(false);
	    editErrorMessageTextArea.setEditable(false);
//	    editErrorMessageTextArea.setFocusable(false);
	    editErrorMessageTextArea.setBackground(UIManager.getColor("Label.background"));
	    editErrorMessageTextArea.setFont(UIManager.getFont("Label.font"));
	    editErrorMessageTextArea.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
	    // username
	    TOUser currentUser = FlexiBookController.getCurrentUser();
		usernameLabel = new JLabel("Username");
		usernameTextField = new JTextField(currentUser == null ? "" : currentUser.getUsername());
		resizeTextField(usernameTextField);
		usernameTextField.setEditable(false);
		// password
		passwordLabel = new JLabel("Password");
		passwordField = new JPasswordField(currentUser == null ? "" : currentUser.getPassword()); // placeholder
		passwordField.setEditable(false);
		resizeTextField(passwordField);
		passwordField.setEchoChar('*');
		passwordVisibilityButton = new JButton("Show");
		// controls
		editButton = new JButton("Edit");
		confirmEditButton = new JButton("Save");
		cancelEditButton = new JButton("Cancel");
		editControlsPanel = new JPanel();
		// delete
		// controls
		deleteButton = new JButton("Delete");
		
		deleteButton.setPreferredSize(new Dimension(deleteButton.getPreferredSize().width * 2, deleteButton.getPreferredSize().height));
		
		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("User Settings");
		
		// listeners
		passwordVisibilityButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				passwordVisibilityButtonActionPerformed(event);
			}
		});
		
		editButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				editButtonActionPerformed(event);
			}
		});
		
		cancelEditButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				cancelEditButtonActionPerformed(event);
			}
		});
		
		confirmEditButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				confirmEditButtonActionPerformed(event);
			}
		});
		
		deleteButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				deleteButtonActionPerformed(event);
			}
		});
		
		//@formatter:off
		// layout
		// edit panel
		editPanel = new JPanel(true);
		GroupLayout editLayout = new GroupLayout(editPanel);
		editLayout.setAutoCreateGaps(true);
		editLayout.setAutoCreateContainerGaps(true);
		editPanel.setLayout(editLayout);
		editPanel.setBorder(
//				BorderFactory.createCompoundBorder(
//						 new EmptyBorder(10, 10, 10, 10),
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder(
										BorderFactory.createEtchedBorder(), 
										"Update account"),
								new EmptyBorder(5, 5, 5, 5) // inner padding
								)
//						)
				);
		
		// controls
		BoxLayout editControlsLayout = new BoxLayout(editControlsPanel, BoxLayout.X_AXIS);
		editControlsPanel.setLayout(editControlsLayout);
		editControlsPanel.add(editButton);
	
		editLayout.setHorizontalGroup(
				editLayout.createParallelGroup()
				.addGroup(	
					editLayout.createSequentialGroup()
					.addGroup(
							editLayout.createParallelGroup(Alignment.TRAILING)
							.addComponent(usernameLabel)
							.addComponent(passwordLabel)
							)
					.addGroup(
							editLayout.createParallelGroup()
							.addComponent(usernameTextField)
							.addGroup(
									editLayout.createSequentialGroup()
									.addComponent(passwordField)
									.addComponent(passwordVisibilityButton)
									)
							.addComponent(editControlsPanel, Alignment.TRAILING)
							)
					)
				.addComponent(editErrorMessageTextArea)
				);
		
		editLayout.setVerticalGroup(
				editLayout.createSequentialGroup()
				.addGroup(
						editLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(usernameLabel)
						.addComponent(usernameTextField)
						)
				.addGroup(
						editLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(passwordLabel)
						.addComponent(passwordField)
						.addComponent(passwordVisibilityButton)
						)
				.addComponent(editErrorMessageTextArea)
				.addGroup(
						editLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(editControlsPanel)
						)
				);
		
		// delete panel
		if (!isCurrentUserOwner) {
			deletePanel = new JPanel(true);
			deletePanel.setLayout(new GridBagLayout());
			deletePanel.add(deleteButton);
			deletePanel.setBorder(
//					BorderFactory.createCompoundBorder(
//							new EmptyBorder(10, 10, 10, 10),
							BorderFactory.createCompoundBorder(
									BorderFactory.createTitledBorder(
											BorderFactory.createEtchedBorder(), 
											"Delete account"),
									new EmptyBorder(5, 5, 5, 5) // inner padding
									)
//							)
					);
			deletePanel.setMinimumSize(new Dimension(editPanel.getPreferredSize().width, 0));

			// frame
			GroupLayout layout = new GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			
			layout.setHorizontalGroup(
					layout.createParallelGroup()
					.addComponent(editPanel)
					.addComponent(deletePanel)
					);
			
			layout.setVerticalGroup(
					layout.createSequentialGroup()
					.addComponent(editPanel)
					.addComponent(deletePanel)
					);
		} else {
			add(editPanel);
		}
		
		//@formatter:on
		pack();
		setVisible(true);
	}
	
	private void refreshData() {
		editErrorMessageTextArea.setText(editErrorMessage);
		if (editErrorMessage == null || editErrorMessage.trim().length() == 0) {
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
	
	private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {
		editControlsPanel.remove(editButton);
		editControlsPanel.add(cancelEditButton);
		editControlsPanel.add(confirmEditButton);
		getRootPane().setDefaultButton(confirmEditButton); // Enter key wired to signUpButton
		usernameTextField.setEditable(true);
		passwordField.setEditable(true);
		pack();
	}
	
	private void finishEditing() {
		editControlsPanel.remove(cancelEditButton);
		editControlsPanel.remove(confirmEditButton);
		editControlsPanel.add(editButton);
		getRootPane().setDefaultButton(null); // Enter key wired to signUpButton
		usernameTextField.setEditable(false);
		passwordField.setEditable(false);
		pack();
	}
	
	private void cancelEditButtonActionPerformed(java.awt.event.ActionEvent evt) {
		finishEditing();
		// clear error message
		editErrorMessage = "";
		refreshData();
	}
	
	private void confirmEditButtonActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			FlexiBookController.updateUserAccount(usernameTextField.getText(), String.valueOf(passwordField.getPassword()));
			finishEditing();
			// clear error message
			editErrorMessage = "";
		} catch (InvalidInputException e) {
			editErrorMessage = e.getMessage();
		} finally {
			refreshData();
		}
	}
	
	private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			String[] options = { "Yes", "Cancel" };
			int chosenOption = JOptionPane.showOptionDialog(this, "Delete this account? This action is final and cannot be undone.", "Are you sure?", 
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
					null, options, options[0]);
			if (chosenOption == 0) {
				if (FlexiBookController.getCurrentUser() != null) {
					FlexiBookController.deleteCustomerAccount(FlexiBookController.getCurrentUser().getUsername());
				} else { 
					JOptionPane.showMessageDialog(this, "There is no user currently no user logged in", "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
		} catch (InvalidInputException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
