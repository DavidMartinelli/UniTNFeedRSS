package it.unitn.hci.feed.android.utils;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedUtils
{
    private static final String SHARED_NAME = "prefs";
    private static final String KEY = "courses";


    private SharedUtils()
    {
        // static methods only
    }


    public static void saveCourses(List<Long> ids, Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        JSONArray arr = new JSONArray();
        for (Long id : ids)
            arr.put(id);
        editor.putString(KEY, arr.toString());
        editor.commit();
    }


    public static List<Long> getCourses(Context context) throws Exception
    {
        List<Long> courses = new ArrayList<Long>();
        JSONArray array = new JSONArray(context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE).getString(KEY, "[]"));
        for (int i = 0; i < array.length(); i++)
            courses.add(array.getLong(i));
        return courses;
    }
}
