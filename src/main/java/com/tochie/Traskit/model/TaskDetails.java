package com.tochie.Traskit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table
@Entity
public class TaskDetails extends BaseEntity{

    private String title;

    @Column(length = 5000)
    private String taskContent;

    @JoinColumn(nullable = false)
    private Task taskFk;

}
