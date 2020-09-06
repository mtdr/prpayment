package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.config.datasource.DbContextHolder;
import com.edu.mtdr.prpayment.config.datasource.DbTypeEnum;
import com.edu.mtdr.prpayment.repository.PaymentRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
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
    private final IGeneratorIdService generatorIdService;
    private final IShardManagerService shardManagerService;

    @Value("${generate.payments.count}")
    private Integer countOfPayments;


    public PaymentService(PaymentRepository paymentRepository,
                          IParticipantService participantService,
                          IGeneratorIdService generatorIdService,
                          IShardManagerService shardManagerService) {
        this.paymentRepository = paymentRepository;
        this.participantService = participantService;
        this.generatorIdService = generatorIdService;
        this.shardManagerService = shardManagerService;
    }

    @Override
    public PaymentEntity save(PaymentEntity payment) {
        if (payment != null) {
            processPayment(payment);
            switch (payment.getShardNum()) {
                case 1:
                    DbContextHolder.setCurrentDb(DbTypeEnum.SHARD1);
                    break;
                case 2:
                    DbContextHolder.setCurrentDb(DbTypeEnum.SHARD2);
                    break;
                case 3:
                    DbContextHolder.setCurrentDb(DbTypeEnum.SHARD3);
                    break;
                default:
                    break;
            }
            return paymentRepository.save(payment);
        } else {
            return null;
        }
    }

    @Override
    public Boolean saveAll(List<PaymentEntity> payments) {
        if (payments != null && payments.size() > 0) {
            for (PaymentEntity payment : payments) {
                processParticipant(payment.getSender()).ifPresent(payment::setSender);
                processParticipant(payment.getReceiver()).ifPresent(payment::setReceiver);
                if (payment.getSender() == null || payment.getReceiver() == null) {
                    continue;
                }
                processPayment(payment);
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

    /**
     * Set id, shard number and date if necessary
     *
     * @param payment payment to process
     */
    private void processPayment(PaymentEntity payment) {
        payment.setId(generatorIdService.generateId());
        payment.setShardNum(shardManagerService.getShardNum(payment));
        if (payment.getDate() == null) {
            payment.setDate(new Date());
        }
    }

    /**
     * @param participant participant to process
     * @return optional with found in db participant
     */
    private Optional<ParticipantEntity> processParticipant(ParticipantEntity participant) {
        if (participant == null) {
            return Optional.empty();
        } else if (participant.getId() != 0) {
            return participantService.findById(participant.getId());
        } else if (participant.getName() != null) {
            return participantService.findFirstByName(participant.getName());
        } else {
            return Optional.empty();
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
