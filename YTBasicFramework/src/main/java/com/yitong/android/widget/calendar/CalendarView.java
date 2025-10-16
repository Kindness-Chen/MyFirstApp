/*
 * 作者		孙靖
 * 开发环境	Win7 Eclipse4.2 JDK1.6
 * 开发日期	2014-3-17
 */
package com.yitong.android.widget.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yitong.android.widget.calendar.utils.DayStyle;
import com.yitong.android.widget.calendar.view.DateWidgetDayCell;
import com.yitong.android.widget.calendar.view.DateWidgetDayHeader;
import com.yitong.android.widget.calendar.vo.CalendarViewListVo;
import com.yitong.basic.R;
import com.yitong.logs.Logs;

public class CalendarView {

    private SimpleDateFormat formater = null;
    // 生成日历，外层容器
    private LinearLayout layContent = null;
    private ArrayList<DateWidgetDayCell> days = null;
    private Calendar calStartDate = null;
    private Calendar calToday = null;
    private Calendar calCalendar = null;
    private Calendar calSelected = null;
    private int iMonthViewCurrentMonth = 0; // 当前月份
    private int iMonthViewCurrentYear = 0;// 当前年份
    private TextView Top_Date = null;// 显示当前年月的标题
    private Button btn_pre_month = null;// 选择上一个月的按钮
    private Button btn_next_month = null;// 选择下一个月的按钮
    private Button btn_pre_year = null; // 选择上一年的按钮
    private Button btn_next_year = null; // 选择下一年的按钮
    private LinearLayout mainLayout = null;// 日历控件的布局
    private Activity activity;
    private ArrayList<CalendarViewListVo> list;
    private String AcNo = "";// 账号
    private String func = "";// 回调后台的方法
    private WebView webView;// 加载的webView

    private TextView arrange_text = null;
    private LinearLayout arrange_layout = null;
    private ScrollView view = null;

    private String todayDate;// 服务端传递过来的今天日期

    private CalendarJsImpl jsImpl;

    public CalendarView(Activity activity, CalendarJsImpl jsImpl) {
        this.activity = activity;
        // 获得屏幕宽和高，并計算出屏幕寬度分七等份的大小
        CalendarConstant.Calendar_Width = activity.getWindowManager()
                .getDefaultDisplay().getWidth();
        CalendarConstant.Cell_Width = CalendarConstant.Calendar_Width / 7 + 1;
        this.jsImpl = jsImpl;
    }

    public void showCalendarView(boolean reset,
                                 ArrayList<CalendarViewListVo> list, WebView webView, String AcNo,
                                 String func, String todayDate) {
        this.list = list;
        this.webView = webView;
        this.AcNo = AcNo;
        this.func = func;
        this.todayDate = todayDate;
        if (reset) {
            reset();
        } else {
            init();
        }

        showCalendarView();
    }

    private void reset() {
        CalendarConstant.rightAndleftButtonClick = 0;
        calStartDate.set(Calendar.DAY_OF_MONTH, 1);
        calStartDate.set(Calendar.MONTH,
                (Integer.parseInt(todayDate.substring(5, 7)) - 1));
        calStartDate.set(Calendar.YEAR,
                Integer.parseInt(todayDate.substring(0, 4)));
        UpdateStartDateForMonth(true);
        mHandler.sendEmptyMessage(0);
    }

    View calendarView;
    WindowManager windowmanager;
    Dialog dialog;

    /**
     * 获取WindowManager设置参数
     *
     * @return
     */
    private WindowManager.LayoutParams getWMLayoutParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        // 类型
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;

        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        params.gravity = Gravity.BOTTOM;

        return params;
    }

    /**
     * * 找控件
     */
    private void findViews() {
        days = new ArrayList<DateWidgetDayCell>();
        calStartDate = Calendar.getInstance();
        calToday = Calendar.getInstance();
        calCalendar = Calendar.getInstance();
        calSelected = Calendar.getInstance();
        calStartDate.set(Calendar.DAY_OF_MONTH, 1);
        calStartDate.set(Calendar.MONTH,
                (Integer.parseInt(todayDate.substring(5, 7)) - 1));
        calStartDate.set(Calendar.YEAR,
                Integer.parseInt(todayDate.substring(0, 4)));
        calendarView = activity.getLayoutInflater().inflate(
                R.layout.calendar_main, null);
        windowmanager = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);

        mainLayout = (LinearLayout) calendarView
                .findViewById(R.id.calendarLayout);
        Top_Date = (TextView) calendarView.findViewById(R.id.Top_Date);
        btn_pre_month = (Button) calendarView.findViewById(R.id.btn_pre_month);
        btn_next_month = (Button) calendarView
                .findViewById(R.id.btn_next_month);
        btn_pre_year = (Button) calendarView.findViewById(R.id.btn_pre_year);
        btn_next_year = (Button) calendarView.findViewById(R.id.btn_next_year);

        btn_pre_year.setOnClickListener(new Pre_yearOnClickListener());
        btn_next_year.setOnClickListener(new Next_YearOnClickListener());
        btn_pre_month.setOnClickListener(new Pre_MonthOnClickListener());
        btn_next_month.setOnClickListener(new Next_MonthOnClickListener());

        // 让一个视图浮动在你的应用程序之上
        // windowmanager.addView(calendarView, getWMLayoutParams());

        dialog = new Dialog(activity, R.style.TransparentNoFrameDialogStyle);
        dialog.setContentView(calendarView, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // 设置显示动画
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.bottom_show_dialog_anim_style);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        dialog.onWindowAttributesChanged(params);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface arg0) {
                jsImpl.hideCalendar();
            }
        });
    }

    public void init() {
        try {
            formater = new SimpleDateFormat("yyyy/MM/dd HH:MM:ss");
            CalendarConstant.currentTimeMillis = formater.parse(
                    todayDate + " " + "12:00:00").getTime();
            findViews();
            setPaintColor();
            calToday = CalendarConstant.GetTodayDate();
            // 计算本月日历中的第一天(一般是上月的某天)，并更新日历
            // 新建线程
            calStartDate = getCalendarStartDate();

            view = new ScrollView(activity);
            view.setVerticalScrollBarEnabled(false);
            view.setHorizontalFadingEdgeEnabled(false);
            LinearLayout.LayoutParams Param1 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            view.addView(generateCalendarMain(), Param1);
            mainLayout.addView(view);
            // mainLayout.setBackgroundColor(Color.WHITE);
            DateWidgetDayCell daySelected = updateCalendar(list);
            if (daySelected != null) {
                daySelected.requestFocus();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * ** // 生成日历主体
     *
     * @return
     * @Description
     * @author 孙靖
     * @version 1.0 2014-3-28
     */
    private View generateCalendarMain() {
        layContent = CalendarConstant.createLayout(LinearLayout.VERTICAL,
                activity);
        layContent.setPadding(2, 2, 2, 2);
        layContent.setBackgroundColor(Color.argb(255, 255, 255, 255));
        layContent.addView(generateCalendarHeader());
        days.clear();
        for (int iRow = 0; iRow < 6; iRow++) {
            layContent.addView(generateCalendarRow());
        }
        return layContent;
    }

    // 生成日历中的一行，仅画矩形
    private View generateCalendarRow() {
        LinearLayout layRow = CalendarConstant.createLayout(
                LinearLayout.HORIZONTAL, activity);
        for (int iDay = 0; iDay < 7; iDay++) {
            DateWidgetDayCell dayCell = new DateWidgetDayCell(activity,
                    CalendarConstant.Cell_Width, CalendarConstant.Cell_Width);
            days.add(dayCell);
            layRow.addView(dayCell);
        }
        return layRow;
    }

    // 设置当天日期和被选中日期
    private Calendar getCalendarStartDate() {
        calToday.setTimeInMillis(CalendarConstant.currentTimeMillis);
        calToday.setFirstDayOfWeek(CalendarConstant.iFirstDayOfWeek);
        if (calSelected.getTimeInMillis() == 0) {
            calSelected.setTimeInMillis(CalendarConstant.currentTimeMillis);
            calSelected.setFirstDayOfWeek(CalendarConstant.iFirstDayOfWeek);
        } else {
            calSelected.setTimeInMillis(calSelected.getTimeInMillis());
            calSelected.setFirstDayOfWeek(CalendarConstant.iFirstDayOfWeek);
        }
        UpdateStartDateForMonth(true);
        return calStartDate;
    }

    // 由于本日历上的日期都是从周一开始的，此方法可推算出上月在本月日历中显示的天数
    private void UpdateStartDateForMonth(boolean updateCalendar) {
        iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);
        iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);
        calStartDate.set(Calendar.DAY_OF_MONTH, 1);
        calStartDate.set(Calendar.HOUR_OF_DAY, 0);
        calStartDate.set(Calendar.MINUTE, 0);
        calStartDate.set(Calendar.SECOND, 0);
        UpdateCurrentMonthDisplay(updateCalendar);
        int iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
        if (iDay < 0) {
            iDay = 6;
        }
        calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
    }

    public DateWidgetDayCell updateCalendar(ArrayList<CalendarViewListVo> list) {
        // layContent.invalidate();
        DateWidgetDayCell daySelected = null;
        boolean bSelected = false;
        boolean bIsSelection = (calSelected.getTimeInMillis() != 0);
        int iSelectedYear = calSelected.get(Calendar.YEAR);
        int iSelectedMonth = calSelected.get(Calendar.MONTH);
        int iSelectedDay = calSelected.get(Calendar.DAY_OF_MONTH);
        calCalendar.setTimeInMillis(calStartDate.getTimeInMillis());
        for (int i = 0; i < days.size(); i++) {
            int iYear = calCalendar.get(Calendar.YEAR);
            int iMonth = calCalendar.get(Calendar.MONTH);
            int iDay = calCalendar.get(Calendar.DAY_OF_MONTH);
            DateWidgetDayCell dayCell = days.get(i);
            // 判断是否当天
            boolean bToday = false;
            if (calToday.get(Calendar.YEAR) == iYear) {
                if (calToday.get(Calendar.MONTH) == iMonth) {
                    if (calToday.get(Calendar.DAY_OF_MONTH) == iDay) {
                        bToday = true;
                    }
                }
            }
            // 是否被选中
            bSelected = false;
            if (bIsSelection) {
                if ((iSelectedDay == iDay) && (iSelectedMonth == iMonth)
                        && (iSelectedYear == iYear)) {
                    bSelected = true;
                }
            }
            dayCell.setSelected(bSelected);

            if (bSelected) {
                daySelected = dayCell;
            }

            boolean isShowBlue = false;
            boolean isShowRed = false;
            String iDaysString = iDay + "";
            boolean isSelectCurrentMonth = (iMonthViewCurrentMonth == iMonth);
            int iMonthI = (iMonth + 1);
            String date1 = "" + iMonthI;
            if (iMonthI < 10) {
                date1 = "0" + iMonthI;
            }
            String date2 = "" + iDay;
            if (iDay < 10) {
                date2 = "0" + iDay;
            }
            String date = iYear + "-" + date1 + "-" + date2;
            dayCell.setItemClick(new MOnDayCellClick(date));
            dayCell.setData(iMonth, iDay, bToday, isSelectCurrentMonth,
                    isShowBlue, isShowRed);

            calCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        for (DateWidgetDayCell dwdc : days) {
            dwdc.invalidate();
        }
        layContent.invalidate();

        return daySelected;
    }

    /**
     * ** 更新日历标题上显示的年月
     */
    private void UpdateCurrentMonthDisplay(boolean updateCalendar) {
        String date = calStartDate.get(Calendar.YEAR) + "-"
                + (calStartDate.get(Calendar.MONTH) + 1);
        Top_Date.setText(date);
        if (updateCalendar) {
            int today = CalendarConstant.maxDayOfMonth(
                    calStartDate.get(Calendar.YEAR),
                    (calStartDate.get(Calendar.MONTH) + 1));
            String mounthString = (calStartDate.get(Calendar.MONTH) + 1) < 10 ? "0"
                    + (calStartDate.get(Calendar.MONTH) + 1)
                    : "" + (calStartDate.get(Calendar.MONTH) + 1);
            String startdateString = calStartDate.get(Calendar.YEAR)
                    + mounthString + "01";
            String enddateString = calStartDate.get(Calendar.YEAR)
                    + mounthString + today;
        }
    }

    // 点击上月按钮，触发事件
    class Pre_MonthOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            int clickNum = Math.abs(CalendarConstant.rightAndleftButtonClick);
            if (CalendarConstant.rightAndleftButtonClick < 0
                    && clickNum >= CalendarConstant.MAX_NUM_CLICK) {
                return;
            }

            CalendarConstant.rightAndleftButtonClick--;

            calSelected.setTimeInMillis(0);
            iMonthViewCurrentMonth--;
            if (iMonthViewCurrentMonth == -1) {
                iMonthViewCurrentMonth = 11;
                iMonthViewCurrentYear--;
            }
            calStartDate.set(Calendar.DAY_OF_MONTH, 1);
            calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
            calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
            calStartDate.set(Calendar.HOUR_OF_DAY, 0);
            calStartDate.set(Calendar.MINUTE, 0);
            calStartDate.set(Calendar.SECOND, 0);
            calStartDate.set(Calendar.MILLISECOND, 0);
            UpdateStartDateForMonth(true);
            mHandler.sendEmptyMessage(0);

        }
    }

    // 点击下月按钮，触发事件
    class Next_MonthOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {

            int clickNum = Math.abs(CalendarConstant.rightAndleftButtonClick);
            if (CalendarConstant.rightAndleftButtonClick > 0
                    && clickNum >= CalendarConstant.MAX_NUM_CLICK) {
                return;
            }

            CalendarConstant.rightAndleftButtonClick++;

            calSelected.setTimeInMillis(System.currentTimeMillis());
            iMonthViewCurrentMonth++;

            if (iMonthViewCurrentMonth == 12) {
                iMonthViewCurrentMonth = 0;
                iMonthViewCurrentYear++;
            }
            calStartDate.set(Calendar.DAY_OF_MONTH, 1);
            calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
            calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
            UpdateStartDateForMonth(true);
            mHandler.sendEmptyMessage(0);
        }
    }

    // 点击下一年响应事件
    class Next_YearOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            CalendarConstant.rightAndleftButtonClick = 0;
            calSelected.setTimeInMillis(System.currentTimeMillis());
            iMonthViewCurrentYear++;
            calStartDate.set(Calendar.DAY_OF_MONTH, 1);
            calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
            calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
            UpdateStartDateForMonth(true);
            mHandler.sendEmptyMessage(0);

        }
    }

    // 点击上一年响应事件
    class Pre_yearOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            CalendarConstant.rightAndleftButtonClick = 0;
            calSelected.setTimeInMillis(System.currentTimeMillis());
            iMonthViewCurrentYear--;
            calStartDate.set(Calendar.DAY_OF_MONTH, 1);
            calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
            calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
            UpdateStartDateForMonth(true);
            mHandler.sendEmptyMessage(0);

        }
    }

    /**
     * ** 点击日历，触发事件
     */
    private class MOnDayCellClick implements DateWidgetDayCell.OnItemClick {

        private String date;

        public MOnDayCellClick(String date) {
            this.date = date;
        }

        @Override
        public void OnClick(DateWidgetDayCell item) {
            webView.loadUrl("javascript:" + func + "('" + date + "')");
            Logs.d("TAG", "javascript:" + func + "('" + date + "')");
            dismissCalendarview();
        }
    }

    // 生成日历头部
    private View generateCalendarHeader() {
        LinearLayout layRow = CalendarConstant.createLayout(
                LinearLayout.HORIZONTAL, activity);
        // layRow.setBackgroundColor(Color.argb(255, 207, 207, 205));
        for (int iDay = 0; iDay < 7; iDay++) {
            DateWidgetDayHeader day = new DateWidgetDayHeader(activity,
                    CalendarConstant.Cell_Width, 50);
            final int iWeekDay = DayStyle.getWeekDay(iDay,
                    CalendarConstant.iFirstDayOfWeek);
            day.setData(iWeekDay);
            layRow.addView(day);
        }
        return layRow;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            updateCalendar(list);
        }
    };

    public void showCalendarView() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                calendarView.setVisibility(View.VISIBLE);
                dialog.show();
                jsImpl.showCalendar();
            }
        });
    }

    Handler handler = new Handler();

    public void dismissCalendarview() {

        handler.post(new Runnable() {

            @Override
            public void run() {
                calendarView.setVisibility(View.GONE);
                dialog.dismiss();
                jsImpl.hideCalendar();
            }
        });
    }

    void setScrollView() {
        arrange_text = new TextView(activity);
        arrange_layout = CalendarConstant.createLayout(LinearLayout.VERTICAL,
                activity);
        view = new ScrollView(activity);
        LinearLayout.LayoutParams Param1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        arrange_layout.setPadding(5, 2, 0, 0);
        arrange_text.setTextColor(Color.BLACK);
        arrange_text.setTextSize(18);
        arrange_layout.addView(arrange_text);
        view.addView(arrange_layout, Param1);
        mainLayout.addView(view);
    }

    /**
     * ** 设置各种画笔的颜色
     */
    private void setPaintColor() {
        CalendarConstant.Calendar_WeekBgColor = activity.getResources()
                .getColor(R.color.Calendar_WeekBgColor);
        CalendarConstant.Calendar_DayBgColor = activity.getResources()
                .getColor(R.color.Calendar_DayBgColor);
        CalendarConstant.isHoliday_BgColor = activity.getResources().getColor(
                R.color.isHoliday_BgColor);
        CalendarConstant.unPresentMonth_FontColor = activity.getResources()
                .getColor(R.color.othermonth_textcolor);
        CalendarConstant.isPresentMonth_FontColor = activity.getResources()
                .getColor(R.color.currentmonth_textcolor);
        CalendarConstant.isToday_BgColor = activity.getResources().getColor(
                R.color.isToday_BgColor);
        CalendarConstant.special_Reminder = activity.getResources().getColor(
                R.color.specialReminder);
        CalendarConstant.common_Reminder = activity.getResources().getColor(
                R.color.commonReminder);
        CalendarConstant.Calendar_WeekFontColor = activity.getResources()
                .getColor(R.color.Calendar_WeekFontColor);
    }

}