package com.tochie.Traskit.repository;


import com.tochie.Traskit.model.TaskSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TaskScheduleRepository extends JpaRepository<TaskSchedule, Long> {

    @Query(value = "select * FROM task_schedule td WHERE td.task_fk = :taskId", nativeQuery = true)
    TaskSchedule getTaskScheduleByTaskFk(Long taskId);
}
