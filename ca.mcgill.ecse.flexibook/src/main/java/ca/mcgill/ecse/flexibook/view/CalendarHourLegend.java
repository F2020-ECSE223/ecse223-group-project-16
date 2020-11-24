package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class CalendarHourLegend extends JPanel {
	private static enum Period {AM, PM};
	
	// UI elements
	// constants
	private static final int MINIMUM_COLUMN_WIDTH = 100;
	private static final int MINIMUM_ROW_HEIGHT = 20;
	private static final int LABEL_HEIGHT = 16;
	
	public CalendarHourLegend() {
		System.out.println("created legend");
		setMaximumSize(new Dimension(50, Integer.MAX_VALUE));
		setMinimumSize(new Dimension(50, 0));
		setPreferredSize(new Dimension(50, getHeight()));
		init();
	}
	
	private int getColumnWidth() {
		return getWidth();
	}
	
	private int getRowHeight() {
		return getHeight() / 24;
	}
	
	private void init() {
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
	
	private void doDrawing(Graphics g) {
		Period period = Period.AM;
		for (int i=0; i<24; i++) {
			int height = i * getRowHeight();
			int hour = i % 12;
			if (i == 0) {
				continue;
			}
			if (i == 12) {
				if (hour == 0) {
					hour = 12;
				}
				period = Period.PM;
			}
			String leftPadding = "";
			if (hour < 10) {
				leftPadding = "  ";
			}
			g.setColor(Color.BLACK);
			g.drawString(leftPadding + String.valueOf(hour) + " " + period.toString(), 0, height + (LABEL_HEIGHT - 5) / 2);
			g.setColor(Color.GRAY);
			g.drawLine(getWidth() - 5, height, getWidth(), height);
		}
	}
}
