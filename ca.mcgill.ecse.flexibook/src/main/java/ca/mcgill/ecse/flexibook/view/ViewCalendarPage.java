package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOAppointment;
import ca.mcgill.ecse.flexibook.controller.TOBusiness;
import ca.mcgill.ecse.flexibook.controller.TOBusinessHour;
import ca.mcgill.ecse.flexibook.controller.TOUser;
import ca.mcgill.ecse.flexibook.util.FlexiBookUtil;
import ca.mcgill.ecse.flexibook.util.SystemTime;

public class ViewCalendarPage extends JFrame implements PropertyChangeListener {
	public enum Periodical {
		Daily, Weekly
	};

	private static final long serialVersionUID = 1704467229218861611L;

	// constants
	private static int REFRESH_DELAY = 5 * 1000; // ms
	private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

	// UI elements
	// error
	private JLabel errorMessageLabel;

	// date range selection
	private JRadioButton dayRadioButton;
	private JRadioButton weekRadioButton;
	private ButtonGroup periodicalButtonGroup;
	private JPanel periodicalPanel;
	private JTextField dateTextField;
	private JButton previousButton;
	private JButton nextButton;
	private JButton todayButton;
	private JButton viewButton;
	
	// appointment calendar visualization
	private AppointmentCalendarVisualizer appointmentCalendarVisualizer;
	private AppointmentCalendarVisualizerWrapper appointmentCalendarVisualizerWrapper;
	private JScrollPane scrollPane;
	private JPanel detailPanel;
	private JLabel usernameLabel;
	private JLabel serviceLabel;
	private JLabel startTimeLabel;
	private JLabel endTimeLabel;

	// data elements
	private Date selectedDate;
	private String errorMessage;
	private Periodical currentPeriodical;
	private boolean scrollPaneWasSetup = false;

	public ViewCalendarPage() {
		initComponents();
		refreshData();
	}

	private void initComponents() {
		// UI elements
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
		Utils.makePlaceholder(dateTextField, DATE_FORMAT_PATTERN);
		
		previousButton = new JButton("<");
		nextButton = new JButton(">");
		
		todayButton = new JButton("Today");

		viewButton = new JButton("Show");

		scrollPane = new JScrollPane();
		
		usernameLabel = new JLabel();
		serviceLabel = new JLabel();
		startTimeLabel = new JLabel();
		endTimeLabel = new JLabel();
	
		detailPanel = new JPanel();
		
		// data elements
		currentPeriodical = Periodical.Daily;

		// global settings
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("View Calendar");
		getRootPane().setDefaultButton(viewButton); // Wire enter key to view button

		// listeners
		dayRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				dayRadioButtonActionPerformed(evt);
			}
		});
		weekRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				weekRadioButtonActionPerformed(evt);
			}
		});
		previousButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				previousButtonActionPerformed(evt);
			}
		});
		nextButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				nextButtonActionPerformed(evt);
			}
		});
		todayButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				todayButtonActionPerformed(evt);
			}
		});
		viewButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				viewButtonActionPerformed(evt);
			}
		});
		
		Timer timer = new Timer(REFRESH_DELAY, new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	try {
		        	if (currentPeriodical == Periodical.Daily) {
						appointmentCalendarVisualizer.setRevealedAppointments(Utils.filterAppointmentsByDate(getRevealedAppointments(), interpretDate()));
						appointmentCalendarVisualizer.setConcealedAppointments(Utils.filterAppointmentsByDate(getConcealedAppointments(), interpretDate()));
		        	} else {
		        		appointmentCalendarVisualizer.setRevealedAppointments(getRevealedAppointments());
		        		appointmentCalendarVisualizer.setConcealedAppointments(getConcealedAppointments());
		        	}
				} catch (InvalidInputException e) {
					e.printStackTrace();
				}
	        }
	    });
	    timer.start();
		

		// layout
		periodicalPanel.setLayout(new BoxLayout(periodicalPanel, BoxLayout.Y_AXIS));
		periodicalPanel.add(dayRadioButton);
		periodicalPanel.add(weekRadioButton);
		
		detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
		detailPanel.setVisible(false);
		detailPanel.add(usernameLabel);
		detailPanel.add(serviceLabel);
		detailPanel.add(startTimeLabel);
		detailPanel.add(endTimeLabel);

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
						.addGroup(
								layout.createParallelGroup()
								.addComponent(dateTextField)
								.addGroup(
										layout.createSequentialGroup()
										.addComponent(todayButton)
										.addComponent(previousButton)
										.addComponent(nextButton)
										)
								)
						.addComponent(viewButton)
						)
				.addComponent(errorMessageLabel)
				.addComponent(scrollPane)
				.addComponent(detailPanel)
				);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup()
						.addComponent(periodicalPanel)
						.addGroup(
								layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup()
										.addComponent(dateTextField)
										.addComponent(viewButton)
										)
								.addGroup(
										layout.createParallelGroup()
										.addComponent(todayButton)
										.addComponent(previousButton)
										.addComponent(nextButton)
										)
								)
						)
				.addComponent(errorMessageLabel)
				.addComponent(scrollPane)
				.addComponent(detailPanel)
				);
		// @formatter:on
		setPreferredSize(new Dimension(getPreferredSize().width + 20, getPreferredSize().height));
		setMinimumSize(new Dimension(getPreferredSize().width, 300));
		setJMenuBar(new FlexiBookMenuBar(this, "View Calendar", false));
		pack();
	}
	
	private void refreshData() {
		if (!scrollPaneWasSetup) { // setup once
			scrollPane.setBorder(BorderFactory.createEmptyBorder());
			scrollPane.getVerticalScrollBar().setUnitIncrement(16);
			scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
			scrollPaneWasSetup = true;
		}
		
		errorMessageLabel.setText(errorMessage);
		if (errorMessage == null || errorMessage.trim().length() == 0) {
			if (appointmentCalendarVisualizerWrapper != null) {
				appointmentCalendarVisualizer.addSelectionChangeListener(this);
				scrollPane.setViewportView(appointmentCalendarVisualizerWrapper);
			}
		}
	}
	
	private List<TOAppointment> getRevealedAppointments() throws InvalidInputException {
		TOUser currentUser = FlexiBookController.getCurrentUser();
		
		if (FlexiBookController.getCurrentUser().getUsername().equals("owner")) {
			return FlexiBookController.getAppointments();
		} else {
			return FlexiBookController.getAppointments(currentUser.getUsername());
		}
	}
	
	private List<TOAppointment> getConcealedAppointments() {
		TOUser currentUser = FlexiBookController.getCurrentUser();

		List<TOAppointment> concealedAppointments = new ArrayList<TOAppointment>();
		if (currentUser.getUsername().equals("owner")) {
			return concealedAppointments;
		} else {
			for (TOAppointment a : FlexiBookController.getAppointments()) {
				if (!a.getCustomerUsername().equals(currentUser.getUsername())) {
					concealedAppointments.add(a);
				}
			}
			return concealedAppointments;
		}
	}
	
	private void refreshAppointmentCalendar() {
		List<TOBusinessHour> allBusinessHours;
		List<TOAppointment> revealedAppointments;
		List<TOAppointment> concealedAppointments;

		TOUser currentUser = FlexiBookController.getCurrentUser();

		if (currentUser == null) {
			throw new IllegalStateException("Current user cannot be null");
		}

		try {
			TOBusiness business = FlexiBookController.viewBusinessInfo();
			if (business != null) {
				allBusinessHours = business.getBusinessHours();
			} else {
				allBusinessHours = new ArrayList<TOBusinessHour>();
			}
			
			concealedAppointments = getConcealedAppointments();
			revealedAppointments = getRevealedAppointments();
		} catch (InvalidInputException e) {
			errorMessage = e.getMessage();
			refreshData();
			return;
		}
		
		Date selectedDate = interpretDate();
		if (selectedDate == null) {
			return;
		}
		
		saveCurrentPeriodical();

		if (currentPeriodical == Periodical.Daily) {
			appointmentCalendarVisualizer = new DailyAppointmentCalendarVisualizer(selectedDate,
					Utils.filterBusinessHoursByDate(allBusinessHours, selectedDate),
					Utils.filterAppointmentsByDate(revealedAppointments, selectedDate),
					Utils.filterAppointmentsByDate(concealedAppointments, selectedDate));
			appointmentCalendarVisualizerWrapper = new AppointmentCalendarVisualizerWrapper((DailyAppointmentCalendarVisualizer) appointmentCalendarVisualizer);
		} else {
			appointmentCalendarVisualizer = new WeeklyAppointmentCalendarVisualizer(selectedDate, allBusinessHours, revealedAppointments, concealedAppointments); // this is lazy, client should not expect viz to filter events in the date range that makes sense
			appointmentCalendarVisualizerWrapper = new AppointmentCalendarVisualizerWrapper((WeeklyAppointmentCalendarVisualizer) appointmentCalendarVisualizer);
		}
		
		refreshData();
	}
	
	private void dayRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (appointmentCalendarVisualizerWrapper == null) {
			return;
		}
		
		saveCurrentPeriodical();
		
		refreshAppointmentCalendar(); // based on the current periodical now selected
	}
	
	private void weekRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (appointmentCalendarVisualizerWrapper == null) {
			return;
		}
		
		saveCurrentPeriodical();
		
		refreshAppointmentCalendar(); // based on the current periodical now selected
	}
	
	private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {
		Date selectedDate = interpretDate();
		if (selectedDate == null) {
			return;
		}
		
		LocalDate selectedLocalDate = selectedDate.toLocalDate();
		
		if (currentPeriodical == Periodical.Daily) {
			selectedLocalDate = selectedLocalDate.minusDays(1);
		} else {
			selectedLocalDate = selectedLocalDate.minusWeeks(1);
		}
		
		dateTextField.setForeground(Color.BLACK);
		dateTextField.setText(Utils.formatDate(Date.valueOf(selectedLocalDate), DATE_FORMAT_PATTERN));
		
		refreshAppointmentCalendar(); // based on the date string now in the date text field
	}
	
	private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
		Date selectedDate = interpretDate();
		if (selectedDate == null) {
			return;
		}
		
		LocalDate selectedLocalDate = selectedDate.toLocalDate();
		
		if (currentPeriodical == Periodical.Daily) {
			selectedLocalDate = selectedLocalDate.plusDays(1);
		} else {
			selectedLocalDate = selectedLocalDate.plusWeeks(1);
		}
		
		dateTextField.setForeground(Color.BLACK);
		dateTextField.setText(Utils.formatDate(Date.valueOf(selectedLocalDate), DATE_FORMAT_PATTERN));
		
		refreshAppointmentCalendar(); // based on the date string now in the date text field
	}
	
	private void todayButtonActionPerformed(java.awt.event.ActionEvent evt) {
		errorMessage = "";
		
		dateTextField.setForeground(Color.BLACK);
		dateTextField.setText(Utils.formatDate(SystemTime.getDate(), DATE_FORMAT_PATTERN));
		
		refreshAppointmentCalendar(); // based on the date string now in the date text field
	}

	private void viewButtonActionPerformed(java.awt.event.ActionEvent evt) {
		errorMessage = "";
		
		saveCurrentPeriodical();
		
		refreshAppointmentCalendar();
	}
	
	private Date interpretDate() {
		if (dateTextField.getText().trim().length() == 0 || dateTextField.getText().equals(DATE_FORMAT_PATTERN)) {
			errorMessage = "Please enter a date";
			refreshData();
			return null;
		}
		try {
			return FlexiBookUtil.getDateFromString(dateTextField.getText());
		} catch (ParseException e) {
			errorMessage = "Make sure you have formatted the date as '" + DATE_FORMAT_PATTERN + "'";
			refreshData();
			return null;
		}
	}
	
	private void saveCurrentPeriodical() {
		if (dayRadioButton.isSelected()) {
			currentPeriodical = Periodical.Daily;
		} else {
			currentPeriodical = Periodical.Weekly;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		TOAppointment appointment = (TOAppointment) evt.getNewValue();

		String currentUser = FlexiBookController.getCurrentUser().getUsername();

		// initialize JLabel strings 
		usernameLabel.setText("");
		serviceLabel.setText("");
		startTimeLabel.setText("");
		endTimeLabel.setText(""); 

		if (appointment != null) {
			if (currentUser.equals(appointment.getCustomerUsername()) || currentUser.equals("owner")) {
				usernameLabel.setText("Customer: " + appointment.getCustomerUsername());
				serviceLabel.setText("Service: " + appointment.getBookableServiceName());
		    }
			
			startTimeLabel.setText("Start: " + appointment.getStartTime().toString());
			endTimeLabel.setText("End: " + appointment.getEndTime().toString());
			
			detailPanel.setVisible(true);
		}
		else {
			detailPanel.setVisible(false);
		}
		
	}
}
