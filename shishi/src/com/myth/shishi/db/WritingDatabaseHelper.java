package com.myth.shishi.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myth.shishi.entity.Writing;

public class WritingDatabaseHelper
{
    private static String TABLE_NAME = "writing";

    public static ArrayList<Writing> getAllWriting(Context context)
    {
        SQLiteDatabase db = DBManager.getDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + "  order by update_dt ", null);
        return getWritingFromCursor(cursor);
    }

    public static synchronized void saveWriting(Context context, Writing writing)
    {
        deleteWriting(context, writing);

        SQLiteDatabase db = DBManager.getDatabase();
        String sqlStr = "insert into " + TABLE_NAME
                + " ( title,id,bgimg,former_name,create_dt,text,update_dt) values ( " + "?,?,?,?,?,?,?)";
        db.execSQL(
                sqlStr,
                new String[] {writing.getTitle(), writing.getId() + "", writing.getBgimg(),
                        writing.getFormerName() + "", writing.getCreate_dt() + "", writing.getText(),
                        System.currentTimeMillis() + ""});
    }

    public static void deleteWriting(Context context, Writing writing)
    {
        SQLiteDatabase db = DBManager.getDatabase();

        db.execSQL("delete from " + TABLE_NAME + " where " + "id" + " = " + writing.getId());
    }

    private static ArrayList<Writing> getWritingFromCursor(Cursor cursor)
    {
        ArrayList<Writing> list = new ArrayList<Writing>();
        while (cursor.moveToNext())
        {
            Writing data = new Writing();
            data.setId(cursor.getInt(cursor.getColumnIndex("id")));
            data.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            data.setBgimg(cursor.getString(cursor.getColumnIndex("bgimg")));
            data.setFormerName(cursor.getString(cursor.getColumnIndex("former_name")));
            data.setCreate_dt(cursor.getLong(cursor.getColumnIndex("create_dt")));
            data.setText(cursor.getString(cursor.getColumnIndex("text")));
            data.setUpdate_dt(cursor.getLong(cursor.getColumnIndex("update_dt")));
            list.add(data);
        }
        return list;
    }

}
