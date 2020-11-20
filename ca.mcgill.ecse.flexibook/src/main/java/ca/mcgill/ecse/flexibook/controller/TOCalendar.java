/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse.flexibook.controller;
import java.util.*;
import java.sql.Date;
import java.sql.Time;

// line 50 "../../../../../FlexiBookTransferObjects.ump"
public class TOCalendar
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOCalendar Associations
  private List<TOTimeSlot> availableTimeSlots;
  private List<TOTimeSlot> unavailableTimeSlots;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOCalendar()
  {
    availableTimeSlots = new ArrayList<TOTimeSlot>();
    unavailableTimeSlots = new ArrayList<TOTimeSlot>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public TOTimeSlot getAvailableTimeSlot(int index)
  {
    TOTimeSlot aAvailableTimeSlot = availableTimeSlots.get(index);
    return aAvailableTimeSlot;
  }

  public List<TOTimeSlot> getAvailableTimeSlots()
  {
    List<TOTimeSlot> newAvailableTimeSlots = Collections.unmodifiableList(availableTimeSlots);
    return newAvailableTimeSlots;
  }

  public int numberOfAvailableTimeSlots()
  {
    int number = availableTimeSlots.size();
    return number;
  }

  public boolean hasAvailableTimeSlots()
  {
    boolean has = availableTimeSlots.size() > 0;
    return has;
  }

  public int indexOfAvailableTimeSlot(TOTimeSlot aAvailableTimeSlot)
  {
    int index = availableTimeSlots.indexOf(aAvailableTimeSlot);
    return index;
  }
  /* Code from template association_GetMany */
  public TOTimeSlot getUnavailableTimeSlot(int index)
  {
    TOTimeSlot aUnavailableTimeSlot = unavailableTimeSlots.get(index);
    return aUnavailableTimeSlot;
  }

  public List<TOTimeSlot> getUnavailableTimeSlots()
  {
    List<TOTimeSlot> newUnavailableTimeSlots = Collections.unmodifiableList(unavailableTimeSlots);
    return newUnavailableTimeSlots;
  }

  public int numberOfUnavailableTimeSlots()
  {
    int number = unavailableTimeSlots.size();
    return number;
  }

  public boolean hasUnavailableTimeSlots()
  {
    boolean has = unavailableTimeSlots.size() > 0;
    return has;
  }

  public int indexOfUnavailableTimeSlot(TOTimeSlot aUnavailableTimeSlot)
  {
    int index = unavailableTimeSlots.indexOf(aUnavailableTimeSlot);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAvailableTimeSlots()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addAvailableTimeSlot(TOTimeSlot aAvailableTimeSlot)
  {
    boolean wasAdded = false;
    if (availableTimeSlots.contains(aAvailableTimeSlot)) { return false; }
    availableTimeSlots.add(aAvailableTimeSlot);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAvailableTimeSlot(TOTimeSlot aAvailableTimeSlot)
  {
    boolean wasRemoved = false;
    if (availableTimeSlots.contains(aAvailableTimeSlot))
    {
      availableTimeSlots.remove(aAvailableTimeSlot);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAvailableTimeSlotAt(TOTimeSlot aAvailableTimeSlot, int index)
  {  
    boolean wasAdded = false;
    if(addAvailableTimeSlot(aAvailableTimeSlot))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAvailableTimeSlots()) { index = numberOfAvailableTimeSlots() - 1; }
      availableTimeSlots.remove(aAvailableTimeSlot);
      availableTimeSlots.add(index, aAvailableTimeSlot);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAvailableTimeSlotAt(TOTimeSlot aAvailableTimeSlot, int index)
  {
    boolean wasAdded = false;
    if(availableTimeSlots.contains(aAvailableTimeSlot))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAvailableTimeSlots()) { index = numberOfAvailableTimeSlots() - 1; }
      availableTimeSlots.remove(aAvailableTimeSlot);
      availableTimeSlots.add(index, aAvailableTimeSlot);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAvailableTimeSlotAt(aAvailableTimeSlot, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfUnavailableTimeSlots()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addUnavailableTimeSlot(TOTimeSlot aUnavailableTimeSlot)
  {
    boolean wasAdded = false;
    if (unavailableTimeSlots.contains(aUnavailableTimeSlot)) { return false; }
    unavailableTimeSlots.add(aUnavailableTimeSlot);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeUnavailableTimeSlot(TOTimeSlot aUnavailableTimeSlot)
  {
    boolean wasRemoved = false;
    if (unavailableTimeSlots.contains(aUnavailableTimeSlot))
    {
      unavailableTimeSlots.remove(aUnavailableTimeSlot);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addUnavailableTimeSlotAt(TOTimeSlot aUnavailableTimeSlot, int index)
  {  
    boolean wasAdded = false;
    if(addUnavailableTimeSlot(aUnavailableTimeSlot))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUnavailableTimeSlots()) { index = numberOfUnavailableTimeSlots() - 1; }
      unavailableTimeSlots.remove(aUnavailableTimeSlot);
      unavailableTimeSlots.add(index, aUnavailableTimeSlot);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveUnavailableTimeSlotAt(TOTimeSlot aUnavailableTimeSlot, int index)
  {
    boolean wasAdded = false;
    if(unavailableTimeSlots.contains(aUnavailableTimeSlot))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUnavailableTimeSlots()) { index = numberOfUnavailableTimeSlots() - 1; }
      unavailableTimeSlots.remove(aUnavailableTimeSlot);
      unavailableTimeSlots.add(index, aUnavailableTimeSlot);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addUnavailableTimeSlotAt(aUnavailableTimeSlot, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    availableTimeSlots.clear();
    unavailableTimeSlots.clear();
  }

}