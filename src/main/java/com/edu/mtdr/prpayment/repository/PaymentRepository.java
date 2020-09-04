package com.edu.mtdr.prpayment.repository;

import com.edu.mtdr.prpayment.schema.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
}
