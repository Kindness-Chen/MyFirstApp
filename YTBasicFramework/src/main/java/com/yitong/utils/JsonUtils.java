/**
 * Created on 2015-9-6 下午4:58:40
 */
package com.yitong.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @Description:json工具类
 * 
 * @author zhangpeizhong(zpz@yitong.com.cn) 2015-9-6 下午4:58:40
 * @Class JsonUtils Copyright (c) 2015 Shanghai P&C Information Technology
 *        Co.,Ltd. All rights reserved.
 ****************************************************************************
 * 修改人  修改时间  修改内容
 *
 * ****************************************************************************
 */
public class JsonUtils {

	public static <T> T get(String jsonStr, Class<T> classOfT){
		JsonObject el = new JsonParser().parse(jsonStr).getAsJsonObject();
		return new Gson().fromJson(el, classOfT);
	}
	public static <T> T get(String jsonStr, Class<T> classOfT, String key){
		
		return get(new JsonParser().parse(jsonStr).getAsJsonObject(), classOfT, key);
	}
	public static <T> T get(JsonObject json, Class<T> classOfT, String key){
		
		if(!json.has(key))
			return null;
		
		JsonElement el = json.get(key);
		T result = null;
		
		if(classOfT.getName().equals(String.class.getName()) )
			result = (T)el.getAsString();
		else
			result = new Gson().fromJson(el, classOfT);
		
		return result;
	}
	
	public static <T> T getList(String jsonStr, Type type, String key){
		JsonObject json = new JsonParser().parse(jsonStr).getAsJsonObject();
		if(!json.has(key))
			return null;
		
		JsonElement el = json.get(key);
		T result = null;
		result = new Gson().fromJson(el, type);
		
		return result;
	}
	
}
