package com.medical.entity.base;

import java.io.Serializable;

/**
 * BaseResponse
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年7月28日 下午4:14:18
 * @version 1.0
 */
public class BaseResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7166368138108344663L;
    
    private boolean success;
    
    private int code;
    
    private String exceptionMessage;
    
    private long currentTime;
    
    private Object returnObject;
    
    public BaseResponse() {
    }
    
    public BaseResponse(boolean success,int code){
        this.success = success;
        this.code = code;
        this.currentTime = System.currentTimeMillis();
    }

    public BaseResponse(boolean success,int code,Object returnObject){
        this.success = success;
        this.code = code;
        this.returnObject = returnObject;
        this.currentTime = System.currentTimeMillis();
    }
    
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public Object getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

}
