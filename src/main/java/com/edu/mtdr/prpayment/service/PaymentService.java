package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.config.datasource.DbContextHolder;
import com.edu.mtdr.prpayment.config.datasource.DbTypeEnum;
import com.edu.mtdr.prpayment.repository.PaymentRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Payment service implementation
 */
@Service
public class PaymentService implements IPaymentService {
    private final PaymentRepository paymentRepository;
    private final IParticipantService participantService;

    @Value("${generate.payments.count}")
    private Integer countOfPayments;


    public PaymentService(PaymentRepository paymentRepository, IParticipantService participantService) {
        this.paymentRepository = paymentRepository;
        this.participantService = participantService;
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
        List<PaymentEntity> payments = generatePaymentsForFixedParticipants();
        return saveAll(payments);
    }

    @Override
    public List<PaymentEntity> generatePaymentsForFixedParticipants() {
        ParticipantEntity aParticipant = participantService.getOrCreate("a");
        ParticipantEntity bParticipant = participantService.getOrCreate("b");
        return generatePayments(aParticipant, bParticipant);
    }

    /**
     * @param aParticipant first participant
     * @param bParticipant second participant
     * @return generated list of N random amounted payments, N = {@link PaymentService#countOfPayments}
     */
    public List<PaymentEntity> generatePayments(ParticipantEntity aParticipant, ParticipantEntity bParticipant) {
        List<PaymentEntity> payments = new ArrayList<>();
        if (aParticipant == null || bParticipant == null) {
            return payments;
        }
        if (countOfPayments != 0) {
            payments.addAll(createPayments(aParticipant, bParticipant));
            payments.addAll(createPayments(bParticipant, aParticipant));
        }
        return payments;
    }

    /**
     * @param sender sender participant
     * @param receiver receiver participant
     * @return list of N created random amounted payments, N = {@link PaymentService#countOfPayments}
     */
    private List<PaymentEntity> createPayments(ParticipantEntity sender, ParticipantEntity receiver) {
        List<PaymentEntity> payments = new ArrayList<>();
        IntStream.rangeClosed(0, countOfPayments / 2).forEach(i -> {
            PaymentEntity payment = new PaymentEntity();
            payment.setSender(sender);
            payment.setReceiver(receiver);
            payment.setAmount(BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(1000)));
            payments.add(payment);
        });
        return payments;
    }

    @Override
    public List<PaymentEntity> findAll() {
        final List<PaymentEntity> payments = new ArrayList<>();
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            payments.addAll(paymentRepository.findAll());
        }
        return payments;
    }

    @Override
    public Optional<PaymentEntity> findById(Long id) {
        Optional<PaymentEntity> payment = Optional.empty();
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            payment = paymentRepository.findById(id);
        }
        return payment;
    }

    @Override
    public void deleteById(Long id) {
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            paymentRepository.deleteById(id);
        }
    }

}
