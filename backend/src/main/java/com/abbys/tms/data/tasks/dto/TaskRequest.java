package com.abbys.tms.data.tasks.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private String title;
    private String description;
    private String status;
    private String priority;
    private Long assignedTo;
    private String dueDate;
    private int version;
}
