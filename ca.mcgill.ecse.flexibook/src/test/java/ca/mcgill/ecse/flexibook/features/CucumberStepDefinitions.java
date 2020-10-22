package ca.mcgill.ecse.flexibook.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
import java.util.stream.IntStream;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.util.FlexiBookUtil;
import ca.mcgill.ecse.flexibook.util.SystemTime;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {
	
	private FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
	private Exception exception;
	
	/**
	 * Setup environment before each scenario
	 */
	@Before
	public void setup() {
		flexiBook = FlexiBookApplication.getFlexiBook();
	}
	
	/**
	 * Teardown environment after each scenario
	 */
	@After
	public void teardown() {
		FlexiBookApplication.unsetCurrentUser();
		
		if (flexiBook.hasOwner()) {
			flexiBook.getOwner().delete();
		}
		
		for (Customer customer : new ArrayList<Customer>(flexiBook.getCustomers())) {
			customer.delete();
		}
		
		for (BookableService bookableService : new ArrayList<BookableService>(flexiBook.getBookableServices())) {
			bookableService.delete();
		}

		for (Appointment a : new ArrayList<Appointment>(flexiBook.getAppointments())){
			a.delete();
		}
		
		exception = null;
	}
		
	@Given("a Flexibook system exists")
	public void a_flexibook_system_exists() {
	    assertTrue(FlexiBookApplication.getFlexiBook() != null);
	}

	//================================================================================
    // DeleteCustomerAccount
    //================================================================================

	/**
	 * @author louca
	 * @param string username
	 * @param string2 password
	 */
	@Given("an owner account exists in the system with username {string} and password {string}")
	public void an_owner_account_exists_in_the_system_with_username_and_password(String string, String string2) {
		if (!flexiBook.hasOwner()) {
			new Owner(string, string2, flexiBook);
		}
	}
	/**
	 * @author louca
	 * @param dataTable username, password rows
	 */
	@Given("the following customers exist in the system:")
	public void the_following_customers_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		
		for (Map<String, String> columns : rows) {
			new Customer(columns.get("username"), columns.get("password"), flexiBook);
		}
	}
	/**
	 * @@author louca
	 * @param string username
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
	 * @param string username
	 */
	@Given("the user is logged in to an account with username {string}")
	public void the_user_is_logged_in_to_an_account_with_username(String string) {
		if (string.equals("owner")) {
			FlexiBookApplication.setCurrentUser(flexiBook.getOwner());
		} else {
			for (Customer customer : flexiBook.getCustomers()) {
				if (customer.getUsername().equals(string)) {
					FlexiBookApplication.setCurrentUser(customer);
				}
			}
		}
	}
	
	List<Appointment> allAppointmentsOfDeletedCustomer;
	
	/**
	 * @author louca
	 * @param string username
	 */
	@When("the user tries to delete account with the username {string}")
	public void the_user_tries_to_delete_account_with_the_username(String string) {
		User user = FlexiBookController.getUserByUsername(string);
		if (user != null && user instanceof Customer) {
			allAppointmentsOfDeletedCustomer = ((Customer) user).getAppointments();
		}
		
	    try {
			FlexiBookController.deleteCustomerAccount(string);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}
	/**
	 * @author louca
	 * @param string username
	 */
	@Then("the account with the username {string} does not exist")
	public void the_account_with_the_username_does_not_exist(String string) {
	    assertEquals(null, FlexiBookController.getCustomerByUsername(string));
	}
	/**
	 * @author louca
	 * @param string username
	 */
	@Then("all associated appointments of the account with the username {string} shall not exist")
	public void all_associated_appointments_of_the_account_with_the_username_shall_not_exist(String string) {
	    for (Appointment appointment : allAppointmentsOfDeletedCustomer) {
	    	assertEquals(null, appointment);
	    }
	}
	/**
	 * @author louca
	 */
	@Then("the user shall be logged out")
	public void the_user_shall_be_logged_out() {
	    assertEquals(null, FlexiBookApplication.getCurrentUser());
	}
	/**
	 * @author louca
	 * @param string username
	 */
	@Then("the account with the username {string} exists")
	public void the_account_with_the_username_exists(String string) {
	    assertTrue(FlexiBookController.getUserByUsername(string) != null);
	}
	/**
	 * @author louca
	 * @param string error message
	 */
	@Then("an error message {string} shall be raised")
	public void an_error_message_shall_be_raised(String string) {
	    assertEquals(string, exception.getMessage());
	}

	//================================================================================
    // SignUpCustomerAccount
    //================================================================================
	
	@Given("there is no existing username {string}")
	public void there_is_no_existing_username(String string) {
		if (string.equals("owner")) {
			assertTrue(!flexiBook.hasOwner());
		} else {
			for (Customer customer : flexiBook.getCustomers()) {
		    	assertTrue(!customer.getUsername().equals(string));
		    }
		}
	}
	
	int priorCustomersCount;
	
	@When("the user provides a new username {string} and a password {string}")
	public void the_user_provides_a_new_username_and_a_password(String string, String string2) {
		priorCustomersCount = flexiBook.getCustomers().size();
		
	    try {
			FlexiBookController.createCustomerAccount(string, string2);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}
	@Then("a new customer account shall be created")
	public void a_new_customer_account_shall_be_created() {
	    assertEquals(priorCustomersCount + 1, flexiBook.getCustomers().size());
	}
	@Then("the account shall have username {string} and password {string}")
	public void the_account_shall_have_username_and_password(String string, String string2) {
		if (string.equals("owner")) {
			Owner owner = flexiBook.getOwner();
			assertEquals(string, owner.getUsername());
			assertEquals(string2, owner.getPassword());
		} else {
			Customer customer = null;
		    for (Customer c : flexiBook.getCustomers()) {
		    	if (c.getUsername().equals(string) && c.getPassword().equals(string2)) {
		    		customer = c;
		    	}
		    }
		    assertTrue(customer != null);
		}
	}
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
	@Then("no new account shall be created")
	public void no_new_account_shall_be_created() {
	    assertEquals(priorCustomersCount, flexiBook.getCustomers().size());
	}

	//================================================================================
    // UpdateAccount
    //================================================================================
	
	String priorUsername;
	String priorPassword;
	
	@When("the user tries to update account with a new username {string} and password {string}")
	public void the_user_tries_to_update_account_with_a_new_username_and_password(String string, String string2) {
		priorUsername = FlexiBookApplication.getCurrentUser().getUsername();
		priorPassword = FlexiBookApplication.getCurrentUser().getPassword();
		
	    try {
			FlexiBookController.updateUserAccount(FlexiBookApplication.getCurrentUser().getUsername(), string, string2);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}
	@Then("the account shall not be updated")
	public void the_account_shall_not_be_updated() {
	    assertEquals(priorUsername, FlexiBookApplication.getCurrentUser().getUsername());
	    assertEquals(priorPassword, FlexiBookApplication.getCurrentUser().getPassword());
	}
	
	//================================================================================
    // MakeAppointments
    //================================================================================	

	// tested
	@Given("the system's time and date is {string}")
	public void the_system_s_time_and_date_is(String string) {
		String[] dateTime = string.split("\\+");
		Date date = null;
		Time time = null;
		try {
			date = FlexiBookUtil.getDateFromString(dateTime[0]);
			time = FlexiBookUtil.getTimeFromString(dateTime[1]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SystemTime.setTesting(date, time);
	}

	// tested
	@Given("an owner account exists in the system")
	public void an_owner_account_exists_in_the_system() {
	    if (!flexiBook.hasOwner()) {
			new Owner("owner", "password", flexiBook);
		}
	}

	// tested
	@Given("a business exists in the system")
	public void a_business_exists_in_the_system() {
	    if(!flexiBook.hasBusiness()){
			flexiBook.setBusiness(
				new Business("Flexibook", "101 Sherbrooke", "5148888888", "flexi@mcgill.ca", flexiBook)
			);
		}
	}

   // tested 4 services are present
	@Given("the following services exist in the system:")
	public void the_following_services_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		dataTable.asMaps().stream().forEach(x -> 
			new Service(
				x.get("name"), 
				flexiBook, 
				Integer.parseInt(x.get("duration")), 
				Integer.parseInt(x.get("downtimeDuration")), 
				Integer.parseInt(x.get("downtimeStart")))
		);
	}

	// tested, 6 bookables and combos are linked properly
	@Given("the following service combos exist in the system:")
	public void the_following_service_combos_exist_in_the_system(io.cucumber.datatable.DataTable dataTable)
			throws InvalidInputException {
		dataTable.asMaps().stream().forEach(x -> {
				ServiceCombo sc = new ServiceCombo(x.get("name"), FlexiBookApplication.getFlexiBook());

				String[] services = x.get("services").split(",");
				String[] mandatory = x.get("mandatory").split(",");
				
				IntStream.range(0, Math.min(services.length, mandatory.length)).forEach(i -> {
						ComboItem c = sc.addService(Boolean.parseBoolean(mandatory[i]), 
							(Service) FlexiBookApplication.getFlexiBook().getBookableServices().stream().filter(y -> 
								y.getName().equals(services[i])).collect(Collectors.toList()).get(0)
						); 
						if(x.get("mainService").equals(c.getService().getName())){
							sc.setMainService(c);
						}
						else{
							sc.addService(c);
						}
					}
				);
			}
		);
	}

	// Tested
	@Given("the business has the following opening hours")
	public void the_business_has_the_following_opening_hours(io.cucumber.datatable.DataTable dataTable) {
		dataTable.asMaps().stream().forEach(x -> 
			{
				try {
					flexiBook.getBusiness().addBusinessHour(new BusinessHour(
						FlexiBookUtil.getDayOfWeek(x.get("day")),
						FlexiBookUtil.getTimeFromString(x.get("startTime")),
						FlexiBookUtil.getTimeFromString(x.get("endTime")), 
						flexiBook));
				} catch (ParseException e) {
					exception = e;
				}
			}
		);
	}

	// tested and this works
	@Given("the business has the following holidays")
	public void the_business_has_the_following_holidays(io.cucumber.datatable.DataTable dataTable) {
		dataTable.asMaps().stream().forEach(x -> 
			{
				try {
					flexiBook.getBusiness().addHoliday(new TimeSlot(
						FlexiBookUtil.getDateFromString(x.get("startDate")),
						FlexiBookUtil.getTimeFromString(x.get("startTime")),
						FlexiBookUtil.getDateFromString(x.get("endDate")),
						FlexiBookUtil.getTimeFromString(x.get("endTime")), 
						flexiBook));
				} 
				catch (ParseException e) {	
					e.printStackTrace();
				}
			}
		);
	}

	// tested and this works
	@Given("the following appointments exist in the system:")
	public void the_following_appointments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable)
			throws Exception {
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
							String[] optServices = x.get("optServices").split(",");
							HashSet<String> set = new HashSet<>(Arrays.stream(optServices).collect(Collectors.toSet()));
							sc.getServices().stream().filter(y -> set.contains(y.getService().getName())).forEach(y -> a.addChosenItem(y));;
						}
						c.addAppointment(a);
					}
				}
			}
		);
	}


	@Given("{string} is logged in to their account")
	public void is_logged_in_to_their_account(String string) {
		if (string.equals("owner")) {
			FlexiBookApplication.setCurrentUser(flexiBook.getOwner());
		} else {
			FlexiBookApplication.setCurrentUser(flexiBook.getCustomers().stream()
				.filter(x -> x.getUsername().equals(string)).findAny().get());
		}
	}

	int appointmentCount;

	@When("{string} schedules an appointment on {string} for {string} at {string}")
	public void schedules_an_appointment_on_for_at(String string, String string2, String string3, String string4) {
		appointmentCount = flexiBook.getAppointments().size();
		try {
			FlexiBookController.makeAppointment(string, string2, string3, string4);
		} catch (InvalidInputException e) {
			exception = e;
		}		
	}

	@When("{string} schedules an appointment on {string} for {string} with {string} at {string}")
	public void schedules_an_appointment_on_for_with_at(String string, String string2, String string3, String string4, String string5) {
		appointmentCount = flexiBook.getAppointments().size();
		try {
			FlexiBookController.makeAppointment(string, string2, string3, string4, string5);
		} catch (InvalidInputException e) {
			exception = e;
		}	
	}

	// tested and it works
	@Then("{string} shall have a {string} appointment on {string} from {string} to {string}")
	public void shall_have_a_appointment_on_from_to(String string, String string2, String string3, String string4, String string5)
			throws Exception {
		Optional<Customer> c = flexiBook.getCustomers().stream().filter(x -> x.getUsername().equals(string)).findFirst();
		Date date = null;
		try {
			date = FlexiBookUtil.getDateFromString(string3);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Time startTime = null;
		try {
			startTime = FlexiBookUtil.getTimeFromString(string4);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Time endTime = null;
		try {
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

	@Then("there shall be {int} more appointment in the system")
	public void there_shall_be_more_appointment_in_the_system(Integer int1) {
		assertEquals(appointmentCount + int1, flexiBook.getAppointments().size());
	}

	@Then("the system shall report {string}")
	public void the_system_shall_report(String string) {
		assertEquals(string, exception.getMessage());
	}


	//================================================================================
    // UpdateAppointments
	//================================================================================	
	
	@Given("{string} has a {string} appointment with optional sevices {string} on {string} at {string}")
	public void has_a_appointment_with_optional_sevices_on_at(String string, String string2, String string3, String string4, String string5)
			throws InvalidInputException {
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
	@When("{string} attempts to update their {string} appointment on {string} at {string} to {string} at {string}")
	public void attempts_to_update_their_appointment_on_at_to_at(String string, String string2, String string3, String string4, String string5, String string6)
			throws Exception {
		appointmentCount = flexiBook.getAppointments().size();
		result = false;
		try {
			result = FlexiBookController.updateAppointment(string, string2, string3, string4, string5, string6);	
		} 
		catch (InvalidInputException e) {
			exception = e;
		}
	}

	@When("{string} attempts to {string} {string} from their {string} appointment on {string} at {string}")
	public void attempts_to_from_their_appointment_on_at(String string, String string2, String string3, String string4, String string5, String string6)
			throws InvalidInputException {
		appointmentCount = flexiBook.getAppointments().size();
		result = false;
		try {
			result = FlexiBookController.updateAppointment(string, string2.equals("add"), string3, string4, string5, string6);
		} 
		catch (InvalidInputException e) {
			exception = e;
		}
	}

	@When("{string} attempts to update {string}'s {string} appointment on {string} at {string} to {string} at {string}")
	public void attempts_to_update_s_appointment_on_at_to_at(String string, String string2, String string3, String string4, String string5, String string6, String string7) {
		try {
			FlexiBookController.updateAppointment(string2, string3, string4, string5, string6, string7);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}

	@Then("the system shall report that the update was {string}")
	public void the_system_shall_report_that_the_update_was(String string) {
		if(result){
			assertEquals("successful", string);
		}
		else{
			assertEquals("unsuccessful", string);
		}
	}
	
	//================================================================================
    // CancelAppointments
	//================================================================================	
	
	
	@When("{string} attempts to cancel their {string} appointment on {string} at {string}")
	public void attempts_to_cancel_their_appointment_on_at(String string, String string2, String string3, String string4) {
		appointmentCount = flexiBook.getAppointments().size();
		try {
			FlexiBookController.cancelAppointment(string, string2, string3, string4);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}

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

	@Then("{string}'s {string} appointment on {string} at {string} shall be removed from the system")
	public void s_appointment_on_at_shall_be_removed_from_the_system(String string, String string2, String string3, String string4) {
		assertEquals(flexiBook.getAppointments().size(), 0);
	}

	@Then("there shall be {int} less appointment in the system")
	public void there_shall_be_less_appointment_in_the_system(Integer int1) {
		assertEquals(appointmentCount - int1, flexiBook.getAppointments().size());
	}
}
