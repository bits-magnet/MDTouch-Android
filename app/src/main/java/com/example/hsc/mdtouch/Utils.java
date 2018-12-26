package com.example.hsc.mdtouch;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    public static final String PREF_FILE = "sample";

    public static void saveSharedSetting(Context ctx, String sn, String sv){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sn,sv);
        editor.apply();

    }

    public static String readSharedSetting(Context ctx, String sn, String sv){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getString(sn,sv);

    }

}