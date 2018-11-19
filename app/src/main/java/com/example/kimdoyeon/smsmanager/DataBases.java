package com.example.kimdoyeon.smsmanager;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns{
        public static final String MESSAGE_ID = "message_id";
        public static final String THREAD_ID = "thread_id";
        public static final String MESSAGE_ADDRESS = "message_address";
        public static final String MESSAGE_TIME = "message_time";
        public static final String MESSAGE_BODY = "message_body";

        public static final String _TABLE = "Messages";

        public static final String _CREATE0 = "create table if not exists "+ _TABLE +"("
                + MESSAGE_ID+" integer primary key, "
                + THREAD_ID +" integer not null, "
                + MESSAGE_ADDRESS +" text not null, "
                + MESSAGE_TIME +" integer not null, "
                + MESSAGE_BODY +" text not null);";
    }
}