package ca.mcgill.ecse.flexibook.view;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class Utils {
	static void openFrame(JFrame sourceFrame, JFrame targetFrame) {
		targetFrame.setVisible(true);
		targetFrame.setLocationRelativeTo(sourceFrame);
	}
	
	static void switchToFrame(JFrame sourceFrame, JFrame targetFrame) {
		openFrame(sourceFrame, targetFrame);
		sourceFrame.dispose();
	}
	
	static void goToFrame(JFrame sourceFrame, JFrame targetFrame, boolean disableSource) {
		openFrame(sourceFrame, targetFrame);
		sourceFrame.setEnabled(!disableSource);
	}
	
	/**
	 * Resize given text field to given width, and fix the maximum height to the preferred height
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
}
