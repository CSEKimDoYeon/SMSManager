package com.example.kimdoyeon.smsmanager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.provider.Telephony.Sms;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private DbOpenHelper mDbOpenHelper;

    public ArrayList<MessageObj> mArray = new ArrayList<MessageObj>();
    static final int SMS_READ_PERMISSON = 1;
    String sort = "message_id";

    Button Ads_btn;

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
        Ads_btn = (Button) findViewById(R.id.ads_button);


        /*--------내부 데이터베이스 OPEN--------*/
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();
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

            MessageObj mObj = new MessageObj(messageId, threadId, address, timestamp, body); // 해당 column을 바탕으로 메시지 객체 생성.
            mArray.add(mObj); // ArrayList에 추가.

            //mDbOpenHelper.open();
            //mDbOpenHelper.insertColumn(messageId, threadId, address, timestamp, body);
            //showDatabase(sort);

        }
        c.close();
        return 0;
    }

    public void showDatabase(String sort) {
        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        //arrayData.clear();
        // arrayIndex.clear();
        while (iCursor.moveToNext()) {
            /*
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String tempID = iCursor.getString(iCursor.getColumnIndex("userid"));
            tempID = setTextLength(tempID,10);
            String tempName = iCursor.getString(iCursor.getColumnIndex("name"));
            tempName = setTextLength(tempName,10);
            String tempAge = iCursor.getString(iCursor.getColumnIndex("age"));
            tempAge = setTextLength(tempAge,10);
            String tempGender = iCursor.getString(iCursor.getColumnIndex("gender"));
            tempGender = setTextLength(tempGender,10);*/

            long message_id = iCursor.getLong(iCursor.getColumnIndex("message_id"));
            long thread_id = iCursor.getLong(iCursor.getColumnIndex("thread_id"));
            String address = iCursor.getString(iCursor.getColumnIndex("message_address"));
            long message_time = iCursor.getLong(iCursor.getColumnIndex("message_time"));
            String body = iCursor.getString(iCursor.getColumnIndex("message_body"));

            Log.e("column","message_id : "+message_id+" / thread_id : "+thread_id+ " / address : "+address+
            " / message_time : "+message_time+ " / body : " + body);
            //String Result = tempID + tempName + tempAge + tempGender;
            //arrayData.add(Result);
            //arrayIndex.add(tempIndex);
        }
        //arrayAdapter.clear();
        //arrayAdapter.addAll(arrayData);
        //arrayAdapter.notifyDataSetChanged();
    }
}
