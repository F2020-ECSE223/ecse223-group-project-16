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
import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
		System.out.println(flexiBook.getCustomers().size());
		System.out.println(flexiBook.getOwner().getUsername());
		boolean customerExists;
		
		for (Map<String, String> columns : rows) {
			customerExists = false;
			for (Customer c : flexiBook.getCustomers()) {
				System.out.println(columns.get("username") + " " + c.getUsername());
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
			FlexiBookController.updateUserAccount(FlexiBookApplication.getCurrentUser().getUsername(), string, string2);
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
	* @author theodore
	*/
	@Given("the following appointments exist in the system:")
	public void the_following_appointments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    	List<Map<String, String>> appointmentData = dataTable.asMaps();
    	for (Map<String, String> a : appointmentData) {
    		Customer cust = null;
    		for (Customer cu : flexiBook.getCustomers()) {
    			if (cu.getUsername().equals(a.get("customer"))) {
    				cust = cu;
    				break;
    			}
    		}
    		BookableService bkable = null;
    		for (BookableService b : flexiBook.getBookableServices()) {
    			if (b.getName().equals(a.get("serviceName"))) {
    				bkable = b;
    				break;
    			}
    		}
    		TimeSlot timeSlotSelected = null;
    		try {
    			Date date = FlexiBookUtil.getDateFromString(a.get("date"));
    			Time startTime = FlexiBookUtil.getTimeFromString(a.get("startTime"));
    			Time endTime = FlexiBookUtil.getTimeFromString(a.get("endTime"));
    			timeSlotSelected = new TimeSlot(date, startTime, date, endTime, flexiBook);
    		} catch (ParseException e) {
    			fail();
    		}
    		Appointment appt = new Appointment(cust, bkable, timeSlotSelected, flexiBook);
    		if (bkable instanceof ServiceCombo) {
    			ServiceCombo sc = (ServiceCombo) bkable;
        		for (ComboItem c : sc.getServices()) {
        			if (c.getMandatory()) {
        				appt.addChosenItem(c);
        			} else {
        				for (String s : a.get("selectedComboItems").split(",")) {
        					if (c.getService().getName().equals(s)) {
        						appt.addChosenItem(c);
        						break;
        					}
        				}
        			}
	    		}
    		}
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
}
