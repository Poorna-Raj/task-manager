package com.abbys.tms.service;

import com.abbys.tms.data.comment.dto.CommentRequest;
import com.abbys.tms.data.comment.dto.CommentResponse;
import com.abbys.tms.data.comment.entity.Comment;
import com.abbys.tms.data.comment.repository.CommentRepo;
import com.abbys.tms.data.tasks.entity.Task;
import com.abbys.tms.data.tasks.repository.TaskRepo;
import com.abbys.tms.data.user.entity.User;
import com.abbys.tms.data.user.repository.UserRepo;
import com.abbys.tms.exception.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepo commentRepo;
    private final TaskRepo taskRepo;
    private final UserRepo userRepo;

    public CommentResponse createComment(Long userId, CommentRequest req) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFound("Invalid user for the given ID"));

        Task task = taskRepo.findById(req.getTaskId())
                .orElseThrow(() -> new NotFound("Invalid task for the given ID"));

        Comment comment = Comment.builder()
                .content(req.getContent())
                .task(task)
                .user(user)
                .build();
        return mapToDto(commentRepo.save(comment));
    }

    public CommentResponse updateComment(Long commentId,CommentRequest req) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new NotFound("Invalid comment for the given ID"));
        Task task = taskRepo.findById(req.getTaskId())
                .orElseThrow(() -> new NotFound("Invalid task for the given ID"));

        comment.setContent(req.getContent());
        if(!Objects.equals(task.getId(), comment.getTask().getId())) {
            comment.setTask(task);
        }
        return mapToDto(commentRepo.save(comment));
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new NotFound("Invalid comment for the given ID"));
        commentRepo.delete(comment);
    }

    public CommentResponse getCommentById(Long commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new NotFound("Invalid comment for the given ID"));
        return mapToDto(comment);
    }

    public Page<CommentResponse> getAllComments(Long taskId, Pageable pageable) {
        Page<Comment> page;

        if (taskId == null) {
            page = commentRepo.findAll(pageable);
        } else {
            page = commentRepo.findByTaskId(taskId, pageable);
        }
        return page.map(this::mapToDto);
    }

    private CommentResponse mapToDto(Comment save) {
        return CommentResponse.builder()
                .id(save.getId())
                .taskId(save.getTask().getId())
                .content(save.getContent())
                .createdAt(save.getCreatedAt().toString())
                .userId(save.getUser().getId())
                .build();
    }
}
