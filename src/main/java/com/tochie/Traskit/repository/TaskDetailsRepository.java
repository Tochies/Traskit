package com.tochie.Traskit.repository;

import com.tochie.Traskit.model.TaskDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDetailsRepository extends JpaRepository<TaskDetails, Long> {
}
