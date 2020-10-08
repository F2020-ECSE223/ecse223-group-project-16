package ca.mcgill.ecse.flexibook.controller;

import java.util.List;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;

public class FlexiBookController {
	/**
	 * @author louca
	 * 
	 * @param username the username that the created Customer account will be given
	 * @param password the password that the created Customer account will be given
	 * @return the created Customer account
	 * 
	 * @throws IllegalArgumentException if any of the username or password are null
	 * @throws InvalidInputException if any of the username or password are empty or whitespace only
	 */
	public static Customer createCustomerAccount(String username, String password) throws InvalidInputException {
		// perform validation according to constraints
		if (validateCustomerAccountUsername(username) && validateUserAccountPassword(password)) { 
			return new Customer(username, password, FlexiBookApplication.getFlexiBook());
		}
		throw new InvalidInputException("Customer account username or password cannot be empty or whitespace only.");
	}

	private static boolean validateCustomerAccountUsername(String username) throws IllegalArgumentException {
		if (username == null) {
			throw new IllegalArgumentException("Customer account username cannot be null.");
		}
		if (username.equals("") || username.trim().isEmpty()) {
			return false;
		}
		return true;
	}
	
	private static boolean validateUserAccountPassword(String password) throws IllegalArgumentException {
		if (password == null) {
			throw new IllegalArgumentException("User account password cannot be null.");
		}
		if (password.equals("") || password.trim().isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * @author louca
	 * 
	 * @param username the username of the Customer account to delete
	 * @return whether or not the Customer account was deleted
	 */
	public static boolean deleteCustomer(String username) {
		List<Customer> allCustomers = FlexiBookApplication.getFlexiBook().getCustomers();
		for (Customer customer : allCustomers) {
			if (customer.getUsername().equals(username)) {
				if (true) {
					deleteAllCustomerAppointments(customer);
					customer.delete();
					return true;
				}
			}
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
	 * @param customer the Customer account to update
	 * @param username the updated username
	 * @return the updated Customer account
	 * 
	 * @throws IllegalArgumentException if any of the username or password are null
	 * @throws InvalidInputException if any of the username or password are invalid
	 */
	public static Customer updateCustomerAccountUsername(Customer customer, String username) throws InvalidInputException {
		if (validateCustomerAccountUsername(username)) {
			customer.setUsername(username);
			return customer;
		}
		throw new InvalidInputException("Customer account username cannot be empty or whitespace only.");
	}
	
	/**
	 * @author louca
	 * 
	 * @param user
	 * @param password
	 * @return
	 * 
	 * @throws IllegalArgumentException
	 * @throws InvalidInputException
	 */
	public static User updateUserAccountPassword(User user, String password) throws InvalidInputException {
		if (validateUserAccountPassword(password)) {
			user.setPassword(password);
			return user;
		}
		throw new InvalidInputException("User account password cannot be empty or whitespace only.");
	}
	
	/**
	 * @author louca
	 * 
	 * @param customer
	 * @param username
	 * @param password
	 * @return
	 * @throws IllegalArgumentException
	 * @throws InvalidInputException
	 */
	public static Customer updateCustomerAccount(Customer customer, String username, String password) throws InvalidInputException {
		updateCustomerAccountUsername(customer, username);
		updateUserAccountPassword(customer, password);
		return customer;
	}
}
