package com.yitong.service.param;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 基本请求参数类(一层结构，包括Json和表单两种方式)
 * {"key1":"value1","key2":"value2"} 或者 key1=value1&key2=value2
 * @author tongxu_li
 * Copyright (c) 2014 Shanghai P&C Information Technology Co., Ltd.
 */
public class YTBaseRequestParams implements YTRequestParams {
	
    public final static int PARAM_TYPE_JSON = 0;
    public final static int PARAM_TYPE_FORM = 1;
    
    protected int mParamType;
    protected Map<String, Object> mParams;
    
    protected static OnParamsInitListener gInitListener = null;
    
    public YTBaseRequestParams(int paramType) {
    	this(paramType, new HashMap<String, Object>());
    }
    
    /**
     * 直接构造JSON格式字符串
     */
    public YTBaseRequestParams(String params) {
    	this(PARAM_TYPE_JSON, parseParams(params));  	
    }
    
    public YTBaseRequestParams(int paramType, Map<String, Object> params) {
    	setParamType(paramType);
    	mParams = params;
    	addCommonParams();
    }
    
    /**
     * 更新通用参数
     * 通用参数中可能有时间等可变参数，如果在此参数基础上添加参数，再次请求，可能时间等参数没有变，导致请求错误
     * @author zhangpeizhong 2015-9-21 上午9:23:52
     */
    public void updateCommonParams(){
    	addCommonParams();
    }
    
    private static HashMap<String, Object> parseParams(String params) {
    	HashMap<String, Object> result = null;
    	try {
            Gson gson = new GsonBuilder().registerTypeAdapter(
                new TypeToken<HashMap<String, Object>>(){}.getType(),
                new JsonDeserializer<HashMap<String, Object>>() {
                    @Override
                    public HashMap<String, Object> deserialize(
                            JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        JsonObject jsonObject = json.getAsJsonObject();
                        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                        for (Map.Entry<String, JsonElement> entry : entrySet) {
                            hashMap.put(entry.getKey(), entry.getValue());
                        }
                        return hashMap;
                    }
                }).create();
        	result = gson.fromJson(params, new TypeToken<HashMap<String, Object>>(){}.getType());			
		} catch (Exception e) {
			throw new IllegalArgumentException("参数必须为json格式");
		}
    	
    	return result;
    }
    
    /**
     * 添加公共参数
     */
    private void addCommonParams() {
    	if (gInitListener != null) {
    		Map<String, Object> commonParams = gInitListener.onAddCommonParams();
        	if (commonParams != null) {
        		mParams.putAll(commonParams);
        	}
    	}
    }

	public void setParamType(int mParamType) {
		this.mParamType = mParamType;
	}
	
	public int getParamType() {
		return mParamType;
	}
	
	@Override
    public void put(String key, Object value) {
    	mParams.put(key, value);
    }
    
    public Map<String, Object> getParams() {
    	return mParams;
    }
    
	@Override
	public String getContentType() {
		String contentType = null;
		switch (getParamType()) {
		case PARAM_TYPE_FORM:
			contentType = "application/x-www-form-urlencoded";
			break;
		case PARAM_TYPE_JSON:
			contentType = "application/json";
			break;
		default:
			break;
		}
		return contentType;
	}
    
	@Override
    public String getParamsString() {
    	String paramsString = "";  	
    	
		StringBuilder sb = new StringBuilder();
		if (mParamType == PARAM_TYPE_FORM) {
			if (mParams.size() > 0) {
		        for (Map.Entry<String, Object> entry : mParams.entrySet()) {
		            if (sb.length() > 0)
		            	sb.append("&");

		            sb.append(entry.getKey());
		            sb.append("=");
		            sb.append(entry.getValue());
		        }
		        paramsString = sb.toString();
			}
		} else if (mParamType == PARAM_TYPE_JSON) {
	    	Gson gson = new Gson();
	    	sb.append(gson.toJson(mParams));
	    	paramsString = sb.toString();
		}
		
        return paramsString;
    }
	
    public static void setParamsInitListener(OnParamsInitListener listener) {
    	YTBaseRequestParams.gInitListener = listener;
    }
    
    /**
     * 监听操作
     * @author tongxu_li
     * Copyright (c) 2014 Shanghai P&C Information Technology Co., Ltd.
     */
    public interface OnParamsInitListener {

    	public Map<String, Object> onAddCommonParams(); 
    }
}