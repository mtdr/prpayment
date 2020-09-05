package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.config.datasource.DbContextHolder;
import com.edu.mtdr.prpayment.config.datasource.DbTypeEnum;
import com.edu.mtdr.prpayment.repository.PaymentRepository;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

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
            int shardNum = getShardNum(payment);
            payment.setShardNum(shardNum);
            if (payment.getDate() == null) {
                payment.setDate(new Date());
            }
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
    public Boolean saveAll(List<PaymentEntity> payments) {
        if (payments != null && payments.size() > 0) {
            for (PaymentEntity payment : payments) {
                int shardNum = getShardNum(payment);
                if (payment.getDate() == null) {
                    payment.setDate(new Date());
                }
                payment.setShardNum(shardNum);
                payment.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
            }
            for (DbTypeEnum dbType : DbTypeEnum.values()) {
                payments.parallelStream().filter(p -> p.getShardNum() == dbType.ordinal() + 1).forEach(p -> {
                    DbContextHolder.setCurrentDb(dbType);
                    paymentRepository.save(p);
                });
            }
            return true;
        } else {
            return false;
        }
    }

    private int getShardNum(PaymentEntity payment) {
        return Math.abs(payment.hashCode()) % 3 + 1;
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
