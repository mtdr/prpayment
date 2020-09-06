package com.edu.mtdr.prpayment.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of {@link IGeneratorIdService} based on random UUID on {@link Long#MAX_VALUE}
 */
@Service
public class GeneratorIdService implements IGeneratorIdService {
    @Override
    public Long generateId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
