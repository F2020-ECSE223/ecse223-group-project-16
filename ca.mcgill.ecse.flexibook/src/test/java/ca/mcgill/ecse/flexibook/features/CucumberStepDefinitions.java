package ca.mcgill.ecse.flexibook.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Arrays;
import java.util.HashSet;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOBusiness;
import ca.mcgill.ecse.flexibook.controller.TOTimeSlot;
import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.util.FlexiBookUtil;
import ca.mcgill.ecse.flexibook.util.SystemTime;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {
	
	private FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
	private Exception exception;
	
	/**
	 * @author louca
	 * Setup environment before each scenario
	 */
	@Before
	public void setup() {
		flexiBook = FlexiBookApplication.getFlexiBook();
	}
	
	/**
	 * @author louca
	 * Teardown environment after each scenario
	 */
	@After
	public void teardown() {
		FlexiBookApplication.unsetCurrentUser();
		
		flexiBook.delete();
		flexiBook = null;
		
		exception = null;
	}
	/**
	 * @author louca
	 */
	@Given("a Flexibook system exists")
	public void a_flexibook_system_exists() {
	    assertTrue(FlexiBookApplication.getFlexiBook() != null);
	}

	//================================================================================
    // DeleteCustomerAccount
    //================================================================================
	/**
	 * @author louca
	 */
	@Given("an owner account exists in the system with username {string} and password {string}")
	public void an_owner_account_exists_in_the_system_with_username_and_password(String string, String string2) {
		if (flexiBook.hasOwner()) {
			if (flexiBook.getOwner().getPassword().equals(string2)) {
				return;	
			} else {
				flexiBook.getOwner().delete();
			}
		}
		new Owner(string, string2, flexiBook);
	}
	/**
	 * @author louca
	 */
	@Given("the following customers exist in the system:")
	public void the_following_customers_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		boolean customerExists;
		
		for (Map<String, String> columns : rows) {
			customerExists = false;
			for (Customer c : flexiBook.getCustomers()) {
				if (c.getUsername().equals(columns.get("username"))) {
					customerExists = true;
					break;
				}
			}
			
			if (!customerExists) {
				new Customer(columns.get("username"), columns.get("password"), flexiBook);
			}
		}
	}
	/**
	 * @author louca
	 */
	@Given("the account with username {string} has pending appointments")
	public void the_account_with_username_has_pending_appointments(String string) {
		Customer customer = null;
	    for (Customer c : flexiBook.getCustomers()) {
	    	if (c.getUsername().equals(string)) {
	    		customer = c;
	    	}
	    }
	    
	    customer.addAppointment(new Appointment(
	    		customer, 
	    		new Service("cut", flexiBook, 60, 0, 0), 
	    		new TimeSlot(
	    				Date.valueOf(LocalDate.now().plusDays(1)), Time.valueOf(LocalTime.NOON), 
	    				Date.valueOf(LocalDate.now().plusDays(1)), Time.valueOf(LocalTime.NOON.plusHours(1)), 
	    				flexiBook), 
	    		flexiBook));
	    customer.addAppointment(new Appointment(
	    		customer, 
	    		new Service("dry", flexiBook, 60, 0, 0), 
	    		new TimeSlot(
	    				Date.valueOf(LocalDate.now().plusDays(2)), Time.valueOf(LocalTime.NOON), 
	    				Date.valueOf(LocalDate.now().plusDays(2)), Time.valueOf(LocalTime.NOON.plusHours(1)), 
	    				flexiBook), 
	    		flexiBook));
	}
	/**
	 * @author louca
	 */
	@Given("the user is logged in to an account with username {string}")
	public void the_user_is_logged_in_to_an_account_with_username(String string) {
		if (string.equals("owner")) {
			if (flexiBook.hasOwner()) {
 				FlexiBookApplication.setCurrentUser(flexiBook.getOwner());
 			} else {
 				FlexiBookApplication.setCurrentUser(new Owner(string, "ownerPass", flexiBook));
 			}
		} else {
			for (Customer customer : flexiBook.getCustomers()) {
				if (customer.getUsername().equals(string)) {
					FlexiBookApplication.setCurrentUser(customer);
					return;
				}
			}
			FlexiBookApplication.setCurrentUser(new Customer(string, "customerPass", flexiBook));
		}
		
	}
	
	List<Appointment> allAppointmentsOfDeletedCustomer;
	
	/**
	 * @author louca
	 */
	@When("the user tries to delete account with the username {string}")
	public void the_user_tries_to_delete_account_with_the_username(String string) {
		for (Customer c : flexiBook.getCustomers()) {
			if (c.getUsername().equals(string)) {
				allAppointmentsOfDeletedCustomer = c.getAppointments();
				break;
			}
		}
		
	    try {
			FlexiBookController.deleteCustomerAccount(string);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}
	/**
	 * @author louca
	 */
	@Then("the account with the username {string} does not exist")
	public void the_account_with_the_username_does_not_exist(String string) {
		if (string.equals("owner")) {
			assertTrue(!flexiBook.hasOwner());
		} else {
			for (Customer c : flexiBook.getCustomers()) {
				if (c.getUsername().equals(string)) {
					fail();
				}
			}
		}
	}
	/**
	 * @author louca
	 */
	@Then("all associated appointments of the account with the username {string} shall not exist")
	public void all_associated_appointments_of_the_account_with_the_username_shall_not_exist(String string) {
	    for (Appointment appointment : allAppointmentsOfDeletedCustomer) {
	    	assertEquals(null, appointment);
	    }
	    
	    for (Customer c : flexiBook.getCustomers()) {
	    	if (c.getUsername().equals(string)) {
	    		assertTrue(!c.hasAppointments());
	    		break;
	    	}
	    }
	}
	/**
	 * @author louca
	 */
	@Then("the user shall be logged out")
	public void the_user_shall_be_logged_out() {
	    assertTrue(!FlexiBookApplication.hasCurrentUser());
	}
	/**
	 * @author louca
	 */
	@Then("the account with the username {string} exists")
	public void the_account_with_the_username_exists(String string) {
		if (string.equals("owner")) {
			assertTrue(flexiBook.hasOwner());
		} else {
			for (Customer c : flexiBook.getCustomers()) {
				if (c.getUsername().equals(string)) {
					return;
				}
			}
			fail();
		}
	}
	/**
	 * @author louca
	 */
	@Then("an error message {string} shall be raised")
	public void an_error_message_shall_be_raised(String string) {
	    assertEquals(string, exception.getMessage());
	}

	//================================================================================
    // SignUpCustomerAccount
    //================================================================================

	/**
	 * @author louca
	 */
	@Given("there is no existing username {string}")
	public void there_is_no_existing_username(String string) {
		if (string.equals("owner")) {
			if (flexiBook.hasOwner()) {
				flexiBook.getOwner().delete();
			}
		} else {
			for (Customer c : new ArrayList<Customer>(flexiBook.getCustomers())) {
				if (c.getUsername().equals(string)) {
					c.delete();
					break; // uniqueness
				}
		    }
		}
	}
	
	int priorCustomersCount;
	
	/**
	 * @author louca
	 */
	@When("the user provides a new username {string} and a password {string}")
	public void the_user_provides_a_new_username_and_a_password(String string, String string2) {
		priorCustomersCount = flexiBook.getCustomers().size();
		
	    try {
			FlexiBookController.createCustomerAccount(string, string2);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}
	/**
	 * @author louca
	 */
	@Then("a new customer account shall be created")
	public void a_new_customer_account_shall_be_created() {
	    assertEquals(priorCustomersCount + 1, flexiBook.getCustomers().size());
	}
	/**
	 * @author louca
	 */
	@Then("the account shall have username {string} and password {string}")
	public void the_account_shall_have_username_and_password(String string, String string2) {
		if (string.equals("owner")) {
			Owner owner = flexiBook.getOwner();
			assertEquals(string, owner.getUsername());
			assertEquals(string2, owner.getPassword());
		} else {
		    for (Customer c : flexiBook.getCustomers()) {
		    	if (c.getUsername().equals(string)) {
		    		assertEquals(string2, c.getPassword());
		    		return;
		    	}
		    }
		    fail();
		}
	}
	/**
	 * @author louca
	 */
	@Given("there is an existing username {string}")
	public void there_is_an_existing_username(String string) {
		if (string.equals("owner")) {
			if (!flexiBook.hasOwner()) {
				new Owner(string, "ownerPass", flexiBook);
			}
		} else {
		    for (Customer customer : flexiBook.getCustomers()) {
		    	if (customer.getUsername().equals(string)) {
		    		return;
		    	}
		    }
		    new Customer(string, "customerPass", flexiBook);
		}
	}
	/**
	 * @author louca
	 */
	@Then("no new account shall be created")
	public void no_new_account_shall_be_created() {
	    assertEquals(priorCustomersCount, flexiBook.getCustomers().size());
	}

	//================================================================================
    // UpdateAccount
    //================================================================================
	
	String priorUsername;
	String priorPassword;
	
	/**
	 * @author louca
	 */
	@When("the user tries to update account with a new username {string} and password {string}")
	public void the_user_tries_to_update_account_with_a_new_username_and_password(String string, String string2) {
		priorUsername = FlexiBookApplication.getCurrentUser().getUsername();
		priorPassword = FlexiBookApplication.getCurrentUser().getPassword();
		
	    try {
			FlexiBookController.updateUserAccount(string, string2);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}
	/**
	 * @author louca
	 */
	@Then("the account shall not be updated")
	public void the_account_shall_not_be_updated() {
	    assertEquals(priorUsername, FlexiBookApplication.getCurrentUser().getUsername());
	    assertEquals(priorPassword, FlexiBookApplication.getCurrentUser().getPassword());
	}
	//================================================================================
    // Login
    //================================================================================
	
	User currentUser;
	
	 /** @author sarah
	  *  @param String username of user
	  *  @param String password of user
	  */
	@When("the user tries to log in with username {string} and password {string}")
	public void the_user_tries_to_log_in_with_username_and_password(String string, String string2) {
		try {
			currentUser = FlexiBookController.login(string, string2);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}
	/** @author sarah
	 */
	@Then("the user should be successfully logged in")
	public void the_user_should_be_successfully_logged_in() {
		assertEquals(currentUser, FlexiBookApplication.getCurrentUser());
	}
	/** @author sarah
	 */
	@Then("the user should not be logged in")
	public void the_user_should_not_be_logged_in() {
		assertTrue(!FlexiBookApplication.hasCurrentUser());
	}
	/** @author sarah
	 */
	@Then("the user shall be successfully logged in")
	public void the_user_shall_be_successfully_logged_in() {
		assertEquals(flexiBook.getOwner(), FlexiBookApplication.getCurrentUser());
	}
	/** @author sarah
	 */
	@Then("a new account shall be created")
	public void a_new_account_shall_be_created() {
		assertTrue(flexiBook.hasOwner());
	}
	
	//================================================================================
    // Logout
    //================================================================================
	
	/** @author sarah
	 */	
	@Given("the user is logged out")
	public void the_user_is_logged_out() {
		if (FlexiBookApplication.hasCurrentUser()) {
			FlexiBookApplication.unsetCurrentUser();
		}
	}
	/** @author sarah
	 */
	@When("the user tries to log out")
	public void the_user_tries_to_log_out() {
		try {
			FlexiBookController.logout();
		}
		catch (InvalidInputException e) { 
			exception = e;
		}
	}

	//================================================================================
    // ViewAppointmentCalendar
    //================================================================================

	/**
	 * @author heqianw
	 */
	private void setTimeFromString(String timestring) {
		String[] dateTime = timestring.split("\\+");
		Date date = null;
		Time time = null;
		try {
			date = FlexiBookUtil.getDateFromString(dateTime[0]);
			time = FlexiBookUtil.getTimeFromString(dateTime[1]);
		} catch (ParseException e) {
			fail();
		}
		SystemTime.setTesting(date, time);	
	}
	/**
	 * @author heqianw
	 */
	@Given("the system's time and date is {string}")
	public void the_system_s_time_and_date_is(String string) {
		setTimeFromString(string);
	}
	/** @author sarah
	 */
	@Given("the business has the following opening hours:")
	@Given("the business has the following opening hours")
	public void the_business_has_the_following_opening_hours(io.cucumber.datatable.DataTable dataTable) {
		 List<Map<String, String>> rows = dataTable.asMaps();
		 
		 for (Map<String, String> columns : rows) {
			 try {
				 flexiBook.getBusiness().addBusinessHour(new BusinessHour(
						 FlexiBookUtil.getDayOfWeek(columns.get("day")),
						 FlexiBookUtil.getTimeFromString(columns.get("startTime")),
						 FlexiBookUtil.getTimeFromString(columns.get("endTime")), 
						 flexiBook));
			 }
			 catch (ParseException e) {
				 exception = e;
			 }
		 }
	}  
	/** @author sarah
	 */
	@Given("the business has the following holidays:")
	@Given("the business has the following holidays")
	public void the_business_has_the_following_holidays(io.cucumber.datatable.DataTable dataTable) {
		 List<Map<String, String>> rows = dataTable.asMaps();
		 for (Map<String, String> columns : rows) {
			 try {
				 flexiBook.getBusiness().addHoliday(new TimeSlot(
						 FlexiBookUtil.getDateFromString(columns.get("startDate")),
						 FlexiBookUtil.getTimeFromString(columns.get("startTime")),
						 FlexiBookUtil.getDateFromString(columns.get("endDate")),
						 FlexiBookUtil.getTimeFromString(columns.get("endTime")), 
						 flexiBook));
			 }
			 catch (ParseException e) {
				 exception = e;
			 }
		 }
	}
	/** @author sarah
	 */
	@Given("{string} is logged in to their account") 
	public void is_logged_in_to_their_account(String string) {
		if (string.equals("owner")) {
			FlexiBookApplication.setCurrentUser(flexiBook.getOwner());
		} 
		else {
			for (Customer customer : flexiBook.getCustomers()) {
				if (customer.getUsername().equals(string)) {
					FlexiBookApplication.setCurrentUser(customer);
				}
			}
		}
	}
	
	List<TOTimeSlot> unavailableTimeSlots;
	List<TOTimeSlot> availableTimeSlots;
	
	/** @author sarah
	 */
	@When("{string} requests the appointment calendar for the day of {string}")
	public void requests_the_appointment_calendar_for_the_day_of(String string, String string2) {
		try {
			unavailableTimeSlots = FlexiBookController.viewAppointmentCalendar(string, string2, null).getUnavailableTimeSlots();
			availableTimeSlots = FlexiBookController.viewAppointmentCalendar(string, string2, null).getAvailableTimeSlots(); // ***
		}
		catch (InvalidInputException e) { 
			exception = e;
		}
	}
	/** @author sarah
	 */
	@When("{string} requests the appointment calendar for the week starting on {string}")
	public void requests_the_appointment_calendar_for_the_week_starting_on(String string, String string2) {
		try {
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 Calendar c = Calendar.getInstance();
			 c.setTime(sdf.parse(string2));
			 c.add(Calendar.DATE, 7);  // number of days to add
			 String endDate = sdf.format(c.getTime());  
			 
			unavailableTimeSlots = FlexiBookController.viewAppointmentCalendar(string, string2, endDate).getUnavailableTimeSlots();
			availableTimeSlots = FlexiBookController.viewAppointmentCalendar(string, string2, endDate).getAvailableTimeSlots(); // ***
		}
		catch (InvalidInputException e) { 
			exception = e;
		} catch (ParseException e) {
			exception = e;
		}
	}
	/** @author sarah
	 */
	@Then("the following slots shall be available:")
	public void the_following_slots_shall_be_available(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();

		boolean isMatch;
		for (Map<String, String> columns : rows) {
			isMatch = false;
			for (TOTimeSlot t: availableTimeSlots) {
				 if (columns.get("date").equals(t.getStartDate().toString())) {
					 try {
						 SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
						 boolean endTimes = fmt.format(t.getEndTime()).equals(fmt.format(FlexiBookUtil.getTimeFromString(columns.get("endTime"))));
						 boolean startTimes = fmt.format(t.getStartTime()).equals(fmt.format(FlexiBookUtil.getTimeFromString(columns.get("startTime"))));
						 if (endTimes && startTimes) {
							 isMatch = true;
							 break;
						 }
					 }
					 catch (ParseException e) {
						 exception = e;
					 }
					 
				 }
			}
			assertTrue(isMatch);
		}
	}
	/** @author sarah
	 */
	@Then("the following slots shall be unavailable:")
	public void the_following_slots_shall_be_unavailable(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		boolean isMatch;
		
		for (Map<String, String> columns : rows) {
			isMatch = false;
			for (TOTimeSlot t: unavailableTimeSlots) {
				 if (columns.get("date").equals(t.getStartDate().toString())) {
					 try {
						 SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
						 boolean endTimes = fmt.format(t.getEndTime()).equals(fmt.format(FlexiBookUtil.getTimeFromString(columns.get("endTime"))));
						 boolean startTimes = fmt.format(t.getStartTime()).equals(fmt.format(FlexiBookUtil.getTimeFromString(columns.get("startTime"))));
						 if (endTimes && startTimes) {
							 isMatch = true;
							 break;
						 }
					 }
					 catch (ParseException e) {
						 exception = e;
					 }
					 
				 }
			}
			assertTrue(isMatch);
		}
	}
    /** @author sarah
     */
	@Then("the system shall report {string}")
	public void the_system_shall_report(String string) {
	    assertEquals(string, exception.getMessage());
	}
	//================================================================================
    // DefineServiceCombo
    //================================================================================
    /**
	/**
	 * @author heqianw
	 */
	@Given("the following appointments exist in the system:")
	public void the_following_appointments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable){
		dataTable.asMaps().stream().forEach(x -> 
			{
				Customer c = flexiBook.getCustomers().stream().filter(y -> y.getUsername().equals(x.get("customer"))).findAny().get();
				TimeSlot newTimeSlot = null;

				try {
					newTimeSlot = new TimeSlot(FlexiBookUtil.getDateFromString(x.get("date")),
						FlexiBookUtil.getTimeFromString(x.get("startTime")),
						FlexiBookUtil.getDateFromString(x.get("date")),
						FlexiBookUtil.getTimeFromString(x.get("endTime")), flexiBook);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				for (BookableService b: flexiBook.getBookableServices()){
					if(b.getName().equals(x.get("serviceName"))){
						final Appointment a = new Appointment(c, b, newTimeSlot, flexiBook);
						if (b instanceof ServiceCombo){
							ServiceCombo sc = (ServiceCombo) b;
							String[] optServices = null;
							if(x.containsKey("optServices")){
								optServices = x.get("optServices").split(",");
							}
							if(x.containsKey("selectedComboItems")){
								optServices = x.get("selectedComboItems").split(",");
							}
							HashSet<String> set = new HashSet<>(Arrays.stream(optServices).collect(Collectors.toSet()));
							sc.getServices().stream().filter(y -> y.getMandatory() || set.contains(y.getService().getName())).forEach(y -> a.addChosenItem(y));;
						}
						c.addAppointment(a);
					}
				}
			}
		);
	}

	int appointmentCount;

	/**
	 * @author heqianw
	 */
	@When("{string} schedules an appointment on {string} for {string} at {string}")
	public void schedules_an_appointment_on_for_at(String string, String string2, String string3, String string4) {
		appointmentCount = flexiBook.getAppointments().size();
		try {
			FlexiBookController.makeAppointment(string, string2, string3, string4);
		} catch (InvalidInputException e) {
			exception = e;
		}		
	}
	/**
	 * @author heqianw
	 */
	@When("{string} schedules an appointment on {string} for {string} with {string} at {string}")
	public void schedules_an_appointment_on_for_with_at(String string, String string2, String string3, String string4, String string5) {
		appointmentCount = flexiBook.getAppointments().size();
		try {
			FlexiBookController.makeAppointment(string, string2, string3, string4, string5);
		} catch (InvalidInputException e) {
			exception = e;
		}	
	}
	/**
	 * @author heqianw
	 */	@Then("{string} shall have a {string} appointment on {string} from {string} to {string}")
	public void shall_have_a_appointment_on_from_to(String string, String string2, String string3, String string4, String string5){
		Optional<Customer> c = flexiBook.getCustomers().stream().filter(x -> x.getUsername().equals(string)).findFirst();
		Date date = null;
		Time startTime = null;
		Time endTime = null;
		try {
			date = FlexiBookUtil.getDateFromString(string3);
			startTime = FlexiBookUtil.getTimeFromString(string4);
			endTime = FlexiBookUtil.getTimeFromString(string5);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		

		Appointment app = null;
		for(Appointment a : c.get().getAppointments()){
			if(a.getBookableService().getName().equals(string2) && a.getTimeSlot().getStartDate().equals(date) 
				&& a.getTimeSlot().getStartTime().equals(startTime) && a.getTimeSlot().getEndTime().equals(endTime)){
				app = a;
			}
		}
		assertTrue(app != null);
	}
	/**
	 * @author heqianw
	 */
	@Then("there shall be {int} more appointment in the system")
	public void there_shall_be_more_appointment_in_the_system(Integer int1) {
		assertEquals(appointmentCount + int1, flexiBook.getAppointments().size());
	}
	//================================================================================
    // UpdateAppointments
	//================================================================================	
	/**
	 * @author heqianw
	 */
	@Given("{string} has a {string} appointment with optional sevices {string} on {string} at {string}")
	public void has_a_appointment_with_optional_sevices_on_at(String string, String string2, String string3, String string4, String string5){
		Customer c = flexiBook.getCustomers().stream().filter(x -> x.getUsername().equals(string)).findFirst().get();
		ServiceCombo sc = (ServiceCombo) flexiBook.getBookableServices().stream().filter(x -> x.getName().equals(string2)).findFirst().get();
		
		Date aStartDate = null;
		Time aStartTime = null;
		
		try{
			aStartDate = FlexiBookUtil.getDateFromString(string4);
			aStartTime =  FlexiBookUtil.getTimeFromString(string5);
		}
		catch (ParseException e){
			e.printStackTrace();
		}
		
		int duration = 0;

		List<ComboItem> chosenItems = new ArrayList<>();	
		for(ComboItem ci: sc.getServices()){
			if(ci.getService().getName().equals(string3) || (ci.isMandatory())){
				duration += ci.getService().getDuration();
				if(!ci.getService().getName().equals(sc.getMainService().getService().getName())){
					chosenItems.add(ci);
				}
			}
		}
		Date aEndDate =  aStartDate;
		Time aEndTime = new Time(aStartTime.getTime() + duration * 60 * 1000);

		Appointment app = new Appointment(c, sc, new TimeSlot(aStartDate, aStartTime, aEndDate, aEndTime, flexiBook), flexiBook);

		for(ComboItem ci : chosenItems){
			app.addChosenItem(ci);
		}
		c.addAppointment(app);
	}
	boolean result;
	/**
	 * @author heqianw
	 */
	@When("{string} attempts to update their {string} appointment on {string} at {string} to {string} at {string}")
	public void attempts_to_update_their_appointment_on_at_to_at(String string, String string2, String string3, String string4, String string5, String string6){
		appointmentCount = flexiBook.getAppointments().size();
		result = false;
		try {
			FlexiBookController.updateAppointment(string, string2, string3, string4, string5, string6);
			result = true;
		} 
		catch (InvalidInputException e) {
			exception = e;
			System.err.println("tried to update appointment but " + e.getMessage());
		}
	}
	/**
	 * @author heqianw
	 */
	@When("{string} attempts to {string} {string} from their {string} appointment on {string} at {string}")
	public void attempts_to_from_their_appointment_on_at(String string, String string2, String string3, String string4, String string5, String string6){
		appointmentCount = flexiBook.getAppointments().size();
		result = false;
		try {
			FlexiBookController.updateAppointment(string, string2.equals("add"), string3, string4, string5, string6);
			result = true;
		} 
		catch (InvalidInputException e) {
			exception = e;
		}
	}
	/**
	 * @author heqianw
	 */
	@When("{string} attempts to update {string}'s {string} appointment on {string} at {string} to {string} at {string}")
	public void attempts_to_update_s_appointment_on_at_to_at(String string, String string2, String string3, String string4, String string5, String string6, String string7) {
		try {
			FlexiBookController.updateAppointment(string2, string3, string4, string5, string6, string7);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}
	/**
	 * @author heqianw
	 */
	@Then("the system shall report that the update was {string}")
	public void the_system_shall_report_that_the_update_was(String string) {
		if(result) {
			assertEquals("successful", string);
		} else {
			assertEquals("unsuccessful", string);
		}
	}
	
	//================================================================================
    // CancelAppointments
	//================================================================================	
	
	/**
	 * @author heqianw
	 */
	@When("{string} attempts to cancel their {string} appointment on {string} at {string}")
	public void attempts_to_cancel_their_appointment_on_at(String string, String string2, String string3, String string4) {
		appointmentCount = flexiBook.getAppointments().size();
		try {
			FlexiBookController.cancelAppointment(string, string2, string3, string4);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}
	/**
	 * @author heqianw
	 */
	@When("{string} attempts to cancel {string}'s {string} appointment on {string} at {string}")
	public void attempts_to_cancel_s_appointment_on_at(String string, String string2, String string3, String string4, String string5) {
		if (string.equals("owner")) {
			FlexiBookApplication.setCurrentUser(flexiBook.getOwner());
		} else {
			for (Customer customer : flexiBook.getCustomers()) {
				if (customer.getUsername().equals(string)) {
					FlexiBookApplication.setCurrentUser(customer);
				}
			}
		}
		appointmentCount = flexiBook.getAppointments().size();
		try {
			FlexiBookController.cancelAppointment(string2, string3, string4, string5);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}
	/**
	 * @author heqianw
	 */
	@Then("{string}'s {string} appointment on {string} at {string} shall be removed from the system")
	public void s_appointment_on_at_shall_be_removed_from_the_system(String string, String string2, String string3, String string4) {
		assertEquals(flexiBook.getAppointments().size(), 0);
	}
	/**
	 * @author heqianw
	 */
	@Then("there shall be {int} less appointment in the system")
	public void there_shall_be_less_appointment_in_the_system(Integer int1) {
		assertEquals(appointmentCount - int1, flexiBook.getAppointments().size());
	}
	
	//================================================================================
    // DefineServiceCombo
    //================================================================================
	/**
	 * @author theodore
	 */
    @Given("the following service combos exist in the system:")
    public void the_following_service_combos_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    	List<Map<String, String>> serviceComboData = dataTable.asMaps();
		for (Map<String, String> e : serviceComboData) {
			ServiceCombo c = new ServiceCombo(e.get("name"), flexiBook);
			String[] services = e.get("services").split(",");
	    	String[] m = e.get("mandatory").split(",");
	    	for (int i=0; i< m.length; i++) {
	    		Service s = null;
	    		for (BookableService j : flexiBook.getBookableServices()) {
	    			if (j.getName().equals(services[i])) {
	    				s = (Service) j;
	    				break;
	    			}
	    		}
	    		ComboItem ci = new ComboItem(m[i].equals("true"), s, c);
	    		if (services[i].equals(e.get("mainService"))) {
	    			c.setMainService(ci);
	    		}
	    	}
		}
    }
    /**
	 * @author theodore
	 */
    @When("{string} initiates the definition of a service combo {string} with main service {string}, services {string} and mandatory setting {string}")
    public void initiates_the_definition_of_a_service_combo_with_main_service_services_and_mandatory_setting(String user, String name, String mainService, String servicesS, String mandatoryS) {
    	String[] services = servicesS.split(",");
    	String[] m = mandatoryS.split(",");
    	boolean[] mandatory = new boolean[m.length];
    	for(int i=0; i<m.length; i++) {
    		mandatory[i] = m[i].equals("true");
    	}
    	try {
    		FlexiBookController.defineServiceCombo(name, services, mainService, mandatory);
    	} catch (InvalidInputException e) {
			exception = e;
		}
    }
    /**
	 * @author theodore
	 */
    @Then("the service combo {string} shall exist in the system")
    public void the_service_combo_shall_exist_in_the_system(String name) {
        ServiceCombo newServiceCombo = null;
    	for (BookableService b : flexiBook.getBookableServices()) {
			if (b instanceof ServiceCombo && b.getName().equals(name)) {
				newServiceCombo = (ServiceCombo) b;
				break;
			}
    	}
		assertTrue(newServiceCombo != null);
    }
    /**
	 * @author theodore
	 */
    @Then("the service combo {string} shall contain the services {string} with mandatory setting {string}")
    public void the_service_combo_shall_contain_the_services_with_mandatory_setting(String name, String servicesS, String mandatoryS) {
    	ServiceCombo newServiceCombo = null;
    	for (BookableService b : flexiBook.getBookableServices()) {
			if (b instanceof ServiceCombo && b.getName().equals(name)) {
				newServiceCombo = (ServiceCombo) b;
				break;
			}
    	}
    	String[] services = servicesS.split(",");
    	String[] m = mandatoryS.split(",");
    	for (int i=0; i<newServiceCombo.numberOfServices(); i++) {
    		assertEquals(newServiceCombo.getService(i).getMandatory(), m[i].equals("true"));
    		assertEquals(newServiceCombo.getService(i).getService().getName(), services[i]);
    	}   	
    }
    /**
	 * @author theodore
	 */
    @Then("the main service of the service combo {string} shall be {string}")
    public void the_main_service_of_the_service_combo_shall_be(String name, String mainService) {
    	ServiceCombo newServiceCombo = null;
    	for (BookableService b : flexiBook.getBookableServices()) {
			if (b instanceof ServiceCombo && b.getName().equals(name)) {
				newServiceCombo = (ServiceCombo) b;
				break;
			}
    	}
    	assertEquals(newServiceCombo.getMainService().getService().getName(), mainService);
    }
    /**
	 * @author theodore
	 */
    @Then("the service {string} in service combo {string} shall be mandatory")
    public void the_service_in_service_combo_shall_be_mandatory(String service, String name) {
    	ServiceCombo newServiceCombo = null;
    	for (BookableService b : flexiBook.getBookableServices()) {
			if (b instanceof ServiceCombo && b.getName().equals(name)) {
				newServiceCombo = (ServiceCombo) b;
				break;
			}
    	}
    	boolean hasService = false;
    	for (ComboItem c : newServiceCombo.getServices()) {
    		if (c.getService().getName().equals(service)) {
    			assertTrue(c.getMandatory());
    			hasService = true;
    		}
    	}
    	assertTrue(hasService);
    }
    /**
	 * @author theodore
	 */
    @Then("the number of service combos in the system shall be {string}")
    public void the_number_of_service_combos_in_the_system_shall_be(String num) {
    	int acc = 0;
    	for (BookableService b : flexiBook.getBookableServices()) {
    		if (b instanceof ServiceCombo) {
    			acc++;
    		}
    	}
        assertEquals(acc,Integer.parseInt(num));
    }
    /**
	 * @author theodore
	 */
    @Then("an error message with content {string} shall be raised")
    public void an_error_message_with_content_shall_be_raised(String string) {
    	assertEquals(string, exception.getMessage());
    }
    /**
	 * @author theodore
	 */
    @Then("the service combo {string} shall not exist in the system")
    public void the_service_combo_shall_not_exist_in_the_system(String name) {
    	ServiceCombo newServiceCombo = null;
    	for (BookableService b : flexiBook.getBookableServices()) {
			if (b instanceof ServiceCombo && b.getName().equals(name)) {
				newServiceCombo = (ServiceCombo) b;
				break;
			}
    	}
		assertEquals(newServiceCombo,null);
    }
    /**
	 * @author theodore
	 */
    @Then("the service combo {string} shall preserve the following properties:")
    public void the_service_combo_shall_preserve_the_following_properties(String name, io.cucumber.datatable.DataTable dataTable) {
    	List<Map<String, String>> serviceComboData = dataTable.asMaps();
		for (Map<String, String> e : serviceComboData) {
			assertEquals(name, e.get("name"));
			ServiceCombo c = null;
			for (BookableService j : flexiBook.getBookableServices()) {
				if(j.getName().equals(name)) {
					c = (ServiceCombo) j;
					break;
				}
			}
			assertEquals(c.getMainService().getService().getName(), e.get("mainService"));
			String[] services = e.get("services").split(",");
	    	String[] m = e.get("mandatory").split(",");
	    	for (int i=0; i< m.length; i++) {
	    		ComboItem ci = c.getService(i);
	    		assertEquals(ci.getMandatory(), m[i].equals("true"));
	    		assertEquals(ci.getService().getName(), services[i]);
	    	}
		}
    }
	//================================================================================
    // DeleteServiceCombo
    //================================================================================
	
    /**
	 * @author theodore
	 */
    @When("{string} initiates the deletion of service combo {string}")
    public void initiates_the_deletion_of_service_combo(String user, String name) {
    	try {
    		FlexiBookController.deleteServiceCombo(name);
    	} catch (InvalidInputException e) {
			exception = e;
		}
    }
	
	//================================================================================
    // UpdateServiceCombo
    //================================================================================
	
    /**
	 * @author theodore
	 */
    @When("{string} initiates the update of service combo {string} to name {string}, main service {string} and services {string} and mandatory setting {string}")
    public void initiates_the_update_of_service_combo_to_name_main_service_and_services_and_mandatory_setting(String user, String oldComboName, String name, String mainService, String servicesS, String mandatoryS) {
    	String[] services = servicesS.split(",");
    	String[] m = mandatoryS.split(",");
    	boolean[] mandatory = new boolean[m.length];
    	for(int i=0; i<m.length; i++) {
    		mandatory[i] = m[i].equals("true");
    	}
    	try {
    	FlexiBookController.updateServiceCombo(oldComboName, name, services, mainService, mandatory);
    	} catch (InvalidInputException e) {
    		exception = e;
    	}
    }
    
	//================================================================================
    // AddService
    //================================================================================
	
    /**
	 * @author aayush
	 */
	@Given("an owner account exists in the system")
	public void an_owner_account_exists_in_the_system() {
		if (!FlexiBookApplication.getFlexiBook().hasOwner()) {
			new Owner("owner", "owner", FlexiBookApplication.getFlexiBook());
		}
	}
	 /**
	 * @author aayush
	 */
	@Given("a business exists in the system")
	public void a_business_exists_in_the_system() {
		if (!FlexiBookApplication.getFlexiBook().hasBusiness()) {
			new Business("TheAve", "33 Rockford Terrace NW", "4036147734", "theave@gmail.com", FlexiBookApplication.getFlexiBook());
		}
	}
	/**
	* @author aayush
	*/
	@Given("the Owner with username {string} is logged in")
	public void the_owner_with_username_is_logged_in(String string) {
		if (string.equals("owner")) {
			FlexiBookApplication.setCurrentUser(flexiBook.getOwner());
		} else {
			FlexiBookApplication.setCurrentUser(new Owner("owner", "owner", FlexiBookApplication.getFlexiBook()));
			}
	}
	/**
	* @author aayush
	*/
	@Given("Customer with username {string} is logged in")
	public void customer_with_username_is_logged_in(String string) {
		for (Customer customer : flexiBook.getCustomers()) {
		    if (customer.getUsername().equals(string)) {
		        FlexiBookApplication.setCurrentUser(customer);
		        return;
		    }
		}
		FlexiBookApplication.setCurrentUser(new Customer(string, "password", flexiBook));
	}
	/**
	 * @author aayush
	 */
	@When("{string} initiates the addition of the service {string} with duration {string}, start of down time {string} and down time duration {string}")
	public void initiates_the_addition_of_the_service_with_duration_start_of_down_time_and_down_time_duration(String string, String string2, String string3, String string4, String string5) {
		try {
			FlexiBookController.addService(string2, string3, string4, string5);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}
	/**
	* @author aayush
	*/
	@Then("the service {string} shall exist in the system")
	public void the_service_shall_exist_in_the_system(String string) {
		for (BookableService s: FlexiBookApplication.getFlexiBook().getBookableServices()) {
			if (s.getName().equals(string)) {
				return;
			}
		}
		fail();
	}
	/**
	* @author aayush
	*/
	@Then("the service {string} shall have duration {string}, start of down time {string} and down time duration {string}")
	public void the_service_shall_have_duration_start_of_down_time_and_down_time_duration(String string, String string2, String string3, String string4) {
		for (BookableService s: FlexiBookApplication.getFlexiBook().getBookableServices()) {
			if (s instanceof Service && s.getName().equals(string)) {
				Service service = (Service) s;
				assertEquals(Integer.parseInt(string2), service.getDuration());
				assertEquals(Integer.parseInt(string3), service.getDowntimeStart());
				assertEquals(Integer.parseInt(string4), service.getDowntimeDuration());
				break;
			}
		}
	}
	/**
	* @author aayush
	*/
	@Then("the number of services in the system shall be {string}")
	public void the_number_of_services_in_the_system_shall_be(String string) {
		int counter = 0;
		for (BookableService s: FlexiBookApplication.getFlexiBook().getBookableServices()) {
			if (s instanceof Service) {
			counter ++;
			}
		}
		assertEquals(Integer.parseInt(string), counter);
	}
	/**
	* @author aayush
	*/
	@Then("the service {string} shall still preserve the following properties:")
	public void the_service_shall_still_preserve_the_following_properties(String string, io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		Service s = null;
		
		for (BookableService bS: flexiBook.getBookableServices()) {
			if (bS instanceof Service && bS.getName().equals(string)) {
				s = (Service) bS;
				break;
			}
		}
		for (Map<String, String> columns : rows) {
			assertEquals((columns.get("name")), s.getName());
			assertEquals(Integer.parseInt(columns.get("duration")), s.getDuration());
			assertEquals(Integer.parseInt(columns.get("downtimeStart")), s.getDowntimeStart());
			assertEquals(Integer.parseInt(columns.get("downtimeDuration")), s.getDowntimeDuration());
		}
	}
	/**
	* @author aayush
	*/
	@When("{string} initiates the update of the service {string} to name {string}, duration {string}, start of down time {string} and down time duration {string}")
	public void initiates_the_update_of_the_service_to_name_duration_start_of_down_time_and_down_time_duration(String string, String string2, String string3, String string4, String string5, String string6) {
		try {
			FlexiBookController.updateService(string2, string3, string4, string5, string6);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}
	/**
	* @author aayush
	*/
	@Then("the service {string} shall be updated to name {string}, duration {string}, start of down time {string} and down time duration {string}")
	public void the_service_shall_be_updated_to_name_duration_start_of_down_time_and_down_time_duration(String string, String string2, String string3, String string4, String string5) {
		Service s = null;
		for (BookableService bS: FlexiBookApplication.getFlexiBook().getBookableServices()) {
			if (bS instanceof Service && bS.getName() == string2) {
				s = (Service) bS;
				assertEquals(string2,s.getName());
				assertEquals(Integer.parseInt(string3),s.getDuration());
				assertEquals(Integer.parseInt(string4),s.getDowntimeStart());
				assertEquals(Integer.parseInt(string3),s.getDowntimeDuration());
			}
		}
	}

	//================================================================================
    // DeleteService
    //================================================================================

	/**
	* @author aayush
	*/
	@Given("the following services exist in the system:")
	public void the_following_services_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		
		for (Map<String, String> columns : rows) {
			new Service(columns.get("name"), flexiBook, Integer.parseInt(columns.get("duration")), Integer.parseInt(columns.get("downtimeDuration")),Integer.parseInt(columns.get("downtimeStart")));
		}	
	}
	/**
	* @author aayush
	*/
	@When("{string} initiates the deletion of service {string}")
	public void initiates_the_deletion_of_service(String string, String string2) {
		try {
			FlexiBookController.deleteService(string2);
		} catch (InvalidInputException e) {
			exception = e;
			System.out.println(e.getMessage());
		}
	}
	/**
	* @author aayush
	*/
	@Then("the service {string} shall not exist in the system")
	public void the_service_shall_not_exist_in_the_system(String string) {
		for (BookableService s: FlexiBookApplication.getFlexiBook().getBookableServices()) {
			if (s instanceof Service && ((Service) s).getName().contentEquals(string)) {
				fail();
			}
		}
	}
	/**
	* @author aayush
	*/
	@Then("the number of appointments in the system with service {string} shall be {string}")
	public void the_number_of_appointments_in_the_system_with_service_shall_be(String string, String string2) {
		int counter = 0;
		for (Appointment a: FlexiBookApplication.getFlexiBook().getAppointments()) {
			if (a.getBookableService().getName().contentEquals(string)) {
				counter++;
			}
		}
		assertEquals(Integer.parseInt(string2), counter);
	}
	/**
	* @author aayush
	*/
	@Then("the number of appointments in the system shall be {string}")
	public void the_number_of_appointments_in_the_system_shall_be(String string) {
		assertEquals(Integer.parseInt(string), FlexiBookApplication.getFlexiBook().getAppointments().size());
	}
	/**
	* @author aayush
	*/
	@Then("the service combos {string} shall not exist in the system")
	public void the_service_combos_shall_not_exist_in_the_system(String string) {
    	for (BookableService b : flexiBook.getBookableServices()) {
    		if (b instanceof ServiceCombo && b.getName().equals(string)) {
				fail();
			}
    	}
	}
	/**
	* @author aayush
	*/
	@Then("the service combos {string} shall not contain service {string}")
	public void the_service_combos_shall_not_contain_service(String string, String string2) {
		ServiceCombo newServiceCombo = null;
    	
		for (BookableService b : flexiBook.getBookableServices()){
			if (b instanceof ServiceCombo && b.getName().equals(string)) {
				newServiceCombo = (ServiceCombo) b;
				for (ComboItem cI : newServiceCombo.getServices()) {
					if (cI.getService().getName().equals(string2)) {
						fail();
					}
				}	
			}   
		}
	}
	
	//================================================================================
    // UpdateService
    //================================================================================
    /**
	 * @author theodore
	 */
    @Then("the service combo {string} shall be updated to name {string}")
    public void the_service_combo_shall_be_updated_to_name(String oldComboName, String name) {
    	the_service_combo_shall_exist_in_the_system(name);
    	if(!oldComboName.equals(name)) {
    		the_service_combo_shall_not_exist_in_the_system(oldComboName);
    	}
    }
	//================================================================================
    // SetUpBusinessInfo
    //================================================================================
	/**
	 * @author Julie
	 */
	@Given("no business exists")
	public void no_business_exists() {
	    if (flexiBook.hasBusiness()) {
	    	flexiBook.delete();
	    } 
	}
	/**
	 * @author Julie
	 */
	@When("the user tries to set up the business information with new {string} and {string} and {string} and {string}")
	public void the_user_tries_to_set_up_the_business_information_with_new_and_and_and(String string, String string2, String string3, String string4) {
		try {
			FlexiBookController.setUpBusinessInfo(string, string2, string3, string4);
		} catch (InvalidInputException e) {
			exception = e;
		}	
	}
	/**
	 * @author Julie
	 */
	@Then("a new business with new {string} and {string} and {string} and {string} shall {string} created")
	public void a_new_business_with_new_and_and_and_shall_created(String string, String string2, String string3, String string4, String string5) {
		if (string5.equals("be")) {
			assertEquals(string,flexiBook.getBusiness().getName());
			assertEquals(string2,flexiBook.getBusiness().getAddress());
			assertEquals(string3,flexiBook.getBusiness().getPhoneNumber());
			assertEquals(string4,flexiBook.getBusiness().getEmail());
		}
		else {
			assertEquals(null,flexiBook.getBusiness());
		}
	}
	/**
	 * @author Julie
	 */
	@Then("an error message {string} shall {string} raised")
	public void an_error_message_shall_raised(String string, String string2) {
		if (string2.equals("not be")) {
			assertEquals(exception, null);
		}
		else { 
			assertEquals(string, exception.getMessage());
		}
	}
	/**
	 * @author Julie
	 */
	@Given("a business exists with the following information:")
	public void a_business_exists_with_the_following_information(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		
		for (Map<String, String> columns : rows) {
			new Business(columns.get("name"), columns.get("address"), columns.get("phone number"), columns.get("email"), flexiBook);
		}
	}
	/**
	 * @author Julie
	 */
	@Given("the business has a business hour on {string} with start time {string} and end time {string}")
	public void the_business_has_a_business_hour_on_with_start_time_and_end_time(String string, String string2, String string3) {
		for (BusinessHour bh : flexiBook.getBusiness().getBusinessHours()) {
			if (string.equals(bh.getDayOfWeek().toString()) && 
					bh.getStartTime().toString().substring(0,5).equals(string2) && 
					bh.getEndTime().toString().substring(0,5).equals(string3)) {
				return;
			}
		}
		BusinessHour aBusinessHour = new BusinessHour(BusinessHour.DayOfWeek.valueOf(string), 
				Time.valueOf(LocalTime.of(Integer.valueOf(string2.substring(0,2)), Integer.valueOf(string2.substring(3,5)))),
				Time.valueOf(LocalTime.of(Integer.valueOf(string3.substring(0,2)), Integer.valueOf(string3.substring(3,5)))), 
				flexiBook);
		flexiBook.getBusiness().addBusinessHour(aBusinessHour);
	}
	/**
	 * @author Julie
	 */
	int numberOfBusinessHours;
	@When("the user tries to add a new business hour on {string} with start time {string} and end time {string}")
	public void the_user_tries_to_add_a_new_business_hour_on_with_start_time_and_end_time(String string, String string2, String string3) {
		numberOfBusinessHours = flexiBook.getBusiness().getBusinessHours().size();
		try {
			FlexiBookController.addNewBusinessHour(string, string2, string3);
		} catch (InvalidInputException e) {
			exception = e;
		}	
	}
	/**
	 * @author Julie
	 */
	@Then("a new business hour shall {string} created")
	public void a_new_business_hour_shall_created(String string) {
		if (string.equals("be")) {
			assertEquals(numberOfBusinessHours + 1, flexiBook.getBusiness().getBusinessHours().size());
		}
		else {
			assertEquals(numberOfBusinessHours, flexiBook.getBusiness().getBusinessHours().size());
		}
	}
	TOBusiness business;
	/**
	 * @author Julie
	 */
	@When("the user tries to access the business information")
	public void the_user_tries_to_access_the_business_information() {
		business = FlexiBookController.viewBusinessInfo();
	}
	/**
	 * @author Julie
	 */
	@Then("the {string} and {string} and {string} and {string} shall be provided to the user")
	public void the_and_and_and_shall_be_provided_to_the_user(String string, String string2, String string3, String string4) {
		assertEquals(string, business.getName());
		assertEquals(string2, business.getAddress());
		assertEquals(string3, business.getPhoneNumber());
		assertEquals(string4, business.getEmail());
	}
	/**
	 * @author Julie
	 */
	@Given("a {string} time slot exists with start time {string} at {string} and end time {string} at {string}")
	public void a_time_slot_exists_with_start_time_at_and_end_time_at(String string, String string2, String string3, String string4, String string5) {
		if (string.equals("vacation")) {
			for (TimeSlot v : flexiBook.getBusiness().getVacation()) {
				if (string.equals(v.getStartDate().toString()) && 
						v.getStartTime().toString().substring(0,5).equals(string2) && 
						string3.equals(v.getEndDate().toString()) && 
						v.getEndTime().toString().substring(0,5).equals(string4)) {
					return;
				}
			}
			TimeSlot aTimeSlot  = new  TimeSlot(Date.valueOf(LocalDate.of(Integer.parseInt(string2.substring(0,4)), Month.of(Integer.parseInt(string2.substring(5,7))), Integer.parseInt(string2.substring(8,10)))), 
					Time.valueOf(LocalTime.of(Integer.parseInt(string3.substring(0,2)), Integer.parseInt(string3.substring(3,5)))), 
					Date.valueOf(LocalDate.of(Integer.parseInt(string4.substring(0,4)), Month.of(Integer.parseInt(string4.substring(5,7))), Integer.parseInt(string4.substring(8,10)))),
					Time.valueOf(LocalTime.of(Integer.parseInt(string5.substring(0,2)), Integer.parseInt(string5.substring(3,5)))),
					flexiBook);
			flexiBook.getBusiness().addVacation(aTimeSlot);
		}
		if (string.equals("holiday")) {
			for (TimeSlot v : flexiBook.getBusiness().getHolidays()) {
				if (string.equals(v.getStartDate().toString()) && 
						v.getStartTime().toString().substring(0,5).equals(string2) && 
						string3.equals(v.getEndDate().toString()) && 
						v.getEndTime().toString().substring(0,5).equals(string4)) {
					return;
				}
			}
			TimeSlot aTimeSlot  = new  TimeSlot(Date.valueOf(LocalDate.of(Integer.parseInt(string2.substring(0,4)), Month.of(Integer.parseInt(string2.substring(5,7))), Integer.parseInt(string2.substring(8,10)))), 
					Time.valueOf(LocalTime.of(Integer.parseInt(string3.substring(0,2)), Integer.parseInt(string3.substring(3,5)))), 
					Date.valueOf(LocalDate.of(Integer.parseInt(string4.substring(0,4)), Month.of(Integer.parseInt(string4.substring(5,7))), Integer.parseInt(string4.substring(8,10)))),
					Time.valueOf(LocalTime.of(Integer.parseInt(string5.substring(0,2)), Integer.parseInt(string5.substring(3,5)))),
					flexiBook);
			flexiBook.getBusiness().addHoliday(aTimeSlot);
		}
	}
	/**
	 * @author Julie
	 */
	int numberOfVacationSlots;
	int numberOfHolidaySlots;
	@When("the user tries to add a new {string} with start date {string} at {string} and end date {string} at {string}")
	public void the_user_tries_to_add_a_new_with_start_date_at_and_end_date_at(String string, String string2, String string3, String string4, String string5) {
		numberOfVacationSlots = flexiBook.getBusiness().getVacation().size();
		numberOfHolidaySlots = flexiBook.getBusiness().getHolidays().size();
		try {
			FlexiBookController.addNewTimeSlot(string, string2, string3, string4, string5);
		} catch (InvalidInputException e) {
			exception = e;
		}	
	}
	/**
	 * @author Julie
	 */
	@Then("a new {string} shall {string} be added with start date {string} at {string} and end date {string} at {string}")
	public void a_new_shall_be_added_with_start_date_at_and_end_date_at(String string, String string2, String string3, String string4, String string5, String string6) {
		if (string2.equals("be")) {
			if (string.equals("vacation")) {
				assertEquals(numberOfVacationSlots + 1, flexiBook.getBusiness().getVacation().size());
			}
			else {
				assertEquals(numberOfHolidaySlots + 1, flexiBook.getBusiness().getHolidays().size());
			}
		}
		else {
			if (string.equals("vacation")) {
				assertEquals(numberOfVacationSlots, flexiBook.getBusiness().getVacation().size());
			}
			else {
				assertEquals(numberOfHolidaySlots, flexiBook.getBusiness().getHolidays().size());
			}
		}
	}
	
	//================================================================================
    // UpdateBusinessInfo
    //================================================================================
	/**
	 * @author Julie
	 */
	String prevName;
	String prevAddress;
	String prevPhoneNumber;
	String prevEmail;
	@When("the user tries to update the business information with new {string} and {string} and {string} and {string}")
	public void the_user_tries_to_update_the_business_information_with_new_and_and_and(String string, String string2, String string3, String string4) {
		prevName = flexiBook.getBusiness().getName();
		prevAddress = flexiBook.getBusiness().getAddress();
		prevPhoneNumber = flexiBook.getBusiness().getPhoneNumber();
		prevEmail = flexiBook.getBusiness().getEmail();
		try {
			FlexiBookController.updateBusinessInfo(string, string2, string3, string4);
		} catch (InvalidInputException e) {
			exception = e;
		}	
	}
	/**
	 * @author Julie
	 */
	@Then("the business information shall {string} updated with new {string} and {string} and {string} and {string}")
	public void the_business_information_shall_updated_with_new_and_and_and(String string, String string2, String string3, String string4, String string5) {
		if (string.equals("be")) {
			assertEquals(string2,flexiBook.getBusiness().getName());
			assertEquals(string3,flexiBook.getBusiness().getAddress());
			assertEquals(string4,flexiBook.getBusiness().getPhoneNumber());
			assertEquals(string5,flexiBook.getBusiness().getEmail());
		}
		else {
			assertEquals(prevName,flexiBook.getBusiness().getName());
			assertEquals(prevAddress,flexiBook.getBusiness().getAddress());
			assertEquals(prevPhoneNumber,flexiBook.getBusiness().getPhoneNumber());
			assertEquals(prevEmail,flexiBook.getBusiness().getEmail());
		}
	}
	/**
	 * @author Julie
	 */
	String prevDay;
	String prevStartTime;
	String prevEndTime;
	String newDay;
	String newStartTime;
	String newEndTime;
	@When("the user tries to change the business hour {string} at {string} to be on {string} starting at {string} and ending at {string}")
	public void the_user_tries_to_change_the_business_hour_at_to_be_on_starting_at_and_ending_at(String string, String string2, String string3, String string4, String string5) {
		for (BusinessHour bh : flexiBook.getBusiness().getBusinessHours()) {
			if (string.equals(bh.getDayOfWeek().toString())) {
				prevDay = bh.getDayOfWeek().toString();
				prevStartTime = bh.getStartTime().toString();
				prevEndTime = bh.getEndTime().toString();
				newDay = string+":00";
				newStartTime = string4+":00";
				newEndTime = string5+":00";
			}
		}
		try {
			FlexiBookController.updateBusinessHour(string, string2, string3, string4, string5);
		} catch (InvalidInputException e) {
			exception = e;
		}	
	}
	/**
	 * @author Julie
	 */
	@Then("the business hour shall {string} be updated")
	public void the_business_hour_shall_be_updated(String string) {
		for (BusinessHour bh : flexiBook.getBusiness().getBusinessHours()) {
			if (string.equals("not")) {
				if (prevDay.equals(bh.getDayOfWeek().toString())) {
					assertEquals(prevStartTime, bh.getStartTime().toString());
					assertEquals(prevEndTime, bh.getEndTime().toString());
				}
			}
			else {
				if (newDay.equals(bh.getDayOfWeek().toString())) {
					assertEquals(newStartTime, bh.getStartTime().toString());
					assertEquals(newEndTime, bh.getEndTime().toString());
				}
			}
		}
	}
	/**
	 * @author Julie
	 */
	int numberOfBusinessHours2;
	@When("the user tries to remove the business hour starting {string} at {string}")
	public void the_user_tries_to_remove_the_business_hour_starting_at(String string, String string2) {
		numberOfBusinessHours2 = flexiBook.getBusiness().getBusinessHours().size();
		try {
			FlexiBookController.removeBusinessHour(string, string2);
		} catch (InvalidInputException e) {
			exception = e;
		}	
	}
	/**
	 * @author Julie
	 */
	@Then("the business hour starting {string} at {string} shall {string} exist")
	public void the_business_hour_starting_at_shall_exist(String string, String string2, String string3) throws ParseException {
		Time startTime = FlexiBookUtil.getTimeFromString(string2);
		for (BusinessHour bh : FlexiBookApplication.getFlexiBook().getBusiness().getBusinessHours()) {
			if (string.equals(bh.getDayOfWeek().toString()) && startTime.equals(bh.getStartTime())) {
				if (string3.isEmpty()) {
					assertEquals(numberOfBusinessHours2, flexiBook.getBusiness().getBusinessHours().size());
					return;
				} else { 
					fail();
				}
			}
		}
	}
	/**
	 * @author Julie
	 */
	@Then("an error message {string} shall {string} be raised")
	public void an_error_message_shall_be_raised(String string, String string2) {
		if (string2.equals("not")) {
			assertEquals(exception, null);
		}
		else { 
			assertEquals(string, exception.getMessage());
		}
	}
	/**
	 * @author Julie
	 */
	String prevStartDate;
	String prevStartTime2;
	@When("the user tries to change the {string} on {string} at {string} to be with start date {string} at {string} and end date {string} at {string}")
	public void the_user_tries_to_change_the_on_at_to_be_with_start_date_at_and_end_date_at(String string, String string2, String string3, String string4, String string5, String string6, String string7) {
		prevStartDate = string2;
		prevStartTime2 = string3+":00";
		try {
			FlexiBookController.updateTimeSlot(string, string2, string3, string4, string5, string6, string7);
		} catch (InvalidInputException e) {
			exception = e;
		}	
	}
	/**
	 * @author Julie
	 */
	@Then("the {string} shall {string} updated with start date {string} at {string} and end date {string} at {string}")
	public void the_shall_be_updated_with_start_date_at_and_end_date_at(String string, String string2, String string3, String string4, String string5, String string6) {
		if (string2.equals("not")) {
			if (string.equals("vacation")) {
				for (TimeSlot ts : flexiBook.getBusiness().getVacation()) {
					if (prevStartDate.equals(ts.getStartDate().toString())) {
						assertEquals(prevStartTime2, ts.getStartTime().toString());
					}
				}
			}
			else {
				for (TimeSlot ts : flexiBook.getBusiness().getHolidays()) {
					if (prevStartDate.equals(ts.getStartDate().toString())) {
						assertEquals(prevStartTime2, ts.getStartTime().toString());
					}
				}
			}
		}
		else {
			if (string.equals("vacation")) {
				for (TimeSlot ts : flexiBook.getBusiness().getVacation()) {
					if (string3.equals(ts.getStartDate().toString())) {
						assertEquals(string4+":00", ts.getStartTime().toString());
						assertEquals(string5, ts.getEndDate().toString());
						assertEquals(string6+":00", ts.getEndTime().toString());
					}
				}
			}
			else {
				for (TimeSlot ts : flexiBook.getBusiness().getHolidays()) {
					if (string3.equals(ts.getStartDate().toString())) {
						assertEquals(string4+":00", ts.getStartTime().toString());
						assertEquals(string5, ts.getEndDate().toString());
						assertEquals(string6+":00", ts.getEndTime().toString());
					}
				}
			}
		}
	}
	/**
	 * @author Julie
	 */
	int numberOfHolidays;
	int numberofVacations;
	@When("the user tries to remove an existing {string} with start date {string} at {string} and end date {string} at {string}")
	public void the_user_tries_to_remove_an_existing_with_start_date_at_and_end_date_at(String string, String string2, String string3, String string4, String string5) {
		numberOfHolidays = flexiBook.getBusiness().getHolidays().size();
		numberofVacations = flexiBook.getBusiness().getVacation().size();
		try {
			FlexiBookController.removeTimeSlot(string, string2, string3, string4, string5);
		} catch (InvalidInputException e) {
			exception = e;
		}	
	}
	/**
	 * @author Julie
	 */
	@Then("the {string} with start date {string} at {string} shall {string} exist")
	public void the_with_start_date_at_shall_exist(String string, String string2, String string3, String string4) {
		if (string4.equals("not")) {
			if (string.equals("holiday")) {
				assertEquals(numberOfHolidays-1, flexiBook.getBusiness().getHolidays().size());
			}
			else {
				assertEquals(numberofVacations-1, flexiBook.getBusiness().getVacation().size());
			}
		}
		else {
			if (string.equals("holiday")) {
				assertEquals(numberOfHolidays, flexiBook.getBusiness().getHolidays().size());
			}
			else {
				assertEquals(numberofVacations, flexiBook.getBusiness().getVacation().size());
			}
		}
	}
	
	//================================================================================
    // AppointmentManagement
    //================================================================================
	
	/**
	 * @author theodore
	 */
	private void setCustomerFromString(String userstring) {
		for (Customer c : flexiBook.getCustomers()) {
			if (c.getUsername().equals(userstring)) {
				FlexiBookApplication.setCurrentUser(c);
			}
		}
	}
	/**
	 * @author theodore
	 */
	@Given("{string} has {int} no-show records")
	public void has_no_show_records(String string, Integer int1) {
		Customer cust = null;
		for (Customer c : flexiBook.getCustomers()) {
			if (c.getUsername().equals(string)) {
				cust = c;
			}
		}
		cust.resetNoShowCount();
		for (int i = 0; i < int1; i++) {
			cust.incrementNoShowCount();
		}
	}
	String apptService;
	String apptDate;
	String apptTime;
	/**
	 * @author theodore
	 */
	@When("{string} makes a {string} appointment for the date {string} and time {string} at {string}")
	public void makes_a_appointment_for_the_date_and_time_at(String cust, String bservice, String adate, String atime, String ctime) {
		setCustomerFromString(cust);
		setTimeFromString(ctime);
		try {
			FlexiBookController.makeAppointment(cust, adate, bservice, atime);
		} catch (InvalidInputException e) {
			exception = e;
			System.err.println(e);
		}
		apptService = bservice;
		apptDate = adate;
		apptTime = atime;
	}
	/**
	 * @author theodore
	 */
	@When("{string} attempts to change the service in the appointment to {string} at {string}")
	public void attempts_to_change_the_service_in_the_appointment_to_at(String cust, String serv, String ctime) {
		setCustomerFromString(cust);
		setTimeFromString(ctime);
		try {
			FlexiBookController.updateAppointment(cust, apptService, apptDate, apptTime, serv);
			apptService = serv;
		} catch (InvalidInputException e) {
			exception = e;
			System.err.println(e);
		}
	}
	/**
	 * @author theodore
	 */
	@When("{string} makes a {string} appointment without choosing optional services for the date {string} and time {string} at {string}")
	public void makes_a_appointment_without_choosing_optional_services_for_the_date_and_time_at(String cust, String bservice, String adate, String atime, String ctime) {
		setCustomerFromString(cust);
		setTimeFromString(ctime);
		try {
			FlexiBookController.makeAppointment(cust, adate, bservice, "", atime);
		} catch (InvalidInputException e) {
			exception = e;
			System.err.println(e);
		}
		apptService = bservice;
		apptDate = adate;
		apptTime = atime;
	}
	/**
	 * @author theodore
	 */
	@When("{string} attempts to update the date to {string} and time to {string} at {string}")
	public void attempts_to_update_the_date_to_and_time_to_at(String cust, String adate, String atime, String ctime) {
		setCustomerFromString(cust);
		setTimeFromString(ctime);
		try {
			FlexiBookController.updateAppointment(cust, apptService, apptDate, apptTime, adate, atime);
			apptDate = adate;
			apptTime = atime;
		} catch (InvalidInputException e) {
			exception = e;
			System.err.println(e);
		}
	}
	/**
	 * @author theodore
	 */
	@When("{string} attempts to add the optional service {string} to the service combo in the appointment at {string}")
	public void attempts_to_add_the_optional_service_to_the_service_combo_in_the_appointment_at(String cust, String serv, String ctime) {
		setCustomerFromString(cust);
		setTimeFromString(ctime);
		try {
			FlexiBookController.updateAppointment(cust, true, serv, apptService, apptDate, apptTime);
		} catch (InvalidInputException e) {
			exception = e;
			System.err.println(e);
		}
	}

	/**
	 * @author theodore
	 */
	Appointment appt = null;
	@Then("the appointment shall be booked")
	public void the_appointment_shall_be_booked() {
		try {
			Date startDate = FlexiBookUtil.getDateFromString(apptDate);
			Time startTime = FlexiBookUtil.getTimeFromString(apptTime);
			for (Appointment a : flexiBook.getAppointments()) {
				if (a.getBookableService().getName().equals(apptService) && a.getTimeSlot().getStartDate().equals(startDate) && a.getTimeSlot().getStartTime().equals(startTime)) {
					appt = a;
					assertEquals(appt.getAppointmentStatus(), Appointment.AppointmentStatus.Booked);
					return;
				}
			}
			fail();
		} catch (ParseException e) {
			fail();
		}
	}
	/**
	 * @author theodore
	 */
	@Then("the service in the appointment shall be {string}")
	public void the_service_in_the_appointment_shall_be(String string) {
		assertEquals(appt.getBookableService().getName(), string);
	}
	/**
	 * @author theodore
	 */
	@Then("the service combo in the appointment shall be {string}")
	public void the_service_combo_in_the_appointment_shall_be(String string) {
		assertEquals(appt.getBookableService().getName(), string);
	}
	/**
	 * @author theodore
	 */
	@Then("the service combo shall have {string} selected services")
	public void the_service_combo_shall_have_selected_services(String string) {
		List<String> servs = Arrays.asList(string.split(","));
		assertEquals(appt.numberOfChosenItems(), servs.size());
		for (ComboItem s : appt.getChosenItems()) {
			assertTrue(servs.contains(s.getService().getName()));
		}
	}
	/**
	 * @author theodore
	 */
	@Then("the appointment shall be for the date {string} with start time {string} and end time {string}")
	public void the_appointment_shall_be_for_the_date_with_start_time_and_end_time(String date, String startTime, String endTime) {
		TimeSlot ts = appt.getTimeSlot();
		try {
			assertEquals(FlexiBookUtil.getDateFromString(date), ts.getStartDate());
			assertEquals(FlexiBookUtil.getTimeFromString(startTime), ts.getStartTime());
			assertEquals(FlexiBookUtil.getTimeFromString(endTime), ts.getEndTime());
		} catch (ParseException e) {
			fail();
		}
	}
	/**
	 * @author theodore
	 */
	@Then("the username associated with the appointment shall be {string}")
	public void the_username_associated_with_the_appointment_shall_be(String string) {
		assertEquals(appt.getCustomer().getUsername(), string);
	}
	/**
	 * @author theodore
	 */
	@Then("the user {string} shall have {int} no-show records")
	public void the_user_shall_have_no_show_records(String string, Integer int1) {
		Customer cust = null;
		for (Customer c : flexiBook.getCustomers()) {
			if (c.getUsername().equals(string)) {
				cust = c;
			}
		}
		assertEquals(int1, cust.getNoShowCount());
	}
	/**
	 * @author theodore
	 */
	@Then("the system shall have {int} appointments")
	public void the_system_shall_have_appointments(Integer int1) {
		assertEquals(int1, flexiBook.numberOfAppointments());
	}
}