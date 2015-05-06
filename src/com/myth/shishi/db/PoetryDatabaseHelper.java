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
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where d_author like '"+author+"'", null);
        return getPoetryListFromCursor(cursor);
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
            list.add(data);
        }
        return list;
    }

}
