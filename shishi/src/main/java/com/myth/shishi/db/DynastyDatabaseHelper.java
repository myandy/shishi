package com.myth.shishi.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DynastyDatabaseHelper {
    private static String TABLE_NAME = "t_dynasty";

    public static String[] dynastyArray = {"全部朝代", "先秦", "汉朝", "魏晋", "南北朝", "唐朝", "北宋", "南宋", "元朝", "明朝", "清朝", "近代",
            "当代"};

    public static ArrayList<String> getAll() {
        SQLiteDatabase db = DBManager.getNewDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " order by d_num", null);

        return getDynastyListFromCursor(cursor);
    }

    private static ArrayList<String> getDynastyListFromCursor(Cursor cursor) {

        ArrayList<String> list = new ArrayList<String>();
        try {
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex("d_dynasty")));
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
