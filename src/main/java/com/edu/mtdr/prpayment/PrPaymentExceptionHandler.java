package com.edu.mtdr.prpayment;

import com.edu.mtdr.prpayment.model.FailureResponseMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class PrPaymentExceptionHandler {

    /**
     * Handling of exceptions
     *
     * @param e {@link Exception} arguments exception
     * @return message with error
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public FailureResponseMessage handleArgumentException(Exception e) {
        return new FailureResponseMessage(400, false, e.getMessage());
    }

}
