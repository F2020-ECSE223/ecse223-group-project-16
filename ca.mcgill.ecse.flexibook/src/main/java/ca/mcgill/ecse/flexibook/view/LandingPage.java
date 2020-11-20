package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.time.LocalTime;

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

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;

@SuppressWarnings("serial")
public class LandingPage extends JFrame {
	private static final boolean DEBUG_MODE = true;

	private JLabel welcomeLabel;
	private JButton goToSignUpButton;
	private JButton goToLoginButton;
	private JLabel debugSectionHeader;
	private JLabel debugInfo;
	private JButton debugLauncherButton;
	
	public LandingPage() {
		initComponents();
		refreshData();
	}
	
	private void initComponents() {
		welcomeLabel = new JLabel("Welcome to FlexiBook");
		welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(32.0f));
		// set thick width border
		goToSignUpButton = new JButton("Sign Up");
		goToLoginButton = new JButton("Login");
		
		if (DEBUG_MODE) {
			debugSectionHeader = new JLabel("Debug");
			debugLauncherButton = new JButton("Launch Page");
			debugInfo = new JLabel("<html>Opened at: " + LocalTime.now() + "<br/>Loaded from persistence: " + FlexiBookApplication.LOAD_PERSISTENCE + "</html>");
		}
		
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
		debugLauncherButton.addActionListener(new java.awt.event.ActionListener() {
		      public void actionPerformed(java.awt.event.ActionEvent evt) {
		    	  Utils.switchToFrame(that, new FlexiBookPage());
		      }
		    });
		
		// layout
		JPanel contents = new JPanel(true);
		contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));

		contents.add(welcomeLabel);
		contents.add(Box.createVerticalStrut(25));
		contents.add(goToSignUpButton);
		contents.add(goToLoginButton);

		
		welcomeLabel.setAlignmentX(CENTER_ALIGNMENT);
		goToSignUpButton.setAlignmentX(CENTER_ALIGNMENT);
		goToLoginButton.setAlignmentX(CENTER_ALIGNMENT);
		
		contents.setAlignmentX(CENTER_ALIGNMENT);
		

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		getContentPane().add(Box.createVerticalGlue());
		getContentPane().add(contents);
		getContentPane().add(Box.createVerticalGlue());
		getContentPane().add(debugSectionHeader);
		getContentPane().add(debugLauncherButton);
		getContentPane().add(debugInfo);
		debugSectionHeader.setAlignmentX(CENTER_ALIGNMENT);
		debugLauncherButton.setAlignmentX(CENTER_ALIGNMENT);
		debugInfo.setAlignmentX(CENTER_ALIGNMENT);
		
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(600, 400));
		
		pack();
		
		getRootPane().setDefaultButton(goToLoginButton); // Enter key wired to signUpButton
	}
	
	private void refreshData() {
		
	}
	// Login or Sign Up; that's it.
}
