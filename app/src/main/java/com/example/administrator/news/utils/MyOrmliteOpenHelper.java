package com.example.administrator.news.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.news.entity.NewsCollect;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/12/22.
 */

public class MyOrmliteOpenHelper extends OrmLiteSqliteOpenHelper{

    private static final String NEWSCOLLECT_TABLE = "newscollect_table";  //快捷键 String类型 key
    private static final int DB_VERSION = 1; // 快捷键 int类型 const

    private static MyOrmliteOpenHelper instance;
    
    public MyOrmliteOpenHelper(Context context) {
        super(context, NEWSCOLLECT_TABLE, null, DB_VERSION);
    }

    public static MyOrmliteOpenHelper getInstance(Context context){
        if(instance == null){

            instance = new MyOrmliteOpenHelper(context);
        }
        return instance;

    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, NewsCollect.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        if(newVersion > oldVersion){
            try {
                TableUtils.dropTable(connectionSource,NewsCollect.class,true);
                 onCreate(database,connectionSource);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
