package com.abbys.tms.service;

import com.abbys.tms.data.taskHistory.dto.TaskHistoryRequest;
import com.abbys.tms.data.taskHistory.dto.TaskHistoryResponse;
import com.abbys.tms.data.taskHistory.entity.TaskHistory;
import com.abbys.tms.data.taskHistory.repository.TaskHistoryRepo;
import com.abbys.tms.data.tasks.entity.Task;
import com.abbys.tms.data.tasks.entity.TaskStatus;
import com.abbys.tms.data.tasks.repository.TaskRepo;
import com.abbys.tms.data.user.entity.User;
import com.abbys.tms.data.user.repository.UserRepo;
import com.abbys.tms.exception.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskHistoryService {
    private final TaskHistoryRepo historyRepo;

    public void createHistory(Task task,
                              TaskStatus oldStatus,
                              TaskStatus newStatus,
                              User changedBy) {

        TaskHistory history = TaskHistory.builder()
                .task(task)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedBy(changedBy)
                .build();

        historyRepo.save(history);
    }

    public List<TaskHistoryResponse> getHistoryByTask(Long taskId) {
        return historyRepo.findByTaskIdOrderByChangedAtDesc(taskId).stream().map(this::mapToDto).toList();
    }

    private TaskHistoryResponse mapToDto(TaskHistory req) {
        return TaskHistoryResponse.builder()
                .id(req.getId())
                .oldStatus(req.getOldStatus().toString())
                .newStatus(req.getNewStatus().toString())
                .changedAt(req.getChangedAt().toString())
                .changedBy(req.getChangedBy().getId())
                .build();
    }
}
