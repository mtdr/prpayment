package com.edu.mtdr.prpayment.dataJpaTests;

import com.edu.mtdr.prpayment.repository.ParticipantRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ParticipantTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    public void testCreateParticipant() {
        String name = "A";
        ParticipantEntity participantEntity = new ParticipantEntity();
        participantEntity.setName(name);
        this.entityManager.persist(participantEntity);

        participantEntity = participantRepository.findFirstByName(name).orElseThrow();
        assertThat(participantEntity.getName()).isEqualTo(name);
    }


}
