package com.example.kimdoyeon.smsmanager.ListViewAdapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kimdoyeon.smsmanager.DeleteKeywordDB.DeleteKeywordDbOpenHelper;
import com.example.kimdoyeon.smsmanager.Objects.MessageObj;
import com.example.kimdoyeon.smsmanager.R;
import com.example.kimdoyeon.smsmanager.SpamNumberDB.SpamNumberDbOpenHelper;

import java.util.ArrayList;

public class DeletedMessageListViewAdapter extends ArrayAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<MessageObj> listViewItemList = new ArrayList<MessageObj>();

    private ArrayList<String> delete_Keyword_Array = new ArrayList<String>();
    private ArrayList<String> spam_Number_Array = new ArrayList<String>();

    private DeleteKeywordDbOpenHelper mDbOpenHelper_delete_Keyword;
    private SpamNumberDbOpenHelper mDbOpenHelper_spam_Number;

    private String sort_keyword = "_id";
    // ListViewAdapter의 생성자


    public DeletedMessageListViewAdapter(@NonNull Context context, int resource, ArrayList<MessageObj> arr) {
        super(context, resource);
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            setDelete_Keyword_Array();
            setSpam_Number_Array();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_deleted_message, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        // ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MessageObj listViewItem = listViewItemList.get(position);


        // 아이템 내 각 위젯에 데이터 반영
        //iconImageView.setImageDrawable(listViewItem.getIcon());
        titleTextView.setText(listViewItem.getMessage_Address());
        descTextView.setText(listViewItem.getMessage_Body());



        Spannable span = (Spannable) descTextView.getText(); // 메시지 바디를 가져오고
        for (int i = 0; i < delete_Keyword_Array.size(); i++) {
            if (descTextView.getText().toString().contains(delete_Keyword_Array.get(i))) {
                int targetStartIndex = descTextView.getText().toString().indexOf(delete_Keyword_Array.get(i));
                int targetEndIndex = targetStartIndex + delete_Keyword_Array.get(i).length();
                span.setSpan(new ForegroundColorSpan(Color.RED), targetStartIndex, targetEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new UnderlineSpan(), targetStartIndex, targetEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                //span.setSpan(new BackgroundColorSpan(Color.YELLOW), targetStartIndex, targetEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        Spannable span_address = (Spannable) titleTextView.getText(); // 메시지 주소를 가져오고
        for (int i = 0; i < spam_Number_Array.size(); i++) {
            if (titleTextView.getText().toString().equals(spam_Number_Array.get(i))) {
                int targetStartIndex = titleTextView.getText().toString().indexOf(spam_Number_Array.get(i));
                int targetEndIndex = targetStartIndex + spam_Number_Array.get(i).length();
                span_address.setSpan(new ForegroundColorSpan(Color.RED), targetStartIndex, targetEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span_address.setSpan(new UnderlineSpan(), targetStartIndex, targetEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                //span_address.setSpan(new BackgroundColorSpan(Color.YELLOW), targetStartIndex, targetEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }


        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String address, String body) {
        MessageObj item = new MessageObj();

        //item.setIcon(icon);
        item.setMessage_Address(address);
        item.setMessage_Body(body);

        listViewItemList.add(item);
    }

    public void setDelete_Keyword_Array() {
        delete_Keyword_Array.clear();
        mDbOpenHelper_delete_Keyword = new DeleteKeywordDbOpenHelper(getContext());
        mDbOpenHelper_delete_Keyword.open();
        mDbOpenHelper_delete_Keyword.create();

        Cursor iCursor = mDbOpenHelper_delete_Keyword.sortColumn(sort_keyword);

        while (iCursor.moveToNext()) {
            String keyword = iCursor.getString(1);
            delete_Keyword_Array.add(keyword);
        }
    }

    public void setSpam_Number_Array() {
        spam_Number_Array.clear();
        mDbOpenHelper_spam_Number = new SpamNumberDbOpenHelper(getContext());
        mDbOpenHelper_spam_Number.open();
        mDbOpenHelper_spam_Number.create();

        Cursor iCursor = mDbOpenHelper_spam_Number.sortColumn(sort_keyword);

        while (iCursor.moveToNext()) {
            String keyword = iCursor.getString(1);
            spam_Number_Array.add(keyword);
        }
    }

}
