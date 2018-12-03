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

import com.example.kimdoyeon.smsmanager.DeleteKeywordDB.DeleteKeywordDbOpenHelper;
import com.example.kimdoyeon.smsmanager.ImportantKeywordDB.ImportantKeywordsDbOpenHelper;
import com.example.kimdoyeon.smsmanager.ImportantMessagesDB.ImportantMessagesDbOpenHelper;
import com.example.kimdoyeon.smsmanager.ListViewAdapter.DeleteKeywordListViewAdapter;
import com.example.kimdoyeon.smsmanager.ListViewAdapter.ImportantMessagesListViewAdapter;
import com.example.kimdoyeon.smsmanager.MainDB.MainDbOpenHelper;
import com.example.kimdoyeon.smsmanager.Objects.MessageObj;
import com.example.kimdoyeon.smsmanager.Objects.MessageObj_Important;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;

public class ImportantMessagesActivity extends Activity {

    private ImportantMessagesDbOpenHelper mDbOpenHelper;
    private MainDbOpenHelper mDbOpenHelper_Main;
    private ImportantKeywordsDbOpenHelper mDbOpenHelper_Keywords;

    FButton btn_open_important_keyword_list;

    ListView important_messages_listview;
    private SwipeRefreshLayout swipeRefreshLayout;
    ImportantMessagesListViewAdapter adapter;

    String sort = "message_id";
    String sort_Important_Keyword = "_id";

    public ArrayList<MessageObj_Important> mArray = new ArrayList<MessageObj_Important>();
    public ArrayList<String> impKeywords = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_activity);

        btn_open_important_keyword_list = (FButton) findViewById(R.id.btn_important_keyword_list);
        btn_open_important_keyword_list.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : click event

                /*중요 키워드 목록으로 이동하도록 구현할 것.*/
                Intent intent = new Intent(ImportantMessagesActivity.this, ImportantKeywordsActivity.class);
                startActivityForResult(intent, 1000);

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
        InsertMainDbToImportantDb(); // 메인DB에 있는 칼럼들을 중요 메시지DB에 넣는다.


        showDatabase(sort);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // 수행을 제대로 한 경우
        if(resultCode == RESULT_OK && data != null)
        {
            String result = data.getStringExtra("data");
            Toast.makeText(getApplicationContext(), "전달받은 데이터 : " + result, Toast.LENGTH_SHORT).show();

            showDatabase(sort);
        }
        // 수행을 제대로 하지 못한 경우
        else if(resultCode == RESULT_CANCELED)
        {

        }
    }

    public void filterImportantMessages() {
        // 중요키워드 데이터베이스를 연다 -> 모든 키워드들을 ArrayList에 넣는다. -> 메인 데이터베이스를 연다
        // -> 메인 데이터베이스 커서 움직일때마다 ArrayList 검사 -> 중요 키워드가 포함된 SMS일 경우 중요Message 데이터베이스에 넣는다.
    }

    public void InsertMainDbToImportantDb() { // 일단 삭제 키워드가 필터링된 메인 디비를 가져와서 복사한다.
        mDbOpenHelper.deleteAllColumns();

        mDbOpenHelper_Main = new MainDbOpenHelper(this);
        mDbOpenHelper_Main.open();
        mDbOpenHelper_Main.create();

        Cursor iCursor = mDbOpenHelper_Main.sortColumn(sort);
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

            mDbOpenHelper.insertColumn(message_id, thread_id, address, message_time, body);
        }
    }

    public void showDatabase(String sort) {
        setImportant_Keyword_Array(); // 중요키워드 디비를 열어서 ArrayList에 저장한다.
        mArray.clear();
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


            if (SMSIsContainImportantKeyword(impKeywords, body) != null) { // 중요 키워드를 포함하고 있을 경우만 객체 만들고
                MessageObj_Important mObj = new MessageObj_Important(message_id, thread_id, address, message_time,
                        body,SMSIsContainImportantKeyword(impKeywords, body)); // 중요 메시지는 중요 키워드들의 ArrayList를 동봉.
                mArray.add(mObj); // ArrayList에 추가.
                adapter.addItem(mObj.getMessage_Address(), mObj.getMessage_Body(),SMSIsContainImportantKeyword(impKeywords, body));
            }
        }
        adapter.notifyDataSetChanged();

        important_messages_listview.setAdapter(adapter);
        important_messages_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                MessageObj_Important mObj = (MessageObj_Important) parent.getItemAtPosition(position);

                String Address = mObj.getMessage_Address();
                String Body = mObj.getMessage_Body();

                Intent intent = new Intent(ImportantMessagesActivity.this, MessageActivity.class);
                intent.putExtra("key_Address", Address);
                intent.putExtra("key_Body", Body);
                startActivity(intent);
            }
        });

    }


    public void setImportant_Keyword_Array() {
        impKeywords.clear();
        mDbOpenHelper_Keywords = new ImportantKeywordsDbOpenHelper(this);
        mDbOpenHelper_Keywords.open();
        mDbOpenHelper_Keywords.create();

        Cursor iCursor = mDbOpenHelper_Keywords.sortColumn(sort_Important_Keyword);
        Log.e("showDatabase", "DB Size: " + iCursor.getCount());

        while (iCursor.moveToNext()) {
            //String delete_keyword = iCursor.getString(iCursor.getColumnIndex("DELETE_KEYWORD"));
            String important_keyword = iCursor.getString(1);
            Log.e("column", "\nIMPORTANT_KEYWORD : " + important_keyword);

            impKeywords.add(important_keyword);
        }
    }


    public ArrayList<String> SMSIsContainImportantKeyword(ArrayList<String> keywords, String body) {
        // 삭제 키워드 디비를 열어서 모든 keyword를 ArrayList에 넣고, body에 포함되어있는지 확인한다.
        ArrayList<String> keywords_temp = new ArrayList<String>();

        for (int i = 0; i < keywords.size(); i++) {
            if (body.contains(keywords.get(i))) {
                keywords_temp.add(keywords.get(i));
            }
        }

        if (keywords_temp.size() == 0) {
            return null;
        } else
            return keywords_temp;
    }


    public static SpannableString getUnderLineColorText(String string, String targetString, int color) {
        SpannableString spannableString = new SpannableString(string);
        int targetStartIndex = string.indexOf(targetString);
        int targetEndIndex = targetStartIndex + targetString.length();
        spannableString.setSpan(new ForegroundColorSpan(color), targetStartIndex, targetEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), targetStartIndex, targetEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }


    @Override
    protected void onRestart() {
        showDatabase(sort);
        super.onRestart();
    }
}
