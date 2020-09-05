package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.schema.PaymentEntity;

import java.math.BigDecimal;

/**
 * Payment service
 */
public interface IPaymentService {
    /**
     * @param payment payment to save
     * @return saved payment
     */
    PaymentEntity save(PaymentEntity payment);

    /**
     * @param senderId participant id
     * @return sum of payments sent by participant
     */
    BigDecimal sumAmountsBySender(Long senderId);
}
