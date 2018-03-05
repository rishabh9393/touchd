package app.touched.com.touched.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import app.touched.com.touched.R;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        String clientDnT = "1/3/2018 13:11:00";
          try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(clientDnT);
            TimeZone tz = TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().toString()); // get time zone of user
            sdf.setTimeZone(tz);
              System.out.println(Calendar.getInstance().getTimeZone().toString());
            // Convert to servertime zone
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TimeZone tzInAmerica = TimeZone.getDefault();
            sdf1.setTimeZone(tzInAmerica);
              System.out.println(date);
            // assign date to date
            String serverDate = sdf1.format(date);
              System.out.println(serverDate);
            // Convert to servertime zone to Timestamp
            Date date2 =  sdf.parse(serverDate);
            Timestamp tsm = new Timestamp(date2.getTime());
              System.out.println(tsm);
          }
        catch(Exception e){
            System.err.println(e);
        }
    }
}
