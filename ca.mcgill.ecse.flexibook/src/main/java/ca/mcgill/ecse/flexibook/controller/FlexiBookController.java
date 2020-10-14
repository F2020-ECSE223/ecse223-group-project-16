package ca.mcgill.ecse.flexibook.controller;

import java.sql.Time;
import java.sql.Date;
import java.util.Calendar;

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
	 * @author He Qian Wang
	 * @return
	 * 
	 *         Current as a method that takes in Bookable service, but easily
	 *         changeable to two methods
	 * @throws InvalidUserException
	 */
	public static boolean makeAppointment(Time startTime, Date startDate, BookableService bookableService)
			throws InvalidUserException {

		canUserMakeAppointment();
		
		Time withDownTime = startTime;
		Time noDowntime = startTime;

		if(bookableService instanceof Service){
			Service service = (Service) bookableService;
			withDownTime = new Time(startTime.getTime() + service.getDuration() * 60 * 1000);
			noDowntime = new Time(startTime.getTime() + (service.getDuration() - service.getDowntimeDuration()) * 60 * 1000);
		}
		else if(bookableService instanceof ServiceCombo){
			ServiceCombo serviceCombo = (ServiceCombo) bookableService;
			int totalDuration = serviceCombo.getServices().stream().mapToInt(x -> x.getService().getDuration()).sum();
			int lastDowntime = serviceCombo.getServices().get(serviceCombo.getServices().size() - 1).getService().getDowntimeDuration();
			withDownTime = new Time(startTime.getTime() + totalDuration * 60 * 1000);
			noDowntime = new Time(startTime.getTime() + (totalDuration - lastDowntime) * 60 * 1000);
		}

		Date endDate = new Date(withDownTime.getTime());
		
		final Time endTimeWithDowntime = withDownTime;
		final Time endTimeNoDowntime = noDowntime;
		// make appointments need to account for next available timeslot
		// first -> future date, then make sure it is within the bounds of 
		// available timeslots.
		// 1. future date
		// 2. Within bounds of business hours
		// 3. no conflicts
		// 4. duration is legit
		// 5. account for downtime

		if(!startDate.after(new Date(System.currentTimeMillis()))){
			// do something about cannot schedule in the past
		}

		if(!startDate.equals(endDate)){
			// do something about must have same day appointments
			// 
		}

		Calendar c = Calendar.getInstance();
		c.setTime(startTime);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		if(!FlexiBookApplication.getFlexiBook().getBusiness().getBusinessHours()
			.stream().anyMatch(x -> x.getDayOfWeek().equals(getDayOfWeek(dayOfWeek)) 
				&& x.getStartTime().before(startTime) 
				&& x.getEndTime().after(endTimeWithDowntime)))
		{
			// nothing matches regular opening hours
		}


		if(FlexiBookApplication.getFlexiBook().getBusiness().getVacation()
			.stream().anyMatch(x -> x.getStartDate().equals(startDate) 
				|| (x.getStartDate().before(startDate) && x.getEndDate().after(startDate))))
		{
			// do something about appointment within vacations
		}

		if(FlexiBookApplication.getFlexiBook().getBusiness().getHolidays()
			.stream().anyMatch(x -> x.getStartDate().equals(startDate) 
				|| (x.getStartDate().before(startDate) && x.getEndDate().after(startDate))))
		{
			// do something about appointment within holidays
		}

		if(FlexiBookApplication.getFlexiBook().getAppointments()
			.stream().anyMatch(x -> x.getTimeSlot().getStartDate().equals(startDate) 
				|| x.getTimeSlot().getStartTime().before(endTimeNoDowntime)
				|| x.getTimeSlot().getEndTime().after(startTime)))
		{
			// do something about invalid timeslot
		}

		// Make the appointment
		return true;
	}

	/**
	 * @author He Qian Wang
	 * @return
	 */
	public static boolean updateAppointment(){

		// run someone checks and see if you can change it
		// call make appointment to see if you can make it.


		return true;
	}

	/**
	 * @author He Qian Wang
	 * @return
	 */
	public static boolean cancelAppointment(){
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

	private static void canUserMakeAppointment() throws InvalidUserException {
		if(FlexiBookApplication.getCurrentUser() == null){
			throw new InvalidUserException("An User must be logged in to make an appointment.");
		}
		if(FlexiBookApplication.getCurrentUser().getUsername().equals("owner")){	
			throw new InvalidUserException("The owner may not create an appointment.");
		}
	}

	private static BusinessHour.DayOfWeek getDayOfWeek(int day){
		BusinessHour.DayOfWeek[] list = { 
			BusinessHour.DayOfWeek.Sunday,
			BusinessHour.DayOfWeek.Monday, 
			BusinessHour.DayOfWeek.Tuesday, 
			BusinessHour.DayOfWeek.Wednesday, 
			BusinessHour.DayOfWeek.Thursday, 
			BusinessHour.DayOfWeek.Friday, 
			BusinessHour.DayOfWeek.Saturday, 
		};
		return list[day];
	}
}


// first open the folder then src/test/java/features
// Right click and run as JUnit test

// .feature translated to method in testRunner
// copy paste from generation to Step Generation