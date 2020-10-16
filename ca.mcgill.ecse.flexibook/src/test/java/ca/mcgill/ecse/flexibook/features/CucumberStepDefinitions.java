package ca.mcgill.ecse.flexibook.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOBusinessHour.DayOfWeek;
import ca.mcgill.ecse.flexibook.model.*;

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
    // Login
    //================================================================================
	
	User currentUser;
	boolean initialHasOwner;
	
	@When("the user tries to log in with username {string} and password {string}")
	public void the_user_tries_to_log_in_with_username_and_password(String string, String string2) {
		try {
			initialHasOwner = flexiBook.hasOwner();
			currentUser = FlexiBookController.login(string, string2);
		} catch (InvalidInputException e) {
			exception = e;
		}
	}

	@Then("the user should be successfully logged in")
	public void the_user_should_be_successfully_logged_in() {
		assertEquals(currentUser, FlexiBookApplication.getCurrentUser());
	}

	@Then("the user should not be logged in")
	public void the_user_should_not_be_logged_in() {
		assertEquals(null, FlexiBookApplication.getCurrentUser());
	}

	@Then("the user shall be successfully logged in")
	public void the_user_shall_be_successfully_logged_in() {
		assertEquals(flexiBook.getOwner(), FlexiBookApplication.getCurrentUser());
	}

	@Then("a new account shall be created")
	public void a_new_account_shall_be_created() {
		assertEquals(true, !initialHasOwner);
	}
	
	
	//================================================================================
    // Logout
    //================================================================================
	
	@Given("the user is logged out")
	public void the_user_is_logged_out() {
		if (FlexiBookApplication.getCurrentUser() != null) {
			FlexiBookApplication.unsetCurrentUser();
		}
	}


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

	// TODO 
	@Given("the system's time and date is {string}")
	public void the_system_s_time_and_date_is(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}
	@Given("an owner account exists in the system")
	public void an_owner_account_exists_in_the_system() {
	    if (!flexiBook.hasOwner()) {
	    	new Owner ("owner", "ownerPass", flexiBook);
	    }
	}
	@Given("a business exists in the system")
	public void a_business_exists_in_the_system() {
	    if (!flexiBook.hasBusiness()) {
	    	new Business("businessName", "address", "phone", "email", flexiBook); 
	    }
	}
	@Given("the following services exist in the system:")
	public void the_following_services_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {			
		List<Map<String, String>> rows = dataTable.asMaps();
		
		for (Map<String, String> columns : rows) {
			new Service(columns.get("name"), flexiBook, Integer.parseInt(columns.get("duration")), 
					Integer.parseInt(columns.get("downtimeStart")), Integer.parseInt(columns.get("downtimeDuration")));
		}
		 
	}
	@Given("the following service combos exist in the system:")
	public void the_following_service_combos_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
	    List<Map<String, String>> rows = dataTable.asMaps();
	    
	    for (Map<String, String> columns : rows) {
			new ServiceCombo(columns.get("name"), flexiBook);
		}
	}
	@Given("the business has the following opening hours:")
	public void the_business_has_the_following_opening_hours(io.cucumber.datatable.DataTable dataTable) {
		 List<Map<String, String>> rows = dataTable.asMaps();
		 
		 for (Map<String, String> columns : rows) {
			ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek day = ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek.valueOf(columns.get("day"));
			Time startTime = Time.valueOf(columns.get("startTime"));
			Time endTime = Time.valueOf(columns.get("endTime"));
			
			flexiBook.getBusiness().addBusinessHour(new BusinessHour(day, startTime, endTime, flexiBook));
		 }
	}  
	@Given("the business has the following holidays:")
	public void the_business_has_the_following_holidays(io.cucumber.datatable.DataTable dataTable) {
		 List<Map<String, String>> rows = dataTable.asMaps();
		    
		 for (Map<String, String> columns : rows) {
			 flexiBook.getBusiness().addHoliday(new TimeSlot(Date.valueOf(columns.get("startDate")), 
					Time.valueOf(columns.get("startTime")), Date.valueOf(columns.get("endDate")), 
					Time.valueOf(columns.get("endTime")), flexiBook));
		 }
	}
	@Given("the following appointments exist in the system:")
	public void the_following_appointments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		Customer customer = null;
		BookableService bookableService = null;
		TimeSlot timeSlot = null;
		
	 
		for (Map<String, String> columns : rows) {
			
			for (Customer c : flexiBook.getCustomers()) {
		    	if (c.getUsername().equals(columns.get("customer"))) {
		    		customer = c;
		    	}
		    }
			
			for (BookableService b : flexiBook.getBookableServices()) {
		    	if (b.getName().equals(columns.get("serviceName"))) {
		    		bookableService = b;
		    	}
		    }
			
			timeSlot = new TimeSlot(Date.valueOf(columns.get("startDate")), Time.valueOf(columns.get("startTime")),
					Date.valueOf(columns.get("startDate")), Time.valueOf(columns.get("endTime")), flexiBook);
			
				
			new Appointment(customer, bookableService, timeSlot, flexiBook);
		}
	}
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
	@When("{string} requests the appointment calendar for the day of {string}")
	public void requests_the_appointment_calendar_for_the_day_of(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}
	@Then("the following slots shall be unavailable:")
	public void the_following_slots_shall_be_unavailable(io.cucumber.datatable.DataTable dataTable) {
	    // Write code here that turns the phrase above into concrete actions
	    // For automatic transformation, change DataTable to one of
	    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
	    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
	    // Double, Byte, Short, Long, BigInteger or BigDecimal.
	    //
	    // For other transformations you can register a DataTableType.
	    throw new io.cucumber.java.PendingException();
	}
	@Then("the following slots shall be available:")
	public void the_following_slots_shall_be_available(io.cucumber.datatable.DataTable dataTable) {
	    // Write code here that turns the phrase above into concrete actions
	    // For automatic transformation, change DataTable to one of
	    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
	    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
	    // Double, Byte, Short, Long, BigInteger or BigDecimal.
	    //
	    // For other transformations you can register a DataTableType.
	    throw new io.cucumber.java.PendingException();
	}



	
	
	
	
	
	
}
