package com.example.kimdoyeon.smsmanager.DeleteKeywordDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DeleteKeywordDbOpenHelper {
    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DeleteKeywordDatabaseHelper mDBHelper;
    private Context mCtx;

    private class DeleteKeywordDatabaseHelper extends SQLiteOpenHelper {

        public DeleteKeywordDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DeleteKeywordDataBases.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DeleteKeywordDataBases.CreateDB._TABLE);
            onCreate(db);
        }
    }

    public DeleteKeywordDbOpenHelper(Context context){
        this.mCtx = context;
    }

    public DeleteKeywordDbOpenHelper open() throws SQLException {
        mDBHelper = new DeleteKeywordDatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
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
    public long insertColumn(String keyword){
        ContentValues values = new ContentValues();
        values.put(DeleteKeywordDataBases.CreateDB.DELETE_KEYWORD, keyword);
        return mDB.insert(DeleteKeywordDataBases.CreateDB._TABLE, null, values);
    }

    // Update DB
    public boolean updateColumn(String keyword){
        ContentValues values = new ContentValues();
        values.put(DeleteKeywordDataBases.CreateDB.DELETE_KEYWORD, keyword);
        // return mDB.update(DataBases.CreateDB._TABLE, values, "_id=" + id, null) > 0;
        return mDB.update(DeleteKeywordDataBases.CreateDB._TABLE, values, null, null) > 0;
    }

    // Delete All
    public void deleteAllColumns() {

        mDB.delete(DeleteKeywordDataBases.CreateDB._TABLE, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id){
        return mDB.delete(DeleteKeywordDataBases.CreateDB._TABLE, "_id="+id, null) > 0;
    }

    public boolean deleteColumnForKeyword(String keyword){
        mDB.execSQL("DELETE FROM Delete_Keywords WHERE DELETE_KEYWORD=" +  "\""+keyword+  "\"");
        return true;
        //return mDB.delete(DeleteKeywordDataBases.CreateDB._TABLE, "DELETE_KEYWORD="+keyword, null) > 0;
    }
    // Select DB
    public Cursor selectColumns(){
        return mDB.query(DeleteKeywordDataBases.CreateDB._TABLE, null, null, null, null, null, null);
    }

    // sort by column
    public Cursor sortColumn(String sort){
        Cursor c = mDB.rawQuery( "SELECT * FROM Delete_Keywords ORDER BY " + sort + ";", null);
        return c;
    }
}
