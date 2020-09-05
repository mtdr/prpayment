package com.edu.mtdr.prpayment.schema;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@ApiModel(value = "Base entity")
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class BaseEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
