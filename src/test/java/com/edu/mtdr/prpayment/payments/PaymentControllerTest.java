package com.edu.mtdr.prpayment.payments;

import com.edu.mtdr.prpayment.controller.PaymentController;
import com.edu.mtdr.prpayment.model.RequestMessage;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.schema.PaymentEntity;
import com.edu.mtdr.prpayment.service.IParticipantService;
import com.edu.mtdr.prpayment.service.IPaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(com.edu.mtdr.prpayment.controller.PaymentController.class)
public class PaymentControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private IParticipantService participantService;
    @MockBean
    private IPaymentService paymentService;

    @Test
    public void shouldCheckSumAddPaymentAndValidateSum() throws Exception {
        ParticipantEntity aParticipant = new ParticipantEntity();
        aParticipant.setName("a");
        aParticipant.setId(1L);
        ParticipantEntity bParticipant = new ParticipantEntity();
        bParticipant.setName("b");
        bParticipant.setId(2L);

        RequestMessage<String> reqSumA = new RequestMessage<>("a");
        given(participantService.findFirstByName("a")).willReturn(Optional.of(aParticipant));
        given(paymentService.sumAmountsBySender(aParticipant.getId())).willReturn(BigDecimal.ONE);
        MvcResult startSumAResult = this.mvc.perform(post("/api/payments/sum")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(reqSumA)))
                .andReturn();

        BigDecimal startSumA = BigDecimal.valueOf(Long.parseLong(
                startSumAResult.getResponse().getContentAsString().split(",")[1].split(":")[1]));

        RequestMessage<List<PaymentEntity>> uploadPaymentsMessage = new RequestMessage<>();
        PaymentEntity aToBPayment = new PaymentEntity();
        aToBPayment.setSender(aParticipant);
        aToBPayment.setReceiver(bParticipant);
        aToBPayment.setAmount(BigDecimal.TEN);
        given(paymentService.saveAll(uploadPaymentsMessage.getData())).willReturn(true);
        this.mvc.perform(post("/api/payments/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(uploadPaymentsMessage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("statusCode").value(200));

        given(paymentService.sumAmountsBySender(aParticipant.getId())).willReturn(BigDecimal.ONE.add(BigDecimal.TEN));
        this.mvc.perform(post("/api/payments/sum")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(reqSumA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("statusCode").value(200))
                .andExpect(jsonPath("data").value(String.valueOf(startSumA.add(BigDecimal.TEN))));
    }

    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
