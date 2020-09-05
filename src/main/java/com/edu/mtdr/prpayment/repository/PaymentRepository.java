package com.edu.mtdr.prpayment.repository;

import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * Payment repository
 */
@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    /**
     * @param sender participant, who sent payment
     * @return sum of payments by this participant
     */
    @Query("SELECT SUM(p.amount) FROM PaymentEntity p WHERE p.sender = :sender")
    BigDecimal getSumBySender(@Param("sender") ParticipantEntity sender);

}
