package ca.mcgill.ecse.flexibook.controller;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.util.FlexiBookUtil;
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
	private static void validateBusinessInfo(String name, String address, String phoneNumber, String email) throws InvalidInputException{
		if (name == null || name.isEmpty()) {
			throw new InvalidInputException("Invalid business name");
		}
		if (address == null || address.isEmpty()) {
			throw new InvalidInputException("Invalid address");
		}
		if (phoneNumber == null || phoneNumber.isEmpty()) {
			throw new InvalidInputException("Invalid phone number");
		}
		if (email == null || email.isEmpty() || !email.contains("@") || !email.contains(".") || email.contains(" ")) {
			throw new InvalidInputException("Invalid email");
		}
		// check that @ and . are in the correct order
		String splitEmail[]= email.split("@", 2);
		if (!splitEmail[1].contains(".")) {
			throw new InvalidInputException("Invalid email");
		}
	}
	/**
	 * @author: Julie
	 * 
	 * @param name of business to be created
	 * @param address of business to be created
	 * @param phone number of business to be created
	 * @param email of business to be created
	 * 
	 * @throws InvalidInputException
	 */
	public static void setUpBusinessInfo(String name, String address, String phoneNumber, String email) throws InvalidInputException {
		if (FlexiBookApplication.getFlexiBook().getOwner() != FlexiBookApplication.getCurrentUser()) {
			throw new InvalidInputException("No permission to set up business information");
		}
		validateBusinessInfo(name, address, phoneNumber, email);
	    Business aNewBusiness = new Business(name, address, phoneNumber, email, FlexiBookApplication.getFlexiBook());
		FlexiBookApplication.getFlexiBook().setBusiness(aNewBusiness);
	}
	/**
	 * @author: Julie
	 * 
	 * @param day of business hour to be added
	 * @param start time of business hour to be added
	 * @param end time of business hour to be added
	 * 
	 * @throws InvalidInputException
	 */
	public static void addNewBusinessHour(String day, String startTime, String endTime) throws InvalidInputException {
		if (FlexiBookApplication.getFlexiBook().getOwner() != FlexiBookApplication.getCurrentUser()) {
			throw new InvalidInputException("No permission to update business information");
		}
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
		FlexiBookApplication.getFlexiBook().getBusiness().addBusinessHour(aNewBusinessHour);
	}
	/**
	 * @author: Julie
	 * 
	 * @return basic business information
	 * 
	 * @throws InvalidInputException
	 */
	public static Business viewBusinessInfo() {
		return FlexiBookApplication.getFlexiBook().getBusiness();
	}
	/**
	 * @author: Julie
	 * 
	 * @param time slot type (vacation or holiday)
	 * @param time slot startDate
	 * @param time slot startTime
	 * @param time slot endDate
	 * @param time slot endTime
	 * 
	 * @throws InvalidInputException
	 * @throws ParseException
	 */
	private static void validateTimeSlot(String vacationOrHoliday, String startDate, String startTime, String endDate, String endTime) throws InvalidInputException {
		Date convertedStartDate = null;
		Date convertedEndDate = null;
		try {
			convertedStartDate = FlexiBookUtil.getDateFromString(startDate);
			convertedEndDate = FlexiBookUtil.getDateFromString(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (FlexiBookApplication.getFlexiBook().getOwner() != FlexiBookApplication.getCurrentUser()) {
			throw new InvalidInputException("No permission to update business information");
		}
		if (convertedStartDate.after(convertedEndDate)) {
			throw new InvalidInputException("Start time must be before end time");
		}
	}
	public static void addNewTimeSlot(String vacationOrHoliday, String startDate, String startTime, String endDate, String endTime) throws InvalidInputException {
		Date convertedStartDate = null;
		Time convertedStartTime = null;
		Date convertedEndDate = null;
		Time convertedEndTime = null;
		try {
			convertedStartDate = FlexiBookUtil.getDateFromString(startDate);
			convertedStartTime = FlexiBookUtil.getTimeFromString(startTime);
			convertedEndDate = FlexiBookUtil.getDateFromString(endDate);
			convertedEndTime = FlexiBookUtil.getTimeFromString(endTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (convertedStartDate.before(SystemTime.getDate())) {
			if (vacationOrHoliday.equals("vacation")) {
				throw new InvalidInputException("Vacation cannot start in the past");
			} else {
				throw new InvalidInputException("Holiday cannot start in the past");
			}
		}
		validateTimeSlot(vacationOrHoliday, startDate, startTime, endDate, endTime);
		for (TimeSlot ts : FlexiBookApplication.getFlexiBook().getBusiness().getVacation()) {
			if ((convertedEndDate.after(ts.getStartDate()) && convertedStartDate.before(ts.getEndDate())) ||
				(convertedEndDate.equals(ts.getStartDate()) && convertedEndTime.after(ts.getStartTime())) ||
				(convertedStartDate.equals(ts.getEndDate()) && convertedStartTime.before(ts.getEndTime()))) {
				if (vacationOrHoliday.equals("vacation")) {
					throw new InvalidInputException("Vacation times cannot overlap");
				} else {
					throw new InvalidInputException("Holiday and vacation times cannot overlap");
				}
			}
		}
		for (TimeSlot ts : FlexiBookApplication.getFlexiBook().getBusiness().getHolidays()) {
			if ((convertedEndDate.after(ts.getStartDate()) && convertedStartDate.before(ts.getEndDate())) ||
				(convertedStartDate.equals(ts.getStartDate()) && convertedStartTime.before(ts.getEndTime())) ||
				(convertedEndDate.equals(ts.getEndDate()) && convertedEndTime.after(ts.getStartDate()))) {
				if (vacationOrHoliday.equals("holiday")) {
					throw new InvalidInputException("Holiday times cannot overlap");
				} else {
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
	}
	/**
	 * @author Julie
	 * 
	 * @param business name to be updated
	 * @param business address to be updated
	 * @param business phoneNumber to be updated
	 * @param business email to be updated
	 * 
	 * @throws InvalidInputException
	 */
	public static void updateBusinessInfo(String name, String address, String phoneNumber, String email) throws InvalidInputException {
		if (FlexiBookApplication.getFlexiBook().getOwner() != FlexiBookApplication.getCurrentUser()) {
			throw new InvalidInputException("No permission to update business information");
		}
		validateBusinessInfo(name, address, phoneNumber, email);
		FlexiBookApplication.getFlexiBook().getBusiness().delete();
	    Business aNewBusiness = new Business(name, address, phoneNumber, email, FlexiBookApplication.getFlexiBook());
		FlexiBookApplication.getFlexiBook().setBusiness(aNewBusiness);
	}
	/**
	 * @author Julie
	 * 
	 * @param previous day of a business hour
	 * @param previous startTime of a business hour
	 * @param new day of a business hour
	 * @param new startTime of a business hour
	 * @param new endTime of a business hour
	 * 
	 * @throws InvalidInputException
	 * @throws ParseException
	 */
	public static void updateBusinessHour(String prevDay, String prevStartTime, String newDay, String newStartTime, String newEndTime) throws InvalidInputException {
		if (FlexiBookApplication.getFlexiBook().getOwner() != FlexiBookApplication.getCurrentUser()) {
			throw new InvalidInputException("No permission to update business information");
		}
		try {
			if (FlexiBookUtil.getTimeFromString(newStartTime).after(FlexiBookUtil.getTimeFromString(newEndTime)) ||
					FlexiBookUtil.getTimeFromString(newStartTime).equals(FlexiBookUtil.getTimeFromString(newEndTime))) {
				throw new InvalidInputException("Start time must be before end time");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!prevDay.equals(newDay)) {
			throw new InvalidInputException("The business hours cannot overlap");
		}
		for (BusinessHour bh : new ArrayList <> (FlexiBookApplication.getFlexiBook().getBusiness().getBusinessHours())) {
			if (newDay.equals(bh.getDayOfWeek().toString())) {
				FlexiBookApplication.getFlexiBook().getBusiness().removeBusinessHour(bh);
			}
		}
		BusinessHour aNewBusinessHour = new BusinessHour(BusinessHour.DayOfWeek.valueOf(newDay), 
				Time.valueOf(LocalTime.of(Integer.valueOf(newStartTime.substring(0,2)), Integer.valueOf(newStartTime.substring(3,5)))), 
				Time.valueOf(LocalTime.of(Integer.valueOf(newEndTime.substring(0,2)), Integer.valueOf(newEndTime.substring(3,5)))), 
				FlexiBookApplication.getFlexiBook());
		FlexiBookApplication.getFlexiBook().getBusiness().addBusinessHour(aNewBusinessHour);
	}
	/**
	 * @author Julie
	 * 
	 * @param day of business hour to be deleted
	 * @param startTime of business hour to be deleted
	 * 
	 * @throws InvalidInputException
	 */
	public static void removeBusinessHour(String day, String startTime) throws InvalidInputException {
		Time startTime2 = null;
		try {
			startTime2 = FlexiBookUtil.getTimeFromString(startTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (FlexiBookApplication.getFlexiBook().getOwner() != FlexiBookApplication.getCurrentUser()) {
			throw new InvalidInputException("No permission to update business information");
		}
		for (BusinessHour bh : new ArrayList <> (FlexiBookApplication.getFlexiBook().getBusiness().getBusinessHours())) {
			if (day.equals(bh.getDayOfWeek().toString()) && startTime2.equals(bh.getStartTime())) {
				FlexiBookApplication.getFlexiBook().getBusiness().removeBusinessHour(bh);
			}
		}
	}
	/**
	 * @author Julie
	 * 
	 * @param time slot type (vacation or holiday)
	 * @param previous startDate of time slot
	 * @param previous startTime of the time slot
	 * @param new startDate of the time slot
	 * @param new startTime of the time slot
	 * @param new endDate of the time slot
	 * @param new endTime of the time slot
	 * 
	 * @throws InvalidInputException
	 * @throws ParseException
	 */
	public static void updateTimeSlot(String vacationOrHoliday, String prevStartDate, String prevStartTime, String newStartDate, String newStartTime, String newEndDate, String newEndTime) throws InvalidInputException {
		Date convertedNewStartDate = null;
		Time convertedNewStartTime = null;
		Date convertedNewEndDate = null;
		Time convertedNewEndTime = null;
		try {
			convertedNewStartDate = FlexiBookUtil.getDateFromString(newStartDate);
			convertedNewStartTime = FlexiBookUtil.getTimeFromString(newStartTime);
			convertedNewEndDate = FlexiBookUtil.getDateFromString(newEndDate);
			convertedNewEndTime = FlexiBookUtil.getTimeFromString(newEndTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (convertedNewStartDate.before(SystemTime.getDate())) {
			if (vacationOrHoliday.equals("vacation")) {
				throw new InvalidInputException("Vacation cannot start in the past");
			} else {
				throw new InvalidInputException("Holiday cannot be in the past");
			}
		}
		validateTimeSlot(vacationOrHoliday, newStartDate, newStartTime, newEndDate, newEndTime);
		for (TimeSlot ts : FlexiBookApplication.getFlexiBook().getBusiness().getHolidays()) {
			if ((convertedNewEndDate.after(ts.getStartDate()) && convertedNewStartDate.before(ts.getEndDate())) ||
				(convertedNewStartDate.equals(ts.getStartDate()) && convertedNewStartTime.before(ts.getEndTime())) ||
				(convertedNewEndDate.equals(ts.getEndDate()) && convertedNewEndTime.after(ts.getStartTime())) ) {
				throw new InvalidInputException("Holiday and vacation times cannot overlap");
			}
		}
		for (TimeSlot ts : FlexiBookApplication.getFlexiBook().getBusiness().getVacation()) {
			if ((convertedNewEndDate.after(ts.getStartDate()) && convertedNewStartDate.before(ts.getEndDate())) ||
				(convertedNewStartDate.equals(ts.getStartDate()) && convertedNewStartTime.before(ts.getEndTime())) ||
				(convertedNewEndDate.equals(ts.getEndDate()) && convertedNewEndTime.after(ts.getStartTime()))) {
				throw new InvalidInputException("Holiday and vacation times cannot overlap");
			}
		}
		if (vacationOrHoliday.equals("holiday")) {
			for(TimeSlot ts: new ArrayList <> (FlexiBookApplication.getFlexiBook().getBusiness().getHolidays())) {
				if (prevStartDate.equals(ts.getStartDate().toString())) {
					FlexiBookApplication.getFlexiBook().getBusiness().removeHoliday(ts);
				}
			}
			TimeSlot aNewTimeSlot = new TimeSlot(Date.valueOf(LocalDate.of(Integer.parseInt(newStartDate.substring(0,4)), Month.of(Integer.parseInt(newStartDate.substring(5,7))), Integer.parseInt(newStartDate.substring(8,10)))), 
					Time.valueOf(LocalTime.of(Integer.parseInt(newStartTime.substring(0,2)), Integer.parseInt(newStartTime.substring(3,5)))), 
					Date.valueOf(LocalDate.of(Integer.parseInt(newEndDate.substring(0,4)), Month.of(Integer.parseInt(newEndDate.substring(5,7))), Integer.parseInt(newEndDate.substring(8,10)))),
					Time.valueOf(LocalTime.of(Integer.parseInt(newEndTime.substring(0,2)), Integer.parseInt(newEndTime.substring(3,5)))),
					FlexiBookApplication.getFlexiBook());
			FlexiBookApplication.getFlexiBook().getBusiness().addHoliday(aNewTimeSlot);
		}
		else {
			for(TimeSlot ts: new ArrayList <> (FlexiBookApplication.getFlexiBook().getBusiness().getVacation())) {
				if (prevStartDate.equals(ts.getStartDate().toString())) {
					FlexiBookApplication.getFlexiBook().getBusiness().removeVacation(ts);
				}
			}
			TimeSlot aNewTimeSlot = new TimeSlot(Date.valueOf(LocalDate.of(Integer.parseInt(newStartDate.substring(0,4)), Month.of(Integer.parseInt(newStartDate.substring(5,7))), Integer.parseInt(newStartDate.substring(8,10)))), 
					Time.valueOf(LocalTime.of(Integer.parseInt(newStartTime.substring(0,2)), Integer.parseInt(newStartTime.substring(3,5)))), 
					Date.valueOf(LocalDate.of(Integer.parseInt(newEndDate.substring(0,4)), Month.of(Integer.parseInt(newEndDate.substring(5,7))), Integer.parseInt(newEndDate.substring(8,10)))),
					Time.valueOf(LocalTime.of(Integer.parseInt(newEndTime.substring(0,2)), Integer.parseInt(newEndTime.substring(3,5)))),
					FlexiBookApplication.getFlexiBook());
			FlexiBookApplication.getFlexiBook().getBusiness().addVacation(aNewTimeSlot);
		}
	}
	/**
	 * @author Julie
	 * 
	 * @param time slot type (vacation or holiday)
	 * @param startDate of time slot to be removed
	 * @param startTime of time slot to be removed
	 * @param endDate of time slot to be removed
	 * @param endTIme of time slot to be removed
	 * 
	 * @throws InvalidInputException
	 */
	public static void removeTimeSlot(String vacationOrHoliday, String startDate, String startTime, String endDate, String endTime) throws InvalidInputException {
		if (FlexiBookApplication.getFlexiBook().getOwner() != FlexiBookApplication.getCurrentUser()) {
			throw new InvalidInputException("No permission to update business information");
		}
		if (vacationOrHoliday.equals("holiday")) {
			for(TimeSlot ts: new ArrayList <> (FlexiBookApplication.getFlexiBook().getBusiness().getHolidays())) {	
				if (startDate.equals(ts.getStartDate().toString())) {
					FlexiBookApplication.getFlexiBook().getBusiness().removeHoliday(ts);
				}
			}
		}
		else {
			for(TimeSlot ts: new ArrayList<> (FlexiBookApplication.getFlexiBook().getBusiness().getVacation())) {
				if (startDate.equals(ts.getStartDate().toString())) {
					FlexiBookApplication.getFlexiBook().getBusiness().removeVacation(ts);
				}
			}
		}
	}
	/**
	 * @author Aayush
	 * @category feature set 4
	 * 
	 * @param name to give to the created service
	 * @param duration to set for the created service
	 * @param downtimeDuration to set for the created service
	 * @param downtimeStart to set for the created service
	 * 
	 * @throws InvalidInputException if any of the time values break constraints and if name is null 
	 */
	public static void addService(String name, String totalDuration, String downtimeStart, String downtimeDuration) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		if (FlexiBookApplication.getCurrentUser() != FlexiBookApplication.getFlexiBook().getOwner()) {
			throw new InvalidInputException("You are not authorized to perform this operation");
		}
		validateDurationTimes(Integer.parseInt(totalDuration),Integer.parseInt(downtimeStart), Integer.parseInt(downtimeDuration));
		
		try {
			new Service(name, flexiBook, Integer.parseInt(totalDuration), Integer.parseInt(downtimeDuration), Integer.parseInt(downtimeStart));
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
	 * @category feature set 4
	 * 
	 * @param name of original service
	 * @param new updated name of service
	 * @param duration to set for the updated service
	 * @param downtimeDuration to set for the updated service
	 * @param downtimeStart to set for the updated service
	 * 
	 * @throws InvalidInputException if any of the time values break constraints and if a user other than owner attempts to update a service
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
				break;
			}
		}
	}
	/**
	 * @author Aayush
	 * @category feature set 4
	 * 
	 * @param name of service to delete
	 * 
	 * @throws InvalidInputException if the service to delete has future appointments and if a user other than owner attempts to update a service
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
				break;
			}
		}
		
		for (Appointment a : serviceToDelete.getAppointments()) {
			if ((a.getTimeSlot().getEndDate()).after(SystemTime.getDate())){
				throw new InvalidInputException("The service contains future appointments");
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
		serviceToDelete.delete();
	}
}