package com.myth.shishi.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.myth.shishi.entity.ColorEntity;

import java.util.ArrayList;

public class ColorDatabaseHelper {
    private static String TABLE_NAME = "color";

    public static ArrayList<ColorEntity> getAll() {
        SQLiteDatabase db = DBManager.getNewDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " order by displayidx", null);
        return getColorListFromCursor(cursor);
    }

    private static ArrayList<ColorEntity> getColorListFromCursor(Cursor cursor) {
        ArrayList<ColorEntity> list = new ArrayList<ColorEntity>();
        try {
            while (cursor.moveToNext()) {
                ColorEntity color = new ColorEntity();
                color.setId(cursor.getInt(cursor.getColumnIndex("id")));
                color.setRed(cursor.getInt(cursor.getColumnIndex("red")));
                color.setGreen(cursor.getInt(cursor.getColumnIndex("green")));
                color.setBlue(cursor.getInt(cursor.getColumnIndex("blue")));
                color.setName(cursor.getString(cursor.getColumnIndex("name")));
                list.add(color);
            }
        } catch (Exception e) {
            Log.e("myth", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

}
