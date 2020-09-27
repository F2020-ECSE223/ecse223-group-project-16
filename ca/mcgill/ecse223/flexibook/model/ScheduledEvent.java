/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;

// line 46 "model.ump"
// line 184 "model.ump"
public abstract class ScheduledEvent
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ScheduledEvent Attributes
  private DateTime startDateTime;
  private DateTime endDateTime;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ScheduledEvent(DateTime aStartDateTime, DateTime aEndDateTime)
  {
    startDateTime = aStartDateTime;
    endDateTime = aEndDateTime;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStartDateTime(DateTime aStartDateTime)
  {
    boolean wasSet = false;
    startDateTime = aStartDateTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndDateTime(DateTime aEndDateTime)
  {
    boolean wasSet = false;
    endDateTime = aEndDateTime;
    wasSet = true;
    return wasSet;
  }

  public DateTime getStartDateTime()
  {
    return startDateTime;
  }

  public DateTime getEndDateTime()
  {
    return endDateTime;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startDateTime" + "=" + (getStartDateTime() != null ? !getStartDateTime().equals(this)  ? getStartDateTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endDateTime" + "=" + (getEndDateTime() != null ? !getEndDateTime().equals(this)  ? getEndDateTime().toString().replaceAll("  ","    ") : "this" : "null");
  }
}