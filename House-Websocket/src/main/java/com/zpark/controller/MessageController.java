package com.zpark.controller;

import com.zpark.entity.ChatMessage;
import com.zpark.repository.MessageRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Date;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;  // 消息发送模板
    private final MessageRepository messageRepository;  // 消息存储仓库

    public MessageController(SimpMessagingTemplate messagingTemplate,
                             MessageRepository messageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
    }

    // 处理用户间私聊消息
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage,
                                   Principal principal) {
        // 设置发送者为当前用户
        chatMessage.setSender(principal.getName());
        chatMessage.setTimestamp(new Date());
        chatMessage.setType(ChatMessage.MessageType.CHAT);

        // 保存到MongoDB
        messageRepository.save(chatMessage);

        // 发送给特定用户 (格式: /user/{username}/queue/private)
        messagingTemplate.convertAndSendToUser(
                chatMessage.getReceiver(),
                "/queue/private",
                chatMessage);
    }

    // 处理群聊消息
    @MessageMapping("/chat.group")
    @SendTo("/topic/group")  // 广播到所有订阅者
    public ChatMessage sendGroupMessage(@Payload ChatMessage chatMessage,
                                        Principal principal) {
        chatMessage.setSender(principal.getName());
        chatMessage.setTimestamp(new Date());
        chatMessage.setType(ChatMessage.MessageType.CHAT);

        messageRepository.save(chatMessage);
        return chatMessage;
    }

    // 服务器发送通知的方法
    public void sendNotificationToUser(String username, String content) {
        ChatMessage notification = new ChatMessage();
        notification.setSender("system");
        notification.setReceiver(username);
        notification.setContent(content);
        notification.setTimestamp(new Date());
        notification.setType(ChatMessage.MessageType.NOTIFICATION);

        messageRepository.save(notification);

        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                notification);
    }

    // 广播通知
    public void broadcastNotification(String content) {
        ChatMessage notification = new ChatMessage();
        notification.setSender("system");
        notification.setContent(content);
        notification.setTimestamp(new Date());
        notification.setType(ChatMessage.MessageType.NOTIFICATION);

        messageRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
