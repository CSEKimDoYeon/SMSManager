package com.example.kimdoyeon.smsmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kimdoyeon.smsmanager.DeleteKeywordDB.DeleteKeywordDbOpenHelper;
import com.example.kimdoyeon.smsmanager.ImportantMessagesDB.ImportantMessagesDbOpenHelper;
import com.example.kimdoyeon.smsmanager.ListViewAdapter.DeleteKeywordListViewAdapter;
import com.example.kimdoyeon.smsmanager.ListViewAdapter.ImportantMessagesListViewAdapter;
import com.example.kimdoyeon.smsmanager.Objects.MessageObj;

import java.util.ArrayList;

public class ImportantMessagesActivity extends Activity {

    private ImportantMessagesDbOpenHelper mDbOpenHelper;

    Button btn_open_important_keyword_list;

    ListView important_messages_listview;
    private SwipeRefreshLayout swipeRefreshLayout;
    ImportantMessagesListViewAdapter adapter;

    String sort = "message_id";

    public ArrayList<MessageObj> mArray = new ArrayList<MessageObj>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_activity);

        btn_open_important_keyword_list = (Button) findViewById(R.id.btn_important_keyword_list);
        btn_open_important_keyword_list.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : click event

                /*중요 키워드 목록으로 이동하도록 구현할 것.*/
                //Intent intent = new Intent(ImportantMessagesActivity.this, AddDeleteKeywordActivity.class);
                //startActivityForResult(intent, 1000);

            }
        });

        /*--------------------------------ListView 정의------------------------------------------*/

        important_messages_listview = (ListView) findViewById(R.id.list_important_messages);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_layout_important);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // 새로고침 할 작업
                showDatabase(sort);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        mDbOpenHelper = new ImportantMessagesDbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        /*mDbOpenHelper.insertColumn("광고");
        mDbOpenHelper.insertColumn("스팸");*/

        showDatabase(sort);
    }

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // 수행을 제대로 한 경우
        if(resultCode == RESULT_OK && data != null)
        {
            String result = data.getStringExtra("data");
            Toast.makeText(getApplicationContext(), "전달받은 데이터 : " + result, Toast.LENGTH_SHORT).show();
            mDbOpenHelper.insertColumn(result);

            showDatabase(sort);
        }
        // 수행을 제대로 하지 못한 경우
        else if(resultCode == RESULT_CANCELED)
        {

        }
    }*/

    public void filterImportantMessages(){
        // 중요키워드 데이터베이스를 연다 -> 모든 키워드들을 ArrayList에 넣는다. -> 메인 데이터베이스를 연다
        // -> 메인 데이터베이스 커서 움직일때마다 ArrayList 검사 -> 중요 키워드가 포함된 SMS일 경우 중요Message 데이터베이스에 넣는다.
    }

    public void showDatabase(String sort) {
        Toast.makeText(getApplicationContext(), "Show Database", Toast.LENGTH_SHORT).show();
        adapter = new ImportantMessagesListViewAdapter(this, R.layout.listview_item, mArray);

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

            MessageObj mObj = new MessageObj(message_id, thread_id, address, message_time, body); // 해당 column을 바탕으로 메시지 객체 생성.
            mArray.add(mObj); // ArrayList에 추가.
            adapter.addItem(mObj.getMessage_Address(), mObj.getMessage_Body());

        }

        important_messages_listview.setAdapter(adapter);
        important_messages_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                MessageObj mObj = (MessageObj) parent.getItemAtPosition(position);

                String Address = mObj.getMessage_Address();
                String Body = mObj.getMessage_Body();

                Intent intent = new Intent(ImportantMessagesActivity.this, MessageActivity.class);
                intent.putExtra("key_Address", Address);
                intent.putExtra("key_Body", Body);
                startActivity(intent);
            }
        });

    }
}
