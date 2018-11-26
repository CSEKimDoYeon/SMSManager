package com.example.kimdoyeon.smsmanager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.Telephony;
import android.provider.Telephony.Sms;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private Button btnShowNavigationDrawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private DbOpenHelper mDbOpenHelper;

    ListView listView;
    ListViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ArrayList<MessageObj> mArray = new ArrayList<MessageObj>();
    static final int SMS_READ_PERMISSON = 1;
    String sort = "message_id";

    Button Ads_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*-------------Navigation Drawer------------*/
        toolbar = (Toolbar) findViewById(R.id.toolbarInclude);
        setSupportActionBar(toolbar);

        btnShowNavigationDrawer = (Button) toolbar.findViewById(R.id.btnShowNavigationDrawer);
        btnShowNavigationDrawer.setOnClickListener(onClickListener);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        //actionBarDrawerToggle = setUpActionBarToggle();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        setUpDrawerContent(navigationView);
        /*-------------Navigation Drawer------------*/


        /*--------------------------------ListView 정의------------------------------------------*/
        adapter = new ListViewAdapter(this, R.layout.listview_item, mArray);
        listView = (ListView) findViewById(R.id.listview1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                MessageObj mObj = (MessageObj) parent.getItemAtPosition(position);

                String Address = mObj.getMessage_Address();
                String Body = mObj.getMessage_Body();

                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                intent.putExtra("key_Address", Address);
                intent.putExtra("key_Body", Body);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // 새로고침 할 작업


                swipeRefreshLayout.setRefreshing(false);
            }
        });
        /*----------------------------------------------------------------------------------------*/


        //문자 읽기 권한이 부여되어 있는지 확인
        int permissonCheck_Read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (permissonCheck_Read == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "SMS 수신권한 있음", Toast.LENGTH_SHORT).show();

            mDbOpenHelper = new DbOpenHelper(this);
            mDbOpenHelper.open();
            mDbOpenHelper.create();

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


        int permissonCheck_Send = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (permissonCheck_Send == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "SMS 송신권한 있음", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "SMS 송신권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(getApplicationContext(), "SMS권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_READ_PERMISSON);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_READ_PERMISSON);
            }
        }


    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnShowNavigationDrawer:
                    drawerLayout.openDrawer(GravityCompat.START);
                    break;
            }
        }
    };

    private void setUpDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.first_navigation_item:
                        Toast.makeText(MainActivity.this, "첫번째 Navigation Item 입니다", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.second_navigation_item:
                        Toast.makeText(MainActivity.this, "두번째 Navigation Item 입니다", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.third_navigation_item:
                        Toast.makeText(MainActivity.this, "세번째 Navigation Item 입니다", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }

    /*private ActionBarDrawerToggle setUpActionBarToggle(){
        return new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }*/


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
        mArray.clear();
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
            mDbOpenHelper.insertColumn(messageId, threadId, address, timestamp, body);
        }
        showDatabase(sort);
        addAllOfData_To_ListView(mArray);

        c.close();
        return 0;
    }

    public void showDatabase(String sort) {
        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        //arrayData.clear();
        // arrayIndex.clear();
        while (iCursor.moveToNext()) {

            long message_id = iCursor.getLong(iCursor.getColumnIndex("message_id"));
            long thread_id = iCursor.getLong(iCursor.getColumnIndex("thread_id"));
            String address = iCursor.getString(iCursor.getColumnIndex("message_address"));
            long message_time = iCursor.getLong(iCursor.getColumnIndex("message_time"));
            String body = iCursor.getString(iCursor.getColumnIndex("message_body"));

            Log.e("column", "message_id : " + message_id + " / thread_id : " + thread_id + " / address : " + address +
                    " / message_time : " + message_time + " / body : " + body + "\n");

        }
    }

    public void addAllOfData_To_ListView(ArrayList<MessageObj> mArray) {
        for (int i = 0; i < mArray.size(); i++) {
            adapter.addItem(mArray.get(i).getMessage_Address(), mArray.get(i).getMessage_Body());
        }
    }
}