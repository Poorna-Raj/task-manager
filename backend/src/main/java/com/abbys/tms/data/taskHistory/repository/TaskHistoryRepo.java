package com.abbys.tms.data.taskHistory.repository;

import com.abbys.tms.data.taskHistory.entity.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskHistoryRepo extends JpaRepository<TaskHistory,Long> {
    List<TaskHistory> findByTaskIdOrderByChangedAtDesc(Long taskId);
}
