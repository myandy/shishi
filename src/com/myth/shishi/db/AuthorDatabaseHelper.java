package com.myth.shishi.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myth.shishi.entity.Author;

public class AuthorDatabaseHelper
{
    private static String TABLE_NAME = "t_author";

    public static ArrayList<Author> getAll()
    {
        SQLiteDatabase db = DBManager.getDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        return getAuthorListFromCursor(cursor);
    }

//    public static Author getByAuthor(String author)
//    {
//        SQLiteDatabase db = DBManager.getDatabase();
//        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where d_author = " + author, null);
//        return getAuthorListFromCursor(cursor).get(0);
//    }

    public static void update(String author, String s)
    {
        SQLiteDatabase db = DBManager.getDatabase();
        db.execSQL(" update " + TABLE_NAME +" set en_name= '"+ s+ " ' where d_author like '" + author +"'");
    }

    private static ArrayList<Author> getAuthorListFromCursor(Cursor cursor)
    {
        ArrayList<Author> list = new ArrayList<Author>();
        while (cursor.moveToNext())
        {
            Author author = new Author();
            author.setDynasty(cursor.getString(cursor.getColumnIndex("d_dynasty")));
            author.setAuthor(cursor.getString(cursor.getColumnIndex("d_author")));
            author.setIntro(cursor.getString(cursor.getColumnIndex("d_intro")));
            author.setP_num(cursor.getInt(cursor.getColumnIndex("p_num")));
            author.setEn_name(cursor.getString(cursor.getColumnIndex("en_name")));
            list.add(author);
        }
        return list;
    }

    public static Author getAuthorById(int ci_id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public static ArrayList<Author> getAllShowAuthor()
    {
        return getAll();
    }

    public static ArrayList<Author> getAllAuthor()
    {
        return getAll();
    }

    public static ArrayList<Author> getAllAuthorByWordCount()
    {
        return getAll();
    }

}
