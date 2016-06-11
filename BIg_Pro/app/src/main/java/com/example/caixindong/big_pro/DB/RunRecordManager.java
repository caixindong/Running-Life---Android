package com.example.caixindong.big_pro.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.caixindong.big_pro.Bean.Run;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixindong on 16/6/8.
 */
public class RunRecordManager {
    private DBHelper dbHelper = null;
    private SQLiteDatabase db = null;

    public RunRecordManager(Context context){
        dbHelper = new DBHelper(context,DBHelper.DB_NAME,null,1);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 插入跑步数据
     * @param run
     * @return
     */
    public long insertRecord(Run run){
        ContentValues values = new ContentValues();
        values.put(DBHelper.DISTANCE,run.getDistance());
        values.put(DBHelper.DUR,run.getTimer());
        values.put(DBHelper.KCAL,run.getCalories());
        values.put(DBHelper.SPEED,run.getVelocity());
        values.put(DBHelper.STEP,run.getStepCount());
        values.put(DBHelper.TIME,run.getStartTime());
        long rid = db.insert(DBHelper.TABLE_NAME,null,values);
        return rid;
    }

    public List<Run> getRunRecords(){
        ArrayList<Run> runs = new ArrayList<Run>();
        Cursor c = db.rawQuery("SELECT * FROM "+DBHelper.TABLE_NAME,null);
        while (c.moveToNext()){
            Run run = new Run();
            run.setId(c.getInt(c.getColumnIndex(DBHelper.ID)));
            run.setCalories(c.getDouble(c.getColumnIndex(DBHelper.KCAL)));
            run.setDistance(c.getDouble(c.getColumnIndex(DBHelper.DISTANCE)));
            run.setTimer(c.getLong(c.getColumnIndex(DBHelper.DUR)));
            run.setVelocity(c.getDouble(c.getColumnIndex(DBHelper.SPEED)));
            run.setStepCount(c.getInt(c.getColumnIndex(DBHelper.STEP)));
            run.setStartTime(c.getString(c.getColumnIndex(DBHelper.TIME)));
            runs.add(run);
        }
        c.close();
        return runs;

    }

    /**
     * 获取跑步次数
     * @return
     */
    public int runCount(){
        return getRunRecords().size();
    }

    public long totalTime(){
        List<Run> runs = getRunRecords();
        long total = 0;
        for (Run run: runs
             ) {
            total += run.getTimer();
        }
        return total;
    }

    public double totalDistance(){
        List<Run> runs = getRunRecords();
        double total = 0;
        for (Run run: runs
                ) {
            total += run.getDistance();
        }
        Log.d("total-->distance",total+"");
        return total;
    }

    public String advSpeed(){
        DecimalFormat df = new DecimalFormat("#.0");

        return df.format(totalDistance()*1000/totalTime());
    }

    public void closeDB(){
        db.close();;
    }

}
