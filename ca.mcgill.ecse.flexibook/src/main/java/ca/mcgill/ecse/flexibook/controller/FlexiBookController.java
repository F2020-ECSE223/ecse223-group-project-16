package ca.mcgill.ecse.flexibook.controller;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Optional;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.util.FlexiBookUtil;
import ca.mcgill.ecse.flexibook.util.SystemTime;

public class FlexiBookController {
	/**
	 * Create a new Customer account with the provided username and password.
	 * 
	 * @author louca
	 * @category Feature set 1
	 * 
	 * @param username to give to the created Customer account
	 * @param password to give to the created Customer account
	 * 
	 * @throws IllegalArgumentException if any of the username or password are null
	 * @throws InvalidInputException if:
	 * - any of the username or password are empty or whitespace 
	 * - the logged in User is the Owner account 
	 * - the username  already exists
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
	 * Update the currently logged in User account with the provided new username and password.
	 * 
	 * @author louca
	 * @category Feature set 1
	 * 
	 * @param newUsername with which to update the User account
	 * @param newPassword with which to update the User account
	 * 
	 * @throws InvalidInputException if 
	 * - the newUsername is empty or whitespace
	 * - the newPassword is empty or whitespace
	 * - the newUsername is not available
	 * - the currently logged in User is the Owner and the newUsername is not "owner"
	 */
	public static void updateUserAccount(String newUsername, String newPassword) throws InvalidInputException {
		User currentUser = FlexiBookApplication.getCurrentUser();
		
		if (currentUser instanceof Owner && !newUsername.equals("owner")) {
			throw new InvalidInputException("Changing username of owner is not allowed");
		}
		
		if (currentUser instanceof Customer) {
			Customer currentCustomer = (Customer) currentUser;
			
			validateCustomerAccountUsername(newUsername);
			
			if (!currentCustomer.setUsername(newUsername)) {
				throw new InvalidInputException("Username not available");
			}
		}
		
		validateUserAccountPassword(newPassword);
		currentUser.setPassword(newPassword);
	}

	/**
	 * Delete the Customer account with the provided username.
	 * 
	 * @author louca
	 * @category Feature set 1
	 * 
	 * @param username of the Customer account to delete
	 * 
	 * @throws InvalidInputException if the Customer account to delete is the current user, or if the username is the that of the Owner account
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
		customerToDelete.delete();
	}
	
	/**
	 * Attempts to define a service combo with the provided data.
	 * 
	 * @author theodore
	 * @category Feature set 5
	 *  
	 * @param name name of the new ServiceCombo
	 * @param services array of names of Service s to be included in the combo
	 * @param mainService name of main Service
	 * @param mandatory array of booleans for whether each service is mandatory in an appointment for that service combo
	 * 
	 * @throws InvalidInputException if the service combo already exists, does not contain enough services or contains services which do not exist, or does not contain a mandatory main service
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
	 * Attempts to update a service combo with new parameters
	 * 
	 * @author theodore
	 * @category Feature set 5
	 * 
	 * @param comboName old name of the service combo, to be updated
	 * @param newComboName change the service combo to this name
	 * @param services array of names of Service s
	 * @param mainService name of main Service
	 * @param mandatory array of booleans for whether each service is mandatory
	 * 
	 * @throws InvalidInputException if the service combo already exists, unless the existing combo is the one to be updated, does not contain enough services or contains services which do not exist, or does not contain a mandatory main service
	 */
	public static void updateServiceCombo(String comboName, String newComboName, String[] services, String mainService, boolean[] mandatory) throws InvalidInputException {
		checkUser("owner");
		if (services.length < 2) {
			throw new InvalidInputException("A service Combo must have at least 2 services"); // this validation could be factored out into a method but this error message says "must have" and the other one says "must contain" so it didnt seem worth it.
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
	 * deletes a service combo if it contains no future appointments
	 * 
	 * @author theodore
	 * @category Feature set 5
	 * 
	 * @param name of the ServiceCombo to be deleted
	 * 
	 * @throws InvalidInputException if the service combo does not exist or there are future appointments booked for that service combo
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

	/**
	 * @author He Qian Wang
	 * @category Feature set 6
	 * This method is responsible to make appointments when a service name is given. This method is not responsible for service Combo appointments
	 * that case will be handle by the other method with overloaded params.	
	 *	
	 * @param customerString    	Customer username
	 * @param serviceName 			apointment service name
	 * @param dateString        	start Date of appointment
	 * @param startTimeString 		start Time of appointment
	 * @throws InvalidInputException if appointment cannot be made
	 * 								 if owner tries to make appointment       
	 */

	public static void makeAppointment(String customerString, String dateString, String serviceName, String startTimeString)
		throws InvalidInputException {

		if(customerString.equals("owner")){
			throw new InvalidInputException("An owner cannot make an appointment");
		}
		
		// This portion of the code deals with build the start and total durations of the appointments based on service and downtimes

		Time startTime = null;
		try {
			startTime = FlexiBookUtil.getTimeFromString(startTimeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Time endTimeWithDownTime = startTime;
		Time endTimeWithNoDowntime = startTime;
		
		Optional<BookableService> optionalService = FlexiBookApplication.getFlexiBook().getBookableServices().stream()
			.filter(x -> x.getName().equals(serviceName)).findFirst();
		
		if(!optionalService.isPresent()){
			throw new InvalidInputException(String.format("Service with name %s does not exist", serviceName));
		}

		// calculate the total duration of the appointment
		Service service = (Service) optionalService.get();

		endTimeWithDownTime = new Time(startTime.getTime() 
			+ service.getDuration() * 60 * 1000);
		endTimeWithNoDowntime = new Time(startTime.getTime()  
			+ (service.getDuration() - service.getDowntimeDuration()) * 60 * 1000);

		long totalDuration = (endTimeWithNoDowntime.getTime() - startTime.getTime()) / 60 / 1000;
		
		if(startTime.after(endTimeWithNoDowntime) || startTime.after(endTimeWithDownTime)){
			throw new InvalidInputException("Start date and end date must be the same");
		}

		Date startDate = null;

		try {
			startDate = FlexiBookUtil.getDateFromString(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// start and end date must be same
		if(startDate.before(SystemTime.getDate())){
			throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
				serviceName, dateString, startTimeString));
		} else if(startDate.equals(SystemTime.getDate()) && startTime.before(SystemTime.getTime())){
			throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
				serviceName, dateString, startTimeString));
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		final Time finalStartTime = startTime;
		final Date finalStartDate = startDate;
		final Time finalEndTimeWithDownTime = endTimeWithDownTime;
		final Time finalEndTimeWithNoDownTime = endTimeWithNoDowntime;

		// this sections checks whether the appointment is in business hours
		if(!(FlexiBookApplication.getFlexiBook().getBusiness().getBusinessHours().stream().anyMatch(x -> 
			x.getDayOfWeek().equals(getDayOfWeek(dayOfWeek))
			&& x.getStartTime().before(finalStartTime)
			&& x.getEndTime().after(finalEndTimeWithDownTime)))) {
			throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
				serviceName, dateString, startTimeString));
		}
		
		// this sections checks whether the appointment is in vacation
		if(FlexiBookApplication.getFlexiBook().getBusiness().getVacation().stream().anyMatch(x -> 
			x.getStartDate().after(finalStartDate) && x.getEndDate().before(finalStartDate)
			|| (x.getStartDate().equals(finalStartDate) && x.getStartTime().before(finalEndTimeWithDownTime))
			|| (x.getEndDate().equals(finalStartDate) && x.getEndTime().after(finalStartTime)))) {
			throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
				serviceName, dateString, startTimeString));
		}

		// this sections checks whether the appointment is in holiday
		if(FlexiBookApplication.getFlexiBook().getBusiness().getHolidays().stream().anyMatch(x -> 
			x.getStartDate().after(finalStartDate) && x.getEndDate().before(finalStartDate)
			|| (x.getStartDate().equals(finalStartDate) && x.getStartTime().before(finalEndTimeWithDownTime))
			|| (x.getEndDate().equals(finalStartDate) && x.getEndTime().after(finalStartTime)))) {
			throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
				serviceName, dateString, startTimeString));
		}

		// this section checks if we have conflicting appointments during that time and if we can fit the appointment
		// inside of a downtime
		if(!validateConflictingAppointments(finalStartDate, finalStartTime, finalEndTimeWithDownTime, 
			finalEndTimeWithNoDownTime, totalDuration)) {
			throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
				serviceName, dateString, startTimeString));
		}

		// make the appointment
		FlexiBookApplication.getFlexiBook().addAppointment(
			new Appointment((Customer) FlexiBookApplication.getCurrentUser(), service, 
			new TimeSlot(startDate, startTime, startDate, endTimeWithDownTime, FlexiBookApplication.getFlexiBook()), 
			FlexiBookApplication.getFlexiBook()));
	}

		/**
	 * @author heqianw
	 * @category Feature set 6
	 * 
	 * This method is for the sake of making appointments for Service Combos when service combos are chosen.
	 * 
	 * @param customerString    	Customer username
	 * @param dateString        	start Date of appointment
	 * @param serviceName 			apointment service name
	 * @param optServices        	list of chosen services
	 * @param startTimeString 		start Time of appointment
	 * @throws InvalidInputException if appointment cannot be updated
	 */
	
	public static void makeAppointment(String customerString, String dateString, String serviceName, String optServices, 
		String startTimeString) throws InvalidInputException{

			if(customerString.equals("owner")){
				throw new InvalidInputException("An owner cannot make an appointment");
			}

			// This portion of the code deals with build the start and total durations of the appointments based on service and downtimes
			Time startTime = null;
			try {
				startTime = FlexiBookUtil.getTimeFromString(startTimeString);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Optional<BookableService> possibleService = FlexiBookApplication.getFlexiBook().getBookableServices().stream()
				.filter(x -> x.getName().equals(serviceName)).findFirst();
			
			if(!possibleService.isPresent()){
				throw new InvalidInputException(String.format("Service with name %s does not exist", serviceName));
			}
	
			ServiceCombo serviceCombo = (ServiceCombo) possibleService.get();
	
			int durationWithAllServices = serviceCombo.getMainService().getService().getDuration();
			int lastDowntime = 0;
			
			// build list of chosen items
			String[] optServiceList = optServices.split(",");
			List<ComboItem> chosenItems = new ArrayList<>();
			
			for(String s: optServiceList){
				for(ComboItem ci: serviceCombo.getServices()){
					if(ci.getService().getName().equals(s)){
						chosenItems.add(ci);
						durationWithAllServices += ci.getService().getDuration();
						lastDowntime = ci.getService().getDowntimeDuration();
					}
				}
			}

			// find the total duration of the appointment with and without the last downtime
			Time endTimeWithDownTime = new Time(startTime.getTime() 
				+ durationWithAllServices * 60 * 1000);
			Time endTimeWithNoDowntime = new Time(startTime.getTime()  
				+ (durationWithAllServices - lastDowntime) * 60 * 1000);
	
			long totalDuration = (endTimeWithNoDowntime.getTime() - startTime.getTime()) / 60 / 1000;
			
			if(startTime.after(endTimeWithNoDowntime) || startTime.after(endTimeWithDownTime)){
				throw new InvalidInputException("Start date and end date must be the same");
			}
	
			Date startDate = null;
	
			try {
				startDate = FlexiBookUtil.getDateFromString(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
	
			// if end/start date aren't on the same date, then throw exception
			if(startDate.before(SystemTime.getDate())){
				throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
					serviceName, dateString, startTimeString));
			} else if(startDate.equals(SystemTime.getDate()) && startTime.before(SystemTime.getTime())){
				throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
					serviceName, dateString, startTimeString));
			}
			
			Calendar c = Calendar.getInstance();
			c.setTime(startTime);
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
	
			final Time finalStartTime = startTime;
			final Date finalStartDate = startDate;
			final Time finalEndTimeWithDownTime = endTimeWithDownTime;
			final Time finalEndTimeWithNoDownTime = endTimeWithNoDowntime;
	
			// checks if the appointment is during business hours
			if(!(FlexiBookApplication.getFlexiBook().getBusiness().getBusinessHours().stream().anyMatch(x -> 
				x.getDayOfWeek().equals(getDayOfWeek(dayOfWeek))
				&& x.getStartTime().before(finalStartTime)
				&& x.getEndTime().after(finalEndTimeWithDownTime)))) {
				throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
					serviceName, dateString, startTimeString));
			}
			
			// checks if appointment is during vacation times
			if(FlexiBookApplication.getFlexiBook().getBusiness().getVacation().stream().anyMatch(x -> 
				x.getStartDate().after(finalStartDate) && x.getEndDate().before(finalStartDate)
				|| (x.getStartDate().equals(finalStartDate) && x.getStartTime().before(finalEndTimeWithDownTime))
				|| (x.getEndDate().equals(finalStartDate) && x.getEndTime().after(finalStartTime)))) {
				throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
					serviceName, dateString, startTimeString));
			}
	
			// checks if appointment is during holiday times
			if(FlexiBookApplication.getFlexiBook().getBusiness().getHolidays().stream().anyMatch(x -> 
				x.getStartDate().after(finalStartDate) && x.getEndDate().before(finalStartDate)
				|| (x.getStartDate().equals(finalStartDate) && x.getStartTime().before(finalEndTimeWithDownTime))
				|| (x.getEndDate().equals(finalStartDate) && x.getEndTime().after(finalStartTime)))) {
				throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
					serviceName, dateString, startTimeString));
			}
	
			// check whether we the appointment conflicts with other appointments and if it fits during a downtime
			if(!validateConflictingAppointments(finalStartDate, finalStartTime, finalEndTimeWithDownTime, 
				finalEndTimeWithNoDownTime, totalDuration)) {
				throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
					serviceName, dateString, startTimeString));
			}
			
			// make the appointment
			Appointment a = new Appointment((Customer) FlexiBookApplication.getCurrentUser(), serviceCombo, 
			new TimeSlot(startDate, startTime, startDate, endTimeWithDownTime, FlexiBookApplication.getFlexiBook()), 
				FlexiBookApplication.getFlexiBook());
			
			for(ComboItem ci : chosenItems){
				a.addChosenItem(ci);
			}

			FlexiBookApplication.getFlexiBook().addAppointment(a);
	}
	

	/**
	 * @author heqianw
	 * @category Feature set 6
	 * 
	 * Updating appointment for a new time for a specific user
	 * 
	 * @param customerString    	Customer username
	 * @param serviceName 			apointment service name
	 * @param dateString        	start Date of appointment
	 * @param startTimeString 		start Time of appointment
	 * @param newdateString    		tentative new start Date of appointment
	 * @param newStartTimeString 	tentative new start Time of appointment
	 * @throws InvalidInputException permission issues or new time invalid or attempt to change appointment on same days
	 */
	public static boolean updateAppointment(String customerString, String serviceName, String dateString,
			String startTimeString, String newdateString, String newStartTimeString) throws InvalidInputException {

		if(FlexiBookApplication.getCurrentUser().getUsername().equals("owner")){
			throw new InvalidInputException("Error: An owner cannot update a customer's appointment");
		}

		if(!FlexiBookApplication.getCurrentUser().getUsername().equals(customerString)){
			throw new InvalidInputException("Error: A customer can only update their own appointments");
		}

		Customer c = (Customer) FlexiBookApplication.getCurrentUser();
		// build Date/Time objects
		Date startDate = null;
		Time startTime = null;
		Date oldStartDate = null;
		Time oldStartTime = null;
		try {
			startDate = FlexiBookUtil.getDateFromString(newdateString);
			startTime = FlexiBookUtil.getTimeFromString(newStartTimeString);
			oldStartDate = FlexiBookUtil.getDateFromString(dateString);
			oldStartTime = FlexiBookUtil.getTimeFromString(startTimeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if(startDate.equals(SystemTime.getDate())){
			throw new InvalidInputException("Cannot cancel an appointment on the appointment date");
		}

		// find the appointment
		Appointment foundAppointment = null;

		for(Appointment a : new ArrayList<Appointment>(c.getAppointments())){
			if(a.getBookableService().getName().equals(serviceName) 
				&& a.getTimeSlot().getStartDate().equals(oldStartDate)
				&& a.getTimeSlot().getStartTime().equals(oldStartTime)){
					foundAppointment = a;
			}
		}
		// if it is a service then try to make new appointment (delete old one and try to use makeappointment)
		// if that fails, reinstate old one
		if(foundAppointment.getBookableService() instanceof Service){
			Service s = (Service) foundAppointment.getBookableService();
			Date oldDateStart = foundAppointment.getTimeSlot().getStartDate();
			Time oldTimeStart = foundAppointment.getTimeSlot().getStartTime();
			Time oldEndTimeWithDownTime = foundAppointment.getTimeSlot().getEndTime();
			
			foundAppointment.delete();

			try{
				makeAppointment(c.getUsername(), startDate.toString(), s.getName(), startTime.toString());
				return true;
			}
			catch(InvalidInputException e){
				FlexiBookApplication.getFlexiBook().addAppointment(
					new Appointment((Customer) FlexiBookApplication.getCurrentUser(), s, 
					new TimeSlot(oldDateStart, oldTimeStart, oldDateStart, oldEndTimeWithDownTime, FlexiBookApplication.getFlexiBook()), 
					FlexiBookApplication.getFlexiBook()));
				throw new InvalidInputException(e.getMessage());
			}
		// if it is a service combo then find total time first and attempt to update (delete old one and try to use makeappointment)
		// if that fails, reinstate old one
		} else if(foundAppointment.getBookableService() instanceof ServiceCombo){
			ServiceCombo sc = (ServiceCombo) foundAppointment.getBookableService();
			Date oldDateStart = foundAppointment.getTimeSlot().getStartDate();
			Time oldTimeStart = foundAppointment.getTimeSlot().getStartTime();
			Time oldEndTimeWithDownTime = foundAppointment.getTimeSlot().getEndTime();
			List<ComboItem> listCI = foundAppointment.getChosenItems();
			StringBuilder sb = new StringBuilder();

			for(ComboItem ci : listCI){
				sb.append(ci.getService().getName());
				sb.append(",");
			}

			foundAppointment.delete();
			try{
				makeAppointment(c.getUsername(), startDate.toString(), sc.getName(), sb.substring(0, sb.length() - 1), startTime.toString());
				return true;
			}
			catch(InvalidInputException e){
				Appointment a = new Appointment((Customer) FlexiBookApplication.getCurrentUser(), sc, 
					new TimeSlot(oldDateStart, oldTimeStart, oldDateStart, oldEndTimeWithDownTime, FlexiBookApplication.getFlexiBook()), 
					FlexiBookApplication.getFlexiBook());
				
				for(ComboItem ci : listCI){
					a.addChosenItem(ci);
				}

				FlexiBookApplication.getFlexiBook().addAppointment(a);

				throw new InvalidInputException(e.getMessage());
			}
		}
		return false;
	}
	
	/**
	 * @author heqianw
	 * @category Feature set 6
	 * 
	 * This method is responsible to update a specific appointment by adding or removing a combo item from the appointment
	 * 
	 * @param customerString    Customer username
	 * @param isAdd    			adding or removing comboItem
	 * @param comboItemName 	comboitem name to add/remove
	 * @param serviceName 		apointment service name
	 * @param dateString        start Date of appointment
	 * @param startTimeString 	start Time of appointment
	 * 
	 * @throws InvalidInputException if owner tries to update appointment, other customer, invalid timeslot
	 */
	public static boolean updateAppointment(String customerString, boolean isAdd, String comboItemName, String serviceName, 
		String dateString, String startTimeString) throws InvalidInputException{

		if(FlexiBookApplication.getCurrentUser().getUsername().equals("owner")){
			throw new InvalidInputException("Error: An owner cannot update a customer's appointment");
		}

		if(!FlexiBookApplication.getCurrentUser().getUsername().equals(customerString)){
			throw new InvalidInputException("Error: A customer can only update their own appointments");
		}

		// build Date Time objects
		Date startDate = null;
		Time startTime = null;
		try {
			startDate = FlexiBookUtil.getDateFromString(dateString);
			startTime = FlexiBookUtil.getTimeFromString(startTimeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// find the appointment for that user
		Customer c = (Customer) FlexiBookApplication.getCurrentUser();
		Appointment foundAppointment = null;
		for(Appointment a : new ArrayList<Appointment>(c.getAppointments())){
			if(a.getBookableService().getName().equals(serviceName) 
				&& a.getTimeSlot().getStartDate().equals(startDate)
				&& a.getTimeSlot().getStartTime().equals(startTime)){
					foundAppointment = a;
			}
		}

		ServiceCombo sc = (ServiceCombo) foundAppointment.getBookableService();
		ComboItem cI = sc.getServices().stream().filter(x -> x.getService().getName().equals(comboItemName)).findFirst().get();
		// depending on add or remove. If we delete old one first and tentatively add new appointment with new info
		// if that fails, then we reinstate the old appointment
		if(isAdd){
			Date oldDateStart = foundAppointment.getTimeSlot().getStartDate();
			Time oldTimeStart = foundAppointment.getTimeSlot().getStartTime();
			Time oldEndTimeWithDownTime = foundAppointment.getTimeSlot().getEndTime();
			List<ComboItem> oldListCI = foundAppointment.getChosenItems();
			StringBuilder sb = new StringBuilder();

			List<ComboItem> newListCI = new ArrayList<>(oldListCI);
			newListCI.add(cI);

			for(ComboItem ci : newListCI){
				sb.append(ci.getService().getName());
				sb.append(",");
			}

			foundAppointment.delete();
			try{
				makeAppointment(c.getUsername(), startDate.toString(), sc.getName(), sb.substring(0, sb.length() - 1), startTime.toString());
				return true;
			}
			catch(InvalidInputException e){
				Appointment a = new Appointment((Customer) FlexiBookApplication.getCurrentUser(), sc, 
					new TimeSlot(oldDateStart, oldTimeStart, oldDateStart, oldEndTimeWithDownTime, FlexiBookApplication.getFlexiBook()), 
					FlexiBookApplication.getFlexiBook());
				
				for(ComboItem ci : oldListCI){
					a.addChosenItem(ci);
				}

				FlexiBookApplication.getFlexiBook().addAppointment(a);

				return false;
			}
		} else {
			if(cI.getMandatory()){
				return false;
			}
			if(sc.getMainService().getService().getName().equals(comboItemName)){
				return false;
			}
			List<ComboItem> listCI = new ArrayList<>();

			for(ComboItem ci : foundAppointment.getChosenItems()){
				if(!ci.getService().getName().equals(comboItemName)){
					listCI.add(ci);
				}
			}

			StringBuilder sb = new StringBuilder();
			for(ComboItem ci : listCI){
				sb.append(ci.getService().getName());
				sb.append(",");
			}
			
			foundAppointment.delete();
			try{
				makeAppointment(c.getUsername(), dateString, sc.getName(), sb.substring(0, sb.length() - 1), startTimeString);
				return true;
			}
			catch(InvalidInputException e){
				return false;
			}
		}
	}

	/**
	 * @author heqianw
	 * @category Feature set 6
	 * 
	 * Cancels an appointment for a specific user for a specific service at a specific date and time
	 * 
	 * @param customerString    Customer username
	 * @param serviceName 		apointment service name
	 * @param dateString        start Date of appointment
	 * @param startTimeString 	start Time of appointment
	 * 
	 * @throws InvalidInputException if owner cancels appointment or another customer attemps to cancel the appointment or on appointment date
	 */
	public static void cancelAppointment(String customerString, String serviceName, String dateString,
			String startTimeString) throws InvalidInputException {		
		if(FlexiBookApplication.getCurrentUser().getUsername().equals("owner")){
			throw new InvalidInputException("An owner cannot cancel an appointment");
		}

		if(!FlexiBookApplication.getCurrentUser().getUsername().equals(customerString)){
			throw new InvalidInputException("A customer can only cancel their own appointments");
		}
		// find date and time of appointment
		Customer c = (Customer) FlexiBookApplication.getCurrentUser();

		Date startDate = null;
		Time startTime = null;
		try {
			startDate = FlexiBookUtil.getDateFromString(dateString);
			startTime = FlexiBookUtil.getTimeFromString(startTimeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(startDate.equals(SystemTime.getDate())){
			throw new InvalidInputException("Cannot cancel an appointment on the appointment date");
		}
		// find and delete that appointment
		for(Appointment a : new ArrayList<Appointment>(c.getAppointments())){
			if(a.getBookableService().getName().equals(serviceName) 
				&& a.getTimeSlot().getStartDate().equals(startDate)
				&& a.getTimeSlot().getStartTime().equals(startTime)){
					a.delete();
			}
		}		
	}
	/**
	 * @author heqianw
	 * 
	 * helper method to find conflicting appointments and whether we can fit the appointment during downtimes
	 */
	private static boolean validateConflictingAppointments(Date finalStartDate, Time finalStartTime, Time finalEndTimeWithDownTime, 
		Time finalEndTimeWithNoDownTime, long totalDuration) throws InvalidInputException {
		for(Appointment app: FlexiBookApplication.getFlexiBook().getAppointments()){
			// if same date then we investigate. we start by finding the total duration of the appointment without downtimes
			if(app.getTimeSlot().getStartDate().equals(finalStartDate)){
				int downtime = 0;
				if(app.getBookableService() instanceof Service){
					Service s = (Service) app.getBookableService();
					downtime = s.getDowntimeDuration();
				} else if(app.getBookableService() instanceof ServiceCombo){
					ServiceCombo sc = (ServiceCombo) app.getBookableService();
					if(app.getChosenItems().isEmpty()){
						downtime = sc.getMainService().getService().getDowntimeDuration();
					} else{
						ComboItem lastCI = app.getChosenItem(app.getChosenItems().size() - 1);
						for(int i = sc.getServices().size() -  1; i >= 0; i++){
							if(sc.getService(i).equals(lastCI)){
								break;
							}
							if(sc.getService(i).equals(sc.getMainService())){
								lastCI = sc.getMainService();
								break;
							}
						}
						downtime = lastCI.getService().getDowntimeDuration();
					}
				}
				
				Time appEndNoDowntime = new Time(app.getTimeSlot().getEndTime().getTime() 
				- downtime * 60 * 1000);

				// if this is a potential conflict, then we try to fit the appointment inside of a downtime
				if((app.getTimeSlot().getStartTime().before(finalEndTimeWithNoDownTime)
				&& appEndNoDowntime.after(finalStartTime))){
					boolean fitsInDowntime = false;
					if(app.getBookableService() instanceof Service){
						fitsInDowntime = ((Service) app.getBookableService()).getDowntimeDuration() >= totalDuration;
					} else{
						for(ComboItem ci : app.getChosenItems()){
							if(ci.getService().getDowntimeDuration() >= totalDuration){
								fitsInDowntime = true;
							}
						}
						if(((ServiceCombo) app.getBookableService()).getMainService().getService().getDowntimeDuration() >= totalDuration){
							fitsInDowntime = true;
						}
					}
					return fitsInDowntime;
				}
			}
		}
		return true;
	}

	private static void checkUser(String username) throws InvalidInputException {
		if (!FlexiBookApplication.getCurrentUser().getUsername().equals(username))
			throw new InvalidInputException("You are not authorized to perform this operation");
	}
		
	/**
	 * @author sarah
	 * @category Login/Logout
	 * 
	 * @param username username of the User account being logged in 
	 * @param password password of the User account being logged in
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
	
	/**
	 * @author sarah
	 * @category Login/Logout
	 * 
	 * @throws InvalidInputException 
	 */
	public static void logout() throws InvalidInputException {
		if (FlexiBookApplication.getCurrentUser() == null ) {
			throw new InvalidInputException ("The user is already logged out");
		}
		FlexiBookApplication.unsetCurrentUser();
	}

	/**
	 * @author sarah
	 * @category View Appointment Calendar
	 * 
	 * @param username username of the User account being logged in 
	 * @param startDate start date requested
	 * @param endDate end date requested
	 * @throws InvalidInputException 
	 */
	public static List<TimeSlot> viewAppointmentCalendarBusy (String username, String startDate, String endDate) throws InvalidInputException {
		// Check if dates are valid
		if (!isDateValid(startDate)) {
			throw new InvalidInputException (startDate + " is not a valid date");
		}
		
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		List<BusinessHour> businessHours = flexiBook.getBusiness().getBusinessHours();
		
		List<Appointment> appointmentsToView = new ArrayList<Appointment>();
		List<Date> datesToView = new ArrayList<Date>();
		
		List<TimeSlot> busyTSlots = new ArrayList<TimeSlot>();
		
		// Get list of dates to view appointments for 
		datesToView.add(Date.valueOf(startDate));
		if (endDate != null) {
			Date currentDate = Date.valueOf(startDate);
			while (!isDatesEqual(currentDate, Date.valueOf(endDate))) { 
				currentDate = addDayToDate(currentDate, 1);
				datesToView.add(currentDate);
			}
		}
		
		// Get list of appointments
		for (Date d: datesToView) {
			for (Appointment a : flexiBook.getAppointments()) {
					if (isDatesEqual(a.getTimeSlot().getStartDate(), d)) {
						appointmentsToView.add(a);
					}
					
				
				
			}
		}
		
		// Get busy holidays from business hours
		for (BusinessHour b: businessHours) {
			if (getWeekdayFromDate(Date.valueOf(startDate)) == b.getDayOfWeek()) {
				if (isHoliday(Date.valueOf(startDate))) {
					busyTSlots.add(new TimeSlot(
							Date.valueOf(startDate), 
							b.getStartTime(),
							Date.valueOf(startDate),
							b.getEndTime(),
							flexiBook));
					break;  
				}
			}
		}
		
		if (endDate != null) {
			Date currentDate = Date.valueOf(startDate);
			while (!isDatesEqual(currentDate, Date.valueOf(endDate))) { 
				currentDate = addDayToDate(currentDate, 1);
				for (BusinessHour b: businessHours) {
					if (getWeekdayFromDate(currentDate) == b.getDayOfWeek()) {
						if (isHoliday(currentDate)) {
							busyTSlots.add(new TimeSlot(
									currentDate, 
									b.getStartTime(),
									currentDate,
									b.getEndTime(),
									flexiBook));
							break; 
						}
					}
				}
				
			}
		} 
		
		
		// Find busy time slots
		int newDuration = 0;
		for (Appointment a: appointmentsToView) {
			TimeSlot aptTS = a.getTimeSlot();
			BookableService aptBService = a.getBookableService();
			List<ComboItem> comboItems = ((ServiceCombo) aptBService).getServices();
			int counter = 0;
			
			if (aptBService instanceof ServiceCombo) {
				for (ComboItem c: comboItems) {
					counter++;
					Service service = c.getService();
					if (service.getDowntimeDuration() == 0) {
						if (counter == comboItems.size()) {
							busyTSlots.add(new TimeSlot (aptTS.getStartDate(), aptTS.getStartTime(), aptTS.getEndDate(), aptTS.getEndTime(), flexiBook));
						}
						else {
							newDuration += service.getDuration();
						}
						
					}
					else {
						try {
							busyTSlots.add(new TimeSlot (
									           aptTS.getStartDate(),
									           aptTS.getStartTime(),
									           aptTS.getEndDate(), 
									           addMinToTime(aptTS.getStartTime(), newDuration + service.getDowntimeStart()),
									           flexiBook));
							
							busyTSlots.add(new TimeSlot (
							           aptTS.getStartDate(),
							           addMinToTime(addMinToTime(aptTS.getStartTime(), newDuration + service.getDowntimeStart()), 
							        		   										   service.getDowntimeDuration()),
							           aptTS.getEndDate(), 
							           aptTS.getEndTime(),
							           flexiBook));
						    break;
					
							
						}
						catch (ParseException e) {
							
						}
					}
				}
			} 
			else {
				Service service = (Service) aptBService;
				
				if (service.getDowntimeDuration() == 0) {
					if (counter == comboItems.size()) {
						busyTSlots.add(new TimeSlot (aptTS.getStartDate(), aptTS.getStartTime(), aptTS.getEndDate(), aptTS.getEndTime(), flexiBook));
					}
					else {
						newDuration += service.getDuration();
					}
					
				}
				else {
					try {
						busyTSlots.add(new TimeSlot (
								           aptTS.getStartDate(),
								           aptTS.getStartTime(),
								           aptTS.getEndDate(), 
								           addMinToTime(aptTS.getStartTime(), newDuration + service.getDowntimeStart()),
								           flexiBook));
						
						busyTSlots.add(new TimeSlot (
						           aptTS.getStartDate(),
						           addMinToTime(addMinToTime(aptTS.getStartTime(), newDuration + service.getDowntimeStart()), 
						        		   										   service.getDowntimeDuration()),
						           aptTS.getEndDate(), 
						           aptTS.getEndTime(),
						           flexiBook));
					    break;
				
						
					}
					catch (ParseException e) {
						
					}
				}
				
			}
			
			
			
		}
		
		return busyTSlots;
	}
	
	/**
	 * @author sarah
	 * @category View Appointment Calendar
	 * 
	 * @param username username of the User account being logged in 
	 * @param startDate start date requested
	 * @param endDate end date requested
	 * @throws InvalidInputException 
	 */
	public static List<TimeSlot> viewAppointmentCalendarAvailable (String username, String startDate, String endDate) throws InvalidInputException {
	// Check if dates are valid
	if (!isDateValid(startDate)) {
		throw new InvalidInputException (startDate + " is not a valid date");
	}
	
	FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
	List<BusinessHour> businessHours = flexiBook.getBusiness().getBusinessHours();
	
	
	//List<Appointment> appointmentsToView = new ArrayList<Appointment>();
	//List<Date> datesToView = new ArrayList<Date>();
	
	List<TimeSlot> availableTSlots = new ArrayList<TimeSlot>();
	List<TimeSlot> newAvailableTSlots = new ArrayList<TimeSlot>();
	List<TimeSlot> busyTSlots = viewAppointmentCalendarBusy(username, startDate, endDate);
	
	// Get available time slots from business hours
	for (BusinessHour b: businessHours) {
		if (getWeekdayFromDate(Date.valueOf(startDate)) == b.getDayOfWeek()) {
			if (!isHoliday(Date.valueOf(startDate))) {
				availableTSlots.add(new TimeSlot(
						Date.valueOf(startDate), 
						b.getStartTime(),
						Date.valueOf(startDate),
						b.getEndTime(),
						flexiBook));
				break; 
			}
		}
	}
	
	if (endDate != null) {
		Date currentDate = Date.valueOf(startDate);
		while (!isDatesEqual(currentDate, Date.valueOf(endDate))) { 
			currentDate = addDayToDate(currentDate, 1);
			for (BusinessHour b: businessHours) {
				if (getWeekdayFromDate(currentDate) == b.getDayOfWeek()) {
					if (!isHoliday(currentDate)) {
						availableTSlots.add(new TimeSlot(
								currentDate, 
								b.getStartTime(),
								currentDate,
								b.getEndTime(),
								flexiBook));
						break; 
					}
				}
			}
			
		}
	} 
	

	// Find available time slots
	//int numOfBusyChecked;
	boolean isBusyOnThisDate;
	for (TimeSlot t: availableTSlots) {
		isBusyOnThisDate = false;
		Time startTime = t.getStartTime();
		
		
		for (int i = 0; i < busyTSlots.size(); i++) {
			TimeSlot curTSlot = busyTSlots.get(i);
			
			if (isDatesEqual(curTSlot.getStartDate(), t.getStartDate())) {
				isBusyOnThisDate = true;
				
				
				newAvailableTSlots.add(new TimeSlot (
				           t.getStartDate(),
				           startTime,
				           t.getEndDate(), 
				           curTSlot.getStartTime(),
				           flexiBook));
				
				if (i == busyTSlots.size() - 1 || (!isDatesEqual(busyTSlots.get(i+1).getStartDate(), curTSlot.getStartDate()))) { // lazy evaluation
					newAvailableTSlots.add(new TimeSlot (
					           t.getStartDate(),
					           curTSlot.getEndTime(),
					           t.getEndDate(), 
					           t.getEndTime(),
					           flexiBook));
				}
				
				
				startTime = curTSlot.getEndTime();
				
				
			}
		}
		
		if (!isBusyOnThisDate) {
			newAvailableTSlots.add(t);
		}
	}
	
	// Remove time slots where start and end time are the same
	for (int i = 0; i < newAvailableTSlots.size() - 1; i++) {
		if (isTimesEqual(newAvailableTSlots.get(i).getStartTime(), newAvailableTSlots.get(i).getEndTime())) {
			newAvailableTSlots.remove(i);
		}
		else {
			
		}
	}
	
	return newAvailableTSlots;
} 
	
	
	/**
	 * @author sarah
	 * @param time time to add minutes to
	 * @param minutes number of minutes
	 * @throws ParseException 
	 */	
	private static Time addMinToTime (Time time, int minutes) throws ParseException {
		 String sTime = time.toString();
		 SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		 Calendar c = Calendar.getInstance();
		 
		 c.setTime(df.parse(sTime));
		 c.add(Calendar.MINUTE, minutes);  
		 sTime = df.format(c.getTime());  
		 
		 return FlexiBookUtil.getTimeFromString(sTime);
	}
	
	/**
	 * @author sarah
	 * @param date date to check
	 * @param days number of days
	 */	
	private static Date addDayToDate (Date date, int days) {
		 String sDate = date.toString();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 Calendar c = Calendar.getInstance();
		 try {
			c.setTime(sdf.parse(sDate));
		 } catch (ParseException e) {
			e.printStackTrace();
		 }
		 c.add(Calendar.DATE, days);  // number of days to add
		 sDate = sdf.format(c.getTime());  
		 
		 return Date.valueOf(sDate);
	}
	
	/**
	 * @author sarah
	 * @param date date to check
	 */	
	private static Boolean isDateValid (String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		
		try {
			sdf.parse(date);
		}
		catch (ParseException e) { 
			return false;
		}
	
		return true;
	}
	
	/**
	 * @author sarah
	 * @param date1 first date
	 * @param date2 second date
	 */	
	private static boolean isDatesEqual(Date date1, Date date2) {
	    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	    return fmt.format(date1).equals(fmt.format(date2));
	}
	
	/**
	 * @author sarah
	 * @param time1 first time
	 * @param time2 second time
	 */	
	private static boolean isTimesEqual(Time time1, Time time2) {
	    SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
	    return fmt.format(time1).equals(fmt.format(time2));
	}
	
	/**
	 * @author sarah
	 * @param date date to get weekday of
	 */	
	private static BusinessHour.DayOfWeek getWeekdayFromDate (Date date) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
        String weekday = simpleDateformat.format(date);
        
        switch (weekday) {
        case "Monday":
        	return BusinessHour.DayOfWeek.Monday;
        case "Tuesday":
        	return BusinessHour.DayOfWeek.Tuesday;
        case "Wednesday":
        	return BusinessHour.DayOfWeek.Wednesday;
        case "Thursday":
        	return BusinessHour.DayOfWeek.Thursday;
        case "Friday":
        	return BusinessHour.DayOfWeek.Friday;
        case "Saturday":
        	return BusinessHour.DayOfWeek.Saturday;
        case "Sunday":
        	return BusinessHour.DayOfWeek.Sunday;
        default:
        	return null;
        }
	}
	
	/**
	 * @author sarah
	 * @param date date to check if its a holiday
	 */	
	private static boolean isHoliday (Date date) {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		List<Date> holidayDates = new ArrayList<Date>();
		
		for (TimeSlot x: flexiBook.getBusiness().getHolidays()) {
			if (!holidayDates.contains(x.getStartDate())) {
				holidayDates.add(x.getStartDate());
				if (!isDatesEqual(x.getStartDate(), x.getEndDate())) {
					holidayDates.add(x.getEndDate());
				}
			}
		}
			
		
		for (Date d: holidayDates) {
			if (isDatesEqual(d, date)) {
				return true;
			}
		} 
		
		return false;
		
	}
	
	/** 
	 * @author Julie
	 */
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
	 * @return basic business information as a transfer object
	 * 
	 * @throws InvalidInputException
	 */
	public static TOBusiness viewBusinessInfo() {
		Business business = FlexiBookApplication.getFlexiBook().getBusiness();
		return new TOBusiness(business.getName(), business.getAddress(), business.getPhoneNumber(), business.getEmail());
	}
	/**
	 * @author: Julie
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
	/**
	 * @author Julie
	 * 
	 * @param vacationOrHoliday
	 * @param startDate
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 * 
	 * @throws InvalidInputException
	 */
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
	 * @author heqianw
	 */
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
		return list[day - 1];
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
	
	/**
	 * Get all appointments of the User with the provided username, as a list of transfer objects.
	 * 
	 * @author louca
	 * @category Query methods
	 * 
	 * @param username of the customer for which to retrieve the appointments
	 * 
	 * @return chronologically sorted list of appointments for the given customer as transfer objects
	 * 
	 * @throws InvalidInputException if the customer with the given username does not exist
	 */
	public static List<TOAppointment> getAppointments(String username) throws InvalidInputException {
		Customer customer = getCustomerByUsername(username);
		List<TOAppointment> appointments = new ArrayList<TOAppointment>();
		
		if (customer == null) {
			throw new InvalidInputException("Customer " + username + " does not exist");
		}
		
		for (Appointment a : customer.getAppointments()) {
			TimeSlot t = a.getTimeSlot();
			appointments.add(new TOAppointment(t.getStartDate(), t.getStartTime(), t.getEndDate(), t.getEndTime(), customer.getUsername(), a.getBookableService().getName()));
		}
		
		Collections.sort(appointments, new Comparator<TOAppointment>() {
			@Override
			public int compare(TOAppointment a1, TOAppointment a2) {
				if (a1.getStartDate().equals(a2.getStartDate())) {
		        	if (a1.getStartTime().equals(a2.getStartTime())) {
		        		return 0; // a1 == a2
		        	} else if (a1.getStartTime().before(a2.getStartTime())) {
		        		return -1; // a1 <= a2 
		        	} else {
		        		return 1; // a1 >= a2
		        	}
		        } else if (a1.getStartDate().before(a2.getStartDate())) {
		        	return -1;
		        } else {
		        	return 0;
		        }
			}
			
		});;
		
		return appointments;
	}
	
	/**
	 * Get all bookable services offered, as a list of transfer objects.
	 * 
	 * These transfer objects represent either a service or a service combo with an association to its combo items
	 * 
	 * @author louca
	 * @category Query methods
	 * 
	 * @return alphabetically sorted list of bookable services as transfer objects
	 */
	public static List<TOBookableService> getBookableServices() {
		List<TOBookableService> bookableServices = new ArrayList<TOBookableService>();
		
		for (BookableService bS : FlexiBookApplication.getFlexiBook().getBookableServices()) {
			if (bS instanceof Service) {
				bookableServices.add(new TOService(((Service) bS).getName()));
			} else {
				ServiceCombo sC = (ServiceCombo) bS;
				TOServiceCombo serviceCombo = new TOServiceCombo(sC.getName());
				
				for (ComboItem cI : sC.getServices()) {
					serviceCombo.addService(cI.getService().getName(), cI.isMandatory());
				}
				
				bookableServices.add(serviceCombo);
			}
		}
		
		Collections.sort(bookableServices, new Comparator<TOBookableService>() {
			@Override
			public int compare(TOBookableService bS1,TOBookableService bS2) {
				return bS1.getName().compareTo(bS2.getName());
			}
		});
		
		return bookableServices;
	}
	
	/**
	 * View the appointment calendar between the provided range of dates, or a single day if only the startDate is provided.
	 * 
	 * Returns a calendar transfer object associated with distinct associations to time slot transfer objects for the available and unavailable time slots over the range of dates or single date.
	 * 
	 * @author louca
	 * @category Query methods
	 * 
	 * @param username of the User requesting the appointment calendar
	 * @param startDate of the range of dates over which to view the appointment, or the single date if no endDate is provided
	 * @param endDate of the range of dates over which to view the appointment, or null if the startDate is to be the single date
	 * 
	 * @return a calendar distinctly containing the available and unavailable time slots sorted chronologically as transfer objects
	 * 
	 * @throws InvalidInputException 
	 */
	public static TOCalendar viewAppointmentCalendar(String username, String startDate, String endDate) throws InvalidInputException {
		TOCalendar calendar = new TOCalendar();
		Comparator<TOTimeSlot> timeSlotComparator = new Comparator<TOTimeSlot>() {
		    @Override
		    public int compare(TOTimeSlot tS1, TOTimeSlot tS2) {
		        if (tS1.getStartDate().equals(tS2.getStartDate())) {
		        	if (tS1.getStartTime().equals(tS2.getStartTime())) {
		        		return 0; // tS1 == tS2
		        	} else if (tS1.getStartTime().before(tS2.getStartTime())) {
		        		return -1; // tS1 <= tS2 
		        	} else {
		        		return 1; // tS1 >= tS2
		        	}
		        } else if (tS1.getStartDate().before(tS2.getStartDate())) {
		        	return -1;
		        } else {
		        	return 0;
		        }
		    }
		};
		
		List<TOTimeSlot> availableTimeSlots = new ArrayList<TOTimeSlot>();
		for (TimeSlot tS : viewAppointmentCalendarAvailable(username, startDate, endDate)) {
			availableTimeSlots.add(new TOTimeSlot(tS.getStartDate(), tS.getStartTime(), tS.getEndDate(), tS.getEndTime()));
		}
		
		List<TOTimeSlot> unavailableTimeSlots = new ArrayList<TOTimeSlot>();
		for (TimeSlot tS : viewAppointmentCalendarBusy(username, startDate, endDate)) {
			unavailableTimeSlots.add(new TOTimeSlot(tS.getStartDate(), tS.getStartTime(), tS.getEndDate(), tS.getEndTime()));
		}
		
		Collections.sort(availableTimeSlots, timeSlotComparator);
		Collections.sort(unavailableTimeSlots, timeSlotComparator);
		
		for (TOTimeSlot tS : availableTimeSlots) {
			calendar.addAvailableTimeSlot(tS);
		} 
		
		for (TOTimeSlot tS : unavailableTimeSlots) {
			calendar.addUnavailableTimeSlot(tS);
		}
		
		return calendar;
	}
	
}

