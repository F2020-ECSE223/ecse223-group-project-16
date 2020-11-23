package ca.mcgill.ecse.flexibook.view;

import javax.swing.JPanel;

/**
 * Adds calendar info (e.g. hours, day of week and and date, month, year) to either a DailyAppointmentCalendarVisualizer or WeeklyAppointmentCalendarVisualizer, and wraps these in a scrollable container (?)
 * 
 * @author louca
 *
 */
public class AppointmentCalendarVisualizerWrapper extends JPanel {
	private enum Periodical {Daily, Weekly};
	
	// data elements
	private Periodical period;
	private AppointmentCalendarVisualizer appointmentCalendarVisualizer;
	
	private AppointmentCalendarVisualizerWrapper(AppointmentCalendarVisualizer appointmentCalendarVisualizer, Periodical period) {
		super();
		this.period = period;
		this.appointmentCalendarVisualizer = appointmentCalendarVisualizer;
		initComponents();
		refreshData();
	}
	
	public AppointmentCalendarVisualizerWrapper(DailyAppointmentCalendarVisualizer dailyAppointmentCalendarVisualizer) {
		this(dailyAppointmentCalendarVisualizer, Periodical.Daily);
	}
	
	public AppointmentCalendarVisualizerWrapper(WeeklyAppointmentCalendarVisualizer weeklyAppointmentCalendarVisualizer) {
		this(weeklyAppointmentCalendarVisualizer, Periodical.Weekly);
	}
	
	private void initComponents() {
		
	}
	
	private void refreshData() {
		
	}
}
