/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse.flexibook.controller;
import java.sql.Time;

// line 16 "../../../../../FlexiBookTransferObjects.ump"
public class TOBusinessHour
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum DayOfWeek { Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOBusinessHour Attributes
  private DayOfWeek dayOfWeek;
  private Time startTime;
  private Time endTime;

  //TOBusinessHour Associations
  private TOBusiness tOBusiness;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOBusinessHour(DayOfWeek aDayOfWeek, Time aStartTime, Time aEndTime, TOBusiness aTOBusiness)
  {
    dayOfWeek = aDayOfWeek;
    startTime = aStartTime;
    endTime = aEndTime;
    boolean didAddTOBusiness = setTOBusiness(aTOBusiness);
    if (!didAddTOBusiness)
    {
      throw new RuntimeException("Unable to create businessHour due to tOBusiness. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDayOfWeek(DayOfWeek aDayOfWeek)
  {
    boolean wasSet = false;
    dayOfWeek = aDayOfWeek;
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

  public DayOfWeek getDayOfWeek()
  {
    return dayOfWeek;
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public Time getEndTime()
  {
    return endTime;
  }
  /* Code from template association_GetOne */
  public TOBusiness getTOBusiness()
  {
    return tOBusiness;
  }
  /* Code from template association_SetOneToMany */
  public boolean setTOBusiness(TOBusiness aTOBusiness)
  {
    boolean wasSet = false;
    if (aTOBusiness == null)
    {
      return wasSet;
    }

    TOBusiness existingTOBusiness = tOBusiness;
    tOBusiness = aTOBusiness;
    if (existingTOBusiness != null && !existingTOBusiness.equals(aTOBusiness))
    {
      existingTOBusiness.removeBusinessHour(this);
    }
    tOBusiness.addBusinessHour(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    TOBusiness placeholderTOBusiness = tOBusiness;
    this.tOBusiness = null;
    if(placeholderTOBusiness != null)
    {
      placeholderTOBusiness.removeBusinessHour(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "dayOfWeek" + "=" + (getDayOfWeek() != null ? !getDayOfWeek().equals(this)  ? getDayOfWeek().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endTime" + "=" + (getEndTime() != null ? !getEndTime().equals(this)  ? getEndTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "tOBusiness = "+(getTOBusiness()!=null?Integer.toHexString(System.identityHashCode(getTOBusiness())):"null");
  }
}