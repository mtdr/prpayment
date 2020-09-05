package com.edu.mtdr.prpayment.schema;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


@ApiModel(value = "Payment entity")
@Table(name = "payment", uniqueConstraints = {
        @UniqueConstraint(name= "uc_paymententity_senderid_receiverid_amount_date",
                columnNames = {"senderid", "receiverid", "amount", "date"})
})
@Entity
@Access(AccessType.FIELD)
public class PaymentEntity extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "senderid")
    @ApiModelProperty(value = "Sender")
    private ParticipantEntity sender;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiverid")
    @ApiModelProperty(value = "Receiver")
    private ParticipantEntity receiver;

    @Basic
    @Min(value = 0)
    @Column(name = "amount", precision = 14, scale = 4)
    @ApiModelProperty(value = "Amount of payment")
    private BigDecimal amount;

    @Type(type = "timestamp")
    @ApiModelProperty(value = "Creation timestamp")
    private Date date;

    @Basic
    private Integer shardNum;

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

    public Integer getShardNum() {
        return shardNum;
    }

    public void setShardNum(Integer shardNum) {
        this.shardNum = shardNum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentEntity that = (PaymentEntity) o;
        return sender.equals(that.sender) &&
                receiver.equals(that.receiver) &&
                amount.equals(that.amount) &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, amount, date);
    }

    @Override
    public String toString() {
        return String.format(
                "Payment [id=%d]",
                getId());
    }


}
