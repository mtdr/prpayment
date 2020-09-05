package com.edu.mtdr.prpayment.controller;

import com.edu.mtdr.prpayment.config.datasource.DbContextHolder;
import com.edu.mtdr.prpayment.config.datasource.DbTypeEnum;
import com.edu.mtdr.prpayment.model.BaseResponseMessage;
import com.edu.mtdr.prpayment.model.FailureResponseMessage;
import com.edu.mtdr.prpayment.model.RequestMessage;
import com.edu.mtdr.prpayment.model.SuccessResponseMessage;
import com.edu.mtdr.prpayment.repository.ParticipantRepository;
import com.edu.mtdr.prpayment.repository.PaymentRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
import com.edu.mtdr.prpayment.service.IPaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


/**
 * Payments controller
 */
@RestController
@RequestMapping("/api/payments")
@Api(tags = {"payments"}, value = "Payments controller")
public class PaymentController {
    private final IPaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final ParticipantRepository participantRepository;

    @Autowired
    public PaymentController(IPaymentService paymentService,
                             PaymentRepository paymentRepository,
                             ParticipantRepository participantRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.participantRepository = participantRepository;
    }

    /**
     * @return {@link SuccessResponseMessage} with list of all {@link PaymentEntity}
     */
    @GetMapping("/list")
    @ApiOperation("List payments")
    public BaseResponseMessage<?> listPayments() {
        final List<ParticipantEntity> payments = new ArrayList<>();
        for (DbTypeEnum dbType : DbTypeEnum.values()) {
            DbContextHolder.setCurrentDb(dbType);
            payments.addAll(participantRepository.findAll());
        }
        return new SuccessResponseMessage<>(payments);
    }

    /**
     * @param id {@link PaymentEntity}'s id
     * @return {@link SuccessResponseMessage} with {@link PaymentEntity}
     */
    @GetMapping("/get/{id}")
    @ApiOperation("Get payment by id")
    public BaseResponseMessage<PaymentEntity> getPayment(@PathVariable("id") Long id) {
        final PaymentEntity dbPayment = paymentRepository.findById(id).orElse(null);
        return new SuccessResponseMessage<>(dbPayment);
    }

    /**
     * @param payment payment to save, without id and date
     * @return {@link SuccessResponseMessage} with saved {@link PaymentEntity}
     */
    @PostMapping("/save")
    @ApiOperation("Create or update payment")
    public BaseResponseMessage<?> savePayment(@RequestBody PaymentEntity payment) {
        return new SuccessResponseMessage<>(paymentService.save(payment));
    }

    /**
     * @param message {@link RequestMessage} with id of payment to delete
     * @return {@link SuccessResponseMessage} empty message if all ok, {@link FailureResponseMessage} if exception
     */
    @PostMapping("/delete")
    @ApiOperation("Delete payment")
    public BaseResponseMessage<?> deletePayment(@RequestBody RequestMessage<Long> message) {
        paymentRepository.deleteById(message.getData());
        return new SuccessResponseMessage<>();
    }

    /**
     * @param message {@link RequestMessage with participant name in {@link RequestMessage#getData()}}
     * @return success with result or failure if unchecked message
     */
    @PostMapping("/sum")
    @ApiOperation("Get sum of payments, where specified participant name is sender")
    public BaseResponseMessage<?> sumPayments(@RequestBody RequestMessage<String> message) {
        ParticipantEntity participant = participantRepository.findFirstByName(message.getData()).orElse(null);
        if (participant != null) {
            BigDecimal sum = paymentService.sumAmountsBySender(participant.getId());
            return new SuccessResponseMessage<>(sum);
        } else {
            return new FailureResponseMessage<>("Participant not found");
        }
    }

    /**
     * @return message with success, or fail if participants not found or some unchecked exception
     */
    @PostMapping("/generate")
    @ApiOperation("Generate payments for a and b participants")
    public BaseResponseMessage<?> generatePayments() {
        ParticipantEntity aParticipant = participantRepository.findFirstByName("a").orElse(null);
        ParticipantEntity bParticipant = participantRepository.findFirstByName("b").orElse(null);
        if (aParticipant == null || bParticipant == null) {
            return new FailureResponseMessage<>();
        }
        IntStream.rangeClosed(0, (int) Math.pow(10, 4)).forEach(i -> {
            PaymentEntity payment = new PaymentEntity();
            payment.setSender(aParticipant);
            payment.setReceiver(bParticipant);
            payment.setAmount(BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(1000)));
            paymentService.save(payment);
        });

        IntStream.rangeClosed(0, (int) Math.pow(10, 4)).forEach(i -> {
            PaymentEntity payment = new PaymentEntity();
            payment.setSender(bParticipant);
            payment.setReceiver(aParticipant);
            payment.setAmount(BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(1000)));
            paymentService.save(payment);
        });
        return new SuccessResponseMessage<>();
    }
}
