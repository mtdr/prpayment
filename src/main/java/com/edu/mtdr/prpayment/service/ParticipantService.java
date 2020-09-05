package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.config.datasource.DbContextHolder;
import com.edu.mtdr.prpayment.config.datasource.DbTypeEnum;
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

    @Override
    public ParticipantEntity save(ParticipantEntity participant) {
        DbContextHolder.setCurrentDb(DbTypeEnum.SHARD1);
        return participantRepository.save(participant);
    }
}
