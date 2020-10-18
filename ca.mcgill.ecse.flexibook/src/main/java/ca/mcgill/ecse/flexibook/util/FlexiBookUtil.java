package ca.mcgill.ecse.flexibook.util;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek;

public class FlexiBookUtil {
    public static DayOfWeek getDayOfWeek(String day) {
        return DayOfWeek.valueOf(day);
    }

    public static Time getTimeFromString(String time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return new Time(formatter.parse(time).getTime());   
    }

    public static Date getDateFromString(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return new Date(formatter.parse(date).getTime());   
    }
}
