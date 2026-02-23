package com.abbys.tms.data.taskHistory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskHistoryRequest {
    private Long taskId;
    private String oldStatus;
    private String newStatus;
    private Long changedBy;
}
