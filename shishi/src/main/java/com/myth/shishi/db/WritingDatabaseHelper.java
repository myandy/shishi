package com.myth.shishi.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.myth.shishi.entity.Writing;

import java.util.ArrayList;

public class WritingDatabaseHelper {
    private static String TABLE_NAME = "writing";

    public static ArrayList<Writing> getAllWriting() {
        SQLiteDatabase db = DBManager.getDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                + "  order by update_dt ", null);
        return getWritingFromCursor(cursor);
    }

    public static ArrayList<Writing> getRecentWriting() {
        SQLiteDatabase db = DBManager.getDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                + "  order by update_dt ", null);
        return getRecentWritingFromCursor(cursor);
    }

    public static synchronized void saveWriting(Writing writing) {
        deleteWriting(writing);

        SQLiteDatabase db = DBManager.getDatabase();
        String sqlStr = "insert into "
                + TABLE_NAME
                + " ( title,id,bgimg,former_name,create_dt,text,update_dt) values ( "
                + "?,?,?,?,?,?,?)";
        db.execSQL(
                sqlStr,
                new String[]{writing.getTitle(), writing.getId() + "",
                        writing.getBgimg(), writing.getFormerName() + "",
                        writing.getCreate_dt() + "", writing.getText(),
                        System.currentTimeMillis() + ""});
        BackupTask.needBackup = true;
    }

    public static void deleteWriting(Writing writing) {
        SQLiteDatabase db = DBManager.getDatabase();

        db.execSQL("delete from " + TABLE_NAME + " where " + "id" + " = "
                + writing.getId());

        BackupTask.needBackup = true;
    }

    private static ArrayList<Writing> getWritingFromCursor(Cursor cursor) {
        ArrayList<Writing> list = new ArrayList<Writing>();
        while (cursor.moveToNext()) {
            Writing data = new Writing();
            data.setId(cursor.getInt(cursor.getColumnIndex("id")));

            String titleString = cursor.getString(cursor
                    .getColumnIndex("title"));
            if (titleString == null) {
                titleString = "";
            }
            data.setTitle(titleString);
            data.setBgimg(cursor.getString(cursor.getColumnIndex("bgimg")));
            data.setFormerName(cursor.getString(cursor
                    .getColumnIndex("former_name")));
            data.setCreate_dt(cursor.getLong(cursor.getColumnIndex("create_dt")));
            String contentString = cursor.getString(cursor
                    .getColumnIndex("text"));
            if (contentString == null) {
                contentString = "";
            }
            data.setText(contentString);
            data.setUpdate_dt(cursor.getLong(cursor.getColumnIndex("update_dt")));
            list.add(data);
        }
        return list;
    }

    private static ArrayList<Writing> getRecentWritingFromCursor(Cursor cursor) {
        ArrayList<Writing> list = new ArrayList<Writing>();
        try {
            while (cursor.moveToNext()) {
                Writing data = new Writing();
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                String titleString = cursor.getString(cursor
                        .getColumnIndex("title"));
                if (titleString == null) {
                    titleString = "";
                }
                data.setTitle(titleString);
                data.setBgimg(cursor.getString(cursor.getColumnIndex("bgimg")));
                data.setFormerName(cursor.getString(cursor
                        .getColumnIndex("former_name")));
                data.setCreate_dt(cursor.getLong(cursor.getColumnIndex("create_dt")));

                String contentString = cursor.getString(cursor
                        .getColumnIndex("text"));
                if (contentString == null) {
                    contentString = "";
                }
                data.setText(contentString);
                data.setUpdate_dt(cursor.getLong(cursor.getColumnIndex("update_dt")));
                list.add(data);
                if (list.size() > 2) {
                    break;
                }
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
