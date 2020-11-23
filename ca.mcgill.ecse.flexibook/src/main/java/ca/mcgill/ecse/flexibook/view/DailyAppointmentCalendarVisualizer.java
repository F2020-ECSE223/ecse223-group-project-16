package ca.mcgill.ecse.flexibook.view;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;

// should not interface directly with the FlexiBookController
import ca.mcgill.ecse.flexibook.controller.TOAppointment;
import ca.mcgill.ecse.flexibook.controller.TOBusinessHour;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DailyAppointmentCalendarVisualizer extends JPanel {
	private static final long serialVersionUID = -8772497283688477006L;
	
	// UI elements
	private Map<Rectangle2D, TOAppointment> appointmentsByRectangle = new LinkedHashMap<Rectangle2D, TOAppointment>();
	// constants
	private static final int COLUMN_WIDTH = 200;
	private static final int ROW_HEIGHT = 20;
	
	// data elements
	private TOAppointment selectedAppointment;
	private Rectangle2D selectedRectangle;
	private TOBusinessHour.DayOfWeek dayOfWeek;
	private List<TOAppointment> revealedAppointments;
	private List<TOAppointment> concealedAppointments;
	private List<TOBusinessHour> businessHours;
	// constants
	private static final int HOURS_PER_DAY = 24;
	private static final int MINUTES_PER_HOUR = 60;
	private static final int MINIMUM_LABEL_HEIGHT = 16; // dirty
	
	// observer support
	private PropertyChangeSupport support;
	
	public DailyAppointmentCalendarVisualizer(TOBusinessHour.DayOfWeek dayOfWeek, List<TOAppointment> revealedAppointments, List<TOAppointment> concealedAppointments, List<TOBusinessHour> businessHours) {
		support = new PropertyChangeSupport(this);
		this.dayOfWeek = dayOfWeek;
		this.revealedAppointments = revealedAppointments;
		this.concealedAppointments = concealedAppointments;
		this.businessHours = businessHours;
		setPreferredSize(new Dimension(COLUMN_WIDTH, ROW_HEIGHT * HOURS_PER_DAY));
		init();
		System.out.println("min height");
		System.out.println(MINIMUM_LABEL_HEIGHT);
	}
	
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
 
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
	
	private void init() {
		selectedAppointment = null;
		selectedRectangle = null;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				
				for (Map.Entry<Rectangle2D, TOAppointment> entry : appointmentsByRectangle.entrySet()) {
					if (entry.getKey().contains(x, y)) {
						System.out.println("clicked on ");
						System.out.println(entry.getValue());
						selectedRectangle = entry.getKey();
						// event consumer is responsible for determining whether user should have read access on this resource
						support.firePropertyChange("selectedAppointment", selectedAppointment, entry.getValue()); // observe as seen here: https://www.baeldung.com/java-observer-pattern
						selectedAppointment = entry.getValue();
						return;
					}
				}
				support.firePropertyChange("selectedAppointment", selectedAppointment, null); // observe as seen here: https://www.baeldung.com/java-observer-pattern
				selectedRectangle = null;
				selectedAppointment = null;
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
	
	/**
	 * Four passes:
	 * 1. Background pass – fill the entire bounds light grey (denoting outside business hours)
	 * 2. Business hour pass – fill the regions denoting the business hours white (denoting business hours)
	 * 3. Hourly line divider pass – draw all the horizontal lines marking the hours
	 * 4. Appointment pass – draw the given appointments (revealed or concealed)
	 */
	private void doDrawing(Graphics g) {
		// First pass
		g.setColor(Color.LIGHT_GRAY); 
		g.fillRect(0,  0, getWidth(), getHeight());
		
		// Second pass
		g.setColor(Color.WHITE);
		
		for (TOBusinessHour bH : businessHours) {
			if (bH.getDayOfWeek() == dayOfWeek) {
				Time startTime = bH.getStartTime();
				Time endTime = bH.getEndTime();
				System.out.println(startTime);
				System.out.println(endTime);
				int y = scaleTime(startTime);
				int height = scaleTime(endTime) - y;
				System.out.println(y);
				System.out.println(height);
				
				g.fillRect(0, y, COLUMN_WIDTH, height);
			}
		}
		
		// Third pass
		g.setColor(Color.GRAY); 

		for (int i=0; i<HOURS_PER_DAY; i++) {
			int height = i * ROW_HEIGHT;
			g.drawLine(0, height, COLUMN_WIDTH, height); // horizontal line accross the bounds at height i * ROW_HEIGHT
		}
		
		// Fourth pass
		g.setColor(UIManager.getColor("Button.select")); // accent
		
		appointmentsByRectangle.clear();
		for (TOAppointment a : revealedAppointments) {
			
			System.out.println(a);
			Time startTime = a.getStartTime();
			Time endTime = a.getEndTime();
			System.out.println(endTime);
			System.out.println(endTime.getHours());
			System.out.println(endTime.getMinutes()/MINUTES_PER_HOUR);
			
			int x = 0;
			int y = scaleTime(startTime);
			int width = COLUMN_WIDTH;
			int height = scaleTime(endTime) - y;
			
			System.out.println(y);
			System.out.println(height);
			Rectangle2D rectangle = new Rectangle2D.Double(x, y, width, height);
			g.fillRect(x, y, width, height);
			appointmentsByRectangle.put(rectangle, a);
			
			if (height >= MINIMUM_LABEL_HEIGHT) {
				// TODO add label
			}
		}
	}
	
	private int scaleTime(Time time) {
		return time.getHours() * ROW_HEIGHT + (int) ((time.getMinutes()/(float) MINUTES_PER_HOUR) * ROW_HEIGHT);
	}
}
