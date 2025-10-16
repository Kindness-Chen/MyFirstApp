package com.yitong.utils.sputil.configdb;

import android.content.Context;

import com.yitong.android.application.YTBaseApplication;
import com.yitong.mbank.util.security.Md5Util;
import com.yitong.utils.sputil.ISharePreference;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类似SharedPreference数据库存储
 * @author tongxu_li
 * Copyright (c) 2015 Shanghai P&C Information Technology Co., Ltd.
 */
public class DBSharedPreference implements ISharePreference {

    private static int CONFIG_DB_VERSION = 1;
    private static String CONFIG_DB_NAME = "YTConfig.db";
    private static final String TABLE_NAME_CONFIG = "TConfig";
    private static final String CONFI_TABLE_SQL = "CREATE TABLE " + TABLE_NAME_CONFIG +
            "(" +
            "KEY TEXT PRIMARY KEY," +
            "VALUE TEXT" +
            ")";

    private YTSQLiteOpenHelper dbHelper;

    public DBSharedPreference(Context context) {
        this(context, CONFIG_DB_NAME, CONFIG_DB_VERSION, CONFI_TABLE_SQL);
    }

    public DBSharedPreference(Context context, String dbName, int dbVersion, String sql) {
        SQLiteDatabase.loadLibs(YTBaseApplication.getInstance());
        dbHelper = new YTSQLiteOpenHelper(YTBaseApplication.getInstance(), CONFIG_DB_NAME, CONFIG_DB_VERSION, sql);
    }

    public boolean removeData(String key) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(TABLE_NAME_CONFIG, "KEY=?", new String[]{Md5Util.encode(key)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
        return true;
    }

    public boolean removeAllData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(TABLE_NAME_CONFIG, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
        return true;
    }

    public String getInfoFromShared(String key) {
        return getInfoFromShared(key, null);
    }

    public String getInfoFromShared(String key, String defValue) {
        String strValue = defValue;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "select * from " +
                TABLE_NAME_CONFIG +
                " where KEY = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{Md5Util.encode(key)});
        if (cursor.moveToFirst()) {
            String tempValue = cursor.getString(cursor.getColumnIndex("VALUE"));
            try {
                JSONObject jsonObject = new JSONObject(tempValue);
                strValue = jsonObject.getString("VALUE");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return strValue;
    }

    public boolean setInfoToShared(String key, String value) {
        Cursor cursor = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("KEY", key);
            jsonObject.put("VALUE", value);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "select * from " +
                    TABLE_NAME_CONFIG +
                    " where KEY = ?";
            cursor = db.rawQuery(sql, new String[]{Md5Util.encode(key)});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                sql = "update " + TABLE_NAME_CONFIG + " set VALUE = ? where KEY = ?";
                db.execSQL(sql, new Object[]{jsonObject.toString(), Md5Util.encode(key)});
            } else {
                sql = "insert into " + TABLE_NAME_CONFIG + "(KEY, VALUE) values(?, ?)";
                db.execSQL(sql, new Object[]{Md5Util.encode(key), jsonObject.toString()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return true;
    }

    public boolean getInfoFromShared(String key, boolean defValue) {
        boolean bValue = defValue;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "select * from " +
                TABLE_NAME_CONFIG +
                " where KEY = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{Md5Util.encode(key)});
        if (cursor.moveToFirst()) {
            String tempValue = cursor.getString(cursor.getColumnIndex("VALUE"));
            try {
                JSONObject jsonObject = new JSONObject(tempValue);
                bValue = jsonObject.getBoolean("VALUE");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return bValue;
    }

    public boolean setInfoToShared(String key, boolean value) {
        Cursor cursor = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("KEY", key);
            jsonObject.put("VALUE", value);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "select * from " +
                    TABLE_NAME_CONFIG +
                    " where KEY = ?";
            cursor = db.rawQuery(sql, new String[]{Md5Util.encode(key)});
            if (cursor.getCount() > 0) {
                sql = "update " + TABLE_NAME_CONFIG + " set VALUE = ? where KEY = ?";
                db.execSQL(sql, new Object[]{jsonObject.toString(), Md5Util.encode(key)});
            } else {
                sql = "insert into " + TABLE_NAME_CONFIG + "(KEY, VALUE) values(?, ?)";
                db.execSQL(sql, new Object[]{Md5Util.encode(key), jsonObject.toString()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return true;
    }

    public int getInfoFromShared(String key, int defValue) {
        int iValue = defValue;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "select * from " +
                TABLE_NAME_CONFIG +
                " where KEY = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{Md5Util.encode(key)});
        if (cursor.moveToFirst()) {
            String tempValue = cursor.getString(cursor.getColumnIndex("VALUE"));
            try {
                JSONObject jsonObject = new JSONObject(tempValue);
                iValue = jsonObject.getInt("VALUE");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return iValue;
    }

    public boolean setInfoToShared(String key, int value) {
        Cursor cursor = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("KEY", key);
            jsonObject.put("VALUE", value);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "select * from " +
                    TABLE_NAME_CONFIG +
                    " where KEY = ?";
            cursor = db.rawQuery(sql, new String[]{Md5Util.encode(key)});
            if (cursor.getCount() > 0) {
                sql = "update " + TABLE_NAME_CONFIG + " set VALUE = ? where KEY = ?";
                db.execSQL(sql, new Object[]{jsonObject.toString(), Md5Util.encode(key)});
            } else {
                sql = "insert into " + TABLE_NAME_CONFIG + "(KEY, VALUE) values(?, ?)";
                db.execSQL(sql, new Object[]{Md5Util.encode(key), jsonObject.toString()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return true;
    }
}