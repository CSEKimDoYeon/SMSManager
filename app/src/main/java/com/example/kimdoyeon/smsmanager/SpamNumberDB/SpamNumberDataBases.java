package com.example.kimdoyeon.smsmanager.SpamNumberDB;

import android.provider.BaseColumns;

public class SpamNumberDataBases {
    public static final class CreateDB implements BaseColumns {

        public static final String SPAM_NUM = "spam_num";
        public static final String _TABLE = "Spam_Numbers";

        public static final String _CREATE0 = "create table if not exists "+ _TABLE +"("
                + _ID+" integer primary key autoincrement, "
                + SPAM_NUM +" text not null unique );";
    }
}
