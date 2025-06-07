package com.zpark.repository;

import com.zpark.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<ChatMessage, String> {

    // 查找两个用户之间的消息
    List<ChatMessage> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(
            String sender1, String receiver1, String sender2, String receiver2);

    // 查找用户接收的所有通知
    List<ChatMessage> findByReceiverAndType(String receiver, ChatMessage.MessageType type);
}
