package com.example.kimdoyeon.smsmanager.ListViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kimdoyeon.smsmanager.DeleteKeywordActivity;
import com.example.kimdoyeon.smsmanager.R;

import java.util.ArrayList;

import at.markushi.ui.CircleButton;

public class DeleteKeywordListViewAdapter extends ArrayAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    Context context;

    private ArrayList<String> listViewItemList = new ArrayList<String>() ;

    // ListViewAdapter의 생성자

    public DeleteKeywordListViewAdapter(@NonNull Context context, int resource, ArrayList<String> arr)  {
        super(context, resource);
        this.context = context;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_delete_keyword, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        // ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;


        CircleButton btn_delete = (CircleButton) convertView.findViewById(R.id.btn_delete_keyword_delete);
        btn_delete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : click event
                Toast.makeText(context, "버튼 눌려진 포지션 : " + pos , Toast.LENGTH_SHORT).show();
            }
        });

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        String Item = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        //iconImageView.setImageDrawable(listViewItem.getIcon());
        titleTextView.setText(Item);

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String item) {
        listViewItemList.add(item);
    }
}
