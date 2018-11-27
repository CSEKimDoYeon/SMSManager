package com.example.kimdoyeon.smsmanager.DeleteKeywordDB;

import android.provider.BaseColumns;

public class DeleteKeywordDataBases {
    public static final class CreateDB implements BaseColumns {

        public static final String DELETE_KEYWORD = "delete_keyword";
        public static final String _TABLE = "Delete_Keywords";

        public static final String _CREATE0 = "create table if not exists "+ _TABLE +"("
                + _ID+" integer primary key autoincrement, "
                + DELETE_KEYWORD +" text not null unique );";
    }
}
