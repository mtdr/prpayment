package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.config.datasource.DbContextHolder;
import com.edu.mtdr.prpayment.config.datasource.DbTypeEnum;
import com.edu.mtdr.prpayment.repository.ParticipantRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Participant service implementation
 */
@Service
public class ParticipantService implements IParticipantService {
    private final Logger LOGGER = LoggerFactory.getLogger(ParticipantService.class);
    private final ParticipantRepository participantRepository;
    private final IGeneratorIdService generatorIdService;

    public ParticipantService(ParticipantRepository participantRepository,
                              IGeneratorIdService generatorIdService) {
        this.participantRepository = participantRepository;
        this.generatorIdService = generatorIdService;
    }

    @Override
    public ParticipantEntity save(ParticipantEntity participant) {
        participant.setId(generatorIdService.generateId());
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
        Set<ParticipantEntity> participantsAllShards = new HashSet<>();
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            participantRepository.findById(id).ifPresent(participantsAllShards::add);
        }
        if (participantsAllShards.size() > 1) {
            LOGGER.warn("Detected loss of synchronization in participants. By id = " + id + " found "
                    + participantsAllShards.size() + " participants");
        }
        return Optional.ofNullable(participantsAllShards.iterator().next());
    }

    @Override
    public Optional<ParticipantEntity> findFirstByName(String name) {
        Set<ParticipantEntity> participantsAllShards = new HashSet<>();
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            participantRepository.findFirstByName(name).ifPresent(participantsAllShards::add);
        }
        if (participantsAllShards.size() > 1) {
            LOGGER.warn("Detected loss of synchronization in participants. By name = \"" + name + "\" found "
                    + participantsAllShards.size() + " participants");
        }
        return Optional.ofNullable(participantsAllShards.iterator().hasNext()
                ? participantsAllShards.iterator().next()
                : null);
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
        return save(participant);
    }

    @Override
    public ParticipantEntity getOrCreate(String name) {
        return findFirstByName(name).orElseGet(() -> createWithName(name));
    }
}
