package com.abbys.tms.data.tasks.repository;

import com.abbys.tms.data.tasks.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task,Long> {
}
