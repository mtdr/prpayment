package com.edu.mtdr.prpayment.dao;

import com.edu.mtdr.prpayment.config.datasource.DbContextHolder;
import com.edu.mtdr.prpayment.config.datasource.DbTypeEnum;
import com.edu.mtdr.prpayment.repository.PaymentRepository;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentDaoImpl implements PaymentDao {
    private final PaymentRepository paymentRepository;

    public PaymentDaoImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentEntity save(PaymentEntity payment) {
        if (payment != null) {
            switch (payment.getShardNum()) {
                case 1:
                    DbContextHolder.setCurrentDb(DbTypeEnum.SHARD1);
                    break;
                case 2:
                    DbContextHolder.setCurrentDb(DbTypeEnum.SHARD2);
                    break;
                case 3:
                    DbContextHolder.setCurrentDb(DbTypeEnum.SHARD3);
                    break;
                default:
                    break;
            }
            return paymentRepository.save(payment);
        } else {
            return null;
        }
    }

    @Override
    public Boolean saveAll(List<PaymentEntity> payments) {
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            paymentRepository.saveAll(payments.parallelStream()
                    .filter(p -> p.getShardNum() == dbType.ordinal() + 1)
                    .collect(Collectors.toList()));
        }
        return true;
    }

    @Override
    public List<PaymentEntity> findAll() {
        final List<PaymentEntity> payments = new ArrayList<>();
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            payments.addAll(paymentRepository.findAll());
        }
        return payments;
    }

    @Override
    public Optional<PaymentEntity> findById(Long id) {
        Optional<PaymentEntity> payment = Optional.empty();
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            payment = paymentRepository.findById(id);
        }
        return payment;
    }

    @Override
    public void deleteById(Long id) {
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            paymentRepository.deleteById(id);
        }
    }

    @Override
    public Long countAllByShardNum(int shardNum) {
        DbTypeEnum dbType = DbTypeEnum.values()[shardNum - 1];
        DbContextHolder.setCurrentDb(dbType);
        return paymentRepository.count();
    }

    @Override
    public BigDecimal sumAmountsBySender(Long senderId) {
        BigDecimal res = BigDecimal.ZERO;
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            BigDecimal temp = paymentRepository.getSumBySenderId(senderId);
            if (temp != null) {
                res = res.add(temp);
            }
        }
        return res;
    }

    @Override
    public BigDecimal sumAmountsBySenderAtOneShard(Long senderId, int shardNum) {
        DbTypeEnum dbType = DbTypeEnum.values()[shardNum - 1];
        DbContextHolder.setCurrentDb(dbType);
        return paymentRepository.getSumBySenderId(senderId);
    }
}
