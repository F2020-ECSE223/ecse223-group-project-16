/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;
import java.util.*;

// line 50 "../../../../../../model.ump"
// line 116 "../../../../../../model.ump"
public class Appointment extends ScheduledEvent
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Appointment Associations
  private Service selectedService;
  private List<SingleService> selectedServiceComboOptions;
  private Customer bookedCustomer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Appointment(DateTime aStartDateTime, DateTime aEndDateTime, Service aSelectedService, Customer aBookedCustomer)
  {
    super(aStartDateTime, aEndDateTime);
    boolean didAddSelectedService = setSelectedService(aSelectedService);
    if (!didAddSelectedService)
    {
      throw new RuntimeException("Unable to create appointmentsOfService due to selectedService. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    selectedServiceComboOptions = new ArrayList<SingleService>();
    boolean didAddBookedCustomer = setBookedCustomer(aBookedCustomer);
    if (!didAddBookedCustomer)
    {
      throw new RuntimeException("Unable to create appointment due to bookedCustomer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Service getSelectedService()
  {
    return selectedService;
  }
  /* Code from template association_GetMany */
  public SingleService getSelectedServiceComboOption(int index)
  {
    SingleService aSelectedServiceComboOption = selectedServiceComboOptions.get(index);
    return aSelectedServiceComboOption;
  }

  public List<SingleService> getSelectedServiceComboOptions()
  {
    List<SingleService> newSelectedServiceComboOptions = Collections.unmodifiableList(selectedServiceComboOptions);
    return newSelectedServiceComboOptions;
  }

  public int numberOfSelectedServiceComboOptions()
  {
    int number = selectedServiceComboOptions.size();
    return number;
  }

  public boolean hasSelectedServiceComboOptions()
  {
    boolean has = selectedServiceComboOptions.size() > 0;
    return has;
  }

  public int indexOfSelectedServiceComboOption(SingleService aSelectedServiceComboOption)
  {
    int index = selectedServiceComboOptions.indexOf(aSelectedServiceComboOption);
    return index;
  }
  /* Code from template association_GetOne */
  public Customer getBookedCustomer()
  {
    return bookedCustomer;
  }
  /* Code from template association_SetOneToMany */
  public boolean setSelectedService(Service aSelectedService)
  {
    boolean wasSet = false;
    if (aSelectedService == null)
    {
      return wasSet;
    }

    Service existingSelectedService = selectedService;
    selectedService = aSelectedService;
    if (existingSelectedService != null && !existingSelectedService.equals(aSelectedService))
    {
      existingSelectedService.removeAppointmentsOfService(this);
    }
    selectedService.addAppointmentsOfService(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSelectedServiceComboOptions()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addSelectedServiceComboOption(SingleService aSelectedServiceComboOption)
  {
    boolean wasAdded = false;
    if (selectedServiceComboOptions.contains(aSelectedServiceComboOption)) { return false; }
    selectedServiceComboOptions.add(aSelectedServiceComboOption);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSelectedServiceComboOption(SingleService aSelectedServiceComboOption)
  {
    boolean wasRemoved = false;
    if (selectedServiceComboOptions.contains(aSelectedServiceComboOption))
    {
      selectedServiceComboOptions.remove(aSelectedServiceComboOption);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSelectedServiceComboOptionAt(SingleService aSelectedServiceComboOption, int index)
  {  
    boolean wasAdded = false;
    if(addSelectedServiceComboOption(aSelectedServiceComboOption))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSelectedServiceComboOptions()) { index = numberOfSelectedServiceComboOptions() - 1; }
      selectedServiceComboOptions.remove(aSelectedServiceComboOption);
      selectedServiceComboOptions.add(index, aSelectedServiceComboOption);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSelectedServiceComboOptionAt(SingleService aSelectedServiceComboOption, int index)
  {
    boolean wasAdded = false;
    if(selectedServiceComboOptions.contains(aSelectedServiceComboOption))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSelectedServiceComboOptions()) { index = numberOfSelectedServiceComboOptions() - 1; }
      selectedServiceComboOptions.remove(aSelectedServiceComboOption);
      selectedServiceComboOptions.add(index, aSelectedServiceComboOption);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSelectedServiceComboOptionAt(aSelectedServiceComboOption, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBookedCustomer(Customer aBookedCustomer)
  {
    boolean wasSet = false;
    if (aBookedCustomer == null)
    {
      return wasSet;
    }

    Customer existingBookedCustomer = bookedCustomer;
    bookedCustomer = aBookedCustomer;
    if (existingBookedCustomer != null && !existingBookedCustomer.equals(aBookedCustomer))
    {
      existingBookedCustomer.removeAppointment(this);
    }
    bookedCustomer.addAppointment(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Service placeholderSelectedService = selectedService;
    this.selectedService = null;
    if(placeholderSelectedService != null)
    {
      placeholderSelectedService.removeAppointmentsOfService(this);
    }
    selectedServiceComboOptions.clear();
    Customer placeholderBookedCustomer = bookedCustomer;
    this.bookedCustomer = null;
    if(placeholderBookedCustomer != null)
    {
      placeholderBookedCustomer.removeAppointment(this);
    }
    super.delete();
  }

}