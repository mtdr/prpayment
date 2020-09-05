package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * Participant service
 */
public interface IParticipantService {
    ParticipantEntity save1(ParticipantEntity participant);
    ParticipantEntity save2(ParticipantEntity participant);

    @Transactional(transactionManager = "DS3TransactionManager")
    ParticipantEntity save3(ParticipantEntity participant);
}
