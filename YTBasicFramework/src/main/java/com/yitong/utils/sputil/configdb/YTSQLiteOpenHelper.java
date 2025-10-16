package com.yitong.utils.sputil.configdb;


import android.content.Context;

import com.yitong.logs.Logs;
import com.yitong.mbank.util.security.Md5Util;
import com.yitong.utils.StringTools;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


/**
 * 数据库操作工具类
 * @author tongxu_li
 * Copyright (c) 2015 Shanghai P&C Information Technology Co., Ltd.
 */
public class YTSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "YTSQLiteOpenHelper";
    private String dbPassword = null;
    private String dbSQL = null;

    public YTSQLiteOpenHelper(Context context, String name, int version, String dbSQL) {
        super(context, name, null, version);
        this.dbPassword = Md5Util.encode(name);
        this.dbSQL = dbSQL;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Logs.d(TAG, "onCreate");
        initDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logs.d(TAG, "onUpgrade");
        updateDatabase(db);
    }

    private void initDatabase(SQLiteDatabase db) {
        if (!StringTools.isEmpty(dbSQL)) {
            execBatchSQL(db, dbSQL);
        }
    }

    private void updateDatabase(SQLiteDatabase db) {
        if (!StringTools.isEmpty(dbSQL)) {
            execBatchSQL(db, dbSQL);
        }
    }

    /**
     * 执行多条SQL语句查询
     * @param db
     * @param batchSQL
     */
    private void execBatchSQL(SQLiteDatabase db, String batchSQL) {
        try {
            String[] sqlArr = batchSQL.split(";");
            for (int i=0; i<sqlArr.length; i++) {
                db.execSQL(sqlArr[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public SQLiteDatabase getWritableDatabase() {
		return getWritableDatabase(dbPassword);
	}
}