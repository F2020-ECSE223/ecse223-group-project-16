/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;

// line 43 "../../../../../../model.ump"
// line 165 "../../../../../../model.ump"
public class ComboItem
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Necessity { Optional, Required }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ComboItem Attributes
  private Necessity necessity;

  //ComboItem Associations
  private SingleService combinedSingleService;
  private ServiceCombo serviceCombo;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ComboItem(Necessity aNecessity, SingleService aCombinedSingleService, ServiceCombo aServiceCombo)
  {
    necessity = aNecessity;
    if (!setCombinedSingleService(aCombinedSingleService))
    {
      throw new RuntimeException("Unable to create ComboItem due to aCombinedSingleService. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddServiceCombo = setServiceCombo(aServiceCombo);
    if (!didAddServiceCombo)
    {
      throw new RuntimeException("Unable to create comboService due to serviceCombo. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNecessity(Necessity aNecessity)
  {
    boolean wasSet = false;
    necessity = aNecessity;
    wasSet = true;
    return wasSet;
  }

  public Necessity getNecessity()
  {
    return necessity;
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
    //Must provide serviceCombo to comboService
    if (aServiceCombo == null)
    {
      return wasSet;
    }

    if (serviceCombo != null && serviceCombo.numberOfComboServices() <= ServiceCombo.minimumNumberOfComboServices())
    {
      return wasSet;
    }

    ServiceCombo existingServiceCombo = serviceCombo;
    serviceCombo = aServiceCombo;
    if (existingServiceCombo != null && !existingServiceCombo.equals(aServiceCombo))
    {
      boolean didRemove = existingServiceCombo.removeComboService(this);
      if (!didRemove)
      {
        serviceCombo = existingServiceCombo;
        return wasSet;
      }
    }
    serviceCombo.addComboService(this);
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
      placeholderServiceCombo.removeComboService(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "necessity" + "=" + (getNecessity() != null ? !getNecessity().equals(this)  ? getNecessity().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "combinedSingleService = "+(getCombinedSingleService()!=null?Integer.toHexString(System.identityHashCode(getCombinedSingleService())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "serviceCombo = "+(getServiceCombo()!=null?Integer.toHexString(System.identityHashCode(getServiceCombo())):"null");
  }
}