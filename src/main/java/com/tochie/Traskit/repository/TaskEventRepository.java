package com.tochie.Traskit.repository;

import com.tochie.Traskit.model.TaskEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TaskEventRepository extends JpaRepository<TaskEvents, Long> {

    @Query(value = "select * FROM task_events td WHERE td.task_fk = :taskId", nativeQuery = true)
    TaskEvents getTaskScheduleByTaskFk(Long taskId);

    @Query(value = "select count(*) FROM task_events td WHERE td.task_fk = :taskId", nativeQuery = true)
    Long getTaskEventCount(Long taskId);
}
