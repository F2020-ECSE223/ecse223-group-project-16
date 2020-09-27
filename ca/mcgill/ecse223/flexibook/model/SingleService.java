/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;

// line 24 "model.ump"
// line 174 "model.ump"
public class SingleService extends Service
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SingleService Attributes
  private int totalDuration;
  private int downtimeDuration;
  private int downtimeOffsetIntoService;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SingleService(String aName, int aTotalDuration, int aDowntimeDuration, int aDowntimeOffsetIntoService)
  {
    super(aName);
    totalDuration = aTotalDuration;
    downtimeDuration = aDowntimeDuration;
    downtimeOffsetIntoService = aDowntimeOffsetIntoService;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTotalDuration(int aTotalDuration)
  {
    boolean wasSet = false;
    totalDuration = aTotalDuration;
    wasSet = true;
    return wasSet;
  }

  public boolean setDowntimeDuration(int aDowntimeDuration)
  {
    boolean wasSet = false;
    downtimeDuration = aDowntimeDuration;
    wasSet = true;
    return wasSet;
  }

  public boolean setDowntimeOffsetIntoService(int aDowntimeOffsetIntoService)
  {
    boolean wasSet = false;
    downtimeOffsetIntoService = aDowntimeOffsetIntoService;
    wasSet = true;
    return wasSet;
  }

  public int getTotalDuration()
  {
    return totalDuration;
  }

  /**
   * 1 -> 0..1 Downtime downtime;
   * constraint: downtimeOffset + dowtimeDuration < duration;
   * could have two 1 -> 0..1 reflexive associations dependencies and dependants to model how some service may require another service
   */
  public int getDowntimeDuration()
  {
    return downtimeDuration;
  }

  public int getDowntimeOffsetIntoService()
  {
    return downtimeOffsetIntoService;
  }

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "totalDuration" + ":" + getTotalDuration()+ "," +
            "downtimeDuration" + ":" + getDowntimeDuration()+ "," +
            "downtimeOffsetIntoService" + ":" + getDowntimeOffsetIntoService()+ "]";
  }
}