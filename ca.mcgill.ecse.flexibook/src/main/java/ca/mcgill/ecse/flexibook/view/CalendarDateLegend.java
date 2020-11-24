package ca.mcgill.ecse.flexibook.view;

import java.sql.Date;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.mcgill.ecse.flexibook.view.ViewCalendarPage.Periodical;


public class CalendarDateLegend extends JPanel {	
	private static final long serialVersionUID = -4927813317988339171L;
	// UI elements
	private JLabel monthAndYearLabel;
	private List<JLabel> dateLabels;
	
	// data elements
	private Date date;
	private Periodical periodical;
	private List<Date> dates;
	private List<String> dateStrings;
	
	public CalendarDateLegend(Date date, Periodical periodical) {
		this.date = date;
		initComponents();
	}
	
	public int getColumnWidth() {
		if (periodical == Periodical.Daily) {
			return getWidth();
		} else {
			return getWidth()/7;
		}
	}
	
	private void initComponents() {
		monthAndYearLabel = new JLabel();
		monthAndYearLabel.setText(new DateFormatSymbols().getMonths()[date.getMonth()-1] + " " + (date.getYear() + 1900));
		
		dates = new ArrayList<Date>();
		dateStrings = new ArrayList<String>();
		
		if (periodical == Periodical.Daily) {
			dates.add(date);
		} else {
			LocalDate tomorrow = date.toLocalDate();
			System.out.println(date);
			System.out.println(date.getDay());
			System.out.println(new DateFormatSymbols().getWeekdays().length);
			System.out.println(Arrays.toString(new DateFormatSymbols().getWeekdays()));
			for (int i=0; i<7; i++) {
				Date today = Date.valueOf(tomorrow);
				dates.add(today);
				System.out.println(today.getDay());
				dateStrings.add(Arrays.copyOfRange(new DateFormatSymbols().getWeekdays(), 1, 8)[today.getDay()].substring(0, 3) + " " + today.getDate());
				tomorrow = tomorrow.plusDays(1);
			}
		}
		
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		SequentialGroup sequentialDateLayout = layout.createSequentialGroup();
		ParallelGroup parallelGroupLayout = layout.createParallelGroup();
		
		for (JLabel l : dateLabels) {
			sequentialDateLayout.addComponent(l);
			parallelGroupLayout.addComponent(l);
		}
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.CENTER)
				.addComponent(monthAndYearLabel)
				.addGroup(sequentialDateLayout)
				);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(monthAndYearLabel)
				.addGroup(parallelGroupLayout)
				);		
	}
}

