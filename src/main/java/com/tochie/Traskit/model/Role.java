package com.tochie.Traskit.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role extends BaseEntity{

    @Column(length = 60)
    private String name;
}
