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
    private Boolean recurring;          // Recurring means task runs hourly, daily or any of the schedule frequency
    private Boolean continuousRun;      // Continuous run means the user can update the options/contents of the task anytime

    @Enumerated(EnumType.STRING)
    private ScheduleFrequency scheduleFrequency;

    @JoinColumn(nullable = false)
    private Task taskFk;

}
