package com.example.myfirstapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myfirstapp.R;

import java.util.ArrayList;
import java.util.List;

public class VerticalDragListViewActivity extends AppCompatActivity {

    private List<String> list = new ArrayList<>();

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_drag_list_view);
        listView = findViewById(R.id.my_list);

        for (int i = 0; i < 100; i++) {
            list.add("text -->" + i);
        }

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(VerticalDragListViewActivity.this).inflate(R.layout.item_text_net, parent, false);
                ((TextView) view.findViewById(R.id.tv_item)).setText(list.get(position));
                return view;
            }
        });
    }
}