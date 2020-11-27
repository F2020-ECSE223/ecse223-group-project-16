package ca.mcgill.ecse.flexibook.view;

import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOUser;

public class FlexiBookMenuBar extends JMenuBar {
	private static final long serialVersionUID = 5123406271612468160L;

	// constants
	private static final int USERNAME_CUTOFF = 15;

	// UI elements
	// navigation
	private JMenu brandMenuItem;
	private JMenu navigationMenu;
	private JMenuItem goToMenuMenuItem;
	private JMenuItem goToBusinessInfoMenuItem;
	private JMenuItem goToViewCalendarMenuItem;
	private JMenuItem goToAppointmentsMenuItem;
	private JMenuItem goToAppointmentManagementMenuItem;
	private JMenuItem goToServicesMenuItem;
	private JMenuItem refreshMenuItem;
	// account
	private JMenu accountMenu;
	private JMenuItem logoutMenuItem;
	private JMenuItem goToAccountSettingsMenuItem;

	// data elements
	private final JFrame parentFrame;
	private String activePageName;
	private final Map<String, JMenuItem> navigationMenuItemsByPageName = new LinkedHashMap<String, JMenuItem>();
	private final boolean isNavigable;
	private final boolean currentUserIsOwner;
	// constants
	private static final String[] pageNames = {"Menu", "Business Info", "View Calendar", "Appointments", "Services"};
	
	public FlexiBookMenuBar(JFrame parentFrame, String currentPageName) {
		this(parentFrame, currentPageName, true);
	}
	
	public FlexiBookMenuBar(JFrame parentFrame, String currentPageName, boolean isNavigable) {
		if (!isValidPageName(currentPageName) && !currentPageName.equals("Account Settings")) {
			throw new IllegalArgumentException("Page with name '" + currentPageName + "' is not a valid page name");
		}
		this.parentFrame = parentFrame;
		activePageName = currentPageName;
		this.isNavigable = isNavigable;
		currentUserIsOwner = FlexiBookController.isCurrentUserOwner();
		initComponents();
		refreshData();
	}
	
	private boolean isValidPageName(String pageName) {
		for (int i=0; i<pageNames.length; i++) {
			if (pageName.equals(pageNames[i])) {
				return true;
			}
		}
		return false;
	}

	private void initComponents() {

		navigationMenu = new JMenu("FlexiBook");
		// navigation menu
		goToMenuMenuItem = new JMenuItem("Menu");
		navigationMenuItemsByPageName.put("Menu", goToMenuMenuItem);
		goToBusinessInfoMenuItem = new JMenuItem("Business Info");
		navigationMenuItemsByPageName.put("Business Info", goToBusinessInfoMenuItem);
		goToViewCalendarMenuItem = new JMenuItem("View Calendar");
		navigationMenuItemsByPageName.put("View Calendar", goToViewCalendarMenuItem);
		if (currentUserIsOwner) {
			goToAppointmentManagementMenuItem = new JMenuItem("Appointment Management");
			navigationMenuItemsByPageName.put("Appointment Management", goToAppointmentManagementMenuItem);
			goToServicesMenuItem = new JMenuItem("Services");
			navigationMenuItemsByPageName.put("Services", goToServicesMenuItem);
		} else {
			goToAppointmentsMenuItem = new JMenuItem("Appointments");
			navigationMenuItemsByPageName.put("Appointments", goToAppointmentsMenuItem);
		}
//			helpMenuItem = new JMenuItem("Help", UIManager.getIcon("OptionPane.questionIcon"));
		refreshMenuItem = new JMenuItem("Refresh");

		for (Map.Entry<String, JMenuItem> entry : navigationMenuItemsByPageName.entrySet()) {
			navigationMenu.add(entry.getValue());
		}
		navigationMenu.addSeparator();
		navigationMenu.add(refreshMenuItem);
//			navigationMenu.add(helpMenuItem);
		brandMenuItem = new JMenu("FlexiBook");

		// account menu
		accountMenu = new JMenu();
		TOUser currentUser = FlexiBookController.getCurrentUser();
		if (currentUser == null) {
			throw new IllegalStateException("Current user cannot be null");
		}
		currentUser = FlexiBookController.getCurrentUser();
		String username = currentUser.getUsername();
		String usernamePreview = username;
		if (username.length() > USERNAME_CUTOFF) {
			usernamePreview = username.substring(0, USERNAME_CUTOFF - 3).concat("...");
		}
		accountMenu.setText(usernamePreview);

		logoutMenuItem = new JMenuItem("Logout");
		goToAccountSettingsMenuItem = new JMenuItem("Account Settings");
		accountMenu.add(logoutMenuItem);
		accountMenu.add(goToAccountSettingsMenuItem);

		// listeners
		// navigation menu
		// navigation menu items
		goToMenuMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Utils.switchToFrame(parentFrame, new MenuPage());
			}
		});
		goToBusinessInfoMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Utils.switchToFrame(parentFrame, new BusinessInfoPage());
			}
		});
		goToViewCalendarMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Utils.goToFrame(parentFrame, new ViewCalendarPage());
			}
		});
		if (currentUserIsOwner) {
			goToAppointmentManagementMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					Utils.switchToFrame(parentFrame, new AppointmentManagementPage());
				}
			});
			goToServicesMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					Utils.switchToFrame(parentFrame, new ServicesPage());
				}
			});
		} else { 
			goToAppointmentsMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					Utils.switchToFrame(parentFrame, new AppointmentsPage());
				}
			});
		}
		refreshMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshMenuItemActionPerformed((java.util.EventObject) evt);
			}
		});

		// brand menu
		// brand menu item
		brandMenuItem.addMenuListener(new javax.swing.event.MenuListener(){
	        @Override
	        public void menuSelected(javax.swing.event.MenuEvent evt){
	        	refreshMenuItemActionPerformed((java.util.EventObject) evt);
	        }

	        @Override
	        public void menuCanceled(javax.swing.event.MenuEvent e) {}

	        @Override
	        public void menuDeselected(javax.swing.event.MenuEvent e) {}
	    });
		
		// account menu
		// account menu items
		logoutMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				logoutMenuItemActionPerformed(evt);
			}
		});
		goToAccountSettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				parentFrame.setEnabled(false);
				Utils.switchToFrame(parentFrame, new AccountSettingsPage());
			}
		});
		if (isNavigable) {
			add(navigationMenu);
		} else {
			add(brandMenuItem);
		}
		add(Box.createHorizontalGlue()); // make account menu right-aligned
		add(accountMenu);
	}

	private void refreshData() {
		if (activePageName != null) {
			setActivePage(activePageName);
		}
	}

	private void setActivePage(String targetPageName) {
		if (targetPageName == null) {
			throw new IllegalArgumentException("Target page name cannot be null");
		}
		
		JMenuItem activePageMenuItem = navigationMenuItemsByPageName.get(activePageName);
		if (activePageMenuItem != null) {
			activePageMenuItem.setEnabled(true);
		}

		JMenuItem targetPageMenuItem = navigationMenuItemsByPageName.get(targetPageName);
		if (targetPageMenuItem == null) {
			if (targetPageName.equals("Account Settings")) {
				targetPageMenuItem = goToAccountSettingsMenuItem;
			} else {
				throw new IllegalArgumentException("Page with name '" + targetPageName + "' is unknown");
			}
		}

		targetPageMenuItem.setEnabled(false);
		activePageName = targetPageName;
	}

	private void clickMenuItemByName(String targetPageName) {
		if (targetPageName == null) {
			throw new IllegalArgumentException("Target page name cannot be null");
		}
		
		setActivePage(targetPageName);

		JMenuItem targetPageMenuItem = navigationMenuItemsByPageName.get(targetPageName);
		if (targetPageMenuItem == null) {
			if (targetPageName.equals("Account Settings")) {
				targetPageMenuItem = goToAccountSettingsMenuItem;
			} else {
				throw new IllegalStateException("Menu item with name '" + targetPageName + "' is unknown");
			}
		}
		
		targetPageMenuItem.setEnabled(true);
		targetPageMenuItem.doClick();
		targetPageMenuItem.setEnabled(false);
	}

	private void logoutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			FlexiBookController.logout();
		} catch (InvalidInputException e) {
			JOptionPane.showMessageDialog(parentFrame, e.getMessage(), "Unable to logout", JOptionPane.ERROR_MESSAGE);
		}
		Utils.switchToFrame(parentFrame, new LandingPage());
		for (JFrame detachedPage : FlexiBookApplication.getDetachedPages()) {
			detachedPage.dispose();
		}
		FlexiBookApplication.clearDetachedPages();
	}
	
	private void refreshMenuItemActionPerformed(EventObject eventObject) {
		clickMenuItemByName(activePageName);
		parentFrame.dispose();
	}
}
