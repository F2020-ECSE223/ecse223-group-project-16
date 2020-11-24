package ca.mcgill.ecse.flexibook.view;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;

import ca.mcgill.ecse.flexibook.controller.TOAppointment;
import ca.mcgill.ecse.flexibook.controller.TOBusinessHour;

public class WeeklyAppointmentCalendarVisualizer extends AppointmentCalendarVisualizer implements PropertyChangeListener {
	// managed views
	private List<DailyAppointmentCalendarVisualizer> dailyAppointmentCalendarVisualizers;

	public WeeklyAppointmentCalendarVisualizer(Date startDate, List<TOBusinessHour> businessHours,
			List<TOAppointment> revealedAppointments, List<TOAppointment> concealedAppointments) {
		super(startDate, businessHours, revealedAppointments, concealedAppointments);

		initComponents();
	}
	
	private void initComponents() {
		dailyAppointmentCalendarVisualizers = new ArrayList<DailyAppointmentCalendarVisualizer>();
		LocalDate tomorrow = date.toLocalDate(); // i.e. start date
		for (int i = 0; i < 7; i++) {
			Date today = Date.valueOf(tomorrow);
			DailyAppointmentCalendarVisualizer dailyAppointmentCalendarVisualizer = new DailyAppointmentCalendarVisualizer(today, Utils.filterBusinessHoursByDate(businessHours, today),
							Utils.filterAppointmentsByDate(revealedAppointments, today),
							Utils.filterAppointmentsByDate(concealedAppointments, today));
			
			dailyAppointmentCalendarVisualizers.add(dailyAppointmentCalendarVisualizer);
			
			// listen
			dailyAppointmentCalendarVisualizer.addPropertyChangeListener(this);
			
			tomorrow = tomorrow.plusDays(1);
		}
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		for (DailyAppointmentCalendarVisualizer v : dailyAppointmentCalendarVisualizers) {
			add(v);
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		TOAppointment appointment = (TOAppointment) evt.getNewValue();
		
		for (DailyAppointmentCalendarVisualizer v : dailyAppointmentCalendarVisualizers) {
			if (appointment == null || !v.getDate().equals(appointment.getStartDate())) {
				v.unsetSelectedAppointment();
			}
		}
		
		// TODO show detailed appointment info
	}
}
