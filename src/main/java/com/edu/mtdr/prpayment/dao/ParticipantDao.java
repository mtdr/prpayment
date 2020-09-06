package com.edu.mtdr.prpayment.dao;

import com.edu.mtdr.prpayment.schema.ParticipantEntity;

import java.util.List;
import java.util.Optional;

public interface ParticipantDao {
    /**
     * @param name participant's name
     * @return participant by name in one shard
     */
    Optional<ParticipantEntity> findFirstByName(String name);

    /**
     * Save participant on each shard
     *
     * @param participant participant to save
     * @return saved entity
     */
    ParticipantEntity save(ParticipantEntity participant);

    /**
     * Delete participant from each shard
     *
     * @param id id of participant to delete
     */
    void deleteById(Long id);

    /**
     * @return list of one-shard participants
     */
    List<ParticipantEntity> findAll();

    /**
     * @param id participant's id
     * @return participant by id in one shard
     */
    Optional<ParticipantEntity> findById(Long id);
}
