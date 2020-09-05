package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.config.datasource.DbContextHolder;
import com.edu.mtdr.prpayment.config.datasource.DbTypeEnum;
import com.edu.mtdr.prpayment.repository.ParticipantRepository;
import com.edu.mtdr.prpayment.repository.PaymentRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Payment service implementation
 */
@Service
public class PaymentService implements IPaymentService {
    private final PaymentRepository paymentRepository;
    private final ParticipantRepository participantRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          ParticipantRepository participantRepository) {
        this.paymentRepository = paymentRepository;
        this.participantRepository = participantRepository;
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
                DbContextHolder.setCurrentDb(dbType);
                paymentRepository.saveAll(payments.parallelStream()
                        .filter(p -> p.getShardNum() == dbType.ordinal() + 1)
                        .collect(Collectors.toList()));
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

    @Override
    public Boolean generateAndSavePayments() {
        List<PaymentEntity> payments = generatePayments();
        return saveAll(payments);
    }

    @Override
    public List<PaymentEntity> generatePayments() {
        List<PaymentEntity> payments = new ArrayList<>();
        ParticipantEntity aParticipant = participantRepository.findFirstByName("a").orElse(null);
        ParticipantEntity bParticipant = participantRepository.findFirstByName("b").orElse(null);
        if (aParticipant == null || bParticipant == null) {
            return payments;
        }
        IntStream.rangeClosed(0, (int) Math.pow(10, 4)).forEach(i -> {
            PaymentEntity payment = new PaymentEntity();
            payment.setSender(aParticipant);
            payment.setReceiver(bParticipant);
            payment.setAmount(BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(1000)));
            payments.add(payment);
        });

        IntStream.rangeClosed(0, (int) Math.pow(10, 4)).forEach(i -> {
            PaymentEntity payment = new PaymentEntity();
            payment.setSender(bParticipant);
            payment.setReceiver(aParticipant);
            payment.setAmount(BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(1000)));
            payments.add(payment);
        });
        return payments;
    }

}
