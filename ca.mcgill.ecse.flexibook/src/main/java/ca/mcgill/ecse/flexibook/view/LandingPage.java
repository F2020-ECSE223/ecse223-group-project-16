package ca.mcgill.ecse.flexibook.view;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class LandingPage extends JFrame {

	private JLabel welcomeLabel;
	private JButton goToSignUpButton;
	private JButton goToLoginButton;
	
	public LandingPage() {
		initComponents();
		refreshData();
	}
	
	private void initComponents() {
		welcomeLabel = new JLabel("Welcome to FlexiBook.");
		welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(32.0f));
		// set thick width border
		goToSignUpButton = new JButton("Sign Up");
		goToLoginButton = new JButton("Login");
		
		
		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("FlexiBook");
		
		// listeners
		final JFrame that = this;
		goToSignUpButton.addActionListener(new java.awt.event.ActionListener() {
		      public void actionPerformed(java.awt.event.ActionEvent evt) {
		        Utils.switchToFrame(that, new SignUpPage());

		      }
		    });
		goToLoginButton.addActionListener(new java.awt.event.ActionListener() {
		      public void actionPerformed(java.awt.event.ActionEvent evt) {
		    	  Utils.switchToFrame(that, new LoginPage());
		      }
		    });
		
		// layout
		JPanel contents = new JPanel(true);
		contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

		contents.add(welcomeLabel);
		contents.add(Box.createVerticalStrut(25));
		contents.add(goToSignUpButton);
		contents.add(goToLoginButton);
		
		welcomeLabel.setAlignmentX(CENTER_ALIGNMENT);
		goToSignUpButton.setAlignmentX(CENTER_ALIGNMENT);
		goToLoginButton.setAlignmentX(CENTER_ALIGNMENT);
		
		contents.setAlignmentY(CENTER_ALIGNMENT);
		
		add(Box.createHorizontalGlue());
		add(contents);
		add(Box.createHorizontalGlue());
		
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(600, 400));
		
		contents.setPreferredSize(getMinimumSize());
		pack();
		
		getRootPane().setDefaultButton(goToLoginButton); // Enter key wired to signUpButton
	}
	
	private void refreshData() {
		
	}
	// Login or Sign Up; that's it.
}
