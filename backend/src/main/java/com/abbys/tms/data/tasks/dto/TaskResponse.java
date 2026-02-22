package com.abbys.tms.data.tasks.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Long createdBy;
    private Long assignedTo;
    private String dueDate;
    private int version;
    private String createdAt;
    private String updatedAt;
}
