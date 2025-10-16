package com.yitong.android.widget.calendar.vo;

import java.util.ArrayList;

public class CalendarViewVo {
	
	private String STATUS;
	
	private String MSG;

	private String AcNo;
	
	private String func;
	
	private ArrayList<CalendarViewListVo>list;

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getAcNo() {
		return AcNo;
	}

	public void setAcNo(String acNo) {
		AcNo = acNo;
	}

	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public ArrayList<CalendarViewListVo> getList() {
		return list;
	}

	public void setList(ArrayList<CalendarViewListVo> list) {
		this.list = list;
	}
}