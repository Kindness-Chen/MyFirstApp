package com.example.myfirstapp.util;

import com.example.myfirstapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Date：2022/4/22
 * Time：15:29
 * Author：chenshengrui
 */
public class EnableOneToTenDecorator implements DayViewDecorator {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final Date date;

    public EnableOneToTenDecorator(Date date) {
        this.date = date;
    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.getDate().getTime() < date.getTime();
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(false);
        view.addSpan(new DotSpan(5, R.color.isToday_BgColor));
    }


}
