package com.edu.mtdr.prpayment.schema;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@ApiModel(value = "Participant entity of payments")
@Table(name = "participant")
@Entity
@Access(AccessType.FIELD)
public class ParticipantEntity extends BaseEntity {
    @ApiModelProperty(value = "name")
    @Basic
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "Participant [id=%d]",
                getId());
    }
}
