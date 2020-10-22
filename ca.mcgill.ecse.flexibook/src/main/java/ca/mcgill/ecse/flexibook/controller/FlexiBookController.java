package ca.mcgill.ecse.flexibook.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.util.SystemTime;

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
	public static void logout() {
		FlexiBookApplication.unsetCurrentUser();
	}
	
	/**
	 * @author: Julie
	 */
	public static void setUpBusinessInfo(String name, String address, String phoneNumber, String email) throws InvalidInputException {
		if (FlexiBookApplication.getFlexiBook().getOwner() == null) {
			throw new InvalidInputException("No permission to set up business information");
		}
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
	// SOMETHING WRONG WITH FOR LOOP
	public static void addNewBusinessHour(String day, String startTime, String endTime) throws InvalidInputException {
		if (FlexiBookApplication.getFlexiBook().getOwner() == null) {
			throw new InvalidInputException("No permission to update business information");
		}
		if (Integer.parseInt(startTime.substring(0,2)+startTime.substring(3,5)) >= Integer.parseInt(endTime.substring(0,2)+endTime.substring(3,5))) {
			throw new InvalidInputException("Start time must be before end time");
		}
		for (BusinessHour bh : FlexiBookApplication.getFlexiBook().getBusiness().getBusinessHours()) {
			System.out.print(day);
			if (day.equals(bh.getDayOfWeek().toString())) {
				System.out.print(bh.getDayOfWeek().toString());
				System.out.print("overlap");
				throw new InvalidInputException("The business hours cannot overlap");
			}
		}
		System.out.print("new");
		BusinessHour aNewBusinessHour = new BusinessHour(BusinessHour.DayOfWeek.valueOf(day), 
				Time.valueOf(LocalTime.of(Integer.valueOf(startTime.substring(0,2)), Integer.valueOf(startTime.substring(3,5)))), 
				Time.valueOf(LocalTime.of(Integer.valueOf(endTime.substring(0,2)), Integer.valueOf(endTime.substring(3,5)))), 
				FlexiBookApplication.getFlexiBook());
		FlexiBookApplication.getFlexiBook().getBusiness().addBusinessHour(aNewBusinessHour);
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
	public static void addNewTimeSlot(String vacationOrHoliday, String startDate, String startTime, String endDate, String endTime) throws InvalidInputException {
		if (FlexiBookApplication.getFlexiBook().getOwner() == null) {
			System.out.println("error owner");
			throw new InvalidInputException("No permission to update business information");
		}
		if (Integer.parseInt(startDate.substring(0,4)+startDate.substring(5,7)+startDate.substring(8,10)) >= Integer.parseInt(endDate.substring(0,4)+endDate.substring(5,7)+endDate.substring(8,10))) {
			System.out.println("error start before end");
			throw new InvalidInputException("Start time must be before end time");
		}
		if (Integer.parseInt(startDate.substring(0,4)+startDate.substring(5,7)+startDate.substring(8,10)) < Integer.parseInt(SystemTime.getDate().toString().substring(0,4)+SystemTime.getDate().toString().substring(5,7)+SystemTime.getDate().toString().substring(8,10))) {
			throw new InvalidInputException(String.format("%s cannot start in the past", vacationOrHoliday));
		}
		/*
		if (Integer.parseInt(startDate.substring(0,4)+startDate.substring(5,7)+startDate.substring(8,10)) > 
		Integer.parseInt(SystemTime.getDate().toString().substring(0,4)+SystemTime.getDate().toString().substring(5,7)+SystemTime.getDate().toString().substring(8,10))) {
			if (vacationOrHoliday.equals("holiday")) {
				System.out.println("error past hol");
				throw new InvalidInputException("Holiday cannot start in the past");
			}
			else {
				System.out.println("error past vac");
				throw new InvalidInputException("Vacation cannot start in the past");
			}
		}
		*/
		// there is probably a more efficient way to do this
		// SOMETHING WRONG WITH FOR LOOP
		for (TimeSlot ts : FlexiBookApplication.getFlexiBook().getBusiness().getVacation()) {;
			if (Integer.parseInt(endDate.substring(0,4)+endDate.substring(5,7)+endDate.substring(8,10)) > Integer.parseInt(ts.getStartDate().toString().substring(0,4)+ts.getStartDate().toString().substring(5,7)+ts.getStartDate().toString().substring(8,10)) &&
					Integer.parseInt(startDate.substring(0,4)+startDate.substring(5,7)+startDate.substring(8,10)) < Integer.parseInt(ts.getEndDate().toString().substring(0,4)+ts.getEndDate().toString().substring(5,7)+ts.getEndDate().toString().substring(8,10))) {
				if (vacationOrHoliday.equals("holiday")) {
					System.out.println("error1");
					throw new InvalidInputException("Holiday and vacation times cannot overlap");
				}
				if (vacationOrHoliday.equals("vacation")) {
					System.out.println("error2");
					throw new InvalidInputException("Vacation times cannot overlap");
				}
			}
			if (Integer.parseInt(startDate.substring(0,4)+startDate.substring(5,7)+startDate.substring(8,10)) == Integer.parseInt(ts.getStartDate().toString().substring(0,4)+ts.getStartDate().toString().substring(5,7)+ts.getStartDate().toString().substring(8,10))) {
				if (Integer.parseInt(endTime.substring(0,2)+endTime.substring(3,5)) > Integer.parseInt(ts.getStartTime().toString().substring(0,2)+ts.getStartTime().toString().substring(3,5))) {
					if (vacationOrHoliday.equals("holiday")) {
						System.out.println("error3");
						throw new InvalidInputException("Holiday and vacation times cannot overlap");
					}
					if (vacationOrHoliday.equals("vacation")) {
						System.out.println("error4");
						throw new InvalidInputException("Vacation times cannot overlap");
					}
				}
				if (Integer.parseInt(startTime.substring(0,2)+startTime.substring(3,5)) < Integer.parseInt(ts.getEndTime().toString().substring(0,2)+ts.getEndTime().toString().substring(3,5))) {
					if (vacationOrHoliday.equals("holiday")) {
						System.out.println("error5");
						throw new InvalidInputException("Holiday and vacation times cannot overlap");
					}
					if (vacationOrHoliday.equals("vacation")) {
						System.out.println("error6");
						throw new InvalidInputException("Vacation times cannot overlap");
					}
				}
			}
			if (Integer.parseInt(endDate.substring(0,4)+endDate.substring(5,7)+endDate.substring(8,10)) == Integer.parseInt(ts.getEndDate().toString().substring(0,4)+ts.getEndDate().toString().substring(5,7)+ts.getEndDate().toString().substring(8,10)) ) {
				if (vacationOrHoliday.equals("holiday")) {
					System.out.println("error7");
					throw new InvalidInputException("Holiday and vacation times cannot overlap");
				}
				if (vacationOrHoliday.equals("vacation")) {
					System.out.println("error8");
					throw new InvalidInputException("Vacation times cannot overlap");
				}
			}
		}
		for (TimeSlot ts : FlexiBookApplication.getFlexiBook().getBusiness().getHolidays()) {
			if (Integer.parseInt(endDate.substring(0,4)+endDate.substring(5,7)+endDate.substring(8,10)) > Integer.parseInt(ts.getStartDate().toString().substring(0,4)+ts.getStartDate().toString().substring(5,7)+ts.getStartDate().toString().substring(8,10)) &&
					Integer.parseInt(startDate.substring(0,4)+startDate.substring(5,7)+startDate.substring(8,10)) < Integer.parseInt(ts.getEndDate().toString().substring(0,4)+ts.getEndDate().toString().substring(5,7)+ts.getEndDate().toString().substring(8,10))) {
				if (vacationOrHoliday.equals("holiday")) {
					throw new InvalidInputException("Holiday times cannot overlap");
				}
				if (vacationOrHoliday.equals("vacation")) {
					throw new InvalidInputException("Holiday and vacation times cannot overlap");
				}
			}
			if (Integer.parseInt(startDate.substring(0,4)+startDate.substring(5,7)+startDate.substring(8,10)) == Integer.parseInt(ts.getStartDate().toString().substring(0,4)+ts.getStartDate().toString().substring(5,7)+ts.getStartDate().toString().substring(8,10))) {
				if (Integer.parseInt(endTime.substring(0,2)+endTime.substring(3,5)) > Integer.parseInt(ts.getStartTime().toString().substring(0,2)+ts.getStartTime().toString().substring(3,5))) {
					if (vacationOrHoliday.equals("holiday")) {
						throw new InvalidInputException("Holiday times cannot overlap");
					}
					if (vacationOrHoliday.equals("vacation")) {
						throw new InvalidInputException("Holiday and vacation times cannot overlap");
					}
				}
				if (Integer.parseInt(startTime.substring(0,2)+startTime.substring(3,5)) < Integer.parseInt(ts.getEndTime().toString().substring(0,2)+ts.getEndTime().toString().substring(3,5))) {
					if (vacationOrHoliday.equals("holiday")) {
						throw new InvalidInputException("Holiday times cannot overlap");
					}
					if (vacationOrHoliday.equals("vacation")) {
						throw new InvalidInputException("Holiday and vacation times cannot overlap");
					}
				}
			}
			if (Integer.parseInt(endDate.substring(0,4)+endDate.substring(5,7)+endDate.substring(8,10)) == Integer.parseInt(ts.getEndDate().toString().substring(0,4)+ts.getEndDate().toString().substring(5,7)+ts.getEndDate().toString().substring(8,10)) ) {
				if (vacationOrHoliday.equals("holiday")) {
					throw new InvalidInputException("Holiday times cannot overlap");
				}
				if (vacationOrHoliday.equals("vacation")) {
					throw new InvalidInputException("Holiday and vacation times cannot overlap");
				}
			}
		}
		TimeSlot aNewTimeSlot = new TimeSlot(Date.valueOf(LocalDate.of(Integer.parseInt(startDate.substring(0,4)), Month.of(Integer.parseInt(startDate.substring(5,7))), Integer.parseInt(startDate.substring(8,10)))), 
				Time.valueOf(LocalTime.of(Integer.parseInt(startTime.substring(0,2)), Integer.parseInt(startTime.substring(3,5)))), 
				Date.valueOf(LocalDate.of(Integer.parseInt(endDate.substring(0,4)), Month.of(Integer.parseInt(endDate.substring(5,7))), Integer.parseInt(endDate.substring(8,10)))),
				Time.valueOf(LocalTime.of(Integer.parseInt(endTime.substring(0,2)), Integer.parseInt(endTime.substring(3,5)))),
				FlexiBookApplication.getFlexiBook());
		if (vacationOrHoliday.equals("holiday")) {
			FlexiBookApplication.getFlexiBook().getBusiness().addHoliday(aNewTimeSlot);
		}
		else {
			FlexiBookApplication.getFlexiBook().getBusiness().addVacation(aNewTimeSlot);
		}
		System.out.println("passed");
		// still need to check if in the past (need system date/time)
	}
	public static void updateBusinessInfo(String name, String address, String phoneNumber, String email) throws InvalidInputException {
		
	}
}