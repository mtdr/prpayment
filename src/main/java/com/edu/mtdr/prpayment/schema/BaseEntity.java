package com.edu.mtdr.prpayment.schema;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;

@ApiModel(value = "Base entity")
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
