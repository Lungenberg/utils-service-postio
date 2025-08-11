package com.post_hub.utils_service.kafka.model;

import com.post_hub.utils_service.model.enums.ActionType;
import com.post_hub.utils_service.model.enums.PostioService;
import com.post_hub.utils_service.model.enums.PriorityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UtilMessage implements Serializable {

    private Integer userId;
    private ActionType actionType;
    private PriorityType priorityType;
    private PostioService service;
    private String message;

}
