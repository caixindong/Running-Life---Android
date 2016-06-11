package com.example.caixindong.big_pro.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by caixindong on 16/6/8.
 */
public class DBHelper extends SQLiteOpenHelper{
    public static final String DB_NAME = "rundb";
    public static final String TABLE_NAME = "run_record";
    public static final String ID = "Id";
    public static final String DISTANCE = "distance";
    public static final String KCAL = "kcal";
    public static final String SPEED = "speed";
    public static final String DUR = "during";
    public static final String STEP = "step";
    public static final String TIME = "starttime";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLE_NAME + "(" + ID + " integer primary key autoincrement," + DISTANCE + " float," + KCAL + " float," + DUR + " int,"+ STEP + " int,"+ TIME + " text,"+ SPEED + " float)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
