package app.touched.com.touched.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import app.touched.com.touched.R;

/**
 * Created by Rishabh.Nagpal on 5/6/2018.
 */

public class PersistanceManager<T> {
    private static PersistanceManager manager;
    private static Context activity;
    private static String nameDirectory;
    private static SharedPreferences sharedpreferences;

    private PersistanceManager() {

    }

    public static PersistanceManager getInstance(Context context) {
        activity = context;
        nameDirectory = activity.getString(R.string.app_name);
        if (manager == null)
            manager = new PersistanceManager();
        return manager;
    }

    public void setPersistanceStringValue(String key, String value) {

    }

    public String getPersistanceStringValue(String key) {
        return null;
    }

    public void setPersistanceObjectValue(String key, T value) {
        sharedpreferences = activity.getSharedPreferences(nameDirectory, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
//        Set<T> set=new HashSet<>();
//        set.addAll(value);
        editor.putString(key, new Gson().toJson(value));
        editor.commit();

    }

    public String getPersistanceJsonObjectValue(String key) {

        sharedpreferences = activity.getSharedPreferences(nameDirectory, Context.MODE_PRIVATE);
        String st = sharedpreferences.getString(key, null);
        if (st != null) {
            return st;
        } else
            return null;


    }

}
