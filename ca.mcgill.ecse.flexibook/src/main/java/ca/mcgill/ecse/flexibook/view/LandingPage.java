package ca.mcgill.ecse.flexibook.view;

import java.awt.Dimension;
import java.time.LocalTime;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import java.nio.ByteBuffer;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;

public class LandingPage extends JFrame {
	private static final long serialVersionUID = 8012520089383050363L;

	private static final boolean DEBUG_MODE = false;
	private static final boolean DEBUG_USER_IS_OWNER = true;

	// UI elements
	private JLabel welcomeLabel;
	private JButton goToSignUpButton;
	private JButton goToLoginButton;
	// debug
	private JLabel debugSectionHeader;
	private JButton debugLauncherButton;
	private JLabel debugInfo;
	private JLabel debugCurrentMockUser;

	public LandingPage() {
		initComponents();
		refreshData();
	}

	private void initComponents() {
		welcomeLabel = new JLabel("Welcome to FlexiBook");
		welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(32.0f)); // increase font size
		goToSignUpButton = new JButton("Sign Up");
		goToLoginButton = new JButton("Login");

		if (DEBUG_MODE) {
			debugSectionHeader = new JLabel("Debug");
			debugLauncherButton = new JButton("Launch Page");
			debugInfo = new JLabel("Opened at: " + LocalTime.now() + " – Loaded from persistence: "
					+ FlexiBookApplication.LOAD_PERSISTENCE);
			debugCurrentMockUser = new JLabel();
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
		if (DEBUG_MODE) {
			debugLauncherButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					Utils.switchToFrame(that, new FlexiBookPage());
				}
			});
		}

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
		if (DEBUG_MODE) {
			getContentPane().add(debugSectionHeader);
			getContentPane().add(debugLauncherButton);
			getContentPane().add(debugInfo);
			getContentPane().add(debugCurrentMockUser);
			debugSectionHeader.setAlignmentX(CENTER_ALIGNMENT);
			debugLauncherButton.setAlignmentX(CENTER_ALIGNMENT);
			debugInfo.setAlignmentX(CENTER_ALIGNMENT);
			debugCurrentMockUser.setAlignmentX(CENTER_ALIGNMENT);
		}

		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(600, 400));

		pack();

		if (DEBUG_MODE) {
			getRootPane().setDefaultButton(debugLauncherButton);
		} else {
			getRootPane().setDefaultButton(goToLoginButton); // Enter key wired to signUpButton
		}
	}

	private void refreshData() {
		if (DEBUG_MODE) {
			String username = Long.toString(ByteBuffer.wrap(UUID.randomUUID().toString().getBytes()).getLong(),
					Character.MAX_RADIX); // short UUID
			String password = "debugPassword";
			try {
				if (DEBUG_USER_IS_OWNER) {
					username = "owner";
					password = "owner";
				} else {
					FlexiBookController.createCustomerAccount(username, password);
				}
				FlexiBookController.login(username, password);
				debugCurrentMockUser.setText("Current user: '" + username + "' / '" + password + "'");
			} catch (InvalidInputException e) {
				e.printStackTrace();
				System.err.println("Failed to create and login debug user");
			}
		}
	}
}
