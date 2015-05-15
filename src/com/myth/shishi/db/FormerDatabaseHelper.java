package com.myth.shishi.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myth.shishi.entity.Former;

public class FormerDatabaseHelper
{
    private static String TABLE_NAME = "t_yun";

    public static ArrayList<Former> getAll()
    {
        SQLiteDatabase db = DBManager.getDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        return getFormerListFromCursor(cursor);
    }

    private static ArrayList<Former> getFormerListFromCursor(Cursor cursor)
    {
        ArrayList<Former> list = new ArrayList<Former>();
        while (cursor.moveToNext())
        {
            Former data = new Former();
            data.setName(cursor.getString(cursor.getColumnIndex("name")));
            data.setYun(cursor.getString(cursor.getColumnIndex("yun")));
            list.add(data);
        }
        return list;
    }

    public static Former getFormerByName(String name)
    {
        SQLiteDatabase db = DBManager.getDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where name like '" + name + "'", null);
        List<Former> list = getFormerListFromCursor(cursor);
        if (list != null && list.size() > 0)
        {
            return list.get(0);
        }
        else
        {
            return new Former();
        }
    }

}
