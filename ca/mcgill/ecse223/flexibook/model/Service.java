/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;
import java.util.*;

// line 21 "../../../../../../model.ump"
// line 109 "../../../../../../model.ump"
public abstract class Service
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Service Attributes
  private String name;

  //Service Associations
  private List<Appointment> appointmentsOfService;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Service(String aName)
  {
    name = aName;
    appointmentsOfService = new ArrayList<Appointment>();
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

  public String getName()
  {
    return name;
  }
  /* Code from template association_GetMany */
  public Appointment getAppointmentsOfService(int index)
  {
    Appointment aAppointmentsOfService = appointmentsOfService.get(index);
    return aAppointmentsOfService;
  }

  public List<Appointment> getAppointmentsOfService()
  {
    List<Appointment> newAppointmentsOfService = Collections.unmodifiableList(appointmentsOfService);
    return newAppointmentsOfService;
  }

  public int numberOfAppointmentsOfService()
  {
    int number = appointmentsOfService.size();
    return number;
  }

  public boolean hasAppointmentsOfService()
  {
    boolean has = appointmentsOfService.size() > 0;
    return has;
  }

  public int indexOfAppointmentsOfService(Appointment aAppointmentsOfService)
  {
    int index = appointmentsOfService.indexOf(aAppointmentsOfService);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAppointmentsOfService()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Appointment addAppointmentsOfService(DateTime aStartDateTime, DateTime aEndDateTime, Customer aBookedCustomer)
  {
    return new Appointment(aStartDateTime, aEndDateTime, this, aBookedCustomer);
  }

  public boolean addAppointmentsOfService(Appointment aAppointmentsOfService)
  {
    boolean wasAdded = false;
    if (appointmentsOfService.contains(aAppointmentsOfService)) { return false; }
    Service existingSelectedService = aAppointmentsOfService.getSelectedService();
    boolean isNewSelectedService = existingSelectedService != null && !this.equals(existingSelectedService);
    if (isNewSelectedService)
    {
      aAppointmentsOfService.setSelectedService(this);
    }
    else
    {
      appointmentsOfService.add(aAppointmentsOfService);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAppointmentsOfService(Appointment aAppointmentsOfService)
  {
    boolean wasRemoved = false;
    //Unable to remove aAppointmentsOfService, as it must always have a selectedService
    if (!this.equals(aAppointmentsOfService.getSelectedService()))
    {
      appointmentsOfService.remove(aAppointmentsOfService);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAppointmentsOfServiceAt(Appointment aAppointmentsOfService, int index)
  {  
    boolean wasAdded = false;
    if(addAppointmentsOfService(aAppointmentsOfService))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAppointmentsOfService()) { index = numberOfAppointmentsOfService() - 1; }
      appointmentsOfService.remove(aAppointmentsOfService);
      appointmentsOfService.add(index, aAppointmentsOfService);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAppointmentsOfServiceAt(Appointment aAppointmentsOfService, int index)
  {
    boolean wasAdded = false;
    if(appointmentsOfService.contains(aAppointmentsOfService))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAppointmentsOfService()) { index = numberOfAppointmentsOfService() - 1; }
      appointmentsOfService.remove(aAppointmentsOfService);
      appointmentsOfService.add(index, aAppointmentsOfService);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAppointmentsOfServiceAt(aAppointmentsOfService, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=appointmentsOfService.size(); i > 0; i--)
    {
      Appointment aAppointmentsOfService = appointmentsOfService.get(i - 1);
      aAppointmentsOfService.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "]";
  }
}