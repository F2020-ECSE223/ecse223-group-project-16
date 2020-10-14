package ca.mcgill.ecse.flexibook.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.util.SystemTime;

public class FlexiBookController {
	/**
	 * @author louca
	 * @category Feature set 1
	 * 
	 * @param username to give to the created Customer account
	 * @param password to give to the created Customer account
	 * 
	 * @throws IllegalArgumentException if any of the username or password are null
	 * @throws InvalidInputException if:
	 * - any of the username or password are empty or whitespace
	 * - the logged in User account is the Owner account
	 * - the username already exists
	 */
	public static void createCustomerAccount(String username, String password) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		
		validateCustomerAccountUsername(username);
		validateUserAccountPassword(password);
		
		User currentUser = FlexiBookApplication.getCurrentUser();
		if (currentUser != null && currentUser == flexiBook.getOwner()) {
			throw new InvalidInputException("You must log out of the owner account before creating a customer account");
		}
		
		try {
			new Customer(username, password, flexiBook);
		} catch (RuntimeException e) {
			if (e.getMessage().startsWith("Cannot create due to duplicate username.")) {
				throw new InvalidInputException("The username already exists");
			}
			throw e;
		}
	}

	/**
	 * @author louca
	 * @category Feature set 1
	 * 
	 * @param username to validate
	 * 
	 * @throws IllegalArgumentException if the username is null
	 * @throws InvalidInputException if the username is empty or whitespace
	 */
	private static void validateCustomerAccountUsername(String username) throws InvalidInputException {
		if (username == null) {
			throw new IllegalArgumentException("The username cannot be null");
		}
		if (username.trim().isEmpty()) {
			throw new InvalidInputException("The user name cannot be empty"); // space here
		}
	}
	
	/**
	 * @author louca
	 * @category Feature set 1
	 * 
	 * @param password to validate
	 * 
	 * @throws IllegalArgumentException if the password is null
	 * @throws InvalidInputException if the password is empty or whitespace
	 */
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
	 * @category Feature set 1
	 * 
	 * @param username of the User account to retrieve
	 * @return the retrieved User account (null if no User account with that username exists)
	 */
	private static User getUserByUsername(String username) {
		if (username.equals("owner")) {
			return FlexiBookApplication.getFlexiBook().getOwner();
		}
		return getCustomerByUsername(username);
	}
	
	/**
	 * @author louca
	 * @category Feature set 1
	 * 
	 * @param username of the Customer account to retrieve
	 * @return the retrieved Customer account (null if no User account with that username exists)
	 * 
	 * @throws IllegalArgumentException if the username is null
	 */
	private static Customer getCustomerByUsername(String username) {
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
	 * @category Feature set 1
	 * 
	 * @param username of the User account to update
	 * @param newUsername with which to update the User account
	 * @param newPassword with which to update the User account
	 * 
	 * @throws InvalidInputException if 
	 * - the newUsername is empty or whitespace
	 * - the newPassword is empty or whitespace
	 * - the newUsername is not available
	 * - the User by the given username is the Owner, and the newUsername is not "owner"
	 */
	public static void updateUserAccount(String username, String newUsername, String newPassword) throws InvalidInputException {
		if (username.equals("owner")) {
			if (!newUsername.equals("owner")) {
				throw new InvalidInputException("Changing username of owner is not allowed");
			}
			
			updateUserAccountPassword(FlexiBookApplication.getFlexiBook().getOwner(), newPassword);
		} else {
			Customer customerToUpdate = getCustomerByUsername(username);
			
			if (customerToUpdate == null) {
				return;
			}
			
			updateCustomerAccountUsername(customerToUpdate, newUsername);
			updateUserAccountPassword(customerToUpdate, newPassword);
		}
	}
	
	/**
	 * @author louca
	 * @category Feature set 1
	 * 
	 * @param customer to update
	 * @param newUsername with which to update the Customer account
	 * 
	 * @throws InvalidInputException if the newUsername is empty or whitespace, or if the newUsername is not available
	 */
	private static void updateCustomerAccountUsername(Customer customer, String newUsername) throws InvalidInputException {
		validateCustomerAccountUsername(newUsername);
		if (!customer.setUsername(newUsername)) {
			throw new InvalidInputException("Username not available");
		}
	}
	
	/**
	 * @author louca
	 * @category Feature set 1
	 * 
	 * @param user to update
	 * @param newPassword with which to update the User account
	 * 
	 * @throws InvalidInputException if the newPassword is empty or whitespace
	 */
	private static void updateUserAccountPassword(User user, String newPassword) throws InvalidInputException {
		validateUserAccountPassword(newPassword);
		user.setPassword(newPassword);
	}
	
	/**
	 * @author louca
	 * @category Feature set 1
	 * 
	 * @param username of the Customer account to delete
	 * @return whether or not the Customer account was deleted
	 * 
	 * @throws InvalidInputException if the Customer account to delete is the current user, or is the username is the Owner account username
	 */
	public static void deleteCustomerAccount(String username) throws InvalidInputException {
		Customer customerToDelete = getCustomerByUsername(username);
		
		if (customerToDelete != FlexiBookApplication.getCurrentUser() || username.equals("owner")) {
			throw new InvalidInputException("You do not have permission to delete this account");
		}
		
		if (customerToDelete == null) {
			return;
		}
		
		logout();
		deleteAllCustomerAppointments(customerToDelete);
		customerToDelete.delete();
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
			if ((a.getTimeSlot().getEndDate()).after(SystemTime.getDate())) { 
				throw new InvalidInputException(String.format("Service combo %s has future appointments", name));
			}
		}
		combo.delete();
	}
	/**
	 * @author theodore
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
	public static void logout() {
		FlexiBookApplication.unsetCurrentUser();
	}
	
	/**
	 * @author Aayush
	 * @category AUD Service
	 * 
	 * @param name to give to the created service
	 * @param duration to set for the created service
	 * @param downtimeDuration to set for the created service
	 * @param downtimeStart to set for the created service
	 * @return the created Service
	 * 
	 * @throws IllegalArgumentException if any of the time values break constraints
	 * @throws IllegalArgumentException if name is null
	 * 
	 */
	
	public static Service addService(String name, String totalDuration, String downtimeStart, String downtimeDuration) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		if (FlexiBookApplication.getCurrentUser() != FlexiBookApplication.getFlexiBook().getOwner()) {
			throw new InvalidInputException("You are not authorized to perform this operation");
		}
		validateDurationTimes(Integer.parseInt(totalDuration),Integer.parseInt(downtimeStart), Integer.parseInt(downtimeDuration));
		
		try {
			return new Service(name, flexiBook, Integer.parseInt(totalDuration), Integer.parseInt(downtimeDuration), Integer.parseInt(downtimeStart));
		}catch(Exception e){
			if (e.getMessage().startsWith("Cannot create due to duplicate name")) {
				throw new InvalidInputException("Service " + name + " already exists");
			}else {
				throw e;
			}
		} 
	}
	/**
	* @author aayush
	*/
	private static void validateDurationTimes(int totalDuration, int downtimeStart, int downtimeDuration) throws InvalidInputException {
		if (totalDuration <= 0) {
			throw new InvalidInputException("Duration must be positive");
		}
		if (downtimeStart != 0 && downtimeDuration == 0) {
			throw new InvalidInputException("Downtime duration must be positive");
		}
		if (downtimeStart > totalDuration) {
			throw new InvalidInputException("Downtime must not start after the end of the service");
		}
		if (downtimeStart + downtimeDuration > totalDuration) {
			throw new InvalidInputException("Downtime must not end after the service");
		}
		if (downtimeStart == 0 && downtimeDuration < 0) {
			throw new InvalidInputException("Downtime duration must be 0"); 
		}
		if (downtimeDuration > 0 && downtimeStart == 0) {
			throw new InvalidInputException("Downtime must not start at the beginning of the service");
		}	
		if (downtimeStart < 0) {
			throw new InvalidInputException("Downtime must not start before the beginning of the service");
		}
	}
	/**
	 * @author Aayush
	 * @category AUD Service
	 * 
	 * @param name of original service
	 * @param new updated name of service
	 * @param duration to set for the updated service
	 * @param downtimeDuration to set for the updated service
	 * @param downtimeStart to set for the updated service
	 * @return void
	 * 
	 * @throws IllegalArgumentException if any of the time values break constraints
	 * @throws IllegalArgumentException if a user other than owner attempts to update a service
	 * 
	 */
	public static void updateService(String ogName, String newName, String totalDuration, String downtimeStart, String downtimeDuration) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
	
		if (FlexiBookApplication.getCurrentUser() != flexiBook.getOwner()) {
			throw new InvalidInputException("You are not authorized to perform this operation");
		}
		validateDurationTimes(Integer.parseInt(totalDuration),Integer.parseInt(downtimeStart), Integer.parseInt(downtimeDuration));
		
		for (BookableService bS1: flexiBook.getBookableServices()) {
			if (bS1.getName().contentEquals(newName)) {
				throw new InvalidInputException("Service " + newName + " already exists");
			}
		}
		
		Service s = null;
		for (BookableService bS: flexiBook.getBookableServices()) {
			if (bS instanceof Service && bS.getName().contentEquals(ogName)) {
				s = (Service) bS;
				s.setName(newName);
				s.setDowntimeDuration(Integer.parseInt(downtimeDuration));
				s.setDuration(Integer.parseInt(totalDuration));
				s.setDowntimeStart(Integer.parseInt(downtimeStart));
			}
		}
	}
	/**
	 * @author Aayush
	 * @category AUD Service
	 * 
	 * @param name of service to delete
	 * @return void
	 * 
	 * @throws IllegalArgumentException if the service to delete has future appointments
	 * @throws IllegalArgumentException if a user other than owner attempts to update a service
	 * 
	 */
	public static void deleteService(String name) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		
		if (FlexiBookApplication.getCurrentUser() != flexiBook.getOwner()) {
			throw new InvalidInputException("You are not authorized to perform this operation");
		}
		
		Service serviceToDelete = null;
		for (BookableService bS: flexiBook.getBookableServices()) {
			if (bS.getName().equals(name) && bS instanceof Service) {
				serviceToDelete = (Service) bS;
			}
		}
		
		int i = 0;
		while (i < flexiBook.getBookableServices().size()) {
			BookableService bS = flexiBook.getBookableService(i);
			if (bS instanceof ServiceCombo) {
				ServiceCombo sC = (ServiceCombo) bS;
				if (sC.getMainService().getService() == serviceToDelete) {
					deleteServiceCombo(bS.getName());
					continue;
				}else {
					int j = 0;
					while (j < sC.numberOfServices()) {
						ComboItem cI = sC.getService(j);
						if (cI.getService() == serviceToDelete) {
							cI.delete();
							break;
						}
						j++;
					}
				}	
			}
			i++;
		}

		for (Appointment a : serviceToDelete.getAppointments()) {
			if ((a.getTimeSlot().getEndDate()).after(SystemTime.getDate())){
				throw new InvalidInputException("The service contains future appointments");
			}	
		}	
		serviceToDelete.delete();
	}
}



