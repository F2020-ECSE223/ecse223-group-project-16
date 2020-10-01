/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;
import java.util.*;

// line 33 "../../../../../../model.ump"
// line 157 "../../../../../../model.ump"
public class ServiceCombo extends Service
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ServiceCombo Associations
  private ComboItem mainSingleService;
  private List<ComboItem> singleServiceCombination;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ServiceCombo(String aName, ComboItem aMainSingleService)
  {
    super(aName);
    if (!setMainSingleService(aMainSingleService))
    {
      throw new RuntimeException("Unable to create ServiceCombo due to aMainSingleService. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    singleServiceCombination = new ArrayList<ComboItem>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public ComboItem getMainSingleService()
  {
    return mainSingleService;
  }
  /* Code from template association_GetMany */
  public ComboItem getSingleServiceCombination(int index)
  {
    ComboItem aSingleServiceCombination = singleServiceCombination.get(index);
    return aSingleServiceCombination;
  }

  /**
   * there must be at least 2 services to be a combo
   */
  public List<ComboItem> getSingleServiceCombination()
  {
    List<ComboItem> newSingleServiceCombination = Collections.unmodifiableList(singleServiceCombination);
    return newSingleServiceCombination;
  }

  public int numberOfSingleServiceCombination()
  {
    int number = singleServiceCombination.size();
    return number;
  }

  public boolean hasSingleServiceCombination()
  {
    boolean has = singleServiceCombination.size() > 0;
    return has;
  }

  public int indexOfSingleServiceCombination(ComboItem aSingleServiceCombination)
  {
    int index = singleServiceCombination.indexOf(aSingleServiceCombination);
    return index;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setMainSingleService(ComboItem aNewMainSingleService)
  {
    boolean wasSet = false;
    if (aNewMainSingleService != null)
    {
      mainSingleService = aNewMainSingleService;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfSingleServiceCombinationValid()
  {
    boolean isValid = numberOfSingleServiceCombination() >= minimumNumberOfSingleServiceCombination();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSingleServiceCombination()
  {
    return 2;
  }
  /* Code from template association_AddMandatoryManyToOne */
  public ComboItem addSingleServiceCombination(boolean aRequired, SingleService aCombinedSingleService)
  {
    ComboItem aNewSingleServiceCombination = new ComboItem(aRequired, aCombinedSingleService, this);
    return aNewSingleServiceCombination;
  }

  public boolean addSingleServiceCombination(ComboItem aSingleServiceCombination)
  {
    boolean wasAdded = false;
    if (singleServiceCombination.contains(aSingleServiceCombination)) { return false; }
    ServiceCombo existingServiceCombo = aSingleServiceCombination.getServiceCombo();
    boolean isNewServiceCombo = existingServiceCombo != null && !this.equals(existingServiceCombo);

    if (isNewServiceCombo && existingServiceCombo.numberOfSingleServiceCombination() <= minimumNumberOfSingleServiceCombination())
    {
      return wasAdded;
    }
    if (isNewServiceCombo)
    {
      aSingleServiceCombination.setServiceCombo(this);
    }
    else
    {
      singleServiceCombination.add(aSingleServiceCombination);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSingleServiceCombination(ComboItem aSingleServiceCombination)
  {
    boolean wasRemoved = false;
    //Unable to remove aSingleServiceCombination, as it must always have a serviceCombo
    if (this.equals(aSingleServiceCombination.getServiceCombo()))
    {
      return wasRemoved;
    }

    //serviceCombo already at minimum (2)
    if (numberOfSingleServiceCombination() <= minimumNumberOfSingleServiceCombination())
    {
      return wasRemoved;
    }

    singleServiceCombination.remove(aSingleServiceCombination);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSingleServiceCombinationAt(ComboItem aSingleServiceCombination, int index)
  {  
    boolean wasAdded = false;
    if(addSingleServiceCombination(aSingleServiceCombination))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSingleServiceCombination()) { index = numberOfSingleServiceCombination() - 1; }
      singleServiceCombination.remove(aSingleServiceCombination);
      singleServiceCombination.add(index, aSingleServiceCombination);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSingleServiceCombinationAt(ComboItem aSingleServiceCombination, int index)
  {
    boolean wasAdded = false;
    if(singleServiceCombination.contains(aSingleServiceCombination))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSingleServiceCombination()) { index = numberOfSingleServiceCombination() - 1; }
      singleServiceCombination.remove(aSingleServiceCombination);
      singleServiceCombination.add(index, aSingleServiceCombination);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSingleServiceCombinationAt(aSingleServiceCombination, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    mainSingleService = null;
    while (singleServiceCombination.size() > 0)
    {
      ComboItem aSingleServiceCombination = singleServiceCombination.get(singleServiceCombination.size() - 1);
      aSingleServiceCombination.delete();
      singleServiceCombination.remove(aSingleServiceCombination);
    }
    
    super.delete();
  }

}