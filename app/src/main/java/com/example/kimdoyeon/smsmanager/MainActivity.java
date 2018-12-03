package com.example.kimdoyeon.smsmanager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kimdoyeon.smsmanager.DeleteKeywordDB.DeleteKeywordDbOpenHelper;
import com.example.kimdoyeon.smsmanager.DeletedMessageDB.DeletedMessageDbOpenHelper;
import com.example.kimdoyeon.smsmanager.ListViewAdapter.ListViewAdapter;
import com.example.kimdoyeon.smsmanager.MainDB.MainDbOpenHelper;
import com.example.kimdoyeon.smsmanager.Objects.MessageObj;
import com.example.kimdoyeon.smsmanager.SpamNumberDB.SpamNumberDbOpenHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private Button btnShowNavigationDrawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private MainDbOpenHelper mDbOpenHelper; // 메인 DB를 오픈 할 Helper.
    private DeleteKeywordDbOpenHelper deleteDbOpenHelper; // DeleteKeywordDb를 오픈할 Helper.
    private SpamNumberDbOpenHelper spamDbOpenHelper; // 스팸번호 DB를 오픈할 Helper.
    private DeletedMessageDbOpenHelper deletedMessageDbOpenHelper; // 삭제된 메시지 DB를 오픈할 Helper.

    ListView listView;
    ListViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ArrayList<MessageObj> mArray = new ArrayList<MessageObj>();
    public ArrayList<String> delete_Keyword_Array = new ArrayList<String>();
    public ArrayList<String> spam_Number_Array = new ArrayList<String>();

    static final int SMS_READ_PERMISSON = 1;
    static final int SMS_SEND_PERMISSON = 1;

    String sort = "message_id";
    String sort_Delete_Keyword = "_id";

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

        listView = (ListView) findViewById(R.id.listview1);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // 새로고침 할 작업
                readSMSMessage();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        /*----------------------------------------------------------------------------------------*/


        //문자 읽기 권한이 부여되어 있는지 확인
        int permissonCheck_Read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (permissonCheck_Read == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(), "SMS 수신권한 있음", Toast.LENGTH_SHORT).show();
            mDbOpenHelper = new MainDbOpenHelper(this);
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
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_SEND_PERMISSON);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_SEND_PERMISSON);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else
            super.onBackPressed();
    }

    @Override
    protected void onStop() {
        drawerLayout.closeDrawer(Gravity.LEFT);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        readSMSMessage();
        super.onRestart();
    }

    private void setUpDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.first_navigation_item:
                        Intent intent1 = new Intent(MainActivity.this, ImportantMessagesActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.second_navigation_item:
                        Intent intent2 = new Intent(MainActivity.this, DeleteKeywordActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.third_navigation_item:
                        Intent intent3 = new Intent(MainActivity.this, SpamNumberActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.fourth_navigation_item:
                        Intent intent4 = new Intent(MainActivity.this, DeletedMessageActivity.class);
                        startActivity(intent4);
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
                    //Toast.makeText(getApplicationContext(), "SMS읽기 승인함", Toast.LENGTH_SHORT).show();

                    mDbOpenHelper = new MainDbOpenHelper(this);
                    mDbOpenHelper.open();
                    mDbOpenHelper.create();

                    readSMSMessage();
                } else {
                    //Toast.makeText(getApplicationContext(), "SMS읽기 거부함", Toast.LENGTH_SHORT).show();
                }
                break;

           /* case SMS_SEND_PERMISSON:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "SMS쓰기 승인함", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "SMS쓰기 거부함", Toast.LENGTH_SHORT).show();
                }
                break;*/
        }
    }

    public int readSMSMessage() {
        mDbOpenHelper.deleteAllColumns(); // 메인 디비를 전부 지운다.

        deletedMessageDbOpenHelper = new DeletedMessageDbOpenHelper(this);
        deletedMessageDbOpenHelper.open();
        deletedMessageDbOpenHelper.create();
        deletedMessageDbOpenHelper.deleteAllColumns();

        /*DeleteKeyword 디비에서 데이터 가져온다*/
        setDelete_Keyword_Array();
        setSpam_Num_Array();
        /*--------------------------------------*/

        mArray.clear();
        adapter = new ListViewAdapter(this, R.layout.listview_item, mArray);

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

            // << 이부분에 검사해서 삭제키워드가 포함 안되었고, 스팸번호가 아닐 경우에만 아래 기능을 수행한다.
            if (deleteSMSIncludeDeleteKeyword(delete_Keyword_Array, body) == false
                    && deleteSMSIncludeSpamNum(spam_Number_Array, address) == false) {
                MessageObj mObj = new MessageObj(messageId, threadId, address, timestamp, body); // 해당 column을 바탕으로 메시지 객체 생성.
                mArray.add(mObj); // ArrayList에 추가.
                adapter.addItem(mObj.getMessage_Address(), mObj.getMessage_Body());
                mDbOpenHelper.insertColumn(messageId, threadId, address, timestamp, body);
            }
            else
                deletedMessageDbOpenHelper.insertColumn(messageId, threadId, address, timestamp, body);
                // 위에 해당하지 않으면 삭제메시지 DB로 보낸다.
            // ------------------------------------------------------------------------------
        }
        showDatabase(sort);
        //addAllOfData_To_ListView(mArray);

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

        c.close();
        return 0;
    }

    public void showDatabase(String sort) {

        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        Log.e("showDatabase", "DB Size: " + iCursor.getCount());
        //arrayData.clear();
        // arrayIndex.clear();
        while (iCursor.moveToNext()) {
            //long _id = iCursor.getLong(iCursor.getColumnIndex("_id"));
            long message_id = iCursor.getLong(iCursor.getColumnIndex("message_id"));
            long thread_id = iCursor.getLong(iCursor.getColumnIndex("thread_id"));
            String address = iCursor.getString(iCursor.getColumnIndex("message_address"));
            long message_time = iCursor.getLong(iCursor.getColumnIndex("message_time"));
            String body = iCursor.getString(iCursor.getColumnIndex("message_body"));

            Log.e("column", "message_id : " + message_id + " / thread_id : " + thread_id + " / address : " + address +
                    " / message_time : " + message_time + " / body : " + body + "\n");

        }
    }


    public void setDelete_Keyword_Array() {
        delete_Keyword_Array.clear();
        deleteDbOpenHelper = new DeleteKeywordDbOpenHelper(this);
        deleteDbOpenHelper.open();
        deleteDbOpenHelper.create();

        Cursor iCursor = deleteDbOpenHelper.sortColumn(sort_Delete_Keyword);
        Log.e("showDatabase", "DB Size: " + iCursor.getCount());

        while (iCursor.moveToNext()) {
            //String delete_keyword = iCursor.getString(iCursor.getColumnIndex("DELETE_KEYWORD"));
            String delete_keyword = iCursor.getString(1);
            Log.e("column", "\nDELETE_KEYWORD : " + delete_keyword);

            delete_Keyword_Array.add(delete_keyword);
        }
    }

    public boolean deleteSMSIncludeDeleteKeyword(ArrayList<String> keywords, String body) {
        // 삭제 키워드 디비를 열어서 모든 keyword를 ArrayList에 넣고, body에 포함되어있는지 확인한다.
        for (int i = 0; i < keywords.size(); i++) {
            if (body.contains(keywords.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void setSpam_Num_Array() {
        spam_Number_Array.clear();
        spamDbOpenHelper = new SpamNumberDbOpenHelper(this);
        spamDbOpenHelper.open();
        spamDbOpenHelper.create();

        Cursor iCursor = spamDbOpenHelper.sortColumn(sort_Delete_Keyword);
        Log.e("showDatabase", "DB Size: " + iCursor.getCount());

        while (iCursor.moveToNext()) {
            String spam_Num = iCursor.getString(1);
            Log.e("column", "\nSPAM_NUM : " + spam_Num);

            spam_Number_Array.add(spam_Num);
        }
    }

    public boolean deleteSMSIncludeSpamNum(ArrayList<String> numbers, String address) {
        for (int i = 0; i < numbers.size(); i++) {
            if (address.equals(numbers.get(i))) {
                return true; // address가 스팸번호와 같다면 true를 리턴한다.
            }
        }
        return false;
    }
}