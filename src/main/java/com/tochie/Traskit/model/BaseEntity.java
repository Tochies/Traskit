package com.tochie.Traskit.model;

import jakarta.persistence.*;
import lombok.Data;


import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Data
public class BaseEntity implements Serializable, Comparable<BaseEntity>  {
    @Serial
    private static final long serialVersionUID = -596544221520696967L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "ID",
            nullable = false,
            insertable = true,
            updatable = false
    )
    private Long id;

    @Column(
            name = "ACTIVE",
            nullable = false,
            insertable = true,
            updatable = true
    )
    private boolean active = true;

    @Column(
            name = "DELETED",
            nullable = false,
            insertable = true,
            updatable = true
    )
    private boolean deleted = false;

    @Column(
            name = "CREATE_DATE",
            nullable = false,
            insertable = true,
            updatable = false
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @Column(
            name = "LAST_MODIFIED",
            nullable = true,
            insertable = true,
            updatable = true
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified = new Date();

    @Override
    public int compareTo(BaseEntity o) {
        return 0;
    }
}
