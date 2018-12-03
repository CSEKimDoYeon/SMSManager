package com.example.kimdoyeon.smsmanager.ImportantKeywordDB;

import android.provider.BaseColumns;

public class ImportantKeywordsDataBases {
    public static final class CreateDB implements BaseColumns {

        public static final String IMPORTANT_KEYWORD = "important_keyword";
        public static final String _TABLE = "important_Keywords";

        public static final String _CREATE0 = "create table if not exists "+ _TABLE +"("
                + _ID+" integer primary key autoincrement, "
                + IMPORTANT_KEYWORD +" text not null unique );";
    }
}