package ca.mcgill.ecse.flexibook.controller;

import java.util.List;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;

public class FlexiBookController {
	/**
	 * @author louca
	 * @category CRUD Account
	 * 
	 * @param username to give to the created Customer account
	 * @param password to give to the created Customer account
	 * @return the created Customer account
	 * 
	 * @throws IllegalArgumentException if any of the username or password are null
	 * @throws InvalidInputException if:
	 * - any of the username or password are empty or whitespace
	 * - the logged in User account is the Owner account
	 * - the username already exists
	 */
	public static Customer createCustomerAccount(String username, String password) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		
		validateCustomerAccountUsername(username);
		validateUserAccountPassword(password);
		
		User currentUser = FlexiBookApplication.getCurrentUser();
		if (currentUser != null && currentUser == flexiBook.getOwner()) {
			throw new InvalidInputException("You must log out of the owner account before creating a customer account");
		}
		
		try {
			return new Customer(username, password, flexiBook);
		} catch (RuntimeException e) { // conceals other RuntimeExceptions that may occur during Customer construction
			// TODO, check e.message() against Umple message, otherwise bubble the Runtime Exception
			throw new InvalidInputException("The username already exists");
		}
	}

	private static void validateCustomerAccountUsername(String username) throws InvalidInputException {
		if (username == null) {
			throw new IllegalArgumentException("The username cannot be null");
		}
		if (username.trim().isEmpty()) {
			throw new InvalidInputException("The user name cannot be empty"); // space here
		}
	}
	
	private static void validateUserAccountPassword(String password) throws InvalidInputException {
		if (password == null) {
			throw new IllegalArgumentException("The password cannot be null");
		}
		if (password.trim().isEmpty()) {
			throw new InvalidInputException("The password cannot be empty");
		}
	}
	
	/**
	 * @author louca
	 * @category CRUD Account
	 * 
	 * @param username of the User account to retrieve
	 * @return the retrieved User account (null if no User account with that username exists)
	 */
	public static User getUserByUsername(String username) {
		if (username.equals("owner")) {
			return FlexiBookApplication.getFlexiBook().getOwner();
		}
		return getCustomerByUsername(username);
	}
	
	/**
	 * @author louca
	 * 
	 * @param username of the Customer account to retrieve
	 * @return the retrieved Customer account (null if no User account with that username exists)
	 */
	public static Customer getCustomerByUsername(String username) {
		if (username.equals(null)) {
			throw new IllegalArgumentException("The username cannot be null");
		}
		for (Customer customer : FlexiBookApplication.getFlexiBook().getCustomers()) {
			if (customer.getUsername().equals(username)) {
				return customer;
			}
		}
		return null;
	}
	
	/**
	 * @author louca
	 * @category CRUD Account
	 * 
	 * @param user of the User account to update
	 * @param newUsername with which to update the User account
	 * @param newPassword with which to update the User account
	 * @return whether or not the User account was updated
	 * 
	 * @throws InvalidInputException if 
	 * - the newUsername is empty or whitespace
	 * - the newPassword is empty or whitespace
	 * - the newUsername is not available
	 * - the User by the given username is the Owner, and the newUsername is not "owner"
	 */
	public static boolean updateUserAccount(String username, String newUsername, String newPassword) throws InvalidInputException {
		if (username.equals("owner")) {
			if (!newUsername.equals("owner")) {
				throw new InvalidInputException("Changing username of owner is not allowed");
			}
			
			return updateUserAccountPassword(FlexiBookApplication.getFlexiBook().getOwner(), newPassword);
		} else {
			Customer customerToUpdate = getCustomerByUsername(username);
			if (customerToUpdate == null) {
				return false;
			}
			
			return updateCustomerAccountUsername(customerToUpdate, newUsername) 
					&& updateUserAccountPassword(customerToUpdate, newPassword);
		}
	}
	
	/**
	 * @author louca
	 * @category CRUD Account
	 * 
	 * @param customer to update
	 * @param newUsername with which to update the Customer account
	 * @return whether or not the Customer account username was updated
	 * 
	 * @throws InvalidInputException if the newUsername is empty or whitespace, or if the newUsername is not available
	 */
	private static boolean updateCustomerAccountUsername(Customer customer, String newUsername) throws InvalidInputException {
		validateCustomerAccountUsername(newUsername);
		if (customer.setUsername(newUsername)) {
			return true;
		}
		throw new InvalidInputException("Username not available");
	}
	
	/**
	 * @author louca
	 * @category CRUD Account
	 * 
	 * @param user to update
	 * @param newPassword with which to update the User account
	 * @return whether or not the User account password was updated
	 * 
	 * @throws InvalidInputException if the newPassword is empty or whitespace
	 */
	private static boolean updateUserAccountPassword(User user, String newPassword) throws InvalidInputException {
		validateUserAccountPassword(newPassword);
		return user.setPassword(newPassword);
	}
	
	/**
	 * @author louca
	 * @category CRUD Account
	 * 
	 * @param username of the Customer account to delete
	 * @return whether or not the Customer account was deleted
	 * 
	 * @throws InvalidInputException 
	 */
	public static boolean deleteCustomerAccount(String username) throws InvalidInputException {
		Customer customerToDelete = getCustomerByUsername(username);
		
		if (customerToDelete != FlexiBookApplication.getCurrentUser() || username.equals("owner")) {
			throw new InvalidInputException("You do not have permission to delete this account");
		}
		
		if (customerToDelete == null) {
			return false;
		}
		
		logout(); 
		deleteAllCustomerAppointments(customerToDelete);
		customerToDelete.delete();
		return true;
	}
	
	/**
	 * @author theodore
	 * @category CRUD ServiceCombo
	 * 
	 * @param name of the new ServiceCombo
	 * @param array of names of Service s
	 * @param name of main Service
	 * @param array of booleans for whether each service is mandatory
	 * 
	 * @throws InvalidInputException
	 */
	public static void defineServiceCombo(String name, String[] services, String mainService, boolean[] mandatory) throws InvalidInputException {
		checkUser("owner");
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		if (services.length < 2) {
			throw new InvalidInputException("A service Combo must contain at least 2 services");
		}
		if (getService(mainService) == null) { // redundant check here b/c defining w/ a main service that doesnt exist AND is not included in list of combos should throw doesnt exist, not isnt included
			throw new InvalidInputException(String.format("Service %s does not exist", mainService));
		}
		Service[] comboServices = new Service[services.length]; // could use array lists but not very necessary & just adds bloat
		int mainServiceIndex = -1;
		for (int i = 0; i < services.length; i++) {
			Service s = getService(services[i]);
			if (s == null) {
				throw new InvalidInputException(String.format("Service %s does not exist", services[i]));
			} else { 
				comboServices[i] = s;
			}
			if (services[i].equals(mainService)) {
				if (!mandatory[i]) {
					throw new InvalidInputException("Main service must be mandatory");
				}
				mainServiceIndex = i;
			}
		}
		if (mainServiceIndex == -1) {
			throw new InvalidInputException("Main service must be included in the services");
		}
		if (getBookableService(name) != null) {
			throw new InvalidInputException(String.format("Service combo %s already exists", name));
		}
		ServiceCombo newServiceCombo = new ServiceCombo(name, flexiBook);
		newServiceCombo.setName(name);
		for (int i = 0; i < comboServices.length; i++) {
			ComboItem c = new ComboItem(mandatory[i], comboServices[i], newServiceCombo);
			if (i == mainServiceIndex) {
				newServiceCombo.setMainService(c);
			}
		}
	}
	
	/**
	 * @author theodore
	 * @category CRUD ServiceCombo
	 * 
	 * @param ServiceCombo to update
	 * @param name of the updated ServiceCombo
	 * @param array of names of Service s
	 * @param name of main Service
	 * @param array of booleans for whether each service is mandatory
	 * 
	 * @throws InvalidInputException
	 */
	public static void updateServiceCombo(String comboName, String newComboName, String[] services, String mainService, boolean[] mandatory) throws InvalidInputException {
		checkUser("owner");
		if (services.length < 2) {
			throw new InvalidInputException("A service Combo must have at least 2 services");
		}
		if (getService(mainService) == null) {
			throw new InvalidInputException(String.format("Service %s does not exist", mainService));
		}
		Service[] comboServices = new Service[services.length];
		int mainServiceIndex = -1;
		for (int i = 0; i < services.length; i++) {
			Service s = getService(services[i]);
			if (s == null) {
				throw new InvalidInputException(String.format("Service %s does not exist", services[i]));
			} else {
				comboServices[i] = s;
			}
			if (services[i].equals(mainService)) {
				if (!mandatory[i]) {
					throw new InvalidInputException("Main service must be mandatory");
				}
				mainServiceIndex = i;
			}
		}
		if (mainServiceIndex == -1) {
			throw new InvalidInputException("Main service must be included in the services");
		}
		ServiceCombo combo = getServiceCombo(comboName);
		if (combo == null) {
			throw new InvalidInputException(String.format("Service combo %s does not exist", comboName));
		}
		if (!newComboName.equals(comboName) && getBookableService(newComboName) != null) {
			throw new InvalidInputException(String.format("Service combo %s already exists", newComboName));
		}
		combo.setName(newComboName);
		int n = combo.numberOfServices();
		for (int i = 0; i < comboServices.length; i++) {
			ComboItem c = new ComboItem(mandatory[i], comboServices[i], combo);
			if (i == mainServiceIndex) {
				combo.setMainService(c);
			}
		}
		for (int i = 0; i < n; i++) { // delete old services in combo
			combo.getService(0).delete();
		}
	}
	
	/**
	 * @author theodore
	 * @category CRUD ServiceCombo
	 * 
	 * @param name of the ServiceCombo to be deleted
	 * 
	 * @throws InvalidInputException
	 */
	public static void deleteServiceCombo(String name) throws InvalidInputException {
		checkUser("owner");
		ServiceCombo combo = getServiceCombo(name);
		if (combo == null) {
			throw new InvalidInputException(String.format("Service combo %s does not exist", name));
		}
		for (Appointment a : combo.getAppointments()) {
			if (false) { // TODO, check if future apptments with service combo -- waiting for date time utils
				throw new InvalidInputException(String.format("Service combo %s has future appointments", name));
			}
		}
		combo.delete();
	}
	/**
	 * @author theodore
	 * @category CRUD BookableService
	 * 
	 * @param name of the BookableService
	 * @return BookableSeervice with that name, or null if not found
	 */
	private static BookableService getBookableService(String name) {
		for (BookableService b : FlexiBookApplication.getFlexiBook().getBookableServices()) {
			if (b.getName().equals(name)) {
				return b;
			}
		}
		return null;
	}
	/**
	 * @author theodore
	 * @category CRUD Service
	 * 
	 * @param name of the Service
	 * @return Service with that name, or null if not found
	 */
	private static Service getService(String name) {
		for (BookableService b : FlexiBookApplication.getFlexiBook().getBookableServices()) {
			if (b instanceof Service && b.getName().equals(name)) {
				return (Service) b;
			}
		}
		return null;
	}
	/**
	 * @author theodore
	 * @category CRUD ServiceCombo
	 * 
	 * @param name of the ServiceCombo
	 * @return ServiceCombo with that name, or null if not found
	 */
	private static ServiceCombo getServiceCombo(String name) {
		for (BookableService b : FlexiBookApplication.getFlexiBook().getBookableServices()) {
			if (b instanceof ServiceCombo && b.getName().equals(name)) {
				return (ServiceCombo) b;
			}
		}
		return null;
	}
	
	private static void deleteAppointment(Appointment appointment) {
		appointment.getTimeSlot().delete();
		appointment.delete();
	}
	
	private static void deleteAllCustomerAppointments(Customer customer) {
		for (Appointment appointment : customer.getAppointments()) {
			deleteAppointment(appointment);
		}
	}

	private static void checkUser(String username) throws InvalidInputException {
		if (!FlexiBookApplication.getCurrentUser().getUsername().equals(username))
			throw new InvalidInputException("You are not authorized to perform this operation");
	}
		
	/**
	 * @author sarah
	 * @category Login/Logout
	 * 
	 * @param String username of the User account being logged in 
	 * @param String password of the User account being logged in
	 * @throws InvalidInputException 
	 */
	public static User login(String username, String password) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		
		if (username.equals("owner")) {
			if (!flexiBook.hasOwner()) {
				 new Owner("owner", "owner", flexiBook); 
			}
			FlexiBookApplication.setCurrentUser(flexiBook.getOwner());
			return FlexiBookApplication.getCurrentUser();
		}
		else {
			for (User user : flexiBook.getCustomers() ) {
				if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
					FlexiBookApplication.setCurrentUser(user);
					return FlexiBookApplication.getCurrentUser();
				}
			}
		}
		
		throw new InvalidInputException ("Username/password not found");
	}
	
	/**
	 * @author sarah
	 * @category Login/Logout
	 * 
	 * @throws InvalidInputException 
	 */
	public static void logout() throws InvalidInputException {
		if (FlexiBookApplication.getCurrentUser() == null ) {
			throw new InvalidInputException ("The user is already logged out");
		}
		FlexiBookApplication.unsetCurrentUser();
	}
	
	/**
	 * @author sarah
	 * @category View Appointment Calendar
	 * 
	 * @param username username of the User account being logged in 
	 * @param day date requested
	 * @param isWeek if the user is requesting for all the appointments of the week of the day
	 * @throws InvalidInputException 
	 */
	public static List<TimeSlot> viewAppointmentCalendar (String username, String day, boolean isWeek) throws InvalidInputException {
		if (!isDateValid(day)) {
			throw new InvalidInputException (day + " is not a valid date");
		}
		
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		List<TimeSlot> busyTSlots = new ArrayList<TimeSlot>();
		List<Appointment> daysAppointments = new ArrayList<Appointment>();
		TimeSlot firstTS, secondTS;
		
		for (Appointment a : flexiBook.getAppointments()) {
			if (a.getTimeSlot().getStartDate() == Date.valueOf(day)) {
				daysAppointments.add(a);
			}
		}
		
		if (isWeek) {
			for (int i = 1; i <= 7; i++) {
				for (Appointment a : flexiBook.getAppointments()) {
					if (a.getTimeSlot().getStartDate() == addDayToDate(Date.valueOf(day), i)) {
						daysAppointments.add(a);
					}
				}
			}
			
			
		}
		
		
		for (Appointment a : daysAppointments) {
				TimeSlot aptTS = a.getTimeSlot();
				BookableService aptBService = a.getBookableService();
				
				if (aptBService instanceof Service) {
					firstTS = new TimeSlot(
							aptTS.getStartDate(), 
							aptTS.getStartTime(), 
							aptTS.getEndDate(), 
							addMinToTime(aptTS.getStartTime(), ((Service) aptBService).getDowntimeStart()), 
							flexiBook);
					
					secondTS = new TimeSlot(
							aptTS.getStartDate(), 
							addMinToTime(aptTS.getStartTime(), ((Service) aptBService).getDowntimeStart() + ((Service) aptBService).getDowntimeDuration()),
							aptTS.getEndDate(), 
							aptTS.getEndTime(), 
							flexiBook);
				
					busyTSlots.add(firstTS);
					busyTSlots.add(secondTS);
				}
				else { 
					
					Time lastServiceEndTime = aptTS.getStartTime();
					for (ComboItem c : ((ServiceCombo) aptBService).getServices()) {
						Service service = c.getService();
						
						firstTS = new TimeSlot(
								aptTS.getStartDate(), 
								lastServiceEndTime, 
								aptTS.getEndDate(), 
								addMinToTime(lastServiceEndTime, service.getDowntimeStart()),
								flexiBook);
						
						secondTS = new TimeSlot(
								aptTS.getStartDate(), 
								addMinToTime(addMinToTime(lastServiceEndTime, service.getDowntimeStart()), service.getDowntimeDuration()), 
								aptTS.getEndDate(), 
								addMinToTime(lastServiceEndTime, service.getDuration()), 
								flexiBook);
						
						busyTSlots.add(firstTS);
						busyTSlots.add(secondTS);
						
						lastServiceEndTime = addMinToTime(lastServiceEndTime, service.getDuration());
					}
				
				}
		}
		
		
		return busyTSlots;
	}
	
	
	/**
	 * @author sarah
	 * @param time time to add minutes to
	 * @param minutes number of minutes
	 */	
	private static Time addMinToTime (Time time, int minutes) {
		 LocalTime lTime = time.toLocalTime();
		 lTime.plusMinutes(minutes);
		 
		 return Time.valueOf(lTime);
	}
	
	/**
	 * @author sarah
	 * @param date date to check
	 * @param days number of days
	 */	
	private static Date addDayToDate (Date date, int days) {
		 LocalDate lDate = date.toLocalDate();
		 lDate.plusDays(days);
		 
		 return Date.valueOf(lDate);
	}
	
	/**
	 * @author sarah
	 * @param date date to check
	 */	
	private static Boolean isDateValid (String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		
		try {
			sdf.parse(date);
		}
		catch (ParseException e) { 
			return false;
		}
	
		return true;
	}
	
}
