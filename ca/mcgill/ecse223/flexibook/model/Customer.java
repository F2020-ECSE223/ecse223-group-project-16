/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;

import java.util.*;

// line 13 "model.ump"
// line 101 "model.ump"
public class Customer extends User
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Customer> customersByUsername = new HashMap<String, Customer>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Customer Attributes
  private String username;

  //Customer Associations
  private List<Appointment> bookedAppointments;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Customer(String aPassword, String aUsername)
  {
    super(aPassword);
    if (!setUsername(aUsername))
    {
      throw new RuntimeException("Cannot create due to duplicate username. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    bookedAppointments = new ArrayList<Appointment>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setUsername(String aUsername)
  {
    boolean wasSet = false;
    String anOldUsername = getUsername();
    if (anOldUsername != null && anOldUsername.equals(aUsername)) {
      return true;
    }
    if (hasWithUsername(aUsername)) {
      return wasSet;
    }
    username = aUsername;
    wasSet = true;
    if (anOldUsername != null) {
      customersByUsername.remove(anOldUsername);
    }
    customersByUsername.put(aUsername, this);
    return wasSet;
  }

  public String getUsername()
  {
    return username;
  }
  /* Code from template attribute_GetUnique */
  public static Customer getWithUsername(String aUsername)
  {
    return customersByUsername.get(aUsername);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithUsername(String aUsername)
  {
    return getWithUsername(aUsername) != null;
  }
  /* Code from template association_GetMany */
  public Appointment getBookedAppointment(int index)
  {
    Appointment aBookedAppointment = bookedAppointments.get(index);
    return aBookedAppointment;
  }

  /**
   * i.e. booked appointments
   */
  public List<Appointment> getBookedAppointments()
  {
    List<Appointment> newBookedAppointments = Collections.unmodifiableList(bookedAppointments);
    return newBookedAppointments;
  }

  public int numberOfBookedAppointments()
  {
    int number = bookedAppointments.size();
    return number;
  }

  public boolean hasBookedAppointments()
  {
    boolean has = bookedAppointments.size() > 0;
    return has;
  }

  public int indexOfBookedAppointment(Appointment aBookedAppointment)
  {
    int index = bookedAppointments.indexOf(aBookedAppointment);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfBookedAppointments()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Appointment addBookedAppointment(DateTime aStartDateTime, DateTime aEndDateTime, Service aSelectedService)
  {
    return new Appointment(aStartDateTime, aEndDateTime, aSelectedService, this);
  }

  public boolean addBookedAppointment(Appointment aBookedAppointment)
  {
    boolean wasAdded = false;
    if (bookedAppointments.contains(aBookedAppointment)) { return false; }
    Customer existingCustomer = aBookedAppointment.getCustomer();
    boolean isNewCustomer = existingCustomer != null && !this.equals(existingCustomer);
    if (isNewCustomer)
    {
      aBookedAppointment.setCustomer(this);
    }
    else
    {
      bookedAppointments.add(aBookedAppointment);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeBookedAppointment(Appointment aBookedAppointment)
  {
    boolean wasRemoved = false;
    //Unable to remove aBookedAppointment, as it must always have a customer
    if (!this.equals(aBookedAppointment.getCustomer()))
    {
      bookedAppointments.remove(aBookedAppointment);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addBookedAppointmentAt(Appointment aBookedAppointment, int index)
  {  
    boolean wasAdded = false;
    if(addBookedAppointment(aBookedAppointment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBookedAppointments()) { index = numberOfBookedAppointments() - 1; }
      bookedAppointments.remove(aBookedAppointment);
      bookedAppointments.add(index, aBookedAppointment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBookedAppointmentAt(Appointment aBookedAppointment, int index)
  {
    boolean wasAdded = false;
    if(bookedAppointments.contains(aBookedAppointment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBookedAppointments()) { index = numberOfBookedAppointments() - 1; }
      bookedAppointments.remove(aBookedAppointment);
      bookedAppointments.add(index, aBookedAppointment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addBookedAppointmentAt(aBookedAppointment, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    customersByUsername.remove(getUsername());
    for(int i=bookedAppointments.size(); i > 0; i--)
    {
      Appointment aBookedAppointment = bookedAppointments.get(i - 1);
      aBookedAppointment.delete();
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "username" + ":" + getUsername()+ "]";
  }
}