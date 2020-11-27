package ca.mcgill.ecse.flexibook.view;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import ca.mcgill.ecse.flexibook.controller.FlexiBookController;

public class MenuPage extends JFrame {
	private static final long serialVersionUID = -4739464810558524924L;

	// UI elements
	private JLabel greetingLabel;
	private JButton goToBusinessInfo;
	private JButton goToViewCalendar;
	private JButton goToAppointments;
	private JButton goToAppointmentManagement;
	private JButton goToServices;

	// data elements
	private final boolean currentUserIsOwner;

	public MenuPage() {
		currentUserIsOwner = FlexiBookController.isCurrentUserOwner();
		initComponents();
	}

	private void initComponents() {
		if (FlexiBookController.getCurrentUser() == null) {
			throw new IllegalStateException("Current user cannot be null");
		}

		greetingLabel = new JLabel("Hi, " + FlexiBookController.getCurrentUser().getUsername());
		greetingLabel.setFont(greetingLabel.getFont().deriveFont(20.0f)); // increase font size

		goToBusinessInfo = new JButton("Business Info");
		goToViewCalendar = new JButton("View Calendar");
		if (currentUserIsOwner) {
		    goToAppointmentManagement = new JButton("Appointment Management");
			goToServices = new JButton("Services");
		} else {
			goToAppointments = new JButton("Appointments");
		}

		// listeners
		JFrame that = this;
		goToBusinessInfo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Utils.switchToFrame(that, new BusinessInfoPage());
			}
		});
		goToViewCalendar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Utils.goToFrame(that, new ViewCalendarPage());
			}
		});
		if (currentUserIsOwner) {
		    goToAppointmentManagement.addActionListener(new java.awt.event.ActionListener() {
		        public void actionPerformed(java.awt.event.ActionEvent evt) {
		          Utils.switchToFrame(that, new AppointmentManagementPage());
		        }
		      });
			goToServices.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					Utils.switchToFrame(that, new ServicesPage());
				}
			});
		} else {
			goToAppointments.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					Utils.switchToFrame(that, new AppointmentsPage());
				}
			});
		}

		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Menu");

		// layout
		// @formatter:off
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		JPanel routerButtons = new JPanel();
		routerButtons.setLayout(new BoxLayout(routerButtons, BoxLayout.X_AXIS));
		
		routerButtons.add(Box.createHorizontalGlue());
		routerButtons.add(goToBusinessInfo);
		routerButtons.add(goToViewCalendar);
		if (currentUserIsOwner) {
			routerButtons.add(goToAppointmentManagement);
			routerButtons.add(goToServices);
		} else {
			routerButtons.add(goToAppointments);
		}
		routerButtons.add(Box.createHorizontalGlue());
		
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.add(Box.createVerticalGlue());
		container.add(routerButtons);
		container.add(Box.createVerticalGlue());
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(greetingLabel)
				.addComponent(container, Alignment.CENTER)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(greetingLabel)
				.addComponent(container)
				);
		// @formatter:on
		setJMenuBar(new FlexiBookMenuBar(this, "Menu", false));
		pack();
		setMinimumSize(new Dimension(getPreferredSize().width + 100, 200));
	}
}
