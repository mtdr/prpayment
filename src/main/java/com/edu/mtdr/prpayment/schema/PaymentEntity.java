package com.edu.mtdr.prpayment.schema;

import javax.persistence.*;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModelProperty;

@Table(name = "payment")
@Entity
@Access(AccessType.FIELD)
public class PaymentEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderid")
    @ApiModelProperty(value = "Sender")
    private ParticipantEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverid")
    @ApiModelProperty(value = "Receiver")
    private ParticipantEntity receiver;

    private BigDecimal amount;

    public ParticipantEntity getSender() {
        return sender;
    }

    public void setSender(ParticipantEntity sender) {
        this.sender = sender;
    }

    public ParticipantEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(ParticipantEntity receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format(
                "Payment [id=%d]",
                getId());
    }
}
