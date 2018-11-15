package com.example.kimdoyeon.smsmanager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    static final int SMS_READ_PERMISSON = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //readSMSMessage();
        //권한이 부여되어 있는지 확인
        int permissonCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (permissonCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "SMS 수신권한 있음", Toast.LENGTH_SHORT).show();

            readSMSMessage();

        } else {
            Toast.makeText(getApplicationContext(), "SMS 수신권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(getApplicationContext(), "SMS권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_READ_PERMISSON);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_READ_PERMISSON);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int grantResults[]) {
        switch (requestCode) {
            case SMS_READ_PERMISSON:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "SMS권한 승인함", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "SMS권한 거부함", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public int readSMSMessage() {
        Toast.makeText(getApplicationContext(), "문자 목록을 로그캣에 출력합니다.", Toast.LENGTH_SHORT).show();

        Uri allMessage = Uri.parse("content://sms/inbox");
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(allMessage, new String[]{"_id", "thread_id", "address", "person", "date", "body"}, null, null, "date DESC");

        String string = "";
        int count = 0;
        while (c.moveToNext()) {
            long messageId = c.getLong(0);
            long threadId = c.getLong(1);
            String address = c.getString(2);
            long contactId = c.getLong(3);
            String contactId_string = String.valueOf(contactId);
            long timestamp = c.getLong(4);
            String body = c.getString(5);

            string = String.format("msgid:%d, threadid:%d, address:%s, " + "contactid:%d, contackstring:%s, timestamp:%d, body:%s", messageId, threadId, address, contactId,
                    contactId_string, timestamp, body);

            Log.e("heylee", ++count + "st, Message: " + string);
        }
        return 0;
    }

    public void SMSDelete(View view) {
        ContentResolver cr = getContentResolver();
        Toast.makeText(getApplicationContext(), "문자 삭제 실행", Toast.LENGTH_SHORT).show();

        try {
            Uri uriSms = Uri.parse("content://sms");
            Cursor c = cr.query(uriSms,
                    new String[]{"_id", "thread_id", "address",
                            "person", "date", "body"}, null, null, null);

            while (c.moveToNext()) {      // Delete SMS
                long thread_id = c.getLong(1);   // SMS는 thread_id 값을

                Uri thread = Uri.parse("content://sms/conversations/" + thread_id);
                getContentResolver().delete(thread, null, null);

            }

        } catch (Exception e) {
            //mLogger.logError("Could not delete SMS from inbox: " + e.getMessage());
        }
    }

    public int deleteSpecifiedSMSHistory(View view){

        Toast.makeText(getApplicationContext(), "문자 삭제합니다.", Toast.LENGTH_SHORT).show();

        Uri allMessage = Uri.parse("content://sms/inbox");
        ContentResolver cr = getContentResolver();
        //Cursor c = cr.query(allMessage, new String[]{"_id", "thread_id", "address", "person", "date", "body"}, null, null, "date DESC");
        Cursor c = cr.query(allMessage, new String[]{"_id", "thread_id", "address", "person", "date", "body"}, null, null, null);
        String string = "";
        int count = 0;
        while (c.moveToNext()) {
            long messageId = c.getLong(0);
            long threadId = c.getLong(1);
            String address = c.getString(2);
            long contactId = c.getLong(3);
            String contactId_string = String.valueOf(contactId);
            long timestamp = c.getLong(4);
            String body = c.getString(5);

            Log.e("삭제한 문자의 threadId", "Thread ID : " + threadId);

            /*string = String.format("msgid:%d, threadid:%d, address:%s, " + "contactid:%d, contackstring:%s, timestamp:%d, body:%s", messageId, threadId, address, contactId,
                    contactId_string, timestamp, body);

            Log.e("heylee", ++count + "st, Message: " + string);*/
            Uri thread = Uri.parse("content://sms/conversations/" + threadId);
            cr.delete(thread,null,null);
        }
        return 0;
    }
}
