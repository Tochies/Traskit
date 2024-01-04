package com.tochie.Traskit.repository;

import com.tochie.Traskit.model.TaskDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TaskDetailsRepository extends JpaRepository<TaskDetails, Long> {

    @Query(value = "select * FROM task_details td WHERE td.task_fk = :taskId", nativeQuery = true)
    TaskDetails getTaskDetailsByTaskFk(Long taskId);

}
