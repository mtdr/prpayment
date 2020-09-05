package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.config.datasource.DbContextHolder;
import com.edu.mtdr.prpayment.config.datasource.DbTypeEnum;
import com.edu.mtdr.prpayment.repository.PaymentRepository;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

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
            int shardNum = Math.abs(payment.hashCode()) % 3 + 1;
            payment.setShardNum(shardNum);
            payment.setDate(new Date());
            payment.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
            switch (shardNum) {
                case 1:
                    DbContextHolder.setCurrentDb(DbTypeEnum.SHARD1);
                    return paymentRepository.save(payment);
                case 2:
                    DbContextHolder.setCurrentDb(DbTypeEnum.SHARD2);
                    return paymentRepository.save(payment);
                case 3:
                    DbContextHolder.setCurrentDb(DbTypeEnum.SHARD3);
                    return paymentRepository.save(payment);
                default:
                    return paymentRepository.save(payment);
            }
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal sumAmountsBySender(Long senderId) {
        BigDecimal res = BigDecimal.ZERO;
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            BigDecimal temp = paymentRepository.getSumBySenderId(senderId);
            if (temp != null) {
                res = res.add(temp);
            }
        }
        return res;
    }

    @Override
    public BigDecimal sumAmountsBySenderAtOneShard(Long senderId, int shardNum) {
        DbTypeEnum dbType = DbTypeEnum.values()[shardNum - 1];
        DbContextHolder.setCurrentDb(dbType);
        return paymentRepository.getSumBySenderId(senderId);
    }

}
