package com.tochie.Traskit.repository;

import com.tochie.Traskit.model.TaskSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskScheduleRepository extends JpaRepository<TaskSchedule, Long> {
}
