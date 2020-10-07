/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse.flexibook.model;
import java.util.*;

// line 71 "../../../../../FlexiBook.ump"
public class ServiceCombo extends BookableService
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ServiceCombo Associations
  private ComboItem mainService;
  private List<ComboItem> services;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ServiceCombo(String aName, FlexiBook aFlexiBook, ComboItem aMainService, ComboItem... allServices)
  {
    super(aName, aFlexiBook);
    if (!setMainService(aMainService))
    {
      throw new RuntimeException("Unable to create ServiceCombo due to aMainService. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    services = new ArrayList<ComboItem>();
    boolean didAddServices = setServices(allServices);
    if (!didAddServices)
    {
      throw new RuntimeException("Unable to create ServiceCombo, must have at least 2 services. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public ComboItem getMainService()
  {
    return mainService;
  }
  /* Code from template association_GetMany */
  public ComboItem getService(int index)
  {
    ComboItem aService = services.get(index);
    return aService;
  }

  /**
   * the services should be {ordered}; however, since Umple translates every * association
   * or composition into a list, it is possible to order items in that list given Umple's API
   */
  public List<ComboItem> getServices()
  {
    List<ComboItem> newServices = Collections.unmodifiableList(services);
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

  public int indexOfService(ComboItem aService)
  {
    int index = services.indexOf(aService);
    return index;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setMainService(ComboItem aNewMainService)
  {
    boolean wasSet = false;
    if (aNewMainService != null)
    {
      mainService = aNewMainService;
      wasSet = true;
    }
    return wasSet;
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
  /* Code from template association_AddManyToManyMethod */
  public boolean addService(ComboItem aService)
  {
    boolean wasAdded = false;
    if (services.contains(aService)) { return false; }
    services.add(aService);
    if (aService.indexOfServiceCombo(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aService.addServiceCombo(this);
      if (!wasAdded)
      {
        services.remove(aService);
      }
    }
    return wasAdded;
  }
  /* Code from template association_AddMStarToMany */
  public boolean removeService(ComboItem aService)
  {
    boolean wasRemoved = false;
    if (!services.contains(aService))
    {
      return wasRemoved;
    }

    if (numberOfServices() <= minimumNumberOfServices())
    {
      return wasRemoved;
    }

    int oldIndex = services.indexOf(aService);
    services.remove(oldIndex);
    if (aService.indexOfServiceCombo(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aService.removeServiceCombo(this);
      if (!wasRemoved)
      {
        services.add(oldIndex,aService);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_SetMStarToMany */
  public boolean setServices(ComboItem... newServices)
  {
    boolean wasSet = false;
    ArrayList<ComboItem> verifiedServices = new ArrayList<ComboItem>();
    for (ComboItem aService : newServices)
    {
      if (verifiedServices.contains(aService))
      {
        continue;
      }
      verifiedServices.add(aService);
    }

    if (verifiedServices.size() != newServices.length || verifiedServices.size() < minimumNumberOfServices())
    {
      return wasSet;
    }

    ArrayList<ComboItem> oldServices = new ArrayList<ComboItem>(services);
    services.clear();
    for (ComboItem aNewService : verifiedServices)
    {
      services.add(aNewService);
      if (oldServices.contains(aNewService))
      {
        oldServices.remove(aNewService);
      }
      else
      {
        aNewService.addServiceCombo(this);
      }
    }

    for (ComboItem anOldService : oldServices)
    {
      anOldService.removeServiceCombo(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addServiceAt(ComboItem aService, int index)
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

  public boolean addOrMoveServiceAt(ComboItem aService, int index)
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
    mainService = null;
    while (services.size() > 0)
    {
      ComboItem aService = services.get(services.size() - 1);
      aService.delete();
      services.remove(aService);
    }
    
    super.delete();
  }

}