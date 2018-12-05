package com.example.kimdoyeon.smsmanager.DeletedMessageDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DeletedMessageDbOpenHelper {
    private static final String DATABASE_NAME = "InnerDatabase(SQLite)_sms.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DeletedMessageDatabaseHelper mDBHelper;
    private Context mCtx;

    private class DeletedMessageDatabaseHelper extends SQLiteOpenHelper {

        public DeletedMessageDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){

            db.execSQL(DeletedMessageDataBases.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DeletedMessageDataBases.CreateDB._TABLE);
            onCreate(db);
        }
    }

    public DeletedMessageDbOpenHelper(Context context){
        this.mCtx = context;
    }

    public DeletedMessageDbOpenHelper open() throws SQLException {
        mDBHelper = new DeletedMessageDatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
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
        values.put(DeletedMessageDataBases.CreateDB.MESSAGE_ID, messageId);
        values.put(DeletedMessageDataBases.CreateDB.THREAD_ID, threadId);
        values.put(DeletedMessageDataBases.CreateDB.MESSAGE_ADDRESS, address);
        values.put(DeletedMessageDataBases.CreateDB.MESSAGE_TIME, timestamp);
        values.put(DeletedMessageDataBases.CreateDB.MESSAGE_BODY, body);
        values.put(DeletedMessageDataBases.CreateDB.NAME, name);
        return mDB.insert(DeletedMessageDataBases.CreateDB._TABLE, null, values);
    }

    // Update DB
    public boolean updateColumn(long messageId, long threadId, String address , String timestamp, String body, String name){
        ContentValues values = new ContentValues();
        values.put(DeletedMessageDataBases.CreateDB.MESSAGE_ID, messageId);
        values.put(DeletedMessageDataBases.CreateDB.THREAD_ID, threadId);
        values.put(DeletedMessageDataBases.CreateDB.MESSAGE_ADDRESS, address);
        values.put(DeletedMessageDataBases.CreateDB.MESSAGE_TIME, timestamp);
        values.put(DeletedMessageDataBases.CreateDB.MESSAGE_BODY, body);
        values.put(DeletedMessageDataBases.CreateDB.NAME, name);
        // return mDB.update(DataBases.CreateDB._TABLE, values, "_id=" + id, null) > 0;
        return mDB.update(DeletedMessageDataBases.CreateDB._TABLE, values, null, null) > 0;
    }

    // Delete All
    public void deleteAllColumns() {

        mDB.delete(DeletedMessageDataBases.CreateDB._TABLE, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id){
        return mDB.delete(DeletedMessageDataBases.CreateDB._TABLE, "_id="+id, null) > 0;
    }
    // Select DB
    public Cursor selectColumns(){
        return mDB.query(DeletedMessageDataBases.CreateDB._TABLE, null, null, null, null, null, null);
    }

    // sort by column
    public Cursor sortColumn(String sort){
        Cursor c = mDB.rawQuery( "SELECT * FROM Deleted_Messages ORDER BY " + sort + " DESC"+";", null);
        return c;
    }
}
