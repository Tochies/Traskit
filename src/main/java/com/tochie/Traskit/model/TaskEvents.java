package com.tochie.Traskit.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class TaskEvents extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "taskFk", nullable = false)
    private Task taskFk;

    private Boolean taskEventBoolean;

    @Column(length = 5000)
    private String taskEventDescription;

    private Long taskEventsCounter;

    @ManyToOne
    @JoinColumn(name = "createdBy")
    private User createdBy;


}
