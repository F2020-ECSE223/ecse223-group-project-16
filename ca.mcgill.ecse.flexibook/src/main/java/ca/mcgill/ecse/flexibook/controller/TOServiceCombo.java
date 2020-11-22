/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse.flexibook.controller;
import java.util.*;

// line 35 "../../../../../FlexiBookTransferObjects.ump"
public class TOServiceCombo extends TOBookableService
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOServiceCombo Associations
  private List<TOComboItem> services;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOServiceCombo(String aName)
  {
    super(aName);
    services = new ArrayList<TOComboItem>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public TOComboItem getService(int index)
  {
    TOComboItem aService = services.get(index);
    return aService;
  }

  public List<TOComboItem> getServices()
  {
    List<TOComboItem> newServices = Collections.unmodifiableList(services);
    return newServices;
  }

  public int numberOfServices()
  {
    int number = services.size();
    return number;
  }

  public boolean hasServices()
  {
    boolean has = services.size() > 0;
    return has;
  }

  public int indexOfService(TOComboItem aService)
  {
    int index = services.indexOf(aService);
    return index;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfServicesValid()
  {
    boolean isValid = numberOfServices() >= minimumNumberOfServices();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfServices()
  {
    return 2;
  }
  /* Code from template association_AddMandatoryManyToOne */
  public TOComboItem addService(String aServiceName, boolean aMandatory)
  {
    TOComboItem aNewService = new TOComboItem(aServiceName, aMandatory, this);
    return aNewService;
  }

  public boolean addService(TOComboItem aService)
  {
    boolean wasAdded = false;
    if (services.contains(aService)) { return false; }
    TOServiceCombo existingTOServiceCombo = aService.getTOServiceCombo();
    boolean isNewTOServiceCombo = existingTOServiceCombo != null && !this.equals(existingTOServiceCombo);

    if (isNewTOServiceCombo && existingTOServiceCombo.numberOfServices() <= minimumNumberOfServices())
    {
      return wasAdded;
    }
    if (isNewTOServiceCombo)
    {
      aService.setTOServiceCombo(this);
    }
    else
    {
      services.add(aService);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeService(TOComboItem aService)
  {
    boolean wasRemoved = false;
    //Unable to remove aService, as it must always have a tOServiceCombo
    if (this.equals(aService.getTOServiceCombo()))
    {
      return wasRemoved;
    }

    //tOServiceCombo already at minimum (2)
    if (numberOfServices() <= minimumNumberOfServices())
    {
      return wasRemoved;
    }

    services.remove(aService);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addServiceAt(TOComboItem aService, int index)
  {  
    boolean wasAdded = false;
    if(addService(aService))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfServices()) { index = numberOfServices() - 1; }
      services.remove(aService);
      services.add(index, aService);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveServiceAt(TOComboItem aService, int index)
  {
    boolean wasAdded = false;
    if(services.contains(aService))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfServices()) { index = numberOfServices() - 1; }
      services.remove(aService);
      services.add(index, aService);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addServiceAt(aService, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (services.size() > 0)
    {
      TOComboItem aService = services.get(services.size() - 1);
      aService.delete();
      services.remove(aService);
    }
    
    super.delete();
  }

}