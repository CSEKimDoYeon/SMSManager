package com.example.kimdoyeon.smsmanager.ListViewAdapter;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
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

import com.example.kimdoyeon.smsmanager.Objects.MessageObj;
import com.example.kimdoyeon.smsmanager.Objects.MessageObj_Important;
import com.example.kimdoyeon.smsmanager.R;

import java.util.ArrayList;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class ImportantMessagesListViewAdapter extends ArrayAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<MessageObj_Important> listViewItemList = new ArrayList<MessageObj_Important>();

    // ListViewAdapter의 생성자


    public ImportantMessagesListViewAdapter(@NonNull Context context, int resource, ArrayList<MessageObj_Important> arr) {
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_important_message, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        // ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.tv_date);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MessageObj_Important listViewItem = listViewItemList.get(position);


        ArrayList<String> keywords = listViewItem.getImportant_Keywords();
        //String impKeyword = keywords.get(0);

        // 아이템 내 각 위젯에 데이터 반영
        //iconImageView.setImageDrawable(listViewItem.getIcon());

        descTextView.setText(listViewItem.getMessage_Body());
        dateTextView.setText(listViewItem.getMessage_Time());

        if (listViewItem.getName().equals("")) {
            titleTextView.setText(listViewItem.getMessage_Address());
        } // 만약 이름을 가지고 있다면 이름으로 대체한다.
        else {
            titleTextView.setText(listViewItem.getName());
        }

        Spannable span = (Spannable) descTextView.getText(); // 메시지 바디를 가져오고
        for (int i = 0; i < keywords.size(); i++) {
            int targetStartIndex = descTextView.getText().toString().indexOf(keywords.get(i));
            int targetEndIndex = targetStartIndex + keywords.get(i).length();
            span.setSpan(new BackgroundColorSpan(Color.YELLOW), targetStartIndex, targetEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

/*
        span.setSpan(new BackgroundColorSpan(Color.RED), 0,  5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new BackgroundColorSpan(Color.RED), 7,  10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/

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
    public void addItem(String address, String body, ArrayList<String> keywords, String date, String name) {
        MessageObj_Important item = new MessageObj_Important();

        //item.setIcon(icon);
        item.setMessage_Address(address);
        item.setMessage_Body(body);
        item.setImportant_Keywords(keywords);
        item.setMessage_Time(date);
        item.setName(name);


        listViewItemList.add(item);
    }

}