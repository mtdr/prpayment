package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.dao.ParticipantDao;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Participant service implementation
 */
@Service
public class ParticipantService implements IParticipantService {
    private final IGeneratorIdService generatorIdService;
    private final ParticipantDao participantDao;

    public ParticipantService(ParticipantDao participantDao, IGeneratorIdService generatorIdService) {
        this.participantDao = participantDao;
        this.generatorIdService = generatorIdService;
    }

    @Override
    public ParticipantEntity save(ParticipantEntity participant) {
        participant.setId(generatorIdService.generateId());
        return participantDao.save(participant);
    }

    @Override
    public List<ParticipantEntity> findAll() {
        return participantDao.findAll();
    }

    @Override
    public Optional<ParticipantEntity> findById(Long id) {
        return participantDao.findById(id);
    }

    @Override
    public Optional<ParticipantEntity> findFirstByName(String name) {
        return participantDao.findFirstByName(name);
    }

    @Override
    public void deleteById(Long id) {
        participantDao.deleteById(id);
    }

    @Override
    public ParticipantEntity createWithName(String name) {
        ParticipantEntity participant = new ParticipantEntity();
        participant.setName(name);
        return save(participant);
    }

    @Override
    public ParticipantEntity getOrCreate(String name) {
        return findFirstByName(name).orElseGet(() -> createWithName(name));
    }
}
