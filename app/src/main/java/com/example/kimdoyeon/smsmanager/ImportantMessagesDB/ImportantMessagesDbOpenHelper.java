package com.example.kimdoyeon.smsmanager.ImportantMessagesDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ImportantMessagesDbOpenHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLite)_sms.db";
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

            db.execSQL(ImportantMessagesDataBases.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ImportantMessagesDataBases.CreateDB._TABLE);
            onCreate(db);
        }
    }

    public ImportantMessagesDbOpenHelper(Context context){
        this.mCtx = context;
    }

    public ImportantMessagesDbOpenHelper open() throws SQLException {
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
    public long insertColumn(long messageId, long threadId, String address , String timestamp, String body, String name){
        ContentValues values = new ContentValues();
        values.put(ImportantMessagesDataBases.CreateDB.MESSAGE_ID, messageId);
        values.put(ImportantMessagesDataBases.CreateDB.THREAD_ID, threadId);
        values.put(ImportantMessagesDataBases.CreateDB.MESSAGE_ADDRESS, address);
        values.put(ImportantMessagesDataBases.CreateDB.NAME, name);
        values.put(ImportantMessagesDataBases.CreateDB.MESSAGE_TIME, timestamp);
        values.put(ImportantMessagesDataBases.CreateDB.MESSAGE_BODY, body);
        return mDB.insert(ImportantMessagesDataBases.CreateDB._TABLE, null, values);
    }

    // Update DB
    public boolean updateColumn(long messageId, long threadId, String address , String timestamp, String body, String name){
        ContentValues values = new ContentValues();
        values.put(ImportantMessagesDataBases.CreateDB.MESSAGE_ID, messageId);
        values.put(ImportantMessagesDataBases.CreateDB.THREAD_ID, threadId);
        values.put(ImportantMessagesDataBases.CreateDB.MESSAGE_ADDRESS, address);
        values.put(ImportantMessagesDataBases.CreateDB.NAME, name);
        values.put(ImportantMessagesDataBases.CreateDB.MESSAGE_TIME, timestamp);
        values.put(ImportantMessagesDataBases.CreateDB.MESSAGE_BODY, body);
        // return mDB.update(DataBases.CreateDB._TABLE, values, "_id=" + id, null) > 0;
        return mDB.update(ImportantMessagesDataBases.CreateDB._TABLE, values, null, null) > 0;
    }

    // Delete All
    public void deleteAllColumns() {

        mDB.delete(ImportantMessagesDataBases.CreateDB._TABLE, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id){
        return mDB.delete(ImportantMessagesDataBases.CreateDB._TABLE, "_id="+id, null) > 0;
    }
    // Select DB
    public Cursor selectColumns(){
        return mDB.query(ImportantMessagesDataBases.CreateDB._TABLE, null, null, null, null, null, null);
    }

    // sort by column
    public Cursor sortColumn(String sort){
        Cursor c = mDB.rawQuery( "SELECT * FROM Important_Messages ORDER BY " + sort + " DESC"+";", null);
        return c;
    }
}