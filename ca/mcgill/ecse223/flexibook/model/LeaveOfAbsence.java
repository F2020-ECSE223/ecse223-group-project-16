/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;

// line 56 "../../../../../../model.ump"
// line 184 "../../../../../../model.ump"
public class LeaveOfAbsence extends ScheduledEvent
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum LeaveType { HOLIDAY, VACATION }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //LeaveOfAbsence Attributes
  private LeaveType leaveType;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public LeaveOfAbsence(DateTime aStartDateTime, DateTime aEndDateTime, LeaveType aLeaveType)
  {
    super(aStartDateTime, aEndDateTime);
    leaveType = aLeaveType;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setLeaveType(LeaveType aLeaveType)
  {
    boolean wasSet = false;
    leaveType = aLeaveType;
    wasSet = true;
    return wasSet;
  }

  public LeaveType getLeaveType()
  {
    return leaveType;
  }

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "leaveType" + "=" + (getLeaveType() != null ? !getLeaveType().equals(this)  ? getLeaveType().toString().replaceAll("  ","    ") : "this" : "null");
  }
}