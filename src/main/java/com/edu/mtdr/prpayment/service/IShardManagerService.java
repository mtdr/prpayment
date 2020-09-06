package com.edu.mtdr.prpayment.service;


import com.edu.mtdr.prpayment.schema.PaymentEntity;

/**
 * Util for processing shard number on {@link PaymentEntity}
 */
public interface IShardManagerService {
    /**
     * @return number of shard (between 1 and 3
     */
    int getShardNum(PaymentEntity payment);
}
