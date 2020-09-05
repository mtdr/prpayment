package com.edu.mtdr.prpayment.controller;

import com.edu.mtdr.prpayment.model.BaseResponseMessage;
import com.edu.mtdr.prpayment.model.SuccessResponseMessage;
import com.edu.mtdr.prpayment.repository.ParticipantRepository;
import com.edu.mtdr.prpayment.repository.PaymentRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
import com.edu.mtdr.prpayment.service.IParticipantService;
import com.edu.mtdr.prpayment.service.IPaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Payments controller
 */
@RestController
@RequestMapping("/api/payments")
@Api(tags = {"payments"}, value = "Payments controller")
public class PaymentController {
    private final IPaymentService paymentService;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentController(IPaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/list")
    @ApiOperation("List payments")
    public BaseResponseMessage<?> listPayments() {
        final List<PaymentEntity> dbPayments = new ArrayList<>(paymentRepository.findAll());
        return new SuccessResponseMessage<>(dbPayments);
    }

    @GetMapping("/get/{id}")
    @ApiOperation("Get payment by id")
    public BaseResponseMessage<PaymentEntity> getPayment(@PathVariable("id") long id) {
        final PaymentEntity dbPayment = paymentRepository.findById(id).orElse(null);
        return new SuccessResponseMessage<>(dbPayment);
    }

    @PostMapping("/save")
    @ApiOperation("Create or update payment")
    public BaseResponseMessage<?> savePayment(@RequestBody PaymentEntity payment) {
        return new SuccessResponseMessage<>(paymentService.save(payment));
    }

    @PostMapping("/delete")
    @ApiOperation("Delete payment")
    public BaseResponseMessage<?> deletePayment(@RequestBody PaymentEntity payment) {
        paymentRepository.deleteById(payment.getId());
        return new SuccessResponseMessage<>();
    }

    @PostMapping("/sum")
    @ApiOperation("Get sum of payments, where specified participant id is sender")
    public BaseResponseMessage<?> deletePayment(@RequestBody Long senderId) {
        BigDecimal sum = Optional.ofNullable(paymentService.sumAmountsBySender(senderId)).orElse(BigDecimal.ZERO);
        return new SuccessResponseMessage<>(sum);
    }
}
