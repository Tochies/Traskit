package com.tochie.Traskit.model;

import com.tochie.Traskit.enums.ScheduleFrequency;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table
public class TaskSchedule extends BaseEntity{


    private Timestamp firstRun;
    private Timestamp lastRun;

    @Enumerated(EnumType.STRING)
    private ScheduleFrequency scheduleFrequency;

    @JoinColumn(nullable = false)
    private Task taskFk;

}
