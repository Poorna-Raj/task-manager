package com.abbys.tms.controller;

import com.abbys.tms.data.comment.dto.CommentRequest;
import com.abbys.tms.data.comment.dto.CommentResponse;
import com.abbys.tms.data.user.entity.User;
import com.abbys.tms.data.user.repository.UserRepo;
import com.abbys.tms.exception.NotFound;
import com.abbys.tms.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserRepo userRepo;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentRequest req,@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = resolveUserId(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(userId,req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long id, @Valid @RequestBody CommentRequest req) {
        return ResponseEntity.ok(commentService.updateComment(id,req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments(@RequestParam Long taskId) {
        return ResponseEntity.ok(commentService.getAllComments(taskId));
    }

    private Long resolveUserId(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFound("User not found with email: " + email));
        return user.getId();
    }
}
