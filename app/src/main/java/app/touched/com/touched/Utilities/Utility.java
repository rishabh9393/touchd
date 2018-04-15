package app.touched.com.touched.Utilities;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anshul on 2/26/2018.
 */

public class Utility {
    public static String tryGetValueFromString(String key, JSONObject fromObject)
    { String result=null;
        try {
            result = fromObject.getString(key);
        } catch (JSONException e) {
            result=null;
        }

        return result;


    }
    public static void showToastForContentNotAvailable(Context con) {
        Toast.makeText(con, "Sorry! this content is not available or you may deleted this file from phone folder", Toast.LENGTH_SHORT).show();
    }
}
