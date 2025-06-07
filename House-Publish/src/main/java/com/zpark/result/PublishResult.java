package com.zpark.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class PublishResult implements Serializable {

    private int code;
    private String message;

    public PublishResult publishResult(int code,String message){
        PublishResult result=new PublishResult();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }



}


