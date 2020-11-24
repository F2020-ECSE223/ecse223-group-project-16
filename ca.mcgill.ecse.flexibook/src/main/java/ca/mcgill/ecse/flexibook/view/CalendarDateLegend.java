package ca.mcgill.ecse.flexibook.view;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.mcgill.ecse.flexibook.view.ViewCalendarPage.Periodical;

public class CalendarDateLegend extends JPanel {	
	private static final long serialVersionUID = -4927813317988339171L;
	
	// UI elements
	private JPanel dateLabelsPanel;
	private JLabel monthAndYearLabel;
	
	// data elements
	private Date date;
	private Periodical periodical;
	
	public CalendarDateLegend(Date date, Periodical periodical) {
		this.date = date;
		this.periodical = periodical;
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
		monthAndYearLabel = new JLabel(Utils.formatDate(date, "MMMMM yyyy"));		
		
		dateLabelsPanel = new JPanel();
		dateLabelsPanel.setLayout(new BoxLayout(dateLabelsPanel, BoxLayout.X_AXIS));
		
		if (periodical == Periodical.Daily) {			
			dateLabelsPanel.add(Box.createHorizontalGlue());
			dateLabelsPanel.add(new JLabel(Utils.formatDate(date, "EEE dd")));
			dateLabelsPanel.add(Box.createHorizontalGlue());
		} else {
			LocalDate tomorrow = date.toLocalDate();
			for (int i=0; i<7; i++) {
				Date today = Date.valueOf(tomorrow);
				dateLabelsPanel.add(new JLabel(Utils.formatDate(today, "EEE dd")));
				dateLabelsPanel.add(Box.createHorizontalGlue());
				tomorrow = tomorrow.plusDays(1);
			}
		}
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.CENTER)
				.addComponent(monthAndYearLabel)
				.addComponent(dateLabelsPanel)
				);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(monthAndYearLabel)
				.addComponent(dateLabelsPanel)
				);
	}
}

