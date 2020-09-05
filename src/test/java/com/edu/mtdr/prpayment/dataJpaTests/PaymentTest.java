package com.edu.mtdr.prpayment.dataJpaTests;

import com.edu.mtdr.prpayment.repository.ParticipantRepository;
import com.edu.mtdr.prpayment.repository.PaymentRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PaymentTest {
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateParticipant() {
        ParticipantEntity aParticipant = new ParticipantEntity();
        aParticipant.setName("A");
        ParticipantEntity bParticipant = new ParticipantEntity();
        bParticipant.setName("B");

        this.entityManager.persist(aParticipant);
        this.entityManager.persist(bParticipant);

        PaymentEntity payment = new PaymentEntity();
        payment.setSender(aParticipant);
        payment.setReceiver(bParticipant);
        payment.setAmount(BigDecimal.valueOf(100));
        this.entityManager.persist(payment);

        assertThat(payment.getAmount()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(payment.getSender()).isEqualTo(aParticipant);
        assertThat(payment.getReceiver()).isEqualTo(bParticipant);
    }


}
