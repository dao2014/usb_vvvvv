package com.esp_v6.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 数据缓存
 * @author chenming
 *
 */
public class SharedFileUtils
{

    private SharedPreferences sp;

    public SharedFileUtils(Context context)
    {
        sp = context.getSharedPreferences("pk_file.xml",
                Context.MODE_PRIVATE);
    }

    public void putString(String key, String value)
    {
        if (key == null || key.equals(""))
            throw new IllegalArgumentException(
                    "Key can't be null or empty string");
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key)
    {
        if (key == null || key.equals(""))
            throw new IllegalArgumentException(
                    "Key can't be null or empty string");

        return sp.getString(key, "");
    }

    public int getInt(String key)
    {
        return sp.getInt(key, 0);
    }

    public void putInt(String key, int value)
    {
        Editor e = sp.edit();
        e.putInt(key, value);
        e.commit();
    }

    public void putBoolean(String key, boolean value)
    {
        if (key == null || key.equals(""))
            throw new IllegalArgumentException(
                    "Key can't be null or empty string");
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key)
    {
        return sp.getBoolean(key, false);
    }

    public boolean isContainsKey(String key)
    {
        return sp.contains(key);
    }

    public long getLong(String key)
    {
        return sp.getLong(key, 0);
    }

    public void putLong(String key, long value)
    {
        Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }
}
