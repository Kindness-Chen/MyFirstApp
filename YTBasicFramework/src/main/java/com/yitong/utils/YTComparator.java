package com.yitong.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.yitong.annotation.YTSort;
import com.yitong.logs.Logs;

/**
 * ================================================= <br>
 * 工程：MyComparator <br>
 * 类名：YTComparator <br>
 * 作者：dlzhang <br>
 * 时间：2015-4-28上午8:58:21 <br>
 * 版本：Version 1.0 <br>
 * 版权：Copyright (c) 2015 Shanghai P&C Information Technology Co.,Ltd. <br>
 * 描述：实体类的比较器，类似SQL的排序功能，具体功能如下 <br>
 * <li>支持多字段排序<br>
 * <li>字段名可自定义，支持多级字段排序，多级字段之间用"."（小数点）进行分割<br>
 * <li>排序字段优先级可配置，同级别按字段申明顺序排序<br>
 * <li>字段优先级可进行倒序设置<br>
 * <li>排序类型可配置<br>
 * ================================================= <br>
 */
public class YTComparator<T> implements Comparator<T> {

	private static final String TAG = "YTComparator";
	
	/** 排序类型-升序 */
	public static final int SORT_TYPE_ASC = 1;

	/** 排序类型-降序 */
	public static final int SORT_TYPE_DESC = -1;

	/** 字段优先级升序, 字段优先级越低则越先进行排序 */
	public static final int SORT_PRIORITY_ASC = 1;

	/** 字段优先级倒序, 字段优先级越高则越先进行排序 */
	public static final int SORT_PRIORITY_DESC = -1;

	/** 排过序的字段集合 */
	private List<Field> fieldSorted = new LinkedList<Field>();

	private Class<T> klassT;

	public YTComparator(Class<T> klass) {
		this(SORT_PRIORITY_ASC, klass);
	}

	public YTComparator(int sortPriority, Class<T> klass) {
		this.klassT = klass;
		initTParams(sortPriority);
	}

	/**
	 * 实体类T排序参数预处理
	 */
	void initTParams(int sortPriority) {
		Map<Integer, List<Field>> priorityMapfieldsMapUnsorted = new HashMap<Integer, List<Field>>();
		Map<Integer, List<Field>> priorityMapfieldsMapSorted;
		if (SORT_PRIORITY_ASC == sortPriority) {
			priorityMapfieldsMapSorted = new TreeMap<Integer, List<Field>>();
		} else {
			priorityMapfieldsMapSorted = new TreeMap<Integer, List<Field>>(Collections.reverseOrder());
		}
		Field[] fieldArray = klassT.getDeclaredFields();
		if (fieldArray != null && fieldArray.length > 0) {
			for (Field field : fieldArray) {
				if (field.isAnnotationPresent(YTSort.class)) {
					YTSort ytCompareInject = field.getAnnotation(YTSort.class);
					int priority = ytCompareInject.priority();
					List<Field> fieldList = priorityMapfieldsMapUnsorted.get(priority);
					if (null == fieldList) {
						fieldList = new LinkedList<Field>();
						priorityMapfieldsMapUnsorted.put(priority, fieldList);
					}
					fieldList.add(field);
				}
			}
		}
		priorityMapfieldsMapSorted.putAll(priorityMapfieldsMapUnsorted);
		Iterator<List<Field>> it = priorityMapfieldsMapSorted.values().iterator();
		for (; it.hasNext();) {
			List<Field> fields = it.next();
			if (null != fields) {
				fieldSorted.addAll(fields);
			}
		}
	}

	@Override
	public int compare(T o1, T o2) {
		for (Field field : fieldSorted) {
			YTSort ytCompareInject = field.getAnnotation(YTSort.class);
			int sortType = ytCompareInject.sortType();
			String fieldName = ytCompareInject.name();
			if (null == fieldName || "".equals(fieldName)) {
				fieldName = field.getName();
			}
			Object obj1 = invokeMethod(fieldName, o1);
			Object obj2 = invokeMethod(fieldName, o2);
			if (null == obj1 || null == obj2)
				continue;
			if (obj1 instanceof String) {
				if (((String) obj1).equals((String) obj2))
					continue; // 相等则判断下一个字段
				Integer len1 = ((String) obj1).length();
				Integer len2 = ((String) obj2).length();
				if (len1 != len2) {
					if (SORT_TYPE_ASC == sortType) {
						return len1.compareTo(len2);
					} else {
						return len2.compareTo(len1);
					}					
				}
				if (SORT_TYPE_ASC == sortType) {
					return ((String) obj1).compareTo((String) obj2);
				} else {
					return ((String) obj2).compareTo((String) obj1);
				}
			} else if (obj1 instanceof Integer) {
				if (((Integer) obj1) == ((Integer) obj2))
					continue; // 相等则判断下一个字段
				if (SORT_TYPE_ASC == sortType) {
					return ((Integer) obj1).compareTo((Integer) obj2);
				} else {
					return ((Integer) obj2).compareTo((Integer) obj1);
				}
			} else if (obj1 instanceof Boolean) {
				if (((Boolean) obj1) == ((Boolean) obj2))
					continue; // 相等则判断下一个字段
				if (SORT_TYPE_ASC == sortType) {
					return ((Boolean) obj1).compareTo((Boolean) obj2);
				} else {
					return ((Boolean) obj2).compareTo((Boolean) obj1);
				}
			} else {
				// TODO 支持类型扩展
			}
		}
		return 0;
	}

	/**
	 * 获取属性值
	 * 
	 * @param fieldName
	 * @param obj
	 * @return
	 */
	private Object invokeMethod(String fieldName, Object obj) {
		try {
			if (obj == null)
				return null;
			if (!fieldName.contains(".")) {
				String methodName = constructMethodName(fieldName);
				Method method = obj.getClass().getMethod(methodName);
				return method.invoke(obj);
			}
			String methodName = constructMethodName(fieldName.substring(0, fieldName.indexOf(".")));
			Method method = obj.getClass().getMethod(methodName);
			return invokeMethod(fieldName.substring(fieldName.indexOf(".") + 1), method.invoke(obj));
		} catch (Exception e) {
			Logs.e(TAG, "获取属性值失败", e);
		}
		return null;
	}

	/**
	 * 构造属性get方法名
	 * 
	 * @param fieldName
	 * @return
	 */
	private String constructMethodName(String fieldName) {
		byte[] items = fieldName.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return "get" + new String(items);
	}

}