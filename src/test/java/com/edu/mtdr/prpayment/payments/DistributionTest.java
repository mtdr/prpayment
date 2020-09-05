package com.edu.mtdr.prpayment.payments;

import com.edu.mtdr.prpayment.repository.ParticipantRepository;
import com.edu.mtdr.prpayment.repository.PaymentRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
import com.edu.mtdr.prpayment.service.IParticipantService;
import com.edu.mtdr.prpayment.service.IPaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(name = "test-db")
@SpringBootTest
public class DistributionTest {
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private IPaymentService paymentService;

    @Test
    public void simpleTest() {
        ParticipantEntity aParticipant = new ParticipantEntity();
        aParticipant.setName("A");
        ParticipantEntity bParticipant = new ParticipantEntity();
        bParticipant.setName("B");

        participantRepository.saveAll(Arrays.asList(aParticipant, bParticipant));
        ParticipantEntity savedAParticipant = participantRepository.findFirstByName(aParticipant.getName()).orElse(null);
        ParticipantEntity savedBParticipant = participantRepository.findFirstByName(bParticipant.getName()).orElse(null);

        assertNotNull(savedAParticipant);
        assertNotNull(savedBParticipant);

        IntStream.rangeClosed(0, (int) Math.pow(10, 4)).forEach(i -> {
            PaymentEntity payment = new PaymentEntity();
            payment.setSender(savedAParticipant);
            payment.setReceiver(savedBParticipant);
            payment.setAmount(BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(1000)));
            paymentService.save(payment);
        });

        IntStream.rangeClosed(0, (int) Math.pow(10, 4)).forEach(i -> {
            PaymentEntity payment = new PaymentEntity();
            payment.setSender(savedBParticipant);
            payment.setReceiver(savedAParticipant);
            payment.setAmount(BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(1000)));
            paymentService.save(payment);
        });

        System.out.println(paymentRepository.countAllByShardNum(0));
        System.out.println(paymentRepository.countAllByShardNum(1));
        System.out.println(paymentRepository.countAllByShardNum(2));

        System.out.println(paymentRepository.getSumBySenderId(savedAParticipant.getId()));
        System.out.println(paymentRepository.getSumBySenderId(savedBParticipant.getId()));
    }

}
