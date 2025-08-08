package com.post_hub.utils_service.kafka.model;

import com.post_hub.utils_service.model.enums.ActionType;
import com.post_hub.utils_service.model.enums.PostioService;
import com.post_hub.utils_service.model.enums.PriorityType;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UtilMessage implements Serializable {

    private Integer userId;
    private ActionType actionType;
    private PriorityType priorityType;
    private PostioService service;
    private String message;

}
