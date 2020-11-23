package ca.mcgill.ecse.flexibook.view;

import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOBusinessHour;
import ca.mcgill.ecse.flexibook.util.SystemTime;

public class ViewCalendarPage extends JFrame {
	private static final long serialVersionUID = 1704467229218861611L;
	private JLabel viewCalendarLabel;

	// appointment calendar visualization
	private DailyAppointmentCalendarVisualizer viz;

	public ViewCalendarPage() {
		populate();
		initComponents();
	}

	private void initComponents() {
		viz = new DailyAppointmentCalendarVisualizer(TOBusinessHour.DayOfWeek.Tuesday, FlexiBookController.getAppointments(), new ArrayList<>(), FlexiBookController.viewBusinessInfo().getBusinessHours());
		viewCalendarLabel = new JLabel();
		viewCalendarLabel.setText("View Calendar Tab here");

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("ViewCalendar Tab");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup().addComponent(viz));

		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(viz));
		pack();
	}
	
	// populate app with mock values
	private void populate() {
		
		System.out.println(SystemTime.getDate());
		System.out.println(SystemTime.getTime());
		try {
			
			FlexiBookController.setUpBusinessInfo("company", "my address", "5141234567", "dasdsad@mgill.ca");
			FlexiBookController.addNewBusinessHour("Tuesday", "09:00", "12:00");
			FlexiBookController.addNewBusinessHour("Tuesday", "14:00", "14:30");
			FlexiBookController.addNewBusinessHour("Tuesday", "14:35", "15:45");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			FlexiBookController.addService("a service", "20", "0", "0");
			FlexiBookController.createCustomerAccount("foo", "bar");
			FlexiBookController.login("foo", "bar");
			FlexiBookController.makeAppointment("foo", "2020-11-24", "a service", "09:01");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
