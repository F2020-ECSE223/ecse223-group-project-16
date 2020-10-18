package ca.mcgill.ecse.flexibook.controller;

import java.util.List;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
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
	
	private static void deleteAppointment(Appointment appointment) {
		appointment.getTimeSlot().delete();
		appointment.delete();
	}
	
	private static void deleteAllCustomerAppointments(Customer customer) {
		for (Appointment appointment : customer.getAppointments()) {
			deleteAppointment(appointment);
		}
	}
	
	public static void logout() throws InvalidInputException {
		if (FlexiBookApplication.getCurrentUser() == null ) {
			throw new InvalidInputException ("The user is already logged out");
		}
		FlexiBookApplication.unsetCurrentUser();
	}
	
	/**
	 * @author sarah
	 * @category Login/Logout
	 * 
	 * @param username of the User account being logged in 
	 * @param password of the User account being logged in
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
	
	public static List<TimeSlot> viewAppointmentCalendar (String username, String day) throws InvalidInputException {
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
		
		
		
		for (Appointment a : daysAppointments) {
				TimeSlot aptTS = a.getTimeSlot();
				BookableService aptBService = a.getBookableService();
				
				if (aptBService instanceof Service) {
					firstTS = new TimeSlot(aptTS.getStartDate(), aptTS.getStartTime(), aptTS.getEndDate(), 
										   addMinToTime(aptTS.getStartTime(), ((Service) aptBService).getDowntimeStart()), flexiBook);
					secondTS = new TimeSlot(aptTS.getStartDate(), 
							                addMinToTime(aptTS.getStartTime(), ((Service) aptBService).getDowntimeStart() + ((Service) aptBService).getDowntimeDuration()),
							                aptTS.getEndDate(), aptTS.getEndTime(), flexiBook);
				
					busyTSlots.add(firstTS);
					busyTSlots.add(secondTS);
				}
				else {
					
					Time lastServiceEndTime = aptTS.getStartTime();
					for (ComboItem c : ((ServiceCombo) aptBService).getServices()) {
						Service service = c.getService();
						
						firstTS = new TimeSlot(aptTS.getStartDate(), addMinToTime(addMinToTime(lastServiceEndTime, service.getDowntimeStart()), service.getDowntimeDuration()), 
								aptTS.getEndDate(), addMinToTime(lastServiceEndTime, service.getDowntimeStart()), flexiBook);
						
						secondTS = new TimeSlot(aptTS.getStartDate(), lastServiceEndTime, 
								aptTS.getEndDate(), addMinToTime(lastServiceEndTime, service.getDuration()), flexiBook);
						
						busyTSlots.add(firstTS);
						busyTSlots.add(secondTS);
						
						lastServiceEndTime = addMinToTime(lastServiceEndTime, service.getDuration());
					}
				
				}
		}
		
		
		return busyTSlots;
	}
	
	private static Time addMinToTime (Time time, int minutes) {
		 LocalTime lTime = time.toLocalTime();
		 lTime.plusMinutes(minutes);
		 
		 return Time.valueOf(lTime);
	}
	
	
	
	private static Boolean isDateValid (String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/DD");
		sdf.setLenient(false);
		
		try {
			sdf.parse(date);
		}
		catch (Exception e) { // ParseException?
			return false;
		}
	
		return true;
	}
	
}
