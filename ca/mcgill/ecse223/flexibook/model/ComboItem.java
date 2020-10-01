/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;

// line 39 "../../../../../../model.ump"
// line 194 "../../../../../../model.ump"
public class ComboItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ComboItem Attributes
  private boolean required;

  //ComboItem Associations
  private SingleService combinedSingleService;
  private ServiceCombo serviceCombo;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ComboItem(boolean aRequired, SingleService aCombinedSingleService, ServiceCombo aServiceCombo)
  {
    required = aRequired;
    if (!setCombinedSingleService(aCombinedSingleService))
    {
      throw new RuntimeException("Unable to create ComboItem due to aCombinedSingleService. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddServiceCombo = setServiceCombo(aServiceCombo);
    if (!didAddServiceCombo)
    {
      throw new RuntimeException("Unable to create singleServiceCombination due to serviceCombo. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRequired(boolean aRequired)
  {
    boolean wasSet = false;
    required = aRequired;
    wasSet = true;
    return wasSet;
  }

  public boolean getRequired()
  {
    return required;
  }
  /* Code from template association_GetOne */
  public SingleService getCombinedSingleService()
  {
    return combinedSingleService;
  }
  /* Code from template association_GetOne */
  public ServiceCombo getServiceCombo()
  {
    return serviceCombo;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCombinedSingleService(SingleService aNewCombinedSingleService)
  {
    boolean wasSet = false;
    if (aNewCombinedSingleService != null)
    {
      combinedSingleService = aNewCombinedSingleService;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetOneToMandatoryMany */
  public boolean setServiceCombo(ServiceCombo aServiceCombo)
  {
    boolean wasSet = false;
    //Must provide serviceCombo to singleServiceCombination
    if (aServiceCombo == null)
    {
      return wasSet;
    }

    if (serviceCombo != null && serviceCombo.numberOfSingleServiceCombination() <= ServiceCombo.minimumNumberOfSingleServiceCombination())
    {
      return wasSet;
    }

    ServiceCombo existingServiceCombo = serviceCombo;
    serviceCombo = aServiceCombo;
    if (existingServiceCombo != null && !existingServiceCombo.equals(aServiceCombo))
    {
      boolean didRemove = existingServiceCombo.removeSingleServiceCombination(this);
      if (!didRemove)
      {
        serviceCombo = existingServiceCombo;
        return wasSet;
      }
    }
    serviceCombo.addSingleServiceCombination(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    combinedSingleService = null;
    ServiceCombo placeholderServiceCombo = serviceCombo;
    this.serviceCombo = null;
    if(placeholderServiceCombo != null)
    {
      placeholderServiceCombo.removeSingleServiceCombination(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "required" + ":" + getRequired()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "combinedSingleService = "+(getCombinedSingleService()!=null?Integer.toHexString(System.identityHashCode(getCombinedSingleService())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "serviceCombo = "+(getServiceCombo()!=null?Integer.toHexString(System.identityHashCode(getServiceCombo())):"null");
  }
}