package ca.mcgill.ecse.flexibook.view;

import java.util.List;
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
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOUser;
import ca.mcgill.ecse.flexibook.controller.TOAppointment;



//import ca.mcgill.ecse.flexibook.util.SystemTime; // THESE IMPORTs SHOULD BE DELETED, IS BEING USED FOR TESTING PURPOSES ONLY
//import ca.mcgill.ecse.flexibook.util.FlexiBookUtil; 
//import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
//import java.io.File;
//import java.text.ParseException;

public class AppointmentManagementPage extends JFrame {
	private static final long serialVersionUID = 18008888888L;

	// constants
	private static final int TEXT_FIELD_WIDTH = 300;

	// UI elements
	private JPanel startingPanel;
	private JPanel inProgressPanel;

	private JLabel noneStartingLabel;
	private JLabel noneInProgressLabel;
	
	private String errorMessage;
	private JLabel errorLabel;

	public AppointmentManagementPage() {
		initComponents();
		refreshData();
	}

	private void initComponents() {
		//below is testing
//		
//		File f = new File("data.flexibook");
//		f.delete();
//		FlexiBookApplication.getFlexiBook();
//		try {
//			FlexiBookController.login("owner", "password");
//			try {
//				FlexiBookController.setUpBusinessInfo("Jimmy G", "WallStreet", "0123456789", "hq@wang.com");
//				FlexiBookController.addNewBusinessHour("Monday", "09:00", "20:00");
//				FlexiBookController.addNewBusinessHour("Tuesday", "09:00", "20:00");
//				FlexiBookController.addNewBusinessHour("Wednesday", "09:00", "20:00");
//				FlexiBookController.addNewBusinessHour("Thursday", "09:00", "20:00");
//				FlexiBookController.addNewBusinessHour("Friday", "09:00", "20:00");
//				FlexiBookController.addNewBusinessHour("Saturday", "09:00", "20:00");
//				FlexiBookController.addNewBusinessHour("Sunday", "09:00", "20:00");
//
//				FlexiBookController.addService("cut", "10", "0", "0");
//				FlexiBookController.addService("trim", "10", "0", "0");
//
//				FlexiBookController.logout();
//				FlexiBookController.createCustomerAccount("test", "password");
//				FlexiBookController.login("test", "password");
//
//			} catch (RuntimeException e) {
//				FlexiBookController.logout();
//				FlexiBookController.login("test", "password");
//			}
//
//			SystemTime.setTesting(FlexiBookUtil.getDateFromString("2020-11-01"), FlexiBookUtil.getTimeFromString("12:00"));
//			try {
//				FlexiBookController.makeAppointment("test", "2020-11-03", "cut", "11:00");
//				FlexiBookController.makeAppointment("test", "2020-11-03", "cut", "10:50");
//			} catch (InvalidInputException e) {
//
//			}
//			SystemTime.setTesting(FlexiBookUtil.getDateFromString("2020-11-03"), FlexiBookUtil.getTimeFromString("11:00"));
//
//			FlexiBookController.logout();
//			FlexiBookController.login("owner", "password");
//		} catch(InvalidInputException e) {
//			System.out.println(e);
//			System.err.println(e);
//			try {
//				FlexiBookController.logout();
//				FlexiBookController.login("owner", "password");
//			} catch (InvalidInputException ee) {
//
//			}
//		} catch (ParseException e) {
//
//		} 
//		
	    // everything above is to be removed
		
		
		
		// UI elements
		errorMessage = null;
		errorLabel = new JLabel();
		errorLabel.setForeground(Color.RED);
		
		noneStartingLabel = new JLabel("There are no appointments starting now.");
		noneInProgressLabel = new JLabel("There are no appointments in progress.");
		
		// global settings
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Appointment Management");

		// @formatter:off
		// layout
		startingPanel = new JPanel(true);
		startingPanel.setLayout(new GridBagLayout());
		startingPanel.add(noneStartingLabel);
		startingPanel.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder(
								BorderFactory.createEtchedBorder(), 
								"Starting appointments"),
						new EmptyBorder(5, 5, 5, 5) // inner padding
						)
				);

		inProgressPanel = new JPanel(true);
		inProgressPanel.setLayout(new GridBagLayout());
		inProgressPanel.add(noneInProgressLabel);
		inProgressPanel.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder(
								BorderFactory.createEtchedBorder(), 
								"Appointments in progress"),
						new EmptyBorder(5, 5, 5, 5) // inner padding
						)
				);
		inProgressPanel.setMinimumSize(new Dimension(startingPanel.getPreferredSize().width, 0));

		// frame
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(errorLabel)
				.addComponent(startingPanel)
				.addComponent(inProgressPanel)
				);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(errorLabel)
				.addComponent(startingPanel)
				.addComponent(inProgressPanel)
				);

		// @formatter:on
		setJMenuBar(new FlexiBookMenuBar(this, "Appointment Management"));
		pack();
		setVisible(true);
	}
	
	private void refreshData() {
		errorLabel.setText(errorMessage);
		
		List<TOAppointment> startingAppt = FlexiBookController.getAppointmentsStarting();
		startingPanel.removeAll();
		GroupLayout startingLayout = new GroupLayout(startingPanel);
		startingLayout.setAutoCreateGaps(true);
		startingLayout.setAutoCreateContainerGaps(true);
		startingPanel.setLayout(startingLayout);		
		GroupLayout.ParallelGroup h = startingLayout.createParallelGroup();
		GroupLayout.SequentialGroup v = startingLayout.createSequentialGroup();		
		if (startingAppt.size() == 0) {
			h.addComponent(noneStartingLabel);
			v.addComponent(noneStartingLabel);
		} else {
			for (TOAppointment a : startingAppt) {
				JPanel aPan = new JPanel(true);
				GroupLayout aLay = new GroupLayout(aPan);
				aLay.setAutoCreateGaps(true);
				aLay.setAutoCreateContainerGaps(true);
				
				JLabel aServ = new JLabel(a.getBookableServiceName());
				JLabel aDateTime = new JLabel(String.format("%s  %s-%s", a.getStartDate(), a.getStartTime(), a.getEndTime()));
				JButton aStart = new JButton("Start Appointment");
				JButton aNoShow = new JButton("Register No Show");
				
				aStart.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent event) {
						startAppointment(event, a);
					}
				});
				aNoShow.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent event) {
						registerNoShow(event, a);
					}
				});

				aLay.setHorizontalGroup(aLay.createParallelGroup(Alignment.CENTER)
						.addGroup(aLay.createSequentialGroup()
								.addComponent(aServ)
								.addComponent(aDateTime))
						.addGroup(aLay.createSequentialGroup()
								.addComponent(aStart)
								.addComponent(aNoShow)));

				aLay.setVerticalGroup(aLay.createSequentialGroup()
						.addGroup(aLay.createParallelGroup(Alignment.CENTER)
								.addComponent(aServ)
								.addComponent(aDateTime))
						.addGroup(aLay.createParallelGroup(Alignment.CENTER)
								.addComponent(aStart)
								.addComponent(aNoShow)));

				h.addComponent(aPan);
				v.addComponent(aPan);
			}
			
		}
		startingLayout.setHorizontalGroup(h);
		startingLayout.setVerticalGroup(v);
		
		List<TOAppointment> progressingAppt = FlexiBookController.getAppointmentsInProgress();
		inProgressPanel.removeAll();
		GroupLayout progressingLayout = new GroupLayout(inProgressPanel);
		progressingLayout.setAutoCreateGaps(true);
		progressingLayout.setAutoCreateContainerGaps(true);
		inProgressPanel.setLayout(progressingLayout);		
		h = progressingLayout.createParallelGroup();
		v = progressingLayout.createSequentialGroup();		
		if (progressingAppt.size() == 0) {
			h.addComponent(noneInProgressLabel);
			v.addComponent(noneInProgressLabel);
		} else {
			for (TOAppointment a : progressingAppt) {
				JPanel aPan = new JPanel(true);
				GroupLayout aLay = new GroupLayout(aPan);
				aLay.setAutoCreateGaps(true);
				aLay.setAutoCreateContainerGaps(true);
				
				JLabel aServ = new JLabel(a.getBookableServiceName());
				JLabel aDateTime = new JLabel(String.format("%s  %s-%s", a.getStartDate(), a.getStartTime(), a.getEndTime()));
				JButton aEnd = new JButton("End Appointment");
				
				aEnd.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent event) {
						endAppointment(event, a);
					}
				});

				aLay.setHorizontalGroup(aLay.createParallelGroup(Alignment.CENTER)
						.addGroup(aLay.createSequentialGroup()
								.addComponent(aServ)
								.addComponent(aDateTime))
						.addGroup(aLay.createSequentialGroup()
								.addComponent(aEnd)));

				aLay.setVerticalGroup(aLay.createSequentialGroup()
						.addGroup(aLay.createParallelGroup(Alignment.CENTER)
								.addComponent(aServ)
								.addComponent(aDateTime))
						.addGroup(aLay.createParallelGroup(Alignment.CENTER)
								.addComponent(aEnd)));

				h.addComponent(aPan);
				v.addComponent(aPan);
			}
			
		}
		progressingLayout.setHorizontalGroup(h);
		progressingLayout.setVerticalGroup(v);
	
		inProgressPanel.setMinimumSize(new Dimension(startingPanel.getPreferredSize().width, 0));
		startingPanel.setMinimumSize(new Dimension(inProgressPanel.getPreferredSize().width, 0));
		
		pack();
		setVisible(true);
	}
	
	private void startAppointment(java.awt.event.ActionEvent evt, TOAppointment appt) {
		try {
			FlexiBookController.startAppointment(appt.getCustomerUsername(), appt.getBookableServiceName(), appt.getStartDate(), appt.getStartTime());
			errorMessage = null;
		} catch (InvalidInputException e) {
			errorMessage = e.getMessage();
		}
		refreshData();
	}
	
	private void endAppointment(java.awt.event.ActionEvent evt, TOAppointment appt) {
		try {
			FlexiBookController.endAppointment(appt.getCustomerUsername(), appt.getBookableServiceName(), appt.getStartDate(), appt.getStartTime());
			errorMessage = null;
		} catch (InvalidInputException e) {
			errorMessage = e.getMessage();
		}
		refreshData();
	}

	private void registerNoShow(java.awt.event.ActionEvent evt, TOAppointment appt) {
		try {
			FlexiBookController.registerNoShow(appt.getCustomerUsername(), appt.getBookableServiceName(), appt.getStartDate(), appt.getStartTime());
			errorMessage = null;
		} catch (InvalidInputException e) {
			errorMessage = e.getMessage();
		}
		refreshData();
	}
}
