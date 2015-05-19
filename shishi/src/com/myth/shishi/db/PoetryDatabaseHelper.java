package com.myth.shishi.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myth.shishi.entity.Author;
import com.myth.shishi.entity.Poetry;

public class PoetryDatabaseHelper
{
    private static String TABLE_NAME = "t_poetry";

    public static ArrayList<Poetry> getAll()
    {
        SQLiteDatabase db = DBManager.getDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        return getPoetryListFromCursor(cursor);
    }

    public static ArrayList<Poetry> getAllByAuthor(String author)
    {
        SQLiteDatabase db = DBManager.getDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where d_author like '" + author + "'", null);
        return getPoetryListFromCursor(cursor);
    }

    public static ArrayList<Poetry> getAllCollect()
    {
        SQLiteDatabase db = DBManager.getDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where collect is " + 1 + "", null);
        return getPoetryListFromCursor(cursor);
    }

    public static void updateCollect(String poetry, boolean isCollect)
    {
        SQLiteDatabase db = DBManager.getDatabase();
        int collect = isCollect ? 1 : 0;
        db.execSQL(" update " + TABLE_NAME + " set collect= " + collect + "  where d_poetry like '" + poetry + "'");
    }

    public static boolean isCollect(String poetry)
    {
        SQLiteDatabase db = DBManager.getDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where d_poetry like '" + poetry + "'", null);
        ArrayList<Poetry> list = getPoetryListFromCursor(cursor);
        if (list != null && list.size() > 0)
        {
            return list.get(0).getCollect() == 1;
        }
        return false;
    }

    private static ArrayList<Poetry> getPoetryListFromCursor(Cursor cursor)
    {
        ArrayList<Poetry> list = new ArrayList<Poetry>();
        while (cursor.moveToNext())
        {
            Poetry data = new Poetry();
            data.setAuthor(cursor.getString(cursor.getColumnIndex("d_author")));
            data.setPoetry(cursor.getString(cursor.getColumnIndex("d_poetry")));
            data.setIntro(cursor.getString(cursor.getColumnIndex("d_intro")));
            data.setTitle(cursor.getString(cursor.getColumnIndex("d_title")));
            data.setCollect(cursor.getInt(cursor.getColumnIndex("collect")));
            list.add(data);
        }
        return list;
    }

}
