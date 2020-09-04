package com.edu.mtdr.prpayment.repository;

import com.edu.mtdr.prpayment.schema.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Payment repository
 */
@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
