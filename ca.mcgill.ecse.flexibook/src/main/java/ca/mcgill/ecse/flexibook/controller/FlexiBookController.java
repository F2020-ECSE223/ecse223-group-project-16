package ca.mcgill.ecse.flexibook.controller;

import java.sql.Time;
import java.text.ParseException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.util.FlexiBookUtil;
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
	 * @throws InvalidInputException    if: - any of the username or password are
	 *                                  empty or whitespace - the logged in User
	 *                                  account is the Owner account - the username
	 *                                  already exists
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
			// TODO, check e.message() against Umple message, otherwise bubble the Runtime
			// Exception
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
	 * @return the retrieved User account (null if no User account with that
	 *         username exists)
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
	 * @return the retrieved Customer account (null if no User account with that
	 *         username exists)
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
	 * @param user        of the User account to update
	 * @param newUsername with which to update the User account
	 * @param newPassword with which to update the User account
	 * @return whether or not the User account was updated
	 * 
	 * @throws InvalidInputException if - the newUsername is empty or whitespace -
	 *                               the newPassword is empty or whitespace - the
	 *                               newUsername is not available - the User by the
	 *                               given username is the Owner, and the
	 *                               newUsername is not "owner"
	 */
	public static boolean updateUserAccount(String username, String newUsername, String newPassword)
			throws InvalidInputException {
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
	 * @param customer    to update
	 * @param newUsername with which to update the Customer account
	 * @return whether or not the Customer account username was updated
	 * 
	 * @throws InvalidInputException if the newUsername is empty or whitespace, or
	 *                               if the newUsername is not available
	 */
	private static boolean updateCustomerAccountUsername(Customer customer, String newUsername)
			throws InvalidInputException {
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
	 * @param user        to update
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
	 * @throws InvalidInputException
	 * @throws InvalidUserException
	 */

	// handle the service booking
	public static void makeAppointment(String customerString, String dateString, String serviceName, String startTimeString)
		throws InvalidInputException {

		if(customerString.equals("owner")){
			throw new InvalidInputException("An owner cannot make an appointment");
		}
		
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

		if(startDate.before(SystemTime.getDate())){
			throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
				serviceName, dateString, startTimeString));
		}
		else if(startDate.equals(SystemTime.getDate()) && startTime.before(SystemTime.getTime())){
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

		if(!(FlexiBookApplication.getFlexiBook().getBusiness().getBusinessHours().stream().anyMatch(x -> 
			x.getDayOfWeek().equals(getDayOfWeek(dayOfWeek))
			&& x.getStartTime().before(finalStartTime)
			&& x.getEndTime().after(finalEndTimeWithDownTime))))
		{
			throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
				serviceName, dateString, startTimeString));
		}
		
		if(FlexiBookApplication.getFlexiBook().getBusiness().getVacation().stream().anyMatch(x -> 
			x.getStartDate().after(finalStartDate) && x.getEndDate().before(finalStartDate)
			|| (x.getStartDate().equals(finalStartDate) && x.getStartTime().before(finalEndTimeWithDownTime))
			|| (x.getEndDate().equals(finalStartDate) && x.getEndTime().after(finalStartTime))))
		{
			throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
				serviceName, dateString, startTimeString));
		}

		if(FlexiBookApplication.getFlexiBook().getBusiness().getHolidays().stream().anyMatch(x -> 
			x.getStartDate().after(finalStartDate) && x.getEndDate().before(finalStartDate)
			|| (x.getStartDate().equals(finalStartDate) && x.getStartTime().before(finalEndTimeWithDownTime))
			|| (x.getEndDate().equals(finalStartDate) && x.getEndTime().after(finalStartTime))))
		{
			throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
				serviceName, dateString, startTimeString));
		}

		if(!validateConflictingAppointments(finalStartDate, finalStartTime, finalEndTimeWithDownTime, 
			finalEndTimeWithNoDownTime, totalDuration))
		{
			throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
			serviceName, dateString, startTimeString));
		}

		FlexiBookApplication.getFlexiBook().addAppointment(
			new Appointment((Customer) FlexiBookApplication.getCurrentUser(), service, 
			new TimeSlot(startDate, startTime, startDate, endTimeWithDownTime, FlexiBookApplication.getFlexiBook()), 
			FlexiBookApplication.getFlexiBook()));
	}

	public static void makeAppointment(String customerString, String dateString, String serviceName, String optServices, 
		String startTimeString) throws InvalidInputException{

			if(customerString.equals("owner")){
				throw new InvalidInputException("An owner cannot make an appointment");
			}

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
	
			if(startDate.before(SystemTime.getDate())){
				throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
					serviceName, dateString, startTimeString));
			}
			else if(startDate.equals(SystemTime.getDate()) && startTime.before(SystemTime.getTime())){
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
	
	
			if(!(FlexiBookApplication.getFlexiBook().getBusiness().getBusinessHours().stream().anyMatch(x -> 
				x.getDayOfWeek().equals(getDayOfWeek(dayOfWeek))
				&& x.getStartTime().before(finalStartTime)
				&& x.getEndTime().after(finalEndTimeWithDownTime))))
			{
				throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
					serviceName, dateString, startTimeString));
			}
			
			if(FlexiBookApplication.getFlexiBook().getBusiness().getVacation().stream().anyMatch(x -> 
				x.getStartDate().after(finalStartDate) && x.getEndDate().before(finalStartDate)
				|| (x.getStartDate().equals(finalStartDate) && x.getStartTime().before(finalEndTimeWithDownTime))
				|| (x.getEndDate().equals(finalStartDate) && x.getEndTime().after(finalStartTime))))
			{
				throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
					serviceName, dateString, startTimeString));
			}
	
			if(FlexiBookApplication.getFlexiBook().getBusiness().getHolidays().stream().anyMatch(x -> 
				x.getStartDate().after(finalStartDate) && x.getEndDate().before(finalStartDate)
				|| (x.getStartDate().equals(finalStartDate) && x.getStartTime().before(finalEndTimeWithDownTime))
				|| (x.getEndDate().equals(finalStartDate) && x.getEndTime().after(finalStartTime))))
			{
				throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
					serviceName, dateString, startTimeString));
			}
	
			if(!validateConflictingAppointments(finalStartDate, finalStartTime, finalEndTimeWithDownTime, 
				finalEndTimeWithNoDownTime, totalDuration)){
				throw new InvalidInputException(String.format("There are no available slots for %s on %s at %s", 
				serviceName, dateString, startTimeString));
			}
			
			Appointment a = new Appointment((Customer) FlexiBookApplication.getCurrentUser(), serviceCombo, 
			new TimeSlot(startDate, startTime, startDate, endTimeWithDownTime, FlexiBookApplication.getFlexiBook()), 
			FlexiBookApplication.getFlexiBook());
			
			for(ComboItem ci : chosenItems){
				a.addChosenItem(ci);
			}

			FlexiBookApplication.getFlexiBook().addAppointment(a);
	}
	

	/**
	 * @author He Qian Wang
	 * @return
	 * @throws InvalidInputException
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

		Appointment foundAppointment = null;

		for(Appointment a : new ArrayList<Appointment>(c.getAppointments())){
			if(a.getBookableService().getName().equals(serviceName) 
				&& a.getTimeSlot().getStartDate().equals(oldStartDate)
				&& a.getTimeSlot().getStartTime().equals(oldStartTime)){
					foundAppointment = a;
			}
		}

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
		}
		else if(foundAppointment.getBookableService() instanceof ServiceCombo){
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
	
	public static boolean updateAppointment(String customerString, boolean isAdd, String comboItemName, String serviceName, 
		String dateString, String startTimeString) throws InvalidInputException{

		if(FlexiBookApplication.getCurrentUser().getUsername().equals("owner")){
			throw new InvalidInputException("Error: An owner cannot update a customer's appointment");
		}

		if(!FlexiBookApplication.getCurrentUser().getUsername().equals(customerString)){
			throw new InvalidInputException("Error: A customer can only update their own appointments");
		}

		Date startDate = null;
		Time startTime = null;
		try {
			startDate = FlexiBookUtil.getDateFromString(dateString);
			startTime = FlexiBookUtil.getTimeFromString(startTimeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

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
		}
		else{
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
	 * @author He Qian Wang
	 * @return
	 * @throws InvalidInputException
	 */
	public static void cancelAppointment(String customerString, String serviceName, String dateString,
			String startTimeString) throws InvalidInputException {		
		if(FlexiBookApplication.getCurrentUser().getUsername().equals("owner")){
			throw new InvalidInputException("An owner cannot cancel an appointment");
		}

		if(!FlexiBookApplication.getCurrentUser().getUsername().equals(customerString)){
			throw new InvalidInputException("A customer can only cancel their own appointments");
		}

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

		// Appointment foundAppointment = null;
		for(Appointment a : new ArrayList<Appointment>(c.getAppointments())){
			if(a.getBookableService().getName().equals(serviceName) 
				&& a.getTimeSlot().getStartDate().equals(startDate)
				&& a.getTimeSlot().getStartTime().equals(startTime)){
					a.delete();
			}
		}		
	}

	private static boolean validateConflictingAppointments(Date finalStartDate, Time finalStartTime, Time finalEndTimeWithDownTime, 
		Time finalEndTimeWithNoDownTime, long totalDuration) throws InvalidInputException {
		for(Appointment app: FlexiBookApplication.getFlexiBook().getAppointments()){
			if(app.getTimeSlot().getStartDate().equals(finalStartDate)){
				int downtime = 0;
				if(app.getBookableService() instanceof Service){
					Service s = (Service) app.getBookableService();
					downtime = s.getDowntimeDuration();
				}
				else if(app.getBookableService() instanceof ServiceCombo){
					ServiceCombo sc = (ServiceCombo) app.getBookableService();
					if(app.getChosenItems().isEmpty()){
						downtime = sc.getMainService().getService().getDowntimeDuration();
					}
					else{
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

				// if((app.getTimeSlot().getStartTime().equals(finalEndTimeWithDownTime) ||
				// appEndNoDowntime.equals(finalStartTime))){
				// 	return true;
				// }
				if((app.getTimeSlot().getStartTime().before(finalEndTimeWithNoDownTime)
				&& appEndNoDowntime.after(finalStartTime))){
					boolean fitsInDowntime = false;
					if(app.getBookableService() instanceof Service){
						fitsInDowntime = ((Service) app.getBookableService()).getDowntimeDuration() >= totalDuration;
					}
					else{
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
}
