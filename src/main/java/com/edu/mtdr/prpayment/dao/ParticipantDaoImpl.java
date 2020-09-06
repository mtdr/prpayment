package com.edu.mtdr.prpayment.dao;

import com.edu.mtdr.prpayment.config.datasource.DbContextHolder;
import com.edu.mtdr.prpayment.config.datasource.DbTypeEnum;
import com.edu.mtdr.prpayment.repository.ParticipantRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParticipantDaoImpl implements ParticipantDao {
    private final Logger LOGGER = LoggerFactory.getLogger(ParticipantDaoImpl.class);

    private final ParticipantRepository participantRepository;

    public ParticipantDaoImpl(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
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
    public ParticipantEntity save(ParticipantEntity participant) {
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            participantRepository.save(participant);
        }
        return participant;
    }

    @Override
    public void deleteById(Long id) {
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            participantRepository.deleteById(id);
        }
    }

    @Override
    public List<ParticipantEntity> findAll() {
        DbContextHolder.setCurrentDb(DbTypeEnum.values()[0]);
        List<ParticipantEntity> participants = participantRepository.findAll();
        for (int i = 1; i < DbTypeEnum.values().length; i++) {
            DbContextHolder.setCurrentDb(DbTypeEnum.values()[i]);
            List<ParticipantEntity> shardParticipants = participantRepository.findAll();
            if (shardParticipants.size() != participants.size()) {
                LOGGER.warn("Detected loss of synchronization in participants. "
                        + DbTypeEnum.values()[i].name() + " has " + shardParticipants.size()
                        + " while " + DbTypeEnum.values()[0] + " has " + participants.size());
            }
        }
        return participants;
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
}
