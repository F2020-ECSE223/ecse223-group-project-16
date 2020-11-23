package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.geom.Rectangle2D;
import java.sql.Date;
import java.sql.Time;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;

import ca.mcgill.ecse.flexibook.controller.TOAppointment;
import ca.mcgill.ecse.flexibook.controller.TOBusinessHour;

public class DailyAppointmentCalendarVisualizer extends AppointmentCalendarVisualizer {
	private static final long serialVersionUID = -8772497283688477006L;
	
	// UI elements
	private Map<Rectangle2D, TOAppointment> appointmentsByRectangle = new LinkedHashMap<Rectangle2D, TOAppointment>();
	// constants
	private static final int MINIMUM_COLUMN_WIDTH = 100;
	private static final int MINIMUM_ROW_HEIGHT = 20;
	private static final int LABEL_HEIGHT = 16;
	private static final int APPOINTMENT_INFO_LABEL_PADDING = 5;
	private static final int APPOINTMENT_ROUNDING_ARC_RADIUS = 10;
	private static final Color APPOINTMENT_COLOR = new Color(3, 155, 229);
	
	public DailyAppointmentCalendarVisualizer(Date date, List<TOBusinessHour> businessHours, List<TOAppointment> revealedAppointments, List<TOAppointment> concealedAppointments) {
		super(date, businessHours, revealedAppointments, concealedAppointments);
		
		setMinimumSize(new Dimension(MINIMUM_COLUMN_WIDTH, MINIMUM_ROW_HEIGHT * 24));
		init();
	}
	
	private int getRowHeight() {
		return getHeight() / 24;
	}
	
	private int getColumnWidth() {
		return getWidth();
	}
	
	public TOAppointment getSelectedAppointment() {
		return selectedAppointment;
	}
	
	public void unsetSelectedAppointment() {
		selectedAppointment = null;
		selectedRectangle = null;
		repaint();
	}
	
	public Date getDate() {
		return date;
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
						repaint();
						return;
					}
				}
				support.firePropertyChange("selectedAppointment", selectedAppointment, null); // observe as seen here: https://www.baeldung.com/java-observer-pattern
				selectedRectangle = null;
				selectedAppointment = null;
				repaint();
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
	
	/**
	 * Six passes:
	 * 1. Background pass – fill the entire bounds light grey (denoting outside business hours)
	 * 2. Business hour pass – fill the regions denoting the business hours white (denoting business hours)
	 * 3. Hourly line divider pass – draw all the horizontal lines marking the hours
	 * 4. Daily line divider pass – draw the right margin marking the border to the next day
	 * 5. Appointment pass – draw the given appointments (revealed or concealed)
	 * 6. Selection pass – draw the outline of the rectangle of the selected appointment, if any 
	 */
	private void doDrawing(Graphics g) {
		// First pass
		g.setColor(Color.LIGHT_GRAY); 
		
		g.fillRect(0,  0, getWidth(), getHeight());
		
		// Second pass
		g.setColor(Color.WHITE);
		
		for (TOBusinessHour bH : businessHours) {
			Time startTime = bH.getStartTime();
			Time endTime = bH.getEndTime();
			System.out.println(startTime);
			System.out.println(endTime);
			int y = scaleTime(startTime);
			int height = scaleTime(endTime) - y;
			System.out.println(y);
			System.out.println(height);
			
			g.fillRect(0, y, getColumnWidth(), height);

		}
		
		// Third pass
		g.setColor(Color.GRAY); 

		for (int i=0; i<24; i++) {
			int height = i * getRowHeight();
			g.drawLine(0, height, getColumnWidth(), height); // horizontal line accross the bounds at height i * ROW_HEIGHT
		}
		
		// Fourth pass
		g.drawLine(getColumnWidth(), 0, getColumnWidth(), getRowHeight() * 24);
		
		// Fifth pass
		g.setColor(APPOINTMENT_COLOR); // accent
		
		selectedRectangle = null;
		appointmentsByRectangle.clear();
		for (TOAppointment a : revealedAppointments) {
			Rectangle2D rectangle = drawAppointment(g, a, false);
			appointmentsByRectangle.put(rectangle, a);
			if (a == selectedAppointment) {
				selectedRectangle = rectangle;
			}
		}
		
		for (TOAppointment a : concealedAppointments) {
			Rectangle2D rectangle = drawAppointment(g, a, true);
			appointmentsByRectangle.put(rectangle, a);
			if (a == selectedAppointment) {
				selectedRectangle = rectangle;
			}
		}
		
		// Sixth pass
		g.setColor(Color.BLUE);
		
		if (selectedRectangle != null) {
			g.drawRoundRect((int) selectedRectangle.getX(), (int) selectedRectangle.getY(), (int) selectedRectangle.getWidth(), (int) selectedRectangle.getHeight(), APPOINTMENT_ROUNDING_ARC_RADIUS, APPOINTMENT_ROUNDING_ARC_RADIUS);
		}
	}
	
	// with the label
	private Rectangle2D drawAppointment(Graphics g, TOAppointment appointment, boolean concealed) {
		System.out.println(appointment);
		Time startTime = appointment.getStartTime();
		Time endTime = appointment.getEndTime();
		System.out.println(endTime);
		System.out.println(endTime.getHours());
		System.out.println(endTime.getMinutes()/60);
		
		int x = 0;
		int y = scaleTime(startTime);
		int width = getColumnWidth();
		int height = scaleTime(endTime) - y - 1; // visually divide consecutive appts
		
		System.out.println(y);
		System.out.println(height);
		Rectangle2D rectangle = new Rectangle2D.Double(x, y, width, height);
		g.fillRoundRect(x, y, width, height, APPOINTMENT_ROUNDING_ARC_RADIUS, APPOINTMENT_ROUNDING_ARC_RADIUS);
		
		Color oldColor = g.getColor();
		g.setColor(Color.BLACK);
		Shape oldClip = g.getClip();
		g.setClip(new Rectangle2D.Double(x + APPOINTMENT_INFO_LABEL_PADDING, y + APPOINTMENT_INFO_LABEL_PADDING, getColumnWidth() - APPOINTMENT_INFO_LABEL_PADDING * 2, height - APPOINTMENT_INFO_LABEL_PADDING * 2));
		if (height >= LABEL_HEIGHT) {
			if (concealed) {
				g.drawString("Appointment", APPOINTMENT_INFO_LABEL_PADDING, y + LABEL_HEIGHT);
			} else {
				if (height >= LABEL_HEIGHT * 2 + APPOINTMENT_INFO_LABEL_PADDING) {
					g.drawString(appointment.getCustomerUsername(), APPOINTMENT_INFO_LABEL_PADDING, y + LABEL_HEIGHT);
					g.drawString(appointment.getBookableServiceName(), APPOINTMENT_INFO_LABEL_PADDING, y + LABEL_HEIGHT * 2);
				} else {
					g.drawString(appointment.getCustomerUsername() + " - " + appointment.getBookableServiceName(), APPOINTMENT_INFO_LABEL_PADDING, y + LABEL_HEIGHT);
				}
			}
		}
		
		// clean up
		g.setClip(oldClip);
		g.setColor(oldColor);
		return rectangle;
	}
	
	private int scaleTime(Time time) {
		return time.getHours() * getRowHeight() + (int) ((time.getMinutes() / (float) 60) * getRowHeight());
	}
}
