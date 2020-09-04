package com.edu.mtdr.prpayment.schema;

import javax.persistence.*;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModelProperty;

@Table(name = "payment")
@Entity
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderid")
    @ApiModelProperty(value = "Sender")
    private ParticipantEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverid")
    @ApiModelProperty(value = "Receiver")
    private ParticipantEntity receiver;

    private BigDecimal amount;
}
