/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse.flexibook.controller;
import java.util.*;
import java.sql.Time;

// line 8 "../../../../../FlexiBookTransferObjects.ump"
public class TOBusiness
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOBusiness Attributes
  private String name;
  private String address;
  private String phoneNumber;
  private String email;

  //TOBusiness Associations
  private List<TOBusinessHour> businessHours;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOBusiness(String aName, String aAddress, String aPhoneNumber, String aEmail)
  {
    name = aName;
    address = aAddress;
    phoneNumber = aPhoneNumber;
    email = aEmail;
    businessHours = new ArrayList<TOBusinessHour>();
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

  public boolean setAddress(String aAddress)
  {
    boolean wasSet = false;
    address = aAddress;
    wasSet = true;
    return wasSet;
  }

  public boolean setPhoneNumber(String aPhoneNumber)
  {
    boolean wasSet = false;
    phoneNumber = aPhoneNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getAddress()
  {
    return address;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public String getEmail()
  {
    return email;
  }
  /* Code from template association_GetMany */
  public TOBusinessHour getBusinessHour(int index)
  {
    TOBusinessHour aBusinessHour = businessHours.get(index);
    return aBusinessHour;
  }

  public List<TOBusinessHour> getBusinessHours()
  {
    List<TOBusinessHour> newBusinessHours = Collections.unmodifiableList(businessHours);
    return newBusinessHours;
  }

  public int numberOfBusinessHours()
  {
    int number = businessHours.size();
    return number;
  }

  public boolean hasBusinessHours()
  {
    boolean has = businessHours.size() > 0;
    return has;
  }

  public int indexOfBusinessHour(TOBusinessHour aBusinessHour)
  {
    int index = businessHours.indexOf(aBusinessHour);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfBusinessHours()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public TOBusinessHour addBusinessHour(TOBusinessHour.DayOfWeek aDayOfWeek, Time aStartTime, Time aEndTime)
  {
    return new TOBusinessHour(aDayOfWeek, aStartTime, aEndTime, this);
  }

  public boolean addBusinessHour(TOBusinessHour aBusinessHour)
  {
    boolean wasAdded = false;
    if (businessHours.contains(aBusinessHour)) { return false; }
    TOBusiness existingTOBusiness = aBusinessHour.getTOBusiness();
    boolean isNewTOBusiness = existingTOBusiness != null && !this.equals(existingTOBusiness);
    if (isNewTOBusiness)
    {
      aBusinessHour.setTOBusiness(this);
    }
    else
    {
      businessHours.add(aBusinessHour);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeBusinessHour(TOBusinessHour aBusinessHour)
  {
    boolean wasRemoved = false;
    //Unable to remove aBusinessHour, as it must always have a tOBusiness
    if (!this.equals(aBusinessHour.getTOBusiness()))
    {
      businessHours.remove(aBusinessHour);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addBusinessHourAt(TOBusinessHour aBusinessHour, int index)
  {  
    boolean wasAdded = false;
    if(addBusinessHour(aBusinessHour))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBusinessHours()) { index = numberOfBusinessHours() - 1; }
      businessHours.remove(aBusinessHour);
      businessHours.add(index, aBusinessHour);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBusinessHourAt(TOBusinessHour aBusinessHour, int index)
  {
    boolean wasAdded = false;
    if(businessHours.contains(aBusinessHour))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBusinessHours()) { index = numberOfBusinessHours() - 1; }
      businessHours.remove(aBusinessHour);
      businessHours.add(index, aBusinessHour);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addBusinessHourAt(aBusinessHour, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (businessHours.size() > 0)
    {
      TOBusinessHour aBusinessHour = businessHours.get(businessHours.size() - 1);
      aBusinessHour.delete();
      businessHours.remove(aBusinessHour);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "address" + ":" + getAddress()+ "," +
            "phoneNumber" + ":" + getPhoneNumber()+ "," +
            "email" + ":" + getEmail()+ "]";
  }
}