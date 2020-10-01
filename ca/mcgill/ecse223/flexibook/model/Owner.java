/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.model;
import java.util.*;

// line 10 "../../../../../../model.ump"
// line 88 "../../../../../../model.ump"
public class Owner extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Owner Attributes
  private String username;
  private String password;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Owner(String aUsername, String aPassword)
  {
    super(aUsername, aPassword);
    username = "owner";
    password = "owner";
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setUsername(String aUsername)
  {
    boolean wasSet = false;
    username = aUsername;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public String getUsername()
  {
    return username;
  }

  public String getPassword()
  {
    return password;
  }

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "username" + ":" + getUsername()+ "," +
            "password" + ":" + getPassword()+ "]";
  }
}