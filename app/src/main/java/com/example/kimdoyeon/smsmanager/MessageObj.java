package com.example.kimdoyeon.smsmanager;

public class MessageObj {
    public long message_Id;
    public long thread_Id;
    public String message_Address;
    public long message_Time;
    public String message_Body;

    public MessageObj(){

    }
    public MessageObj(long message_Id, long thread_Id, String message_Address,
                      long message_Time, String message_Body){
        this.message_Id = message_Id;
        this.thread_Id = thread_Id;
        this.message_Address = message_Address;
        this.message_Time = message_Time;
        this.message_Body = message_Body;
    }

    public long getMessage_Id() {
        return message_Id;
    }

    public void setMessage_Id(long message_Id) {
        this.message_Id = message_Id;
    }

    public long getThread_Id() {
        return thread_Id;
    }

    public void setThread_Id(long thread_Id) {
        this.thread_Id = thread_Id;
    }

    public String getMessage_Address() {
        return message_Address;
    }

    public void setMessage_Address(String message_Address) {
        this.message_Address = message_Address;
    }

    public long getMessage_Time() {
        return message_Time;
    }

    public void setMessage_Time(long message_Time) {
        this.message_Time = message_Time;
    }

    public String getMessage_Body() {
        return message_Body;
    }

    public void setMessage_Body(String message_Body) {
        this.message_Body = message_Body;
    }
}
