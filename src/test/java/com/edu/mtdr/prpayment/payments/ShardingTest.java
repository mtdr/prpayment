package com.edu.mtdr.prpayment.payments;

import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.service.IParticipantService;
import com.edu.mtdr.prpayment.service.IPaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test based on using main database cluster
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(name = "test-db")
@SpringBootTest
@ActiveProfiles("test")
public class ShardingTest {
    @Autowired
    private IPaymentService paymentService;
    @Autowired
    private IParticipantService participantService;

    @Test
    public void shouldGetParticipantsAndCreatePayments_checkSum() {
        ParticipantEntity aParticipant = participantService.getOrCreate("A");
        ParticipantEntity bParticipant = participantService.getOrCreate("B");

        assertNotNull(aParticipant);
        assertNotNull(bParticipant);

        int countOfPayments = (int) Math.pow(10, 4);
        paymentService.createBatchOfRandomPayments(aParticipant, bParticipant, countOfPayments / 2);
        paymentService.createBatchOfRandomPayments(bParticipant, aParticipant, countOfPayments / 2);

        System.out.println(paymentService.countAllByShardNum(1));
        System.out.println(paymentService.countAllByShardNum(2));
        System.out.println(paymentService.countAllByShardNum(3));

        System.out.println(paymentService.sumAmountsBySender(aParticipant.getId()));
        System.out.println(paymentService.sumAmountsBySender(bParticipant.getId()));
    }

}
