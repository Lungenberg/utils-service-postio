package com.post_hub.utils_service.entity;

import com.post_hub.utils_service.model.enums.ActionType;
import com.post_hub.utils_service.model.enums.PostioService;
import com.post_hub.utils_service.model.enums.PriorityType;
import com.post_hub.utils_service.utils.enum_converter.PostioServiceConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "action_logs")
@Getter
@Setter
@ToString

public class ActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_read", nullable = false, updatable = false)
    private Boolean isRead;

    @Column(name = "message", nullable = false, updatable = false)
    private String message;

    @Column(name = "action_type", nullable = false, updatable = false)
    @Enumerated(value = EnumType.STRING)
    private ActionType actionType;

    @Column(name = "priority_type", nullable = false, updatable = false)
    @Enumerated(value = EnumType.STRING)
    private PriorityType priorityType;

    @Column(name = "service", nullable = false, updatable = false)
    @Convert(converter = PostioServiceConverter.class)
    private PostioService service;
}
