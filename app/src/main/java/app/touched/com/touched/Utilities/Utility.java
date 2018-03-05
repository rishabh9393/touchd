package app.touched.com.touched.Utilities;

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
}
