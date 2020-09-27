/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;
import java.sql.Time;

// line 68 "../../../../../../model.ump"
// line 172 "../../../../../../model.ump"
public class OpeningHours
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Weekday { Monday, Tuesday, Wednesday }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //OpeningHours Attributes
  private Weekday weekday;
  private Time startTime;
  private Time endTime;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public OpeningHours(Weekday aWeekday, Time aStartTime, Time aEndTime)
  {
    weekday = aWeekday;
    startTime = aStartTime;
    endTime = aEndTime;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setWeekday(Weekday aWeekday)
  {
    boolean wasSet = false;
    weekday = aWeekday;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartTime(Time aStartTime)
  {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndTime(Time aEndTime)
  {
    boolean wasSet = false;
    endTime = aEndTime;
    wasSet = true;
    return wasSet;
  }

  /**
   * ... , or as an int in [0;7]
   */
  public Weekday getWeekday()
  {
    return weekday;
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public Time getEndTime()
  {
    return endTime;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "weekday" + "=" + (getWeekday() != null ? !getWeekday().equals(this)  ? getWeekday().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endTime" + "=" + (getEndTime() != null ? !getEndTime().equals(this)  ? getEndTime().toString().replaceAll("  ","    ") : "this" : "null");
  }
}