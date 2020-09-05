package com.edu.mtdr.prpayment.service;

import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Participant service
 */
public interface IParticipantService {
    /**
     * Save participant on each shard
     *
     * @param participant participant to save
     * @return saved entity
     */
    ParticipantEntity save(ParticipantEntity participant);

    /**
     * @return all participants in one shard
     */
    List<ParticipantEntity> findAll();

    /**
     * @param id participant's id
     * @return participant by id in one shard
     */
    Optional<ParticipantEntity> findById(Long id);

    /**
     * @param name participant's name
     * @return participant by name in one shard
     */
    Optional<ParticipantEntity> findFirstByName(String name);

    /**
     * Delete participant from each shard
     *
     * @param id id of participant to delete
     */
    void deleteById(Long id);
}
