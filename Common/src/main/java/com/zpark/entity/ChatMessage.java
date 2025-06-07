package com.zpark.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Data
@Document(collection = "messages")  // MongoDB集合名称
public class ChatMessage {
    @Id
    private String id;  // 消息ID
    private String sender;  // 发送者
    private String receiver;  // 接收者
    private String content;  // 消息内容
    private Date timestamp;  // 时间戳
    private MessageType type;  // 消息类型

    // 消息类型枚举
    public enum MessageType {
        CHAT,  // 用户间聊天
        NOTIFICATION  // 服务器通知
    }

    // 构造方法、getter和setter省略
}
