/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse.flexibook.controller;

// line 40 "../../../../../FlexiBookTransferObjects.ump"
public class TOComboItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOComboItem Attributes
  private String serviceName;
  private boolean mandatory;

  //TOComboItem Associations
  private TOServiceCombo tOServiceCombo;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOComboItem(String aServiceName, boolean aMandatory, TOServiceCombo aTOServiceCombo)
  {
    serviceName = aServiceName;
    mandatory = aMandatory;
    boolean didAddTOServiceCombo = setTOServiceCombo(aTOServiceCombo);
    if (!didAddTOServiceCombo)
    {
      throw new RuntimeException("Unable to create service due to tOServiceCombo. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setServiceName(String aServiceName)
  {
    boolean wasSet = false;
    serviceName = aServiceName;
    wasSet = true;
    return wasSet;
  }

  public boolean setMandatory(boolean aMandatory)
  {
    boolean wasSet = false;
    mandatory = aMandatory;
    wasSet = true;
    return wasSet;
  }

  public String getServiceName()
  {
    return serviceName;
  }

  public boolean getMandatory()
  {
    return mandatory;
  }
  /* Code from template association_GetOne */
  public TOServiceCombo getTOServiceCombo()
  {
    return tOServiceCombo;
  }
  /* Code from template association_SetOneToMandatoryMany */
  public boolean setTOServiceCombo(TOServiceCombo aTOServiceCombo)
  {
    boolean wasSet = false;
    //Must provide tOServiceCombo to service
    if (aTOServiceCombo == null)
    {
      return wasSet;
    }

    if (tOServiceCombo != null && tOServiceCombo.numberOfServices() <= TOServiceCombo.minimumNumberOfServices())
    {
      return wasSet;
    }

    TOServiceCombo existingTOServiceCombo = tOServiceCombo;
    tOServiceCombo = aTOServiceCombo;
    if (existingTOServiceCombo != null && !existingTOServiceCombo.equals(aTOServiceCombo))
    {
      boolean didRemove = existingTOServiceCombo.removeService(this);
      if (!didRemove)
      {
        tOServiceCombo = existingTOServiceCombo;
        return wasSet;
      }
    }
    tOServiceCombo.addService(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    TOServiceCombo placeholderTOServiceCombo = tOServiceCombo;
    this.tOServiceCombo = null;
    if(placeholderTOServiceCombo != null)
    {
      placeholderTOServiceCombo.removeService(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "serviceName" + ":" + getServiceName()+ "," +
            "mandatory" + ":" + getMandatory()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "tOServiceCombo = "+(getTOServiceCombo()!=null?Integer.toHexString(System.identityHashCode(getTOServiceCombo())):"null");
  }
}