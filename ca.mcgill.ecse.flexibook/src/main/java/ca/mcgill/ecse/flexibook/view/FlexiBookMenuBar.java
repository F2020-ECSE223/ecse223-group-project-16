package ca.mcgill.ecse.flexibook.view;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;

import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOUser;
import ca.mcgill.ecse.flexibook.model.User;

@SuppressWarnings("serial")
public class FlexiBookMenuBar extends JMenuBar {
	// constants
	private static final int USERNAME_CUTOFF = 15;
	
	// navigation
	private JMenu navigationMenu;
	private JMenuItem goToHomeMenuItem;
	private JMenuItem goToServicesMenuItem;
	private JMenuItem goToBusinessInfoMenuItem;
	private JMenuItem goToAppointmentsMenuItem;
	private JMenuItem goToViewCalendarMenuItem;
	private JMenuItem refreshMenuItem;
	// account
	private JMenu accountMenu;
	private JMenuItem logoutMenuItem;
	private JMenuItem goToAccountSettingsMenuItem;

	// data elements
	private JFrame parent;
	private static String activePageName;
	private static Map<String, JMenuItem> navigationMenuItemsByPageName = new LinkedHashMap<String, JMenuItem>();

	public FlexiBookMenuBar(JFrame parent) {
		initComponents();
		refreshData();
	}

	public FlexiBookMenuBar(JFrame parent, String currentPageName) {
		this.parent = parent;
		activePageName = currentPageName;
		initComponents();
		refreshData();
	}

	private void initComponents() {
		navigationMenu = new JMenu("FlexiBook");
		
		// navigation menu
		goToHomeMenuItem = new JMenuItem("Home");
		goToBusinessInfoMenuItem = new JMenuItem("Business Info");
		goToServicesMenuItem = new JMenuItem("Services");
		goToAppointmentsMenuItem = new JMenuItem("Appointments");
		goToViewCalendarMenuItem = new JMenuItem("View Calendar");
//		helpMenuItem = new JMenuItem("Help", UIManager.getIcon("OptionPane.questionIcon"));
		refreshMenuItem = new JMenuItem("Refresh");
		
		navigationMenuItemsByPageName.put("Home", goToHomeMenuItem);
		navigationMenuItemsByPageName.put("Business Info", goToBusinessInfoMenuItem);
		navigationMenuItemsByPageName.put("View Calendar", goToViewCalendarMenuItem);
		navigationMenuItemsByPageName.put("Services", new JMenuItem("Services"));
		navigationMenuItemsByPageName.put("Appointments", new JMenuItem("Appointments"));
		
		for (Map.Entry<String, JMenuItem> entry : navigationMenuItemsByPageName.entrySet()) {
			navigationMenu.add(entry.getValue());
		}
		navigationMenu.addSeparator();
		navigationMenu.add(refreshMenuItem);
//		navigationMenu.add(helpMenuItem);
		
		// account menu
		TOUser currentUser = FlexiBookController.getCurrentUser();
		if (currentUser == null) {
			try {
				FlexiBookController.createCustomerAccount("myCustUName12345678", "myCustPass");
				FlexiBookController.login("myCustUName12345678", "myCustPass");
			} catch (InvalidInputException e) {
				e.printStackTrace();
			}
//			throw new IllegalStateException("Current user cannot be null");
		}
		currentUser = FlexiBookController.getCurrentUser();
		String username = currentUser.getUsername();
		String usernamePreview = username;
		if (username.length() > USERNAME_CUTOFF) {
			usernamePreview = username.substring(0, USERNAME_CUTOFF - 3).concat("...");
		}
		usernamePreview += "  ▼";
		accountMenu = new JMenu(usernamePreview);
		
		logoutMenuItem = new JMenuItem("Logout");
		goToAccountSettingsMenuItem = new JMenuItem("Account Settings");
		accountMenu.add(logoutMenuItem);
		accountMenu.add(goToAccountSettingsMenuItem);

		// listeners
		// navigation menu
		// navigation menu items
		goToHomeMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Utils.switchToFrame(parent, new LandingPage());
			}
		});
		goToBusinessInfoMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Utils.switchToFrame(parent, new BusinessInfoPage());
			}
		});
		goToServicesMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Utils.switchToFrame(parent, new ServicesPage());
			}
		});
		goToAppointmentsMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Utils.switchToFrame(parent, new AppointmentsPage());
			}
		});
		goToViewCalendarMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Utils.switchToFrame(parent, new ViewCalendarPage());
			}
		});
		refreshMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				clickMenuItemByName(activePageName);
				parent.dispose();
			}
		});
		
		// account menu
		accountMenu.addMenuListener(new javax.swing.event.MenuListener() {
			@Override
			public void menuSelected(javax.swing.event.MenuEvent e) {
				System.out.println("selectted");
				accountMenu.setText(toggleCaret(accountMenu.getText()));
			}
			@Override
	        public void menuCanceled(javax.swing.event.MenuEvent e) {
				System.out.println("canceld");
				toggleCaret(accountMenu.getText());
	        }
			@Override
			public void menuDeselected(MenuEvent e) {
				accountMenu.setText(toggleCaret(accountMenu.getText()));
				
			}
		});
		// account menu items
		logoutMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				logout();
			}
		});
		goToAccountSettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				parent.setEnabled(false);
				Utils.goToFrame(parent, new AccountSettingsPage(), true);
			}
		});
		
		add(navigationMenu);
		add(Box.createHorizontalGlue()); // make account menu right-aligned
		add(accountMenu);
	}

	private void refreshData() {
		if (activePageName != null) {
			setActivePage(activePageName);
		}
	}

	private void setActivePage(String targetPageName) {
		JMenuItem activePageMenuItem = navigationMenuItemsByPageName.get(activePageName);		
		if (activePageMenuItem != null) {
			activePageMenuItem.setSelected(false);
			activePageMenuItem.setEnabled(true);
		}

		JMenuItem targetPageMenuItem = navigationMenuItemsByPageName.get(targetPageName);
		if (targetPageMenuItem == null) {
			if (targetPageName.equals("Account Settings")) {
				targetPageMenuItem = goToAccountSettingsMenuItem;
			} else {
				throw new IllegalArgumentException("Page with name '" + targetPageName + "' was not found.");
			}
		}
		targetPageMenuItem.setSelected(true);
		targetPageMenuItem.setEnabled(false);
		activePageName = targetPageName;
	}

	private void clickMenuItemByName(String targetPageName) {
		System.out.println(targetPageName);
		if (targetPageName == null) {
			throw new IllegalArgumentException("Target page name cannot be null");
		}
		setActivePage(targetPageName); // just in case
		
		JMenuItem targetPageMenuItem = navigationMenuItemsByPageName.get(targetPageName);
		if (targetPageMenuItem == null) {
			if (targetPageName.equals("Account Settings")) {
				targetPageMenuItem = goToAccountSettingsMenuItem;
			} else {
				throw new IllegalStateException("There is no such target page");
			}
		}
		targetPageMenuItem.setEnabled(true);
		targetPageMenuItem.doClick();
		targetPageMenuItem.setEnabled(false);
	}
	
	private void logout() {
		clickMenuItemByName("Home");
		
		System.out.println("logging out");
	}
	
	private String toggleCaret(String text) { 
		char caretChar = '▲';
		
		if (text.charAt(text.length() - 1) == '▲') {
			caretChar = '▼';
		}
		return text.substring(0, text.length() - 1) + String.valueOf(caretChar);
	}
}
