package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.schema.PaymentEntity;
import org.springframework.stereotype.Service;

@Service
public class ShardManagerService implements IShardManagerService {
    @Override
    public int getShardNum(PaymentEntity payment) {
        return Math.abs(payment.hashCode()) % 3 + 1;
    }
}
