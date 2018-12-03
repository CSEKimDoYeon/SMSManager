package com.example.kimdoyeon.smsmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kimdoyeon.smsmanager.ImportantKeywordDB.ImportantKeywordsDbOpenHelper;
import com.example.kimdoyeon.smsmanager.ListViewAdapter.ImportantKeywordsListViewAdapter;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;

public class ImportantKeywordsActivity extends Activity {
    private ImportantKeywordsDbOpenHelper mDbOpenHelper;

    FButton btn_add_Important_Keyword;


    ListView important_keyword_listView;
    ImportantKeywordsListViewAdapter adapter;

    String sort = "_id";

    public ArrayList<String> mArray = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_keywords);

        btn_add_Important_Keyword = (FButton) findViewById(R.id.btn_add_important_Keyword);
        btn_add_Important_Keyword.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : click event
                Intent intent = new Intent(ImportantKeywordsActivity.this, AddImportantKeywordActivity.class);

                startActivityForResult(intent, 1000);
            }
        });

        /*--------------------------------ListView 정의------------------------------------------*/

        //delete_keyword_listView = (ListView) findViewById(R.id.list_delete_keyword);
        important_keyword_listView = (ListView) findViewById(R.id.list_important_keyword);



        mDbOpenHelper = new ImportantKeywordsDbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        /*mDbOpenHelper.insertColumn("광고");
        mDbOpenHelper.insertColumn("스팸");*/

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
            mDbOpenHelper.insertColumn(result);

            showDatabase(sort);
        }
        // 수행을 제대로 하지 못한 경우
        else if(resultCode == RESULT_CANCELED)
        {

        }
    }

    public void showDatabase(String sort) {
        Toast.makeText(getApplicationContext(), "Show Database", Toast.LENGTH_SHORT).show();
        adapter = new ImportantKeywordsListViewAdapter(this, R.layout.listview_item_important_keyword, mArray);

        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        Log.e("showDatabase", "DB Size: " + iCursor.getCount());
        //arrayData.clear();
        // arrayIndex.clear();

        while (iCursor.moveToNext()) {
            //String delete_keyword = iCursor.getString(iCursor.getColumnIndex("DELETE_KEYWORD"));
            String important_keyword = iCursor.getString(1);
            Log.e("column", "\nIMPORTNAT_KEYWORD : " + important_keyword );

            mArray.add(important_keyword);
            adapter.addItem(important_keyword);
        }

        important_keyword_listView.setAdapter(adapter);
        important_keyword_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                //MessageObj mObj = (MessageObj) parent.getItemAtPosition(position);
                String mKeyword = (String) parent.getItemAtPosition(position);

                //String Address = mObj.getMessage_Address();
                //String Body = mObj.getMessage_Body();

                //Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                //intent.putExtra("key_Address", Address);
                // intent.putExtra("key_Body", Body);
                //startActivity(intent);
            }
        });
    }
}
