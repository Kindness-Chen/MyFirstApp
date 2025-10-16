package com.yitong.android.widget.wheel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.yitong.basic.R;
import com.yitong.logs.Logs;
import com.yitong.utils.ToastTools;

/**
 * 上海屹通安全键盘V2.0
 * @author tongxu_li
 * Copyright (c) 2015 Shanghai P&C Information Technology Co., Ltd.
 */
public class YTDatePicker extends Dialog implements OnClickListener {
	private final static String TAG = "YTTimePicker";
	
	public enum DatePickerType {
		DATE, 		// 日期选择
		TIME 		// 时间键盘
	};
	
	public enum DatePickerHideState {
		OK,
		CANCEL
	}
	
	private Context mContext;
	
	// 键盘是否模态模式
	protected boolean mIsModalMode = true;
	// 绑定显示框
	protected EditText etAttach;
	
	// 键盘操作监听
	protected DatePickerStateListener mDatePickerStateListener;
	// 键盘操作监听
	protected DatePickerSelectedListener mDatePickerSelectedListener;
	
    private View datePickerPanel;
    private FrameLayout datePickerTitle;
    private Button btnPickerCancel;
    private Button btnPickerOK;
    private FrameLayout datePickerContent;
    
	private WheelView wlvHour;
	private WheelView wlvMinute;
	private WheelView wlvSecond;
   
	public YTDatePicker(Context context) {
    	super(context, R.style.TranslucentNoFrameDialogStyle);
    	this.mContext = context;
    	initView();
    	initData();
    }
    
    private void initView() {
    	datePickerPanel = LayoutInflater.from(mContext).inflate(R.layout.datepicker_panel, null);
    	datePickerTitle = (FrameLayout) datePickerPanel.findViewById(R.id.flayout_datepicker_title);
    	datePickerContent = (FrameLayout) datePickerPanel.findViewById(R.id.flayout_datepicker_content);
    	btnPickerCancel = (Button) datePickerTitle.findViewById(R.id.btnPickerCancel);
    	btnPickerOK = (Button) datePickerTitle.findViewById(R.id.btnPickerOK);
    	
		setContentView(datePickerPanel, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		datePickerContent.addView(createTimePickerView());
		
		getWindow().setWindowAnimations(R.style.bottom_show_dialog_anim_style);
		
		initKeyboardWindowParams();
    }
    
    private void initData() {
    	
    	btnPickerCancel.setOnClickListener(this);
    	btnPickerOK.setOnClickListener(this);
    }
    
    /**
     * 初始化键盘窗口属性
     */
    private void initKeyboardWindowParams() {
    	
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
		if (!mIsModalMode) {
			lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN 
					| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
					| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		} else {
			lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
			setCanceledOnTouchOutside(true);			
		}
		
        getWindow().setAttributes(lp);    	
    }
    
	private View createTimePickerView() {
		
		final View view = LayoutInflater.from(mContext).inflate(R.layout.datepicker_time, null);
		
		wlvHour = (WheelView) view.findViewById(R.id.wlvHour);
		wlvMinute = (WheelView) view.findViewById(R.id.wlvMinute);
		wlvSecond = (WheelView) view.findViewById(R.id.wlvSecond);
		
		wlvHour.setAdapter(new NumericWheelAdapter(0, 23, "%02d"));
		wlvHour.setCyclic(true);
		wlvHour.addScrollingListener(new OnWheelScrollListener() {
			
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				if (wlvHour.getCurrentItem() < 5) {
					wlvHour.setCurrentItem(5, true);
				}
			}
		});
		
		wlvMinute.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		wlvMinute.setCyclic(true);
		
		wlvSecond.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		wlvSecond.setCyclic(true);
		
		return view;
	}
	
	private String getSelectedTime() {
		String format = "%02d:%02d";
		return String.format(format, wlvHour.getCurrentItem(), wlvMinute.getCurrentItem());
	}
    
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btnPickerCancel) {
			hideDatePicker();
		} else if (id == R.id.btnPickerOK) {
			hideDatePicker(DatePickerHideState.OK);
		}
	}
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
			if (etAttach != null) {
				int[] location = new int[2];   
				etAttach.getLocationOnScreen(location);
				if (event.getRawX()<location[0] || event.getRawX()>location[0]+etAttach.getWidth()
						|| event.getRawY()<location[1] || event.getRawY()>location[1]+etAttach.getHeight()) {
					hideDatePicker();					
				}
			} else {
				hideDatePicker();
			}
		}
    	return super.onTouchEvent(event);
    }
	
	/**
	 * 设置键盘是否模态显示
	 * @param isModalMode
	 */
	public void setModalMode(boolean isModalMode) {
		this.mIsModalMode = isModalMode;
		initKeyboardWindowParams();
	}
	
	/**
	 * 弹出键盘
	 */
	public void showDatePicker() {
//		if (mKeyboardView != null) {
			Logs.d(TAG, "showKeyboard");
			super.show();
			if (mDatePickerStateListener != null) {
				mDatePickerStateListener.onShowDatePicker(this);
			}
//		}
    }
	
	/**
	 * 关闭键盘   
	 */
    public void hideDatePicker() {
    	Logs.d(TAG, "hideKeyboard");
    	hideDatePicker(DatePickerHideState.CANCEL);
    }
    
    public void hideDatePicker(DatePickerHideState state) {
    	super.dismiss();
    	if (state == DatePickerHideState.OK && mDatePickerSelectedListener != null) {
    		mDatePickerSelectedListener.selected(this, getSelectedTime());    		
    	}
		if (mDatePickerStateListener != null) {
			mDatePickerStateListener.onHideDatePicker(this, state);
		}
    }
    
	@Override
	public void show() {
		showDatePicker();
	}
    
    @Override
    public void dismiss() {
    	hideDatePicker();
    }
        
	public void setDatePickerStateListener(DatePickerStateListener datePickerStateListener) {
		this.mDatePickerStateListener = datePickerStateListener;
	}

	public void setDatePickerSelectedListener(DatePickerSelectedListener datePickerSelectedListener) {
		this.mDatePickerSelectedListener = datePickerSelectedListener;
	}
	
	/**
	 * 键盘操作监听
	 */
    public interface DatePickerStateListener {
    	/**
    	 * 键盘显示
    	 */
    	public void onShowDatePicker(YTDatePicker keyboard);
    	/**
    	 * 键盘隐藏
    	 */
    	public void onHideDatePicker(YTDatePicker keyboard, DatePickerHideState state);
    }	
    
	/**
	 * 键盘操作监听
	 */
    public interface DatePickerSelectedListener {    	
    	/**
    	 * 键盘输入后字符串信息
    	 */
    	public void selected(YTDatePicker keyboard, String text);
    }
}
