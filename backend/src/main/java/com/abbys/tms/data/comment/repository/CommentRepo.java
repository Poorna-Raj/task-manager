package com.abbys.tms.data.comment.repository;

import com.abbys.tms.data.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment,Long> {
    List<Comment> findByTaskIdOrderByCreatedAtDesc(Long taskId);
}
