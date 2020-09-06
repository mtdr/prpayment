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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(name = "test-db")
@SpringBootTest
@ActiveProfiles("test")
public class DistributionTest {
    @Autowired
    private IPaymentService paymentService;
    @Autowired
    private IParticipantService participantService;



    @Test
    public void simpleTest() {
        ParticipantEntity aParticipant = new ParticipantEntity();
        aParticipant.setName("A");
        ParticipantEntity bParticipant = new ParticipantEntity();
        bParticipant.setName("B");

        participantService.save(aParticipant);
        participantService.save(bParticipant);
        ParticipantEntity savedAParticipant = participantService.findFirstByName(aParticipant.getName()).orElse(null);
        ParticipantEntity savedBParticipant = participantService.findFirstByName(bParticipant.getName()).orElse(null);

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

        System.out.println(paymentService.countAllByShardNum(1));
        System.out.println(paymentService.countAllByShardNum(2));
        System.out.println(paymentService.countAllByShardNum(3));

        System.out.println(paymentService.sumAmountsBySender(savedAParticipant.getId()));
        System.out.println(paymentService.sumAmountsBySender(savedBParticipant.getId()));
    }

}
