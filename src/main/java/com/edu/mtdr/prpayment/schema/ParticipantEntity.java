package com.edu.mtdr.prpayment.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@ApiModel(value = "Participant entity of payments")
@Table(name = "participant")
@Entity
@Access(AccessType.FIELD)
public class ParticipantEntity extends BaseEntity {
    @ApiModelProperty(value = "name")
    @Basic
    private String name;

    @ApiModelProperty(value = "Sent payments by this participant")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PaymentEntity> sentPayments = new HashSet<>();

    @ApiModelProperty(value = "Received payments by this participant")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PaymentEntity> receivedPayments = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PaymentEntity> getSentPayments() {
        return sentPayments;
    }

    public void setSentPayments(Set<PaymentEntity> sentPayments) {
        this.sentPayments = sentPayments;
    }

    public Set<PaymentEntity> getReceivedPayments() {
        return receivedPayments;
    }

    public void setReceivedPayments(Set<PaymentEntity> receivedPayments) {
        this.receivedPayments = receivedPayments;
    }

    @Override
    public String toString() {
        return String.format(
                "Participant [id=%d]",
                getId());
    }
}
