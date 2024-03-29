package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.TOAppointment;
import ca.mcgill.ecse.flexibook.controller.TOBusinessHour;

public class Utils {
	public static final int TEXT_FIELD_WIDTH = 300;
	
	static void openFrame(JFrame sourceFrame, JFrame targetFrame) {
		targetFrame.setVisible(true);
		targetFrame.setLocationRelativeTo(sourceFrame);
	}

	static void switchToFrame(JFrame sourceFrame, JFrame targetFrame) {
		openFrame(sourceFrame, targetFrame);
		sourceFrame.dispose();
	}

	static void goToFrame(JFrame sourceFrame, JFrame targetFrame) {
		openFrame(sourceFrame, targetFrame);
		FlexiBookApplication.addDetachedPage(targetFrame);
	}

	/**
	 * Resize given text field to given width, and fix the maximum height to the
	 * preferred height
	 * 
	 * @author louca
	 * 
	 * @param textField
	 * @param width
	 */
	static void resizeTextFieldToWidth(JTextField textField, int width) {
		Dimension preferredSize = textField.getPreferredSize();
		textField.setPreferredSize(new Dimension(width, preferredSize.height)); // resize text field width to given width
		textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredSize.height));
	}
	
	static void togglePasswordFieldVisibility(JPasswordField passwordField, JButton passwordVisibilityButton) {
		if (passwordField.getEchoChar() == '*' && passwordVisibilityButton.getText().equals("Show")) {
			passwordVisibilityButton.setText("Hide");
			passwordField.setEchoChar((char) 0);
		} else if (passwordField.getEchoChar() == ((char) 0) && passwordVisibilityButton.getText().equals("Hide")) {
			passwordVisibilityButton.setText("Show");
			passwordField.setEchoChar('*');
		} else {
			throw new IllegalStateException("Password field and password visibility button are inconsistent");
		}
		
		passwordField.requestFocus();
		int previousCaretPosition = passwordField.getCaretPosition();
		passwordField.setCaret(new DefaultCaret()); // macOS L&F bug, see https://stackoverflow.com/a/17103816
		passwordField.setCaretPosition(previousCaretPosition);
	}
	
	static void makePlaceholder(JTextField textField, String placeholderText) {
		textField.setText(placeholderText);
		textField.setForeground(Color.GRAY);
		textField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField.getText().equals(placeholderText)) {
					textField.setText("");
					textField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().isEmpty()) {
					textField.setForeground(Color.GRAY);
					textField.setText(placeholderText);
				}
			}
		});
	}
    static String formatTime(Time time, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(time);
    }
	static void fixSize(Component component, Dimension dimension) {
		component.setPreferredSize(dimension);
		component.setMinimumSize(dimension);
		component.setMaximumSize(dimension);
	}
	
	static TOBusinessHour.DayOfWeek getDayOfWeekFromDate(Date date) {
		switch (date.getDay()) {
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
	
	static List<TOBusinessHour> filterBusinessHoursByDate(List<TOBusinessHour> businessHours, Date date) {
		List<TOBusinessHour> result = new ArrayList<TOBusinessHour>();
		for (TOBusinessHour bH : businessHours) {
			if (bH.getDayOfWeek() == Utils.getDayOfWeekFromDate(date)) {
				result.add(bH);
			}
		}
		return result;
	}

	static List<TOAppointment> filterAppointmentsByDate(List<TOAppointment> appointments, Date date) {
		List<TOAppointment> result = new ArrayList<TOAppointment>();
		for (TOAppointment a : appointments) {
			if (a.getStartDate().equals(date)) {
				result.add(a);
			}
		}
		return result;
	}
	
	static String formatDate(Date date, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}
}
