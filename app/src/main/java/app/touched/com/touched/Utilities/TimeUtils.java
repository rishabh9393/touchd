package app.touched.com.touched.Utilities;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Anshul on 2/24/2018.
 */

public class TimeUtils {
    public static String getCurrentDateTime() {
        return new Date().toString();
    }
    public static Integer getYear()
    {
        return new Date().getYear();
    }

    public static String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
