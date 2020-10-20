package ca.mcgill.ecse.flexibook.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

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
	
	public static void logout() {
		FlexiBookApplication.unsetCurrentUser();
	}
	
	/**
	 * @author: Julie
	 */
	public static void setUpBusinessInfo(String name, String address, String phoneNumber, String email) throws InvalidInputException {
		if (name == null || name.isEmpty()) {
			throw new InvalidInputException("Invalid business name");
		}
		if (address == null || address.isEmpty()) {
			throw new InvalidInputException("Invalid address");
		}
		if (phoneNumber == null || phoneNumber.isEmpty()) {
			throw new InvalidInputException("Invalid phone number");
		}
		if (email == null || email.isEmpty() || !email.contains("@") ||!email.contains(".")) {
			throw new InvalidInputException("Invalid email");
		}
		// check that @ and . are in the correct order
		for (int i = 0 ; i < email.length(); i++) {
			char c = email.charAt(i);
			if (c == '@') {
					if (!email.substring(i,email.length()-1).contains(".")) {
						throw new InvalidInputException("Invalid email");
				}
			}
		}

	    Business aNewBusiness = new Business(name, address, phoneNumber, email, FlexiBookApplication.getFlexiBook());
		FlexiBookApplication.getFlexiBook().setBusiness(aNewBusiness);
	}
	/**
	 * @author: Julie
	 */
	public static void addNewBusinessHour(String day, String startTime, String endTime) throws InvalidInputException {
		if (Integer.parseInt(startTime.substring(0,2)+startTime.substring(3,5)) >= Integer.parseInt(endTime.substring(0,2)+endTime.substring(3,5))) {
			throw new InvalidInputException("Start time must be before end time");
		}
		for (BusinessHour bh : FlexiBookApplication.getFlexiBook().getBusiness().getBusinessHours()) {
			if (day.equals(bh.getDayOfWeek().toString())) {
				throw new InvalidInputException("The business hours cannot overlap");
			}
		}
		BusinessHour aNewBusinessHour = new BusinessHour(BusinessHour.DayOfWeek.valueOf(day), 
				Time.valueOf(LocalTime.of(Integer.valueOf(startTime.substring(0,2)), Integer.valueOf(startTime.substring(3,5)))), 
				Time.valueOf(LocalTime.of(Integer.valueOf(endTime.substring(0,2)), Integer.valueOf(endTime.substring(3,5)))), 
				FlexiBookApplication.getFlexiBook());
	}
	/**
	 * @author: Julie
	 */
	public static void viewBusinessInfo() throws InvalidInputException {
		if (FlexiBookApplication.getFlexiBook().getBusiness() == null) {
			throw new InvalidInputException("No business exists");
		}
	}
	/**
	 * @author: Julie
	 */
	public static void addNewTimeSlot(String vacationOrHoliday, String startDate, String startTime, String endDate, String endTime) {
		if (Integer.parseInt(startTime.substring(0,2)+startTime.substring(3,5)) >= Integer.parseInt(endTime.substring(0,2)+endTime.substring(3,5))) {
			throw new InvalidInputException("Start time must be before end time");
		}
		if (vacationOrHoliday.equals("vacation")) {
			for (TimeSlot ts : FlexiBookApplication.getFlexiBook().getBusiness().getVacation()) {
				if (startDate.equals(ts.getStartDate().toString()) && endDate.equals(ts.getEndDate().toString())) {
					throw new InvalidInputException("Vacation times cannot overlap");
				}
			}
		}
		if (vacationOrHoliday.equals("holiday")) {
			for (TimeSlot ts : FlexiBookApplication.getFlexiBook().getBusiness().getHolidays()) {
				if (startDate.equals(ts.getStartDate().toString()) && endDate.equals(ts.getEndDate().toString())) {
					throw new InvalidInputException("Vacation times cannot overlap");
				}
			}
		}
	}
}