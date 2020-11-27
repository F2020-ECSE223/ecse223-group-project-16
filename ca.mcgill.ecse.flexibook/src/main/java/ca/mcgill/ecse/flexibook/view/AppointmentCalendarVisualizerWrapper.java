package ca.mcgill.ecse.flexibook.view;

import javax.swing.GroupLayout;
import javax.swing.JPanel;

import ca.mcgill.ecse.flexibook.view.ViewCalendarPage.Periodical;

/**
 * Adds calendar info (e.g. hours, day of week and and date, month, year) to either a DailyAppointmentCalendarVisualizer or WeeklyAppointmentCalendarVisualizer, and wraps these in a scrollable container (?)
 * 
 * @author louca
 *
 */
public class AppointmentCalendarVisualizerWrapper extends JPanel {	
	private static final long serialVersionUID = -3236830295952403675L;
	
	// data elements
	private Periodical periodical;
	private AppointmentCalendarVisualizer appointmentCalendarVisualizer;
	private CalendarHourLegend calendarHourLegend;
	private CalendarDateLegend calendarDateLegend;
	
	private AppointmentCalendarVisualizerWrapper(AppointmentCalendarVisualizer appointmentCalendarVisualizer, Periodical periodical) {
		super();
		this.periodical = periodical;
		this.appointmentCalendarVisualizer = appointmentCalendarVisualizer;
		initComponents();
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
		calendarHourLegend = new CalendarHourLegend();
		calendarDateLegend = new CalendarDateLegend(appointmentCalendarVisualizer.getDate(), periodical);
		
		// @formatter:off
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addComponent(calendarHourLegend)
				.addGroup(
						layout.createParallelGroup()
						.addComponent(calendarDateLegend)
						.addComponent(appointmentCalendarVisualizer)
						)
				);

		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(calendarDateLegend)
				.addGroup(
					layout.createParallelGroup()
					.addComponent(calendarHourLegend)
					.addComponent(appointmentCalendarVisualizer)
					)
				);
		// @formatter:on
	}
}
