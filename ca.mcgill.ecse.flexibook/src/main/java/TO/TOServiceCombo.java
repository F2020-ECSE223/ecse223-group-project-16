/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package TO;

// line 22 "../../../../../FlexiBookTransferObjects.ump"
public class TOServiceCombo
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOServiceCombo Attributes
  private String name;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOServiceCombo(String aName)
  {
    name = aName;
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

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "]";
  }
}