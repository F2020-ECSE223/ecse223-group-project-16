package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOAppointment;
import ca.mcgill.ecse.flexibook.controller.TOBusinessHour;
import ca.mcgill.ecse.flexibook.controller.TOUser;
import ca.mcgill.ecse.flexibook.util.FlexiBookUtil;
import ca.mcgill.ecse.flexibook.util.SystemTime;

public class ViewCalendarPage extends JFrame {
	public enum Periodical {
		Daily, Weekly
	};

	private static final long serialVersionUID = 1704467229218861611L;

	// constants
	private static final String DATE_FORMAT_STRING = "yyyy-MM-dd";

	// UI elements
	// error
	private JLabel errorMessageLabel;

	// date range selection
	private JRadioButton dayRadioButton;
	private JRadioButton weekRadioButton;
	private ButtonGroup periodicalButtonGroup;
	private JPanel periodicalPanel;
	private JTextField dateTextField;
	private JButton viewButton;
	// appointment calendar visualization
	private JPanel contentPanel;
	private JLabel monthAndYearLabel;
	private List<JLabel> dateLabels;
	private AppointmentCalendarVisualizer appointmentCalendarVisualizer;
	private AppointmentCalendarVisualizerWrapper appointmentCalendarVisualizerWrapper;
	private JScrollPane scrollPane;

	// data elements
	private String errorMessage;
	private Periodical currentPeriodical;
	private boolean scrollPaneBorderWasSet = false;

	public ViewCalendarPage() {
		populate();
		initComponents();
		refreshData();
	}

	private void initComponents() {
		errorMessageLabel = new JLabel();
		errorMessageLabel.setForeground(Color.RED);

		dayRadioButton = new JRadioButton("Day of", true);
		weekRadioButton = new JRadioButton("Week starting on");
		periodicalButtonGroup = new ButtonGroup();
		periodicalButtonGroup.add(dayRadioButton);
		periodicalButtonGroup.add(weekRadioButton);
	
		periodicalPanel = new JPanel();

		dateTextField = new JTextField();
		Utils.fixSize(dateTextField, new Dimension(300, dateTextField.getPreferredSize().height));
		Utils.makePlaceholder(dateTextField, DATE_FORMAT_STRING);

		viewButton = new JButton("Show");

		monthAndYearLabel = new JLabel();

//		appointmentCalendarVisualizer = new DailyAppointmentCalendarVisualizer(SystemTime.getDate(), filterBusinessHoursByDay(FlexiBookController.viewBusinessInfo().getBusinessHours(), TOBusinessHour.DayOfWeek.Tuesday), FlexiBookController.getAppointments(), new ArrayList<>());
//		appointmentCalendarVisualizer = new WeeklyAppointmentCalendarVisualizer(Date.valueOf(LocalDate.now()), FlexiBookController.viewBusinessInfo().getBusinessHours(), FlexiBookController.getAppointments(), new ArrayList<>());
//		appointmentCalendarVisualizerWrapper = new AppointmentCalendarVisualizerWrapper((WeeklyAppointmentCalendarVisualizer)  appointmentCalendarVisualizer);
		
		scrollPane = new JScrollPane();
//		
//		scrollPane = new JScrollPane(scrollableContentPanel);
//		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		// global settings
		setMinimumSize(new Dimension(200, 300));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("View Appointment Calendar");
		getRootPane().setDefaultButton(viewButton); // Wire enter key to view button

		// listeners
		viewButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				viewButtonActionPerformed(evt);
			}
		});

		// layout
		periodicalPanel.setLayout(new BoxLayout(periodicalPanel, BoxLayout.Y_AXIS));
		periodicalPanel.add(dayRadioButton);
		periodicalPanel.add(weekRadioButton);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		// @formatter:off
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(
						layout.createSequentialGroup()
						.addComponent(periodicalPanel)
						.addComponent(dateTextField)
						.addComponent(viewButton)
						)
				.addComponent(errorMessageLabel)
				.addComponent(scrollPane)
				);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(Alignment.CENTER)
						.addComponent(periodicalPanel)
						.addComponent(dateTextField)
						.addComponent(viewButton)
						)
				.addComponent(errorMessageLabel)
				.addComponent(scrollPane)
				);

		setPreferredSize(new Dimension(getPreferredSize().width + 20, getPreferredSize().height));
		// @formatter:on
		
		pack();
	}

	// populate app with mock values
	private void populate() {

		System.out.println(SystemTime.getDate());
		System.out.println(SystemTime.getTime());
		try {

			FlexiBookController.setUpBusinessInfo("company", "my address", "5141234567", "dasdsad@mgill.ca");
			FlexiBookController.addNewBusinessHour("Tuesday", "09:00", "14:00");
			FlexiBookController.addNewBusinessHour("Tuesday", "14:00", "14:30");
			FlexiBookController.addNewBusinessHour("Tuesday", "14:35", "15:45");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			FlexiBookController.addService("a service", "90", "0", "0");
			FlexiBookController.createCustomerAccount("Cutomer Username", "bar");
			FlexiBookController.login("Cutomer Username", "bar");
			FlexiBookController.makeAppointment("Cutomer Username", "2020-11-24", "a service", "09:01");
			FlexiBookController.makeAppointment("Cutomer Username", "2020-11-24", "a service", "10:31");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void refreshData() {
		
		if (scrollPaneBorderWasSet == false) {
			scrollPane.setBorder(BorderFactory.createEmptyBorder());
			scrollPaneBorderWasSet = true;
		}
		
		errorMessageLabel.setText(errorMessage);
		if (errorMessage == null || errorMessage.trim().length() == 0) {
			if (appointmentCalendarVisualizerWrapper != null) {
				scrollPane.setViewportView(appointmentCalendarVisualizerWrapper);
			}
		}
	}

	/**
	 * As a "MMMMM yyyy" String.
	 * 
	 * @param date
	 */
	private String formatDate(Date date) {
		String pattern = "MMMMM yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		return simpleDateFormat.format(date);
	}

	private void viewButtonActionPerformed(ActionEvent evt) {
		errorMessage = "";
		String selectedDateString = dateTextField.getText();

		if (dayRadioButton.isSelected()) {
			currentPeriodical = Periodical.Daily;
		} else {
			currentPeriodical = Periodical.Weekly;
		}

		List<TOBusinessHour> allBusinessHours;
		List<TOAppointment> allCurrentUserAppointments;
		List<TOAppointment> allAppointments;

		TOUser currentUser = FlexiBookController.getCurrentUser();

		if (currentUser == null) {
			throw new IllegalStateException("Current user cannot be null");
		}

		try {
			allBusinessHours = FlexiBookController.viewBusinessInfo().getBusinessHours();
			allCurrentUserAppointments = FlexiBookController.getAppointments(currentUser.getUsername());
			allAppointments = FlexiBookController.getAppointments();
		} catch (InvalidInputException e) {
			errorMessage = e.getMessage();
			refreshData();
			return;
		}
		Date selectedDate;
		try {
			selectedDate = FlexiBookUtil.getDateFromString(selectedDateString);
		} catch (ParseException e) {
			errorMessage = "Make sure you have formatted the date as '" + DATE_FORMAT_STRING + "'";
			refreshData();
			return;
		}

		if (currentPeriodical == Periodical.Daily) {
			appointmentCalendarVisualizer = new DailyAppointmentCalendarVisualizer(selectedDate,
					Utils.filterBusinessHoursByDate(allBusinessHours, selectedDate),
					Utils.filterAppointmentsByDate(allCurrentUserAppointments, selectedDate),
					Utils.filterAppointmentsByDate(allAppointments, selectedDate));
			appointmentCalendarVisualizerWrapper = new AppointmentCalendarVisualizerWrapper((DailyAppointmentCalendarVisualizer) appointmentCalendarVisualizer);

		} else {
			appointmentCalendarVisualizer = new WeeklyAppointmentCalendarVisualizer(selectedDate, allBusinessHours, allCurrentUserAppointments, allAppointments); // this is lazy, client should not expect viz to filter events in the date range that makes sense
			appointmentCalendarVisualizerWrapper = new AppointmentCalendarVisualizerWrapper((WeeklyAppointmentCalendarVisualizer) appointmentCalendarVisualizer);
		}
		
		refreshData();
	}
}
