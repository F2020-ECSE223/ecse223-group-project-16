/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;

import java.util.*;

// line 34 "model.ump"
// line 152 "model.ump"
public class ServiceCombo extends Service
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ServiceCombo Associations
  private ComboItem mainService;
  private List<ComboItem> comboServices;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ServiceCombo(String aName, ComboItem aMainService)
  {
    super(aName);
    if (!setMainService(aMainService))
    {
      throw new RuntimeException("Unable to create ServiceCombo due to aMainService. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    comboServices = new ArrayList<ComboItem>();
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
  public ComboItem getComboService(int index)
  {
    ComboItem aComboService = comboServices.get(index);
    return aComboService;
  }

  /**
   * there must be at least 2 services to be a combo
   */
  public List<ComboItem> getComboServices()
  {
    List<ComboItem> newComboServices = Collections.unmodifiableList(comboServices);
    return newComboServices;
  }

  public int numberOfComboServices()
  {
    int number = comboServices.size();
    return number;
  }

  public boolean hasComboServices()
  {
    boolean has = comboServices.size() > 0;
    return has;
  }

  public int indexOfComboService(ComboItem aComboService)
  {
    int index = comboServices.indexOf(aComboService);
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
  public boolean isNumberOfComboServicesValid()
  {
    boolean isValid = numberOfComboServices() >= minimumNumberOfComboServices();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfComboServices()
  {
    return 2;
  }
  /* Code from template association_AddMandatoryManyToOne */
  public ComboItem addComboService(ComboItem.Necessity aNecessity, SingleService aCombinedSingleService)
  {
    ComboItem aNewComboService = new ComboItem(aNecessity, aCombinedSingleService, this);
    return aNewComboService;
  }

  public boolean addComboService(ComboItem aComboService)
  {
    boolean wasAdded = false;
    if (comboServices.contains(aComboService)) { return false; }
    ServiceCombo existingServiceCombo = aComboService.getServiceCombo();
    boolean isNewServiceCombo = existingServiceCombo != null && !this.equals(existingServiceCombo);

    if (isNewServiceCombo && existingServiceCombo.numberOfComboServices() <= minimumNumberOfComboServices())
    {
      return wasAdded;
    }
    if (isNewServiceCombo)
    {
      aComboService.setServiceCombo(this);
    }
    else
    {
      comboServices.add(aComboService);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeComboService(ComboItem aComboService)
  {
    boolean wasRemoved = false;
    //Unable to remove aComboService, as it must always have a serviceCombo
    if (this.equals(aComboService.getServiceCombo()))
    {
      return wasRemoved;
    }

    //serviceCombo already at minimum (2)
    if (numberOfComboServices() <= minimumNumberOfComboServices())
    {
      return wasRemoved;
    }

    comboServices.remove(aComboService);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addComboServiceAt(ComboItem aComboService, int index)
  {  
    boolean wasAdded = false;
    if(addComboService(aComboService))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfComboServices()) { index = numberOfComboServices() - 1; }
      comboServices.remove(aComboService);
      comboServices.add(index, aComboService);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveComboServiceAt(ComboItem aComboService, int index)
  {
    boolean wasAdded = false;
    if(comboServices.contains(aComboService))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfComboServices()) { index = numberOfComboServices() - 1; }
      comboServices.remove(aComboService);
      comboServices.add(index, aComboService);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addComboServiceAt(aComboService, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    mainService = null;
    while (comboServices.size() > 0)
    {
      ComboItem aComboService = comboServices.get(comboServices.size() - 1);
      aComboService.delete();
      comboServices.remove(aComboService);
    }
    
    super.delete();
  }

}