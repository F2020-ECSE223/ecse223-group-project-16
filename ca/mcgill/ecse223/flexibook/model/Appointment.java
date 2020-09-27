/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;
import java.util.*;

// line 55 "../../../../../../model.ump"
// line 119 "../../../../../../model.ump"
public class Appointment extends ScheduledEvent
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Appointment Associations
  private Service selectedService;
  private List<SingleService> selectedComboOptions;
  private Customer customer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Appointment(DateTime aStartDateTime, DateTime aEndDateTime, Service aSelectedService, Customer aCustomer)
  {
    super(aStartDateTime, aEndDateTime);
    if (!setSelectedService(aSelectedService))
    {
      throw new RuntimeException("Unable to create Appointment due to aSelectedService. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    selectedComboOptions = new ArrayList<SingleService>();
    boolean didAddCustomer = setCustomer(aCustomer);
    if (!didAddCustomer)
    {
      throw new RuntimeException("Unable to create bookedAppointment due to customer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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
  public SingleService getSelectedComboOption(int index)
  {
    SingleService aSelectedComboOption = selectedComboOptions.get(index);
    return aSelectedComboOption;
  }

  public List<SingleService> getSelectedComboOptions()
  {
    List<SingleService> newSelectedComboOptions = Collections.unmodifiableList(selectedComboOptions);
    return newSelectedComboOptions;
  }

  public int numberOfSelectedComboOptions()
  {
    int number = selectedComboOptions.size();
    return number;
  }

  public boolean hasSelectedComboOptions()
  {
    boolean has = selectedComboOptions.size() > 0;
    return has;
  }

  public int indexOfSelectedComboOption(SingleService aSelectedComboOption)
  {
    int index = selectedComboOptions.indexOf(aSelectedComboOption);
    return index;
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setSelectedService(Service aNewSelectedService)
  {
    boolean wasSet = false;
    if (aNewSelectedService != null)
    {
      selectedService = aNewSelectedService;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSelectedComboOptions()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addSelectedComboOption(SingleService aSelectedComboOption)
  {
    boolean wasAdded = false;
    if (selectedComboOptions.contains(aSelectedComboOption)) { return false; }
    selectedComboOptions.add(aSelectedComboOption);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSelectedComboOption(SingleService aSelectedComboOption)
  {
    boolean wasRemoved = false;
    if (selectedComboOptions.contains(aSelectedComboOption))
    {
      selectedComboOptions.remove(aSelectedComboOption);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSelectedComboOptionAt(SingleService aSelectedComboOption, int index)
  {  
    boolean wasAdded = false;
    if(addSelectedComboOption(aSelectedComboOption))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSelectedComboOptions()) { index = numberOfSelectedComboOptions() - 1; }
      selectedComboOptions.remove(aSelectedComboOption);
      selectedComboOptions.add(index, aSelectedComboOption);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSelectedComboOptionAt(SingleService aSelectedComboOption, int index)
  {
    boolean wasAdded = false;
    if(selectedComboOptions.contains(aSelectedComboOption))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSelectedComboOptions()) { index = numberOfSelectedComboOptions() - 1; }
      selectedComboOptions.remove(aSelectedComboOption);
      selectedComboOptions.add(index, aSelectedComboOption);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSelectedComboOptionAt(aSelectedComboOption, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCustomer(Customer aCustomer)
  {
    boolean wasSet = false;
    if (aCustomer == null)
    {
      return wasSet;
    }

    Customer existingCustomer = customer;
    customer = aCustomer;
    if (existingCustomer != null && !existingCustomer.equals(aCustomer))
    {
      existingCustomer.removeBookedAppointment(this);
    }
    customer.addBookedAppointment(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    selectedService = null;
    selectedComboOptions.clear();
    Customer placeholderCustomer = customer;
    this.customer = null;
    if(placeholderCustomer != null)
    {
      placeholderCustomer.removeBookedAppointment(this);
    }
    super.delete();
  }

}