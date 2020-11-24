package ca.mcgill.ecse.flexibook.view;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOBusinessHour;
import ca.mcgill.ecse.flexibook.util.SystemTime;

public class ViewCalendarPage extends JFrame {
	public enum Periodical {Daily, Weekly};
	
	// data elements
	private static final long serialVersionUID = 1704467229218861611L;
	private JLabel viewCalendarLabel;

	// appointment calendar visualization
	private AppointmentCalendarVisualizer viz;
	private AppointmentCalendarVisualizerWrapper wrapper;

	public ViewCalendarPage() {
		populate();
		initComponents();
	}

	private void initComponents() {
		
//		viz = new DailyAppointmentCalendarVisualizer(SystemTime.getDate(), filterBusinessHoursByDay(FlexiBookController.viewBusinessInfo().getBusinessHours(), TOBusinessHour.DayOfWeek.Tuesday), FlexiBookController.getAppointments(), new ArrayList<>());
		viz = new WeeklyAppointmentCalendarVisualizer(Date.valueOf(LocalDate.now()), FlexiBookController.viewBusinessInfo().getBusinessHours(), FlexiBookController.getAppointments(), new ArrayList<>());
		viewCalendarLabel = new JLabel();
		viewCalendarLabel.setText("View Calendar Tab here");
		wrapper = new AppointmentCalendarVisualizerWrapper((WeeklyAppointmentCalendarVisualizer)  viz);
		
		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("View Appointment Calendar");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup().addComponent(wrapper));

		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(wrapper));
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
	
	private List<TOBusinessHour> filterBusinessHoursByDay(List<TOBusinessHour> businessHours, TOBusinessHour.DayOfWeek dayOfWeek) {
		List<TOBusinessHour> result = new ArrayList<TOBusinessHour>();
		for (TOBusinessHour bH : businessHours) {
			if (bH.getDayOfWeek() == dayOfWeek) {
				result.add(bH);
			}
		}
		return result;
	}

}
