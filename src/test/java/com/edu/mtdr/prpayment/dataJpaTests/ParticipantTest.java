package com.edu.mtdr.prpayment.dataJpaTests;

import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.service.IParticipantService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ParticipantTest {
    @Autowired
    private IParticipantService participantService;

    @Test
    public void shouldCreateParticipantAndCheck() {
        String name = "test2";
        ParticipantEntity participantEntity = new ParticipantEntity();
        participantEntity.setName(name);
        participantService.save(participantEntity);

        participantEntity = participantService.findFirstByName(name).orElseThrow();
        assertThat(participantEntity.getName()).isEqualTo(name);
        participantService.deleteById(participantEntity.getId());
    }


}
