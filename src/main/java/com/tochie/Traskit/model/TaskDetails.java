package com.tochie.Traskit.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table
@Entity
public class TaskDetails extends BaseEntity{

    private String title;

    @Column(length = 5000)
    private String taskContent;

    @OneToOne
    @JoinColumn(name = "taskFk", nullable = false)
    private Task taskFk;

}
