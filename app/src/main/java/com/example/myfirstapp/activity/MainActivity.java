package com.example.myfirstapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myfirstapp.R;
//import com.example.myfirstapp.activity.ibeacon.IBeaconActivity;
import com.example.myfirstapp.adapter.DynamicBannersViewFlowAdapter;
import com.example.myfirstapp.dragFloatActionButton.DragFloatActionButton;
import com.example.myfirstapp.eventBus.Event;
import com.example.myfirstapp.model.ViewFlowModel;
import com.example.myfirstapp.multiThreading.SecondCallBack;
import com.example.myfirstapp.widgetProvider.BusWidgetProvider;
import com.yitong.android.widget.viewflow.CircleFlowIndicator;
import com.yitong.android.widget.viewflow.ViewFlow;
import com.yitong.logs.Logs;
import com.yitong.utils.ToastTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    TextView tips;

    Button next;

    ViewFlow vfFlow;

    CircleFlowIndicator clfFlow;

    ImageView imgView;

    DragFloatActionButton dbMove;

    private int lastX, lastY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        tips = findViewById(R.id.tv_tips);
        next = findViewById(R.id.btn_next);
        vfFlow = findViewById(R.id.vf_flow);
        clfFlow = findViewById(R.id.cfl_flow);
        imgView = findViewById(R.id.img_view);
        dbMove = findViewById(R.id.db_move);
        dbMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logs.e(TAG, "111");
            }
        });
        next.setOnClickListener(view -> {
//            Intent intent = new Intent(this, MyDownloaderActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("title", "Smile");
//            bundle.putString("content", "Smile is very good !");
//            intent.putExtras(bundle);
//            startActivity(intent);
//            Intent updateWidgetIntent = new Intent(this, BusWidgetProvider.class);
//            //指定广播行为动作的名字
//            updateWidgetIntent.setAction(BusWidgetProvider.GOD_START_ACTIVITY);
//            //发送广播
//            sendBroadcast(updateWidgetIntent);
            Uri data = Uri.parse("uatbocmmobilebankeid://");
            Intent intent =new Intent(Intent.ACTION_VIEW,data);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity (intent);
            }catch (Exception e) {
                e.printStackTrace();
            }

        });
        //初始化轮播
        DynamicBannersViewFlowAdapter adapter = new DynamicBannersViewFlowAdapter(this, R.drawable.lock_pattern_node_normal);
        adapter.setItems(initData());
        vfFlow.setAdapter(adapter);
        vfFlow.setFlowIndicator(clfFlow);
        vfFlow.setAutoFlow(true);
        viewFlowAction();
        Glide.with(this)
                .load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0116405d84f975a801211d537707c9.gif&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1636622963&t=baa54f216b9d60f5e22b1aaa36d79b15")
                .asGif()
                .placeholder(R.drawable.abc_vector_test)
                .error(R.drawable.abc_vector_test)
                .into(imgView);
        initSecondCallBack();
//        jvmReflex();

    }

    private List<ViewFlowModel> initData() {
        List<ViewFlowModel> viewFlowModelList = new ArrayList<>();
        String[] imgStr = {"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg95.699pic.com%2Fphoto%2F40111%2F6749.gif_wh300.gif%21%2Fgifto%2Ftrue&refer=http%3A%2F%2Fimg95.699pic.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1636619496&t=c0e873f5d9de6cd7ad486d311d4315ad"
                , "https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/baike/pic/item/0df431adcbef7609d98a756127dda3cc7dd99e4f.jpg"
                , "https://ss1.baidu.com/9vo3dSag_xI4khGko9WTAnF6hhy/baike/pic/item/adaf2edda3cc7cd9e4cf04df3201213fb90e91d1.jpg"
                , "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwx4.sinaimg.cn%2Fmw690%2F002UWGmply1guojkaind4j60u011i79n02.jpg&refer=http%3A%2F%2Fwx4.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1636616936&t=3a425c9f821c14c81e1006e9c6990c94"};
        for (int i = 0; i < 5; i++) {
            //防止数组越界
            if (i < imgStr.length) {
                ViewFlowModel viewFlowModel = new ViewFlowModel(imgStr[i]);
                viewFlowModelList.add(viewFlowModel);
            }
        }
        return viewFlowModelList;
    }

    /**
     * 轮播图触发时间
     */
    private void viewFlowAction() {

        vfFlow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                vfFlow.getParent().requestDisallowInterceptTouchEvent(true);
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int deltaY = y - lastY;
                        int deltaX = x - lastX;
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            vfFlow.getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            vfFlow.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    default:
                        break;
                }
                return false;
            }
        });

        vfFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (vfFlow.getAdapter() != null) {
                    ViewFlowModel viewFlowModel = (ViewFlowModel) vfFlow.getAdapter().getItem(i);
                    Uri uri = Uri.parse(viewFlowModel.getImgUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (vfFlow != null) {
            vfFlow.stopAutoFlow();
        }
    }

    @Subscribe()
    public void onEvent(Event event) {
        tips.setText(event.value);
    }

    private void initSecondCallBack() {
        SecondCallBack.initSecondCallBack(new SecondCallBack.EncryptDelegate() {
            @Override
            public String getEncryptString(String decryptString) {
                return "csr666";
            }
        });
    }

    //java反射机制
//    private void jvmReflex() {
//        try {
//            Class<?> cls = Class.forName("com.example.myfirstapp.model.ViewFlowModel");//加载ViewFlowModel类
//            Object obj = (Object) cls.newInstance(); //实例化ViewFlowModel
//            Method setImgUrl = cls.getDeclaredMethod("setImgUrl", String.class);//获取setImgUrl方法
//            setImgUrl.invoke(obj, "http:baidu.com");//设置调用setImgUrl的对象和传入setName的值
//            Method getImgUrl = cls.getDeclaredMethod("getImgUrl");//获取getImgUrl方法
//            Logs.e(TAG, (String) getImgUrl.invoke(obj, null));//设置调用getImgUrl方法的对象.把值打印到控制台
//        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
}