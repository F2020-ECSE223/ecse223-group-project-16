package ca.mcgill.ecse.flexibook.controller;

import java.util.List;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;

public class FlexiBookController {
	/**
	 * @author louca
	 * 
	 * @param username of the Customer to create
	 * @param password of the Customer to create
	 * @return the created Customer or null 
	 * 
	 * @throws IllegalArgumentException if any of the username or password are null
	 * @throws InvalidInputException if any of the username or password were invalid
	 */
	public static Customer createCustomerAccount(String username, String password) throws IllegalArgumentException, InvalidInputException {
		// perform validation according to constraints
		if (validateCustomerUsername(username) && validateUserPassword(password)) { 
			return new Customer(username, password, FlexiBookApplication.getFlexiBook());
		}
		throw new InvalidInputException("Could not create Customer account.");
	}
	
	/**
	 * @TODO
	 */
	private static boolean validateCustomerUsername(String username) throws IllegalArgumentException {
		if (username == null) {
			throw new IllegalArgumentException("Customer username cannot be null.");
		}
		return true;
	}
	
	/**
	 * @TODO
	 */
	private static boolean validateUserPassword(String password) throws IllegalArgumentException {
		if (password == null) {
			throw new IllegalArgumentException("User password cannot be null.");
		}
		return true;
	}
	
	/**
	 * @author louca
	 * 
	 * @param username String of the Customer account to delete
	 * @return whether or not the Customer account was deleted
	 */
	public static boolean deleteCustomerAccount(String username) {
		List<Customer> allCustomers = FlexiBookApplication.getFlexiBook().getCustomers();
		for (Customer customer : allCustomers) {
			if (customer.getUsername().equals(username)) {
				deleteAllCustomerAppointments(customer);
				return true;
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
}
