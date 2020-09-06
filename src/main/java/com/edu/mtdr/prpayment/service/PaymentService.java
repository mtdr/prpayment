package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.dao.PaymentDao;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Payment service implementation
 */
@Service
public class PaymentService implements IPaymentService {
    private final PaymentDao paymentDao;
    private final IParticipantService participantService;
    private final IGeneratorIdService generatorIdService;
    private final IShardManagerService shardManagerService;

    @Value("${generate.payments.count}")
    private Integer countOfPayments;


    public PaymentService(PaymentDao paymentDao,
                          IParticipantService participantService,
                          IGeneratorIdService generatorIdService,
                          IShardManagerService shardManagerService) {
        this.paymentDao = paymentDao;
        this.participantService = participantService;
        this.generatorIdService = generatorIdService;
        this.shardManagerService = shardManagerService;
    }

    @Override
    public PaymentEntity save(PaymentEntity payment) {
        processPayment(payment);
        return paymentDao.save(payment);
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
            paymentDao.saveAll(payments);
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
            payment.setDate(LocalDateTime.now());
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
        return paymentDao.sumAmountsBySender(senderId);
    }

    @Override
    public BigDecimal sumAmountsBySenderAtOneShard(Long senderId, int shardNum) {
        return paymentDao.sumAmountsBySenderAtOneShard(senderId, shardNum);
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
     * @param sender   sender participant
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
        return paymentDao.findAll();
    }

    @Override
    public Optional<PaymentEntity> findById(Long id) {
        return paymentDao.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        paymentDao.deleteById(id);
    }

    @Override
    public Long countAllByShardNum(int shardNum) {
        return paymentDao.countAllByShardNum(shardNum);
    }

}
