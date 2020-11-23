package ca.mcgill.ecse.flexibook.view;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.TOAppointment;
import ca.mcgill.ecse.flexibook.controller.TOBusinessHour;

public class WeeklyAppointmentCalendarVisualizer extends AppointmentCalendarVisualizer {

	// data elements

	// managed views
	private List<DailyAppointmentCalendarVisualizer> dailyAppointmentCalendarVisualizer;

	public WeeklyAppointmentCalendarVisualizer(Date startDate, List<TOBusinessHour> businessHours,
			List<TOAppointment> revealedAppointments, List<TOAppointment> concealedAppointments) {
		super(startDate, businessHours, revealedAppointments, concealedAppointments);
	}
	
	private void initComponents() {
		LocalDate tomorrow = date.toLocalDate(); // i.e. start date
		for (int i = 0; i < 7; i++) {
			Date today = Date.valueOf(tomorrow);
			dailyAppointmentCalendarVisualizer
					.add(new DailyAppointmentCalendarVisualizer(today, filterBusinessHoursByDate(businessHours, today),
							filterAppointmentsByDate(revealedAppointments, today),
							filterAppointmentsByDate(concealedAppointments, today)));
			tomorrow = tomorrow.plusDays(1);
		}
	}

	private List<TOBusinessHour> filterBusinessHoursByDate(List<TOBusinessHour> businessHours, Date date) {
		List<TOBusinessHour> result = new ArrayList<TOBusinessHour>();
		for (TOBusinessHour bH : businessHours) {
			if (bH.getDayOfWeek() == getDayOfWeekFromDate(date)) {
				result.add(bH);
			}
		}
		return result;
	}

	private List<TOAppointment> filterAppointmentsByDate(List<TOAppointment> appointments, Date date) {
		List<TOAppointment> result = new ArrayList<TOAppointment>();
		for (TOAppointment a : appointments) {
			if (a.getStartDate() == date) {
				result.add(a);
			}
		}
		return result;
	}

	private TOBusinessHour.DayOfWeek getDayOfWeekFromDate(Date date) {
		int day = date.getDay();
		switch (day) {
		case 0:
			return TOBusinessHour.DayOfWeek.Sunday;
		case 1:
			return TOBusinessHour.DayOfWeek.Monday;
		case 2:
			return TOBusinessHour.DayOfWeek.Tuesday;
		case 3:
			return TOBusinessHour.DayOfWeek.Wednesday;
		case 4:
			return TOBusinessHour.DayOfWeek.Thursday;
		case 5:
			return TOBusinessHour.DayOfWeek.Friday;
		case 6:
			return TOBusinessHour.DayOfWeek.Saturday;
		}
		return null;
	}

}
