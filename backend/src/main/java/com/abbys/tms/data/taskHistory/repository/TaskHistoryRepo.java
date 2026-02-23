package com.abbys.tms.data.taskHistory.repository;

import com.abbys.tms.data.taskHistory.entity.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskHistoryRepo extends JpaRepository<TaskHistory,Long> {
}
