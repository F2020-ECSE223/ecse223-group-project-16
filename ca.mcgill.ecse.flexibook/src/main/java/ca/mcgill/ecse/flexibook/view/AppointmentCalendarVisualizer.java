package ca.mcgill.ecse.flexibook.view;

import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;
import java.util.List;

import javax.swing.JPanel;

import ca.mcgill.ecse.flexibook.controller.TOAppointment;
import ca.mcgill.ecse.flexibook.controller.TOBusinessHour;

//should not interface directly with the FlexiBookController
public abstract class AppointmentCalendarVisualizer extends JPanel {
	private static final long serialVersionUID = 4774093206640158797L;
	
	// data elements
	protected Date date;
	protected TOAppointment selectedAppointment;
	protected Rectangle2D selectedRectangle;
	protected List<TOAppointment> revealedAppointments;
	protected List<TOAppointment> concealedAppointments;
	protected List<TOBusinessHour> businessHours;
	
	// observer support
	protected PropertyChangeSupport support;
	
	public AppointmentCalendarVisualizer(Date date, List<TOBusinessHour> businessHours, List<TOAppointment> revealedAppointments, List<TOAppointment> concealedAppointments) {
		this.date = date;
		this.businessHours = businessHours;
		this.revealedAppointments = revealedAppointments;
		this.concealedAppointments = concealedAppointments;
		
		support = new PropertyChangeSupport(this);
	}
	
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
 
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
}
