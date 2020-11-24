package ca.mcgill.ecse.flexibook.util;

import java.sql.Date;
import java.sql.Time;

public class SystemTime {
    private static boolean isTesting;
    private static Date testDate;
    private static Time testTime;

    public static void setTesting(Date date, Time time){
        isTesting = true;
        testDate = date;
        testTime = time;
    }

    public static void unsetTesting(){
        isTesting = false;
    }

	public static Date getDate(){
        if(isTesting){
            return testDate;
        }
        
        Date currentDate = new Date(System.currentTimeMillis());
        // Remove hours, minutes, seconds and milliseconds by creating new "clean" Date objects
        return new Date(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay());
    }

    public static Time getTime(){
        if(isTesting){
            return testTime;
        }
        return new Time(System.currentTimeMillis());
    }
}
