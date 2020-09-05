package com.edu.mtdr.prpayment;

import com.edu.mtdr.prpayment.model.FailureResponseMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class PrPaymentExceptionHandler {

    /**
     * Handling of constraints exceptions
     *
     * @param e {@link DataIntegrityViolationException} constraint fail exception
     * @return message with error
     */
    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public FailureResponseMessage<?> handleArgumentException(DataIntegrityViolationException e) {
        return new FailureResponseMessage<>(400, false, e.getMessage());
    }


    /**
     * Handling of all unchecked exceptions
     *
     * @param e {@link Exception} all exceptions
     * @return message with error
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public FailureResponseMessage<?> handleArgumentException(Exception e) {
        return new FailureResponseMessage<>(400, false, e.getMessage());
    }
}
