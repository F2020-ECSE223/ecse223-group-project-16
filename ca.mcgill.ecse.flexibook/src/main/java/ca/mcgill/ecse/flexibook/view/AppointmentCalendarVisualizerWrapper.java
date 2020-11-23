package ca.mcgill.ecse.flexibook.view;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
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
	private Periodical periodical;
	private AppointmentCalendarVisualizer appointmentCalendarVisualizer;
	
	private AppointmentCalendarVisualizerWrapper(AppointmentCalendarVisualizer appointmentCalendarVisualizer, Periodical periodical) {
		super();
		this.periodical = periodical;
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
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
	}
	
	private void refreshData() {
		
	}
}
