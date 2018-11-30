package com.example.kimdoyeon.smsmanager.ListViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.kimdoyeon.smsmanager.R;

import java.util.ArrayList;

public class ImportantKeywordsListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class ImportantKeywordsListViewHolder extends RecyclerView.ViewHolder {

        TextView tv_keyword;

        ImportantKeywordsListViewHolder(View view) {
            super(view);
            tv_keyword = view.findViewById(R.id.tv_important_keyword);
        }
    }

    private ArrayList<String> keywordList;

    public ImportantKeywordsListViewAdapter(ArrayList<String> keywordList) {
        this.keywordList = keywordList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_important_keyword, parent, false);

        return new ImportantKeywordsListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ImportantKeywordsListViewHolder myViewHolder = (ImportantKeywordsListViewHolder) holder;

        myViewHolder.tv_keyword.setText(keywordList.get(position));

    }

    @Override
    public int getItemCount() {
        return keywordList.size();
    }



}
