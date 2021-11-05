package com.sunnni.blogapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import static com.sunnni.blogapp.Code.PREF_KEY_BLOG_NAME;
import static com.sunnni.blogapp.Code.PREF_KEY_FIRST_EXEC;
import static com.sunnni.blogapp.Code.PREF_KEY_USER_NAME;

public class PreferenceManager {
    public static final String PREFERENCES_NAME = "blog_data_pref";

    private static final String DEFAULT_BLOG_NAME = "Blog";
    private static final String DEFAULT_USER_NAME = "User";
    private static final boolean DEFAULT_FIRST_EXEC = true;

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void setBlogName(Context context, String value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_BLOG_NAME, value);
        editor.apply();
    }

    public static void setUserName(Context context, String value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_USER_NAME, value);
        editor.apply();
    }

    public static void setFirstExecFalse(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_FIRST_EXEC, false);
        editor.apply();
    }

    public static String getBlogName(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(PREF_KEY_BLOG_NAME, DEFAULT_BLOG_NAME);
    }

    public static String getUserName(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(PREF_KEY_USER_NAME, DEFAULT_USER_NAME);
    }

    public static boolean getFirstExec(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getBoolean(PREF_KEY_FIRST_EXEC, DEFAULT_FIRST_EXEC);
    }
}
