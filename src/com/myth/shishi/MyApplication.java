package com.myth.shishi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import com.myth.shishi.db.ColorDatabaseHelper;
import com.myth.shishi.db.DBManager;
import com.myth.shishi.db.YunDatabaseHelper;
import com.myth.shishi.entity.ColorEntity;

public class MyApplication extends Application
{

    private static List<ColorEntity> colorList;

    public static Typeface typeface;

    public static String TypefaceString[] = {"简体", "繁体"};

    @Override
    public void onCreate()
    {
        super.onCreate();
        DBManager.initDatabase(getApplicationContext());
        YunDatabaseHelper.getYunList(this);
        setTypeface(getApplicationContext(), getDefaulTypeface(this));
    }

    public static ColorEntity getColorByPos(int pos)
    {
        if (colorList == null)
        {
            colorList = ColorDatabaseHelper.getAllColor();
        }
        while (pos >= colorList.size())
        {
            pos -= colorList.size();
        }
        if (pos >= 0)
        {
            return colorList.get(pos);
        }
        else
        {
            return null;
        }
    }
//    public static int getRandomColor()
//    {
//        if (colorList == null)
//        {
//            colorList = ColorDatabaseHelper.getAllColor();
//        }
//        return colorList.get(new Random().nextInt(colorList.size())).toColor();
//    }

    public static void setTypeface(Context context, int type)
    {
        if (type == 0)
        {
            typeface = Typeface.createFromAsset(context.getAssets(), "fzqkyuesong.TTF");
        }
        else
        {
            typeface = Typeface.createFromAsset(context.getAssets(), "fzsongkebenxiukai_fanti.ttf");
        }
    }

    public static int[] bgimgList = {R.drawable.dust, R.drawable.bg001, R.drawable.bg002, R.drawable.bg004,
            R.drawable.bg006, R.drawable.bg007, R.drawable.bg011, R.drawable.bg013, R.drawable.bg072, R.drawable.bg084,
            R.drawable.bg096, R.drawable.bg118};

    public static int[] bgSmallimgList = {R.drawable.dust, R.drawable.bg001_small, R.drawable.bg002_small,
            R.drawable.bg004_small, R.drawable.bg006_small, R.drawable.bg007_small, R.drawable.bg011_small,
            R.drawable.bg013_small, R.drawable.bg072_small, R.drawable.bg084_small, R.drawable.bg096_small,
            R.drawable.bg118_small};

    public static void setDefaultTypeface(Context context, int i)
    {
        if (i < 2 && i >= 0)
        {
            Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
            edit.putInt("typeface", i);
            edit.commit();
        }
    }

    public static int getDefaulTypeface(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("typeface", 0);
    }

    public static boolean getDefaulListType(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("listType", true);
    }

    public static void setDefaultListType(Context context, boolean bool)
    {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putBoolean("listType", bool);
        edit.commit();
    }

    public static boolean getCheckAble(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("check", true);
    }

    public static void setCheckAble(Context context, boolean bool)
    {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putBoolean("check", bool);
        edit.commit();
    }

}
