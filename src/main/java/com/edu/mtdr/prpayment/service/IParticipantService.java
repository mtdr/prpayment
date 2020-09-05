package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * Participant service
 */
public interface IParticipantService {
    /**
     * @param participant participant to save
     * @return saved entity
     */
    ParticipantEntity save(ParticipantEntity participant);
}
