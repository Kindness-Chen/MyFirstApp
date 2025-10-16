package com.example.myfirstapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.example.myfirstapp.R;
import com.example.myfirstapp.util.EnableOneToTenDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.yitong.utils.ToastTools;

import java.util.Calendar;
import java.util.Date;


public class CalendarViewActivity extends AppCompatActivity implements OnDateSelectedListener {

    MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        calendarView = findViewById(R.id.calendarView);
        initData();
    }

    public void initData() {
        // 显示兴起补全的整个礼拜的上个月或者下个月的日期 一般会多出一行整个礼拜
        // 点击补全出来的另外一个月的信息 可以直接跳到那个月
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_NONE);

        Calendar instance = Calendar.getInstance();
        instance.set(instance.get(Calendar.YEAR), Calendar.JANUARY, 1);

        // 设置日历默认的时间为当前的时间
        calendarView.setSelectedDate(new Date());

        // 日历的主要设置
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .isCacheCalendarPositionEnabled(false)
                .setMinimumDate(instance.getTime())
//                .setMaximumDate(CalendarDay.from(instance.getYear(), 12, 1))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        calendarView.setSelectionColor(getResources().getColor(R.color.light_blue));
        calendarView.setOnDateChangedListener(this);
        calendarView = findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1); //向前走一天
        calendarView.addDecorator(new EnableOneToTenDecorator(calendar.getTime()));

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        ToastTools.showShort(this, date.getDate().toString());
        calendarView.invalidateDecorators();
    }
}