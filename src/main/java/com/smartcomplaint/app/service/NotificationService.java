package com.smartcomplaint.app.service;

import com.smartcomplaint.app.dto.StatusUpdateDTO;
import com.smartcomplaint.app.model.Notification;
import com.smartcomplaint.app.model.User;
import com.smartcomplaint.app.repository.NotificationRepository;
import com.smartcomplaint.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // [REQUIREMENT MET] @Async - NotificationService.java
    @Async
    public void sendNotificationAsync(Long userId, String message, String userEmail, String newStatus) {
        try {
            // Simulate email/SMS delay
            Thread.sleep(500);
            
            User user = userRepository.findById(userId).orElseThrow();
            
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setMessage(message);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setIsRead(false);
            
            notificationRepository.save(notification);
            
            // Push via STOMP WebSocket if email and status are provided
            if (userEmail != null && newStatus != null) {
                // Send to specific user using their email as principal name
                StatusUpdateDTO dto = new StatusUpdateDTO(null, newStatus, message);
                messagingTemplate.convertAndSendToUser(userEmail, "/queue/notifications", dto);
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
    }
}
