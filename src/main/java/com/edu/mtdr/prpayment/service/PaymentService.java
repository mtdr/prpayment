package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.repository.PaymentRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Payment service implementation
 */
@Service
public class PaymentService implements IPaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentEntity save(PaymentEntity payment) {
        if (payment != null) {
            int shardNum = payment.hashCode() % 3;
            switch (shardNum) {
                case 0:
                    return save1(payment);
                case 1:
                    return save1(payment);
                case 2:
                    return save1(payment);
                default:
                    return save1(payment);
            }
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal sumAmountsBySender(ParticipantEntity sender) {
        return paymentRepository.getSumBySender(sender);
    }

//    @Transactional(transactionManager = "tm1")
    protected PaymentEntity save1(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }


}
