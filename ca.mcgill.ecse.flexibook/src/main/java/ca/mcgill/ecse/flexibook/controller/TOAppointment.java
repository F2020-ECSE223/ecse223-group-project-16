/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse.flexibook.controller;
import java.sql.Date;
import java.sql.Time;

// line 29 "../../../../../FlexiBookTransferObjects.ump"
public class TOAppointment
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOAppointment Attributes
  private Date startDate;
  private Time startTime;
  private Date endDate;
  private Time endTime;
  private String customerUsername;
  private String bookableServiceName;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOAppointment(Date aStartDate, Time aStartTime, Date aEndDate, Time aEndTime, String aCustomerUsername, String aBookableServiceName)
  {
    startDate = aStartDate;
    startTime = aStartTime;
    endDate = aEndDate;
    endTime = aEndTime;
    customerUsername = aCustomerUsername;
    bookableServiceName = aBookableServiceName;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStartDate(Date aStartDate)
  {
    boolean wasSet = false;
    startDate = aStartDate;
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

  public boolean setEndDate(Date aEndDate)
  {
    boolean wasSet = false;
    endDate = aEndDate;
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

  public boolean setCustomerUsername(String aCustomerUsername)
  {
    boolean wasSet = false;
    customerUsername = aCustomerUsername;
    wasSet = true;
    return wasSet;
  }

  public boolean setBookableServiceName(String aBookableServiceName)
  {
    boolean wasSet = false;
    bookableServiceName = aBookableServiceName;
    wasSet = true;
    return wasSet;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public Time getEndTime()
  {
    return endTime;
  }

  /**
   * not sure, may be redundant
   */
  public String getCustomerUsername()
  {
    return customerUsername;
  }

  public String getBookableServiceName()
  {
    return bookableServiceName;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "customerUsername" + ":" + getCustomerUsername()+ "," +
            "bookableServiceName" + ":" + getBookableServiceName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startDate" + "=" + (getStartDate() != null ? !getStartDate().equals(this)  ? getStartDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endDate" + "=" + (getEndDate() != null ? !getEndDate().equals(this)  ? getEndDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endTime" + "=" + (getEndTime() != null ? !getEndTime().equals(this)  ? getEndTime().toString().replaceAll("  ","    ") : "this" : "null");
  }
}