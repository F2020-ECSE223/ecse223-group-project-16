package ca.mcgill.ecse.flexibook.controller;

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
		
		if (getLoggedInUserAccount() == flexiBook.getOwner()) {
			throw new InvalidInputException("You must log out of the owner account before creating a customer account");
		}
		
		try {
			return new Customer(username, password, flexiBook);
		} catch (RuntimeException e) { // conceals other RuntimeExceptions that may occur during Customer construction
			throw new InvalidInputException("The username already exists");
		}
	}

	private static void validateCustomerAccountUsername(String username) throws InvalidInputException {
		if (username == null) {
			throw new IllegalArgumentException("The username cannot be null");
		}
		if (username.trim().isEmpty()) {
			throw new InvalidInputException("The username cannot be empty");
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
	
	private static Customer getCustomerByUsername(String username) {
		for (Customer customer : FlexiBookApplication.getFlexiBook().getCustomers()) {
			if (customer.getUsername().equals(username)) {
				return customer;
			}
		}
		return null;
	}
	
	private static User getLoggedInUserAccount() {
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
		User userToUpdate = getUserByUsername(username);
		
		if (username.equals("owner")) {
			if (!newUsername.equals("owner")) {
				throw new InvalidInputException("Changing username of owner is not allowed");
			}
			return updateUserAccountPassword(userToUpdate, newPassword);
		} else if (userToUpdate instanceof Customer) {
			return updateCustomerAccountUsername((Customer) userToUpdate, newUsername) 
					&& updateUserAccountPassword(userToUpdate, newPassword);
		} else { // userToUpdate == null
			return false;
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
		User loggedInUser = getLoggedInUserAccount();
		
		if (customerToDelete == null) {
			return false;
		}
		
		if (customerToDelete == loggedInUser && !username.equals("owner")) {
			deleteAllCustomerAppointments(customerToDelete);
			customerToDelete.delete();
			return true;
		}
		
		throw new InvalidInputException("You do not have permission to delete this account");
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
}
