package com.myth.shishi.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.myth.shishi.entity.Poetry;

import java.util.ArrayList;

public class PoetryDatabaseHelper {
    private static String TABLE_NAME = "t_poetry";

    public static ArrayList<Poetry> getAll() {
        SQLiteDatabase db = DBManager.getNewDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        return getPoetryListFromCursor(cursor);
    }

    public static ArrayList<Poetry> getAllByAuthor(String author) {
        SQLiteDatabase db = DBManager.getNewDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where d_author like '" + author + "'", null);
        return getPoetryListFromCursor(cursor);
    }

    public static ArrayList<Poetry> getAllCollect() {
        SQLiteDatabase db = DBManager.getNewDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where collect is " + 1 + "", null);
        return getPoetryListFromCursor(cursor);
    }

    public static void updateCollect(int id, boolean isCollect) {
        SQLiteDatabase db = DBManager.getNewDatabase();
        int collect = isCollect ? 1 : 0;
        db.execSQL(" update " + TABLE_NAME + " set collect= " + collect + "  where d_num is " + id);
    }

    public static boolean isCollect(String poetry) {
        SQLiteDatabase db = DBManager.getNewDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where d_poetry like '" + poetry + "'", null);
        ArrayList<Poetry> list = getPoetryListFromCursor(cursor);
        if (list != null && list.size() > 0) {
            return list.get(0).getCollect() == 1;
        }
        return false;
    }

    public static boolean isCollect(int id) {
        SQLiteDatabase db = DBManager.getNewDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where d_num is " + id, null);
        ArrayList<Poetry> list = getPoetryListFromCursor(cursor);
        if (list != null && list.size() > 0) {
            return list.get(0).getCollect() == 1;
        }
        return false;
    }

    private static ArrayList<Poetry> getPoetryListFromCursor(Cursor cursor) {
        ArrayList<Poetry> list = new ArrayList<Poetry>();
        try {
            while (cursor.moveToNext()) {
                Poetry data = new Poetry();
                data.setAuthor(cursor.getString(cursor.getColumnIndex("d_author")));
                data.setPoetry(cursor.getString(cursor.getColumnIndex("d_poetry")));
                data.setIntro(cursor.getString(cursor.getColumnIndex("d_intro")));
                data.setTitle(cursor.getString(cursor.getColumnIndex("d_title")));
                data.setCollect(cursor.getInt(cursor.getColumnIndex("collect")));
                data.setId(cursor.getInt(cursor.getColumnIndex("d_num")));
                list.add(data);
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
