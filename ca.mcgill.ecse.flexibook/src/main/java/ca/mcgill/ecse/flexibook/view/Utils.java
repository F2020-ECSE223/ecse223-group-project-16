package ca.mcgill.ecse.flexibook.view;

import javax.swing.JFrame;

public class Utils {
	public static void switchToFrame(JFrame sourceFrame, JFrame targetFrame) {
		targetFrame.setVisible(true);
		targetFrame.setLocationRelativeTo(sourceFrame);
		sourceFrame.dispose();
	}
	
	public static void goToFrame(JFrame sourceFrame, JFrame targetFrame, boolean disableSource) {
		targetFrame.setVisible(true);
		targetFrame.setLocationRelativeTo(sourceFrame);
		sourceFrame.setEnabled(!disableSource);
	}
}
