package com.example.administrator.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/12/13.
 */

public class CacheUtil {

    private static String CONFIG = "config";
    public static String IS_FIRST = "is_first";
    public static final String READED = "readed";


    public static void putBooleanIntoSp(Context context, String key, boolean b) {

        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, b).commit();

    }

    public static boolean getBooleanFromSp(Context context, String key, boolean b) {

        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);

        return sp.getBoolean(key, b);

    }

    public static void putStringIntoSp(Context context, String key, String str) {

        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        sp.edit().putString(key, str).commit();
    }

    public static String getStringFromSp(Context context, String key) {

        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);

        return sp.getString(key,"");

    }


}
