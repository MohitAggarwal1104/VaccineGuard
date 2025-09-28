package com.vaccine.management.controller;

import com.vaccine.management.dto.NotificationDto;
import com.vaccine.management.model.Child;
import com.vaccine.management.model.Notification;
import com.vaccine.management.model.User;
import com.vaccine.management.repository.ChildRepository;
import com.vaccine.management.repository.NotificationRepository;
import com.vaccine.management.repository.UserRepository;
import com.vaccine.management.security.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final ChildRepository childRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    public NotificationController(NotificationRepository notificationRepository, ChildRepository childRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.childRepository = childRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationDto notificationDto) {
        MyUserDetailsService.CustomUserDetails doctorDetails = (MyUserDetailsService.CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String doctorId = doctorDetails.getId();
        String doctorName = doctorDetails.getName();

        log.info("Doctor {} ({}) is sending a notification for child ID: {}", doctorName, doctorId, notificationDto.getChildId());

        Child child = childRepository.findById(notificationDto.getChildId())
                .orElse(null);

        if (child == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Child not found."));
        }

        Notification notification = new Notification();
        notification.setSenderId(doctorId);
        notification.setSenderName(doctorName);
        notification.setRecipientId(child.getParentId());
        notification.setChildId(child.getId());
        notification.setChildName(child.getName());
        notification.setMessage(notificationDto.getMessage());
        notification.setTimestamp(LocalDateTime.now());

        notificationRepository.save(notification);
        log.info("Notification saved successfully for parent ID: {}", child.getParentId());

        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @GetMapping("/my-notifications")
    @PreAuthorize("hasAuthority('ROLE_PARENT')")
    public ResponseEntity<List<Notification>> getMyNotifications() {
        MyUserDetailsService.CustomUserDetails parentDetails = (MyUserDetailsService.CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String parentId = parentDetails.getId();
        
        log.info("Fetching notifications for parent ID: {}", parentId);
        List<Notification> notifications = notificationRepository.findByRecipientIdOrderByTimestampDesc(parentId);
        
        return ResponseEntity.ok(notifications);
    }
}

