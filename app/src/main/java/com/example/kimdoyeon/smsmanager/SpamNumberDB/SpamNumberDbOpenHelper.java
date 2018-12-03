package com.example.kimdoyeon.smsmanager.SpamNumberDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SpamNumberDbOpenHelper {
    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private SpamNumberDatabaseHelper mDBHelper;
    private Context mCtx;

    private class SpamNumberDatabaseHelper extends SQLiteOpenHelper {

        public SpamNumberDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(SpamNumberDataBases.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+SpamNumberDataBases.CreateDB._TABLE);
            onCreate(db);
        }
    }

    public SpamNumberDbOpenHelper(Context context){
        this.mCtx = context;
    }

    public SpamNumberDbOpenHelper open() throws SQLException {
        mDBHelper = new SpamNumberDatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
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
        values.put(SpamNumberDataBases.CreateDB.SPAM_NUM, keyword);
        return mDB.insert(SpamNumberDataBases.CreateDB._TABLE, null, values);
    }

    // Update DB
    public boolean updateColumn(String keyword){
        ContentValues values = new ContentValues();
        values.put(SpamNumberDataBases.CreateDB.SPAM_NUM, keyword);
        // return mDB.update(DataBases.CreateDB._TABLE, values, "_id=" + id, null) > 0;
        return mDB.update(SpamNumberDataBases.CreateDB._TABLE, values, null, null) > 0;
    }

    // Delete All
    public void deleteAllColumns() {

        mDB.delete(SpamNumberDataBases.CreateDB._TABLE, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id){
        return mDB.delete(SpamNumberDataBases.CreateDB._TABLE, "_id="+id, null) > 0;
    }

    public boolean deleteColumnForKeyword(String num){ // 리스트에서 누른 아이템과 일치하는 데이터를 삭제한다.
        mDB.execSQL("DELETE FROM Spam_Numbers WHERE SPAM_NUM=" +  "\""+num+  "\"");
        return true;
        //return mDB.delete(DeleteKeywordDataBases.CreateDB._TABLE, "DELETE_KEYWORD="+keyword, null) > 0;
    }
    // Select DB
    public Cursor selectColumns(){
        return mDB.query(SpamNumberDataBases.CreateDB._TABLE, null, null, null, null, null, null);
    }

    // sort by column
    public Cursor sortColumn(String sort){
        Cursor c = mDB.rawQuery( "SELECT * FROM Spam_Numbers ORDER BY " + sort + ";", null);
        return c;
    }
}
