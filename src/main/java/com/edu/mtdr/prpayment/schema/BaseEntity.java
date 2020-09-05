package com.edu.mtdr.prpayment.schema;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;

@ApiModel(value = "Base entity")
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class BaseEntity implements Serializable {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
