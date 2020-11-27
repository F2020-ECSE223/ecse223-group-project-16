package ca.mcgill.ecse.flexibook.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;
import java.sql.Time;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import ca.mcgill.ecse.flexibook.controller.TOAppointment;
import ca.mcgill.ecse.flexibook.controller.TOBusinessHour;
import ca.mcgill.ecse.flexibook.util.SystemTime;

public class DailyAppointmentCalendarVisualizer extends AppointmentCalendarVisualizer {
	private static final long serialVersionUID = -8772497283688477006L;
	
	// constants
	private static int REFRESH_DELAY = 5 * 1000; // ms
	
	// UI elements
	private Map<Rectangle2D, TOAppointment> appointmentsByRectangle = new LinkedHashMap<Rectangle2D, TOAppointment>();
	// constants
	private static final int MINIMUM_COLUMN_WIDTH = 60;
	private static final int MINIMUM_ROW_HEIGHT = 40;
	private static final int PREFERRED_COLUMN_WIDTH = 100;
	private static final int PREFERRED_ROW_HEIGHT = 50;
	private static final int LABEL_HEIGHT = 14;
	private static final int APPOINTMENT_INFO_LABEL_PADDING = 5;
	private static final int APPOINTMENT_ROUNDING_ARC_RADIUS = 7;
	private static final int APPOINTMENT_MARGIN_BOTTOM = 1;
	private static final int APPOINTMENT_MARGIN_LEFT = 1;
	private static final int APPOINTMENT_BORDER_STROKE = 3;
	private static final Color BUSINESS_HOUR_COLOR  = Color.WHITE;
	private static final Color REVEALED_APPOINTMENT_FILL_COLOR = new Color(3, 155, 229);
	private static final Color REVEALED_APPOINTMENT_BORDER_COLOR = REVEALED_APPOINTMENT_FILL_COLOR;
	private static final Color CONCEALED_APPOINTMENT_FILL_COLOR = Color.WHITE;
	private static final Color CONCEALED_APPOINTMENT_BORDER_COLOR = REVEALED_APPOINTMENT_FILL_COLOR;
	
	public DailyAppointmentCalendarVisualizer(Date date, List<TOBusinessHour> businessHours, List<TOAppointment> revealedAppointments, List<TOAppointment> concealedAppointments) {
		super(date, businessHours, revealedAppointments, concealedAppointments);
		init();
	}
	
	private int getRowHeight() {
		return getHeight() / 24;
	}
	
	private int getColumnWidth() {
		return getWidth();
	}
	
	public void setRevealedAppointments(List<TOAppointment> revealedAppointments) {
		super.setRevealedAppointments(revealedAppointments);
		repaint();
	}
	
	public void setConcealedAppointments(List<TOAppointment> concealedAppointments) {
		super.setConcealedAppointments(concealedAppointments);
		repaint();
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
		// global settings
		setMinimumSize(new Dimension(MINIMUM_COLUMN_WIDTH, MINIMUM_ROW_HEIGHT * 24));
		setPreferredSize(new Dimension(PREFERRED_COLUMN_WIDTH, PREFERRED_ROW_HEIGHT));
		setSize(getPreferredSize());
		
		selectedAppointment = null;
		selectedRectangle = null;
		
		// listeners
		addMouseListener(new MouseAdapter() {
			/**
			 * @see https://www.baeldung.com/java-observer-pattern
			 */
			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				
				for (Map.Entry<Rectangle2D, TOAppointment> entry : appointmentsByRectangle.entrySet()) {
					if (entry.getKey().contains(x, y)) {
						selectedRectangle = entry.getKey();
						// fire new appointment selected event
						support.firePropertyChange("selectedAppointment", selectedAppointment, entry.getValue());
						selectedAppointment = entry.getValue();
						repaint();
						return;
					}
				}
				
				// fire appointment unselected event
				support.firePropertyChange("selectedAppointment", selectedAppointment, null);
				
				// fire no event
				selectedRectangle = null;
				selectedAppointment = null;
				repaint();
			}
		});
		
		// repaint at regular intervals to account for moving "now" line, and for business hour / time slot changes made in another open window of the application
		Timer timer = new Timer(REFRESH_DELAY, new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent e) {
	            repaint();
	        }
	    });
	    timer.start();
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
	 * 7. Current time pass – draw a horizonal line marking the current time if the current date is this date
	 */
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();

		// First pass
		g2d.setColor(Color.LIGHT_GRAY); 
		
		g2d.fillRect(0,  0, getWidth(), getHeight());
		
		// Second pass
		g2d.setColor(BUSINESS_HOUR_COLOR);
		
		for (TOBusinessHour bH : businessHours) {
			Time startTime = bH.getStartTime();
			Time endTime = bH.getEndTime();
			int y = scaleTime(startTime);
			int height = scaleTime(endTime) - y;
			
			g2d.fillRect(0, y, getColumnWidth() - 1, height);
		}
		
		// Third pass
		g2d.setColor(Color.GRAY); 

		for (int i=0; i<24; i++) {
			int height = i * getRowHeight();
			g2d.drawLine(0, height, getColumnWidth(), height); // horizontal line accross the bounds at height i * ROW_HEIGHT
		}
		
		// Fourth pass
		g2d.drawLine(getColumnWidth() - 1, 0, getColumnWidth() - 1, getRowHeight() * 24);
		
		// Fifth pass
		selectedRectangle = null;
		appointmentsByRectangle.clear();
		
		for (TOAppointment a : revealedAppointments) {
			Rectangle2D rectangle = drawAppointment(g2d, a, false);
			appointmentsByRectangle.put(rectangle, a);
			if (a == selectedAppointment) {
				selectedRectangle = rectangle;
			}
		}
		
		for (TOAppointment a : concealedAppointments) {
			Rectangle2D rectangle = drawAppointment(g2d, a, true);
			appointmentsByRectangle.put(rectangle, a);
			if (a == selectedAppointment) {
				selectedRectangle = rectangle;
			}
		}
		
		// Sixth pass
		g2d.setColor(Color.BLUE);
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(APPOINTMENT_BORDER_STROKE));
		if (selectedRectangle != null) {
			g2d.drawRoundRect((int) selectedRectangle.getX(), (int) selectedRectangle.getY(), (int) selectedRectangle.getWidth(), (int) selectedRectangle.getHeight(), APPOINTMENT_ROUNDING_ARC_RADIUS, APPOINTMENT_ROUNDING_ARC_RADIUS);
		}
		g2d.setStroke(oldStroke);
		
		// Seventh pass
		g2d.setColor(Color.RED);
		
		if (SystemTime.getDate().equals(date)) {
			int height = scaleTime(SystemTime.getTime());
			g2d.drawLine(0, height, getColumnWidth() - 1, height);
		}
	}
	
	// with the label
	private Rectangle2D drawAppointment(Graphics2D g, TOAppointment appointment, boolean concealed) {
		Time startTime = appointment.getStartTime();
		Time endTime = appointment.getEndTime();
		
		Color oldColor = g.getColor();
		Shape oldClip = g.getClip();
		Stroke oldStroke = g.getStroke();

		
		int x = 0;
		int y = scaleTime(startTime);
		int width = getColumnWidth();
		int height = scaleTime(endTime) - y - APPOINTMENT_MARGIN_BOTTOM; // visually divide consecutive appts
		
		int marginRight = (int) (getColumnWidth() * 0.07);
		Rectangle2D rectangle = new Rectangle2D.Double(x + APPOINTMENT_MARGIN_LEFT, y, width - APPOINTMENT_MARGIN_LEFT - marginRight, height);
		
		// pick color of appointment rectangle based on whether it is concealed or revealed
		Color fillColor = REVEALED_APPOINTMENT_FILL_COLOR;
		Color borderColor = REVEALED_APPOINTMENT_BORDER_COLOR;
		if (concealed) {
			fillColor = CONCEALED_APPOINTMENT_FILL_COLOR;
			borderColor = CONCEALED_APPOINTMENT_BORDER_COLOR;
		}
		g.setColor(borderColor);
		g.setStroke(new BasicStroke(APPOINTMENT_BORDER_STROKE));
		g.drawRoundRect(x + APPOINTMENT_MARGIN_LEFT, y, width - APPOINTMENT_MARGIN_LEFT - marginRight, height, APPOINTMENT_ROUNDING_ARC_RADIUS, APPOINTMENT_ROUNDING_ARC_RADIUS);
		g.setStroke(oldStroke);
		g.setColor(fillColor);
		g.fillRoundRect(x + APPOINTMENT_MARGIN_LEFT, y, width - APPOINTMENT_MARGIN_LEFT - marginRight, height, APPOINTMENT_ROUNDING_ARC_RADIUS, APPOINTMENT_ROUNDING_ARC_RADIUS);
		
		g.setColor(Color.BLACK);
		if (height >= LABEL_HEIGHT + APPOINTMENT_INFO_LABEL_PADDING * 2) { // enough height for the label with top and bottom padding
			g.setClip(new Rectangle2D.Double(x + APPOINTMENT_INFO_LABEL_PADDING, y + APPOINTMENT_INFO_LABEL_PADDING, getColumnWidth() - APPOINTMENT_INFO_LABEL_PADDING * 2 - marginRight, height - APPOINTMENT_INFO_LABEL_PADDING * 2));
			if (concealed) {
				g.drawString("Appointment", APPOINTMENT_INFO_LABEL_PADDING, y + LABEL_HEIGHT);
			} else {
				if (height >= LABEL_HEIGHT * 2 + APPOINTMENT_INFO_LABEL_PADDING * 2) { // enough height to stack the text
					g.drawString(appointment.getCustomerUsername(), APPOINTMENT_INFO_LABEL_PADDING, y + LABEL_HEIGHT);
					g.drawString(appointment.getBookableServiceName(), APPOINTMENT_INFO_LABEL_PADDING, y + LABEL_HEIGHT * 2);
				} else { // only enough room to format the text on a single line
					g.drawString(appointment.getCustomerUsername() + " - " + appointment.getBookableServiceName(), APPOINTMENT_INFO_LABEL_PADDING, y + LABEL_HEIGHT);
				}
			}
		} else { // possibly enough height for the label formatted as a single line without any verical padding, possibly not even enough height for that
			g.setClip(new Rectangle2D.Double(x + APPOINTMENT_INFO_LABEL_PADDING, y, getColumnWidth() - APPOINTMENT_INFO_LABEL_PADDING * 2 - marginRight, height)); // draw as much as possible vertically and clip what exceeds the appointment rectangle
			if (concealed) {
				g.drawString("Appointment", APPOINTMENT_INFO_LABEL_PADDING, y + LABEL_HEIGHT);
			} else {
				g.drawString(appointment.getCustomerUsername() + " - " + appointment.getBookableServiceName(), APPOINTMENT_INFO_LABEL_PADDING, y + LABEL_HEIGHT);
			}
		}
		
		// clean up
		g.setClip(oldClip);
		g.setColor(oldColor);
		g.setStroke(oldStroke);
		return rectangle;
	}
	
	private int scaleTime(Time time) {
		// we are expecting a value between 0 & 1 and we'll use this to place the size/location of the block
		return time.getHours() * getRowHeight() + (int) ((time.getMinutes() / (float) 60) * getRowHeight());
	}
}
