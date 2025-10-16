package com.example.myfirstapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.myfirstapp.R;
import com.example.myfirstapp.adapter.BaseAdapterAdapter;
import com.example.myfirstapp.eventBus.Event;
import com.example.myfirstapp.model.BaseAdapterModel;
import com.example.myfirstapp.multiThreading.SecondCallBack;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private final String TAG = SecondActivity.this.getClass().getSimpleName();

    RecyclerView model;
    BaseAdapterAdapter baseAdapterAdapter;
    SwipeRefreshLayout sp_model;
    List<BaseAdapterModel> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        model = findViewById(R.id.rv_model);
        sp_model = findViewById(R.id.sp_model);
        model.postDelayed(this::sendEvent, 2000);
        //创建先行垂直布局
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        model.setLayoutManager(linearLayout);
        baseAdapterAdapter = new BaseAdapterAdapter(R.layout.rv_model_list, showData());
        model.setAdapter(baseAdapterAdapter);
        //动画
        baseAdapterAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //加载数据
        initData();
        //加载监听
        initAction();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add();
//        transaction.replace();
        transaction.addToBackStack(null);
        transaction.commit();
        String csr = SecondCallBack.post("csr");

    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        String content = bundle.getString("content");
        Log.e(TAG, title + ": " + content);
    }

    public void initAction() {
        //时间监听
        baseAdapterAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BaseAdapterModel baseAdapterModel = (BaseAdapterModel) adapter.getItem(position);
                Toast.makeText(SecondActivity.this, baseAdapterModel.getContent() == null ? "" : baseAdapterModel.getContent(), Toast.LENGTH_SHORT).show();
            }
        });
        //下拉刷新
        sp_model.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (baseAdapterAdapter.getData().size() != 0) {
                    baseAdapterAdapter.getData().clear();
                }
                baseAdapterAdapter.setNewData(showData2());
                sp_model.setRefreshing(false);
            }
        });
        //上拉加载更多
        baseAdapterAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                baseAdapterAdapter.loadMoreEnd();
            }
        }, model);
    }

    public void sendEvent() {
        EventBus.getDefault().post(new Event("真的是牛"));
    }

    public List<BaseAdapterModel> showData() {
        Log.e(TAG, "获取Data");
        for (int i = 0; i < 100; i++) {
            BaseAdapterModel baseAdapterModel = new BaseAdapterModel();
            baseAdapterModel.setTitle("title" + i);
            baseAdapterModel.setContent("content" + i);
            lists.add(baseAdapterModel);
        }
        return lists;
    }

    public List<BaseAdapterModel> showData2() {
        Log.e(TAG, "获取Data");
        for (int i = 5; i < 15; i++) {
            BaseAdapterModel baseAdapterModel = new BaseAdapterModel();
            baseAdapterModel.setTitle("title" + i);
            baseAdapterModel.setContent("content" + i);
            lists.add(baseAdapterModel);
        }
        return lists;
    }
}