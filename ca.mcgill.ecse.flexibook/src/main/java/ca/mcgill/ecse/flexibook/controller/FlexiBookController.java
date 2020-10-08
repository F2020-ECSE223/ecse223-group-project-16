package ca.mcgill.ecse.flexibook.controller;

import java.util.List;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;

public class FlexiBookController {
	/**
	 * @author louca
	 * 
	 * @param username to give to the created Customer account
	 * @param password to give to the created Customer account
	 * @return the created Customer account
	 * 
	 * @throws IllegalArgumentException if any of the username or password are null
	 * @throws InvalidInputException if any of the username or password are empty or whitespace, or if the username already exists
	 */
	public static Customer createCustomerAccount(String username, String password) throws InvalidInputException {
		validateCustomerAccountUsername(username);
		validateUserAccountPassword(password);
		try {
			return new Customer(username, password, FlexiBookApplication.getFlexiBook());
		} catch (RuntimeException e) { // this conceals other RuntimeExceptions that may occur during Customer construction
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
	
	public static Customer getCustomerByUsername(String username) {
		for (Customer customer : FlexiBookApplication.getFlexiBook().getCustomers()) {
			if (customer.getUsername().equals(username)) {
				return customer;
			}
		}
		return null;
	}
	
	public static User getLoggedInUser() {
		return null;
	}
	
	/**
	 * @author louca
	 * 
	 * @param username of the Customer account to delete
	 * @return whether or not the Customer account was deleted
	 */
	public static boolean deleteCustomer(String username) {
		Customer customerToDelete = getCustomerByUsername(username);
		if (customerToDelete != null && customerToDelete == getLoggedInUser()) {
			deleteAllCustomerAppointments(customerToDelete);
			customerToDelete.delete();
			return true;
		}
		return false;
	}
	
	private static void deleteAppointment(Appointment appointment) {
		appointment.getTimeSlot().delete();
		appointment.delete();
	}
	
	private static void deleteAllCustomerAppointments(Customer customer) {
		List<Appointment> allCustomerAppointments = customer.getAppointments();
		for (Appointment appointment : allCustomerAppointments) {
			deleteAppointment(appointment);
		}
	}
	
	/**
	 * @author louca
	 * 
	 * @param customer to update
	 * @param newUsername with which to update the Customer account
	 * @return the updated Customer account
	 * 
	 * @throws InvalidInputException if the newUsername is empty or whitespace, or if the newUsername already exists
	 */
	public static Customer updateCustomerAccountUsername(Customer customer, String newUsername) throws InvalidInputException {
		validateCustomerAccountUsername(newUsername);
		customer.setUsername(newUsername);
		return customer;
	}
	
	/**
	 * @author louca
	 * 
	 * @param user to update
	 * @param newPassword with which to update the User account
	 * @return the updated User account
	 * 
	 * @throws InvalidInputException if the newPassword is empty or whitespace
	 */
	public static User updateUserAccountPassword(User user, String newPassword) throws InvalidInputException {
		validateUserAccountPassword(newPassword);
		user.setPassword(newPassword);
		return user;
	}
	
	/**
	 * @author louca
	 * 
	 * @param customer to update
	 * @param newUsername with which to update the Customer account
	 * @param newPassword with which to update the Customer account
	 * @return
	 * 
	 * @throws InvalidInputException
	 */
	public static Customer updateCustomerAccount(Customer customer, String newUsername, String newPassword) throws InvalidInputException {
		updateCustomerAccountUsername(customer, newUsername);
		updateUserAccountPassword(customer, newPassword);
		return customer;
	}
}
