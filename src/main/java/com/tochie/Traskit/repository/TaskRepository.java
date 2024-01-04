package com.tochie.Traskit.repository;

import com.tochie.Traskit.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t FROM Task t WHERE t.taskReference = ?1")
    Task getTaskByReference(String taskReference);

}
