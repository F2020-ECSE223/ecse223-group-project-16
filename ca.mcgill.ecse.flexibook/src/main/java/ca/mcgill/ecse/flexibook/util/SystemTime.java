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
        return new Date(System.currentTimeMillis());
    }

    public static Time getTime(){
        if(isTesting){
            return testTime;
        }
        return new Time(System.currentTimeMillis());
    }
}
