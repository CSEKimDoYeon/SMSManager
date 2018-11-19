package com.example.kimdoyeon.smsmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DataBases.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLE);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context){
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }

    // Insert DB
    public long insertColumn(long messageId, long threadId, String address , long timestamp, String body){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.MESSAGE_ID, messageId);
        values.put(DataBases.CreateDB.THREAD_ID, threadId);
        values.put(DataBases.CreateDB.MESSAGE_ADDRESS, address);
        values.put(DataBases.CreateDB.MESSAGE_TIME, timestamp);
        values.put(DataBases.CreateDB.MESSAGE_BODY, body);
        return mDB.insert(DataBases.CreateDB._TABLE, null, values);
    }

    // Update DB
    public boolean updateColumn(long messageId, long threadId, String address , long timestamp, String body){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.MESSAGE_ID, messageId);
        values.put(DataBases.CreateDB.THREAD_ID, threadId);
        values.put(DataBases.CreateDB.MESSAGE_ADDRESS, address);
        values.put(DataBases.CreateDB.MESSAGE_TIME, timestamp);
        values.put(DataBases.CreateDB.MESSAGE_BODY, body);
       // return mDB.update(DataBases.CreateDB._TABLE, values, "_id=" + id, null) > 0;
        return mDB.update(DataBases.CreateDB._TABLE, values, null, null) > 0;
    }

    // Delete All
    public void deleteAllColumns() {
        mDB.delete(DataBases.CreateDB._TABLE, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id){
        return mDB.delete(DataBases.CreateDB._TABLE, "_id="+id, null) > 0;
    }
    // Select DB
    public Cursor selectColumns(){
        return mDB.query(DataBases.CreateDB._TABLE, null, null, null, null, null, null);
    }

    // sort by column
    public Cursor sortColumn(String sort){
        Cursor c = mDB.rawQuery( "SELECT * FROM Messages ORDER BY " + sort + ";", null);
        return c;
    }
}