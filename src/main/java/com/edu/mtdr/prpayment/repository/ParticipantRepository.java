package com.edu.mtdr.prpayment.repository;

import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Participants repository
 */
@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {
    /**
     * @param name participant name
     * @return firs found participant
     */
    Optional<ParticipantEntity> findFirstByName(String name);
}
