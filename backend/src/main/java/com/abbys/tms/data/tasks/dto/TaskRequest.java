package com.abbys.tms.data.tasks.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskRequest {
    private String title;
    private String description;
    private String status;
    private String priority;
    private Long assignedTo;
    private String dueDate;
    private int version;
}
