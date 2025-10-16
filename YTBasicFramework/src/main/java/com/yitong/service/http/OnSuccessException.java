package com.yitong.service.http;

/**
 * onSuccess业务处理异常未捕捉
 * @author tongxu_li
 * Copyright (c) 2015 Shanghai P&C Information Technology Co., Ltd.
 */
public class OnSuccessException extends RuntimeException {

	private static final long serialVersionUID = -6710914363381659854L;
	
    public OnSuccessException(Throwable cause) {
        super(cause);
    }
}
