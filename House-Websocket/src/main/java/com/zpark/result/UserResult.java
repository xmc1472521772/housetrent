package com.zpark.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserResult implements Serializable {

    private int code;
    private String message;
    private String token;


    public UserResult loginandregister(int code,String message){
        UserResult result=new UserResult();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public UserResult tokenResult(int code,String message,String token){
        UserResult result=new UserResult();
        result.setCode(code);
        result.setMessage(message);
        result.setToken(token);
        return result;
    }


}


