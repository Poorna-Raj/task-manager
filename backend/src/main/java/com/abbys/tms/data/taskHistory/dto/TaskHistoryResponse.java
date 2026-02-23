package com.abbys.tms.data.taskHistory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskHistoryResponse {
    private Long id;
    private String oldStatus;
    private String newStatus;
    private Long changedBy;
    private String changedAt;
}
