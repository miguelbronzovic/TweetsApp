package com.tweetapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

public final class PreferenceHelper {

    private final static String FILE_NAME = "TweetOLX";

    private PreferenceHelper() {}

    /** The app preferences object */
    private static SharedPreferences mSharedPreferences;

    /**
     * Initialize the helper. <br/>
     *
     * @param context
     */
    public static void init(Context context) {
        mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Convenient method to check if a preference exists or not. <br/>
     *
     * @param key preference
     * @return true if the preference exists, false otherwise
     */
    public static boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    /**
     * Convenient method to persist the value in the preferences.<br/>
     *
     * @param name of the data point
     * @param value of the data point
     */
    public static void save(String name, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(name, value);
        editor.commit();
    }

    /**
     * Convenient method to persist the value in the preferences.<br/>
     *
     * @param name of the data point
     * @param value of the data point
     */
    public static void save(String name, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    /**
     * Convenient method to persist the value in the preferences.<br/>
     *
     * @param name of the data point
     * @param value of the data point
     */
    public static void save(String name, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    /**
     * Convenient method to persist the value in the preferences.<br/>
     *
     * @param name of the data point
     * @param value of the data point
     */
    public static void save(String name, long value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(name, value);
        editor.commit();
    }

    /**
     * Retrieves the string value for the given name. <br/>
     *
     * @param name of the field
     */
    public static String getString(String name) {
        return mSharedPreferences.getString(name, null);
    }

    /**
     * Retrieves the boolean value for the given name and returns the default if
     * null.<br/>
     *
     * @param name of the field
     * @param defaultValue if it's null
     */
    public static boolean getBoolean(String name, boolean defaultValue) {
        return mSharedPreferences.getBoolean(name, defaultValue);
    }

    /**
     * Retrieves the integer value for the given name. <br/>
     *
     * @param name of the field
     */
    public static int getInt(String name) {
        return mSharedPreferences.getInt(name, 0);
    }

    /**
     * Retrieves the long value for the given name. <br/>
     *
     * @param name of the field
     */
    public static long getLong(String name) {
        return mSharedPreferences.getLong(name, 0);
    }

    /**
     * Remove the given preference from the system. <br/>
     *
     * @param name of the field
     */
    public static void remove(String name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(name);
        editor.commit();
    }
}
