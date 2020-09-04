package com.edu.mtdr.prpayment.repository;

import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Participants repository
 */
@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {
}
