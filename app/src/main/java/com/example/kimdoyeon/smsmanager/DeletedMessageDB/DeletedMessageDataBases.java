package com.example.kimdoyeon.smsmanager.DeletedMessageDB;

import android.provider.BaseColumns;

public class DeletedMessageDataBases {
    public static final class CreateDB implements BaseColumns {
        public static final String MESSAGE_ID = "message_id";
        public static final String THREAD_ID = "thread_id";
        public static final String MESSAGE_ADDRESS = "message_address";
        public static final String MESSAGE_TIME = "message_time";
        public static final String MESSAGE_BODY = "message_body";
        public static final String NAME = "name";

        public static final String _TABLE = "Deleted_Messages";

        public static final String _CREATE0 = "create table if not exists "+ _TABLE +"("
                + _ID+" integer primary key autoincrement, "
                + MESSAGE_ID+" integer not null , "
                + THREAD_ID +" integer not null , "
                + MESSAGE_ADDRESS +" text not null , "
                + NAME +" text , "
                + MESSAGE_TIME +" text not null , "
                + MESSAGE_BODY +" text not null );";
    }
}
