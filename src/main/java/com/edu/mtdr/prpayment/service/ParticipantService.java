package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.config.datasource.DbContextHolder;
import com.edu.mtdr.prpayment.config.datasource.DbTypeEnum;
import com.edu.mtdr.prpayment.repository.ParticipantRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        participant.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            participantRepository.save(participant);
        }
        return participant;
    }

    @Override
    public List<ParticipantEntity> findAll() {
        return participantRepository.findAll();
    }

    @Override
    public Optional<ParticipantEntity> findById(Long id) {
        return participantRepository.findById(id);
    }

    @Override
    public Optional<ParticipantEntity> findFirstByName(String name) {
        return participantRepository.findFirstByName(name);
    }

    @Override
    public void deleteById(Long id) {
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            participantRepository.deleteById(id);
        }
    }

    @Override
    public ParticipantEntity createWithName(String name) {
        ParticipantEntity participant = new ParticipantEntity();
        participant.setName(name);
        return participantRepository.save(participant);
    }

    @Override
    public ParticipantEntity getOrCreate(String name) {
        return participantRepository.findFirstByName(name).orElseGet(() -> createWithName(name));
    }
}
