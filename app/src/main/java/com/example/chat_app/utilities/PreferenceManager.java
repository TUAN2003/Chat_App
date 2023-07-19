package com.example.chat_app.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class PreferenceManager {
    private static PreferenceManager onlyPreferenceManager;
    private final SharedPreferences sharedPreferences;

    private PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static PreferenceManager newInstance(Context context) {
        if (onlyPreferenceManager == null)
            onlyPreferenceManager = new PreferenceManager(context);
        return onlyPreferenceManager;
    }

    public void putStringSet(String key, Set<String> setValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, setValue);
        editor.apply();
    }

    public Set<String> getSetString(String key) {
        return sharedPreferences.getStringSet(key, null);
    }
}
