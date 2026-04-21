package com.smartcomplaint.app.controller;

import com.smartcomplaint.app.model.User;
import com.smartcomplaint.app.service.NotificationService;
import com.smartcomplaint.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private UserService userService;

    // Optional handler for generic broadcast if needed: 
    // @MessageMapping("/complaint.update")
    // @SendTo("/topic/notifications")

    @GetMapping("/user/notifications")
    public String viewNotifications(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("notifications", notificationService.getUserNotifications(user.getId()));
        }
        return "user/notifications"; // Could make a small fragment or just full page. But requirements didn't explicitly demand a full template for this, just an endpoint. We will create a simple one or load it via AJAX.
    }
    
    @PostMapping("/user/notifications/{id}/read")
    @ResponseBody
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
    
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }
}
