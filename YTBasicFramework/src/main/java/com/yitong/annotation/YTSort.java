package com.yitong.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.yitong.utils.YTComparator;

/**
 * ================================================= <br>
 * 工程：YTSort <br>
 * 类名：YTCompareAnnotation <br>
 * 作者：dlzhang <br>
 * 时间：2015-4-28下午1:58:27 <br>
 * 版本：Version 1.0 <br>
 * 版权：Copyright (c) 2015 Shanghai P&C Information Technology Co.,Ltd. <br>
 * 描述：排序注解类 <br>
 * ================================================= <br>
 */
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface YTSort {
	
	/**
	 * 自定义字段名称,支持多级字段排序,多级字段之间用"."（小数点）进行分割. 默认为字段本身名称
	 * 
	 * @return 返回自定义字段名称
	 */
	String name() default "";

	/**
	 * 排序类型,默认为升序排列. 参见：{@link YTComparator#SORT_TYPE_ASC},
	 * {@link YTComparator#SORT_TYPE_DESC}
	 * 
	 * @return 返回排序类型
	 */
	int sortType() default 1;

	/**
	 * 排序优先级，值越大则优先级越高（同级别按字段声明顺序优先）
	 * 
	 * @return 返回排序顺序
	 */
	int priority() default 0;
}
