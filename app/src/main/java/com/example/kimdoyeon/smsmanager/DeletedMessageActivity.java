package com.example.kimdoyeon.smsmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kimdoyeon.smsmanager.DeletedMessageDB.DeletedMessageDbOpenHelper;
import com.example.kimdoyeon.smsmanager.ListViewAdapter.DeletedMessageListViewAdapter;
import com.example.kimdoyeon.smsmanager.Objects.MessageObj;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import info.hoang8f.widget.FButton;

public class DeletedMessageActivity extends Activity {

    private DeletedMessageDbOpenHelper mDbOpenHelper;

    //private MainDbOpenHelper mDbOpenHelper_Main;
    //private ImportantKeywordsDbOpenHelper mDbOpenHelper_Keywords;


    ListView deleted_messages_listview;
    private SwipeRefreshLayout swipeRefreshLayout;
    DeletedMessageListViewAdapter adapter;

    String sort = "message_id";
    String sort_Important_Keyword = "_id";

    public ArrayList<MessageObj> mArray = new ArrayList<MessageObj>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_message);

        /*btn_open_important_keyword_list = (FButton) findViewById(R.id.btn_important_keyword_list);
        btn_open_important_keyword_list.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : click event

                *//*중요 키워드 목록으로 이동하도록 구현할 것.*//*
                Intent intent = new Intent(ImportantMessagesActivity.this, ImportantKeywordsActivity.class);
                startActivityForResult(intent, 1000);

            }
        });*/

        /*--------------------------------ListView 정의------------------------------------------*/

        deleted_messages_listview = (ListView) findViewById(R.id.list_deleted_messages);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_layout_important);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // 새로고침 할 작업

                showDatabase(sort);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        mDbOpenHelper = new DeletedMessageDbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        showDatabase(sort);
    }


    public void showDatabase(String sort) {

        mArray.clear();
        //Toast.makeText(getApplicationContext(), "Show Database", Toast.LENGTH_SHORT).show();
        adapter = new DeletedMessageListViewAdapter(this, R.layout.listview_item_deleted_message, mArray);

        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        Log.e("showDatabase", "DB Size: " + iCursor.getCount());
        //arrayData.clear();
        // arrayIndex.clear();
        while (iCursor.moveToNext()) {

            //long _id = iCursor.getLong(iCursor.getColumnIndex("_id"));
            long message_id = iCursor.getLong(iCursor.getColumnIndex("message_id"));
            long thread_id = iCursor.getLong(iCursor.getColumnIndex("thread_id"));
            String address = iCursor.getString(iCursor.getColumnIndex("message_address"));
            String message_time = iCursor.getString(iCursor.getColumnIndex("message_time"));
            String body = iCursor.getString(iCursor.getColumnIndex("message_body"));
            String name = iCursor.getString(iCursor.getColumnIndex("name"));

            Log.e("column", "message_id : " + message_id + " / thread_id : " + thread_id + " / address : " + address +
                    " / message_time : " + message_time + " / body : " + body + "\n");


            MessageObj mObj = new MessageObj(message_id, thread_id, address, message_time,
                    body, name); // 중요 메시지는 중요 키워드들의 ArrayList를 동봉.
            mArray.add(mObj); // ArrayList에 추가.
            adapter.addItem(mObj.getMessage_Address(), mObj.getMessage_Body(),mObj.getMessage_Time(), mObj.getName());

        }
        adapter.notifyDataSetChanged();

        deleted_messages_listview.setAdapter(adapter);
        deleted_messages_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                MessageObj mObj = (MessageObj) parent.getItemAtPosition(position);

                String Address = mObj.getMessage_Address();
                String Body = mObj.getMessage_Body();
                String Name = mObj.getName();

                Intent intent = new Intent(DeletedMessageActivity.this, MessageActivity.class);
                intent.putExtra("key_Address", Address);
                intent.putExtra("key_Body", Body);
                intent.putExtra("key_Name", Name);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        showDatabase(sort);
        super.onRestart();
    }
}
