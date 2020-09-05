package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.schema.PaymentEntity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
     * @param payments list of saving payments
     * @return is successfully saved
     */
    Boolean saveAll(List<PaymentEntity> payments);

    /**
     * @param senderId participant id
     * @return sum of payments sent by participant
     */
    BigDecimal sumAmountsBySender(Long senderId);

    /**
     * @param senderId participant id
     * @param shardNum num of shard (1 - 3)
     * @return sum of payments sent by participant
     */
    BigDecimal sumAmountsBySenderAtOneShard(Long senderId, int shardNum);

    /**
     * @return is successfully generated and saved payments
     */
    Boolean generateAndSavePayments();

    /**
     * @return list of generated but not persisted payments
     */
    List<PaymentEntity> generatePayments();

    /**
     * @return all payments from all shards
     */
    List<PaymentEntity> findAll();

    /**
     * @param id payment's id
     * @return payment found last at all shards
     */
    Optional<PaymentEntity> findById(Long id);

    /**
     * Remove payment found on specified shard
     *
     * @param id payment id
     */
    void deleteById(Long id);
}
