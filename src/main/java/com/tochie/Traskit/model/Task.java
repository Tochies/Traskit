package com.tochie.Traskit.model;

import com.tochie.Traskit.enums.ScheduleType;
import com.tochie.Traskit.enums.TaskType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class Task extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

}
