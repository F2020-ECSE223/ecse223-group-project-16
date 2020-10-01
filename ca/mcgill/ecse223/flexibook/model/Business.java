/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;
import java.util.*;

// line 69 "../../../../../../model.ump"
// line 134 "../../../../../../model.ump"
public class Business
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Business Attributes
  private String name;
  private String address;
  private String phoneNumber;
  private String emailAddress;

  //Business Associations
  private List<OpeningHours> weeklyOpeningHours;
  private List<LeaveOfAbsence> leavesOfAbsence;
  private List<Appointment> appointments;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Business(String aName, String aAddress, String aPhoneNumber, String aEmailAddress)
  {
    name = aName;
    address = aAddress;
    phoneNumber = aPhoneNumber;
    emailAddress = aEmailAddress;
    weeklyOpeningHours = new ArrayList<OpeningHours>();
    leavesOfAbsence = new ArrayList<LeaveOfAbsence>();
    appointments = new ArrayList<Appointment>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setAddress(String aAddress)
  {
    boolean wasSet = false;
    address = aAddress;
    wasSet = true;
    return wasSet;
  }

  public boolean setPhoneNumber(String aPhoneNumber)
  {
    boolean wasSet = false;
    phoneNumber = aPhoneNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmailAddress(String aEmailAddress)
  {
    boolean wasSet = false;
    emailAddress = aEmailAddress;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getAddress()
  {
    return address;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }
  /* Code from template association_GetMany */
  public OpeningHours getWeeklyOpeningHour(int index)
  {
    OpeningHours aWeeklyOpeningHour = weeklyOpeningHours.get(index);
    return aWeeklyOpeningHour;
  }

  public List<OpeningHours> getWeeklyOpeningHours()
  {
    List<OpeningHours> newWeeklyOpeningHours = Collections.unmodifiableList(weeklyOpeningHours);
    return newWeeklyOpeningHours;
  }

  public int numberOfWeeklyOpeningHours()
  {
    int number = weeklyOpeningHours.size();
    return number;
  }

  public boolean hasWeeklyOpeningHours()
  {
    boolean has = weeklyOpeningHours.size() > 0;
    return has;
  }

  public int indexOfWeeklyOpeningHour(OpeningHours aWeeklyOpeningHour)
  {
    int index = weeklyOpeningHours.indexOf(aWeeklyOpeningHour);
    return index;
  }
  /* Code from template association_GetMany */
  public LeaveOfAbsence getLeavesOfAbsence(int index)
  {
    LeaveOfAbsence aLeavesOfAbsence = leavesOfAbsence.get(index);
    return aLeavesOfAbsence;
  }

  public List<LeaveOfAbsence> getLeavesOfAbsence()
  {
    List<LeaveOfAbsence> newLeavesOfAbsence = Collections.unmodifiableList(leavesOfAbsence);
    return newLeavesOfAbsence;
  }

  public int numberOfLeavesOfAbsence()
  {
    int number = leavesOfAbsence.size();
    return number;
  }

  public boolean hasLeavesOfAbsence()
  {
    boolean has = leavesOfAbsence.size() > 0;
    return has;
  }

  public int indexOfLeavesOfAbsence(LeaveOfAbsence aLeavesOfAbsence)
  {
    int index = leavesOfAbsence.indexOf(aLeavesOfAbsence);
    return index;
  }
  /* Code from template association_GetMany */
  public Appointment getAppointment(int index)
  {
    Appointment aAppointment = appointments.get(index);
    return aAppointment;
  }

  public List<Appointment> getAppointments()
  {
    List<Appointment> newAppointments = Collections.unmodifiableList(appointments);
    return newAppointments;
  }

  public int numberOfAppointments()
  {
    int number = appointments.size();
    return number;
  }

  public boolean hasAppointments()
  {
    boolean has = appointments.size() > 0;
    return has;
  }

  public int indexOfAppointment(Appointment aAppointment)
  {
    int index = appointments.indexOf(aAppointment);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWeeklyOpeningHours()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addWeeklyOpeningHour(OpeningHours aWeeklyOpeningHour)
  {
    boolean wasAdded = false;
    if (weeklyOpeningHours.contains(aWeeklyOpeningHour)) { return false; }
    weeklyOpeningHours.add(aWeeklyOpeningHour);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeWeeklyOpeningHour(OpeningHours aWeeklyOpeningHour)
  {
    boolean wasRemoved = false;
    if (weeklyOpeningHours.contains(aWeeklyOpeningHour))
    {
      weeklyOpeningHours.remove(aWeeklyOpeningHour);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWeeklyOpeningHourAt(OpeningHours aWeeklyOpeningHour, int index)
  {  
    boolean wasAdded = false;
    if(addWeeklyOpeningHour(aWeeklyOpeningHour))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWeeklyOpeningHours()) { index = numberOfWeeklyOpeningHours() - 1; }
      weeklyOpeningHours.remove(aWeeklyOpeningHour);
      weeklyOpeningHours.add(index, aWeeklyOpeningHour);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWeeklyOpeningHourAt(OpeningHours aWeeklyOpeningHour, int index)
  {
    boolean wasAdded = false;
    if(weeklyOpeningHours.contains(aWeeklyOpeningHour))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWeeklyOpeningHours()) { index = numberOfWeeklyOpeningHours() - 1; }
      weeklyOpeningHours.remove(aWeeklyOpeningHour);
      weeklyOpeningHours.add(index, aWeeklyOpeningHour);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWeeklyOpeningHourAt(aWeeklyOpeningHour, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLeavesOfAbsence()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addLeavesOfAbsence(LeaveOfAbsence aLeavesOfAbsence)
  {
    boolean wasAdded = false;
    if (leavesOfAbsence.contains(aLeavesOfAbsence)) { return false; }
    leavesOfAbsence.add(aLeavesOfAbsence);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeLeavesOfAbsence(LeaveOfAbsence aLeavesOfAbsence)
  {
    boolean wasRemoved = false;
    if (leavesOfAbsence.contains(aLeavesOfAbsence))
    {
      leavesOfAbsence.remove(aLeavesOfAbsence);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLeavesOfAbsenceAt(LeaveOfAbsence aLeavesOfAbsence, int index)
  {  
    boolean wasAdded = false;
    if(addLeavesOfAbsence(aLeavesOfAbsence))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLeavesOfAbsence()) { index = numberOfLeavesOfAbsence() - 1; }
      leavesOfAbsence.remove(aLeavesOfAbsence);
      leavesOfAbsence.add(index, aLeavesOfAbsence);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLeavesOfAbsenceAt(LeaveOfAbsence aLeavesOfAbsence, int index)
  {
    boolean wasAdded = false;
    if(leavesOfAbsence.contains(aLeavesOfAbsence))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLeavesOfAbsence()) { index = numberOfLeavesOfAbsence() - 1; }
      leavesOfAbsence.remove(aLeavesOfAbsence);
      leavesOfAbsence.add(index, aLeavesOfAbsence);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addLeavesOfAbsenceAt(aLeavesOfAbsence, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAppointments()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addAppointment(Appointment aAppointment)
  {
    boolean wasAdded = false;
    if (appointments.contains(aAppointment)) { return false; }
    appointments.add(aAppointment);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAppointment(Appointment aAppointment)
  {
    boolean wasRemoved = false;
    if (appointments.contains(aAppointment))
    {
      appointments.remove(aAppointment);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAppointmentAt(Appointment aAppointment, int index)
  {  
    boolean wasAdded = false;
    if(addAppointment(aAppointment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAppointments()) { index = numberOfAppointments() - 1; }
      appointments.remove(aAppointment);
      appointments.add(index, aAppointment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAppointmentAt(Appointment aAppointment, int index)
  {
    boolean wasAdded = false;
    if(appointments.contains(aAppointment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAppointments()) { index = numberOfAppointments() - 1; }
      appointments.remove(aAppointment);
      appointments.add(index, aAppointment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAppointmentAt(aAppointment, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    weeklyOpeningHours.clear();
    leavesOfAbsence.clear();
    appointments.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "address" + ":" + getAddress()+ "," +
            "phoneNumber" + ":" + getPhoneNumber()+ "," +
            "emailAddress" + ":" + getEmailAddress()+ "]";
  }
}