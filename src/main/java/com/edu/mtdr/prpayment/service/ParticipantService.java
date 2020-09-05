package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.repository.ParticipantRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Participant service implementation
 */
@Service
public class ParticipantService implements IParticipantService {
    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

//    @Transactional(transactionManager = "transactionManager")
    public ParticipantEntity save1(ParticipantEntity participant) {
        return participantRepository.save(participant);
    }

//    @Qualifier("DS2TransactionManager")
//    @Transactional(transactionManager = "DS2TransactionManager")
    public ParticipantEntity save2(ParticipantEntity participant) {
        return participantRepository.save(participant);
    }

//    @Transactional(transactionManager = "DS3TransactionManager")
    public ParticipantEntity save3(ParticipantEntity participant) {
        return participantRepository.save(participant);
    }

    @Override
    public ParticipantEntity save(ParticipantEntity participant) {
        return save2(participant);
    }
}
