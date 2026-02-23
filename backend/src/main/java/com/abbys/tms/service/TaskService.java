package com.abbys.tms.service;

import com.abbys.tms.data.tasks.dto.TaskRequest;
import com.abbys.tms.data.tasks.dto.TaskResponse;
import com.abbys.tms.data.tasks.entity.Task;
import com.abbys.tms.data.tasks.entity.TaskPriority;
import com.abbys.tms.data.tasks.entity.TaskStatus;
import com.abbys.tms.data.tasks.repository.TaskRepo;
import com.abbys.tms.data.user.entity.User;
import com.abbys.tms.data.user.repository.UserRepo;
import com.abbys.tms.exception.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;
    private final TaskHistoryService taskHistoryService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public TaskResponse createTask(Long creatorId, TaskRequest request) {
        User creator = userRepo.findById(creatorId)
                .orElseThrow(() -> new NotFound("Creator not found with id: " + creatorId));

        User assignee = null;
        if (request.getAssignedTo() != null) {
            assignee = userRepo.findById(request.getAssignedTo())
                    .orElseThrow(() -> new NotFound("Assignee not found with id: " + request.getAssignedTo()));
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? TaskStatus.valueOf(request.getStatus().toUpperCase()) : TaskStatus.PENDING)
                .priority(request.getPriority() != null ? TaskPriority.valueOf(request.getPriority().toUpperCase()) : TaskPriority.LOW)
                .createdBy(creator)
                .assigned_to(assignee)
                .dueDate(request.getDueDate() != null ? LocalDateTime.parse(request.getDueDate(), FORMATTER) : null)
                .build();

        return mapToResponse(taskRepo.save(task));
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new NotFound("Task not found with id: " + taskId));
        return mapToResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest request) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new NotFound("Task not found with id: " + taskId));

        User assignee = userRepo.findById(request.getAssignedTo())
                .orElseThrow(() -> new NotFound("Assignee not found with id: " + request.getAssignedTo()));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        taskHistoryService.createHistory(task,task.getStatus(), TaskStatus.valueOf(request.getStatus()),assignee);

        task.setStatus(TaskStatus.valueOf(request.getStatus().toUpperCase()));
        task.setPriority(TaskPriority.valueOf(request.getPriority().toUpperCase()));
        task.setDueDate(LocalDateTime.parse(request.getDueDate(), FORMATTER));

        if (request.getAssignedTo() != null) {
            task.setAssigned_to(assignee);
        }

        return mapToResponse(taskRepo.save(task));
    }

    public void deleteTask(Long taskId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new NotFound("Task not found with id: " + taskId));
        taskRepo.delete(task);
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .priority(task.getPriority().name())
                .createdBy(task.getCreatedBy().getId())
                .assignedTo(task.getAssigned_to() != null ? task.getAssigned_to().getId() : null)
                .dueDate(task.getDueDate() != null ? task.getDueDate().format(FORMATTER) : null)
                .version(task.getVersion())
                .createdAt(task.getCreatedAt().format(FORMATTER))
                .updatedAt(task.getUpdatedAt().format(FORMATTER))
                .build();
    }
}