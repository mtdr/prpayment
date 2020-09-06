package com.edu.mtdr.prpayment.payments;

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
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test based on using main database cluster
 * IMPORTANT! Working on main db cluster
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(name = "application-test")
@SpringBootTest
@ActiveProfiles("test")
public class PaymentServiceTest {
    @Autowired
    private IPaymentService paymentService;
    @Autowired
    private IParticipantService participantService;

    @Test
    public void shouldGetParticipantsAndCreatePayments_checkSum() {
        ParticipantEntity aParticipant = participantService.getOrCreate("a");
        ParticipantEntity bParticipant = participantService.getOrCreate("b");

        assertNotNull(aParticipant);
        assertNotNull(bParticipant);
        BigDecimal startSum = paymentService.sumAmountsBySender(aParticipant.getId());
        assertNotNull(startSum);
        PaymentEntity payment = new PaymentEntity();
        payment.setSender(aParticipant);
        payment.setReceiver(bParticipant);
        payment.setAmount(BigDecimal.TEN);
        paymentService.save(payment);

        BigDecimal finishSum = paymentService.sumAmountsBySender(aParticipant.getId());
        assertNotNull(finishSum);
        assertThat(startSum.add(BigDecimal.TEN).setScale(0, RoundingMode.DOWN))
                .isEqualTo(finishSum.setScale(0, RoundingMode.DOWN));
    }

}
