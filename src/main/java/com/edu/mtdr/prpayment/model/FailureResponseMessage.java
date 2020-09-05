package com.edu.mtdr.prpayment.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

/**
 * Failure response message
 * @param <D> message data
 */
@ApiModel(description = "Failure response message")
public class FailureResponseMessage<D> extends BaseResponseMessage<D> {
    @ApiModelProperty("Optional fail reason")
    private String reason;

    public FailureResponseMessage(int statusCode, boolean isSuccess, String reason) {
        super(statusCode, isSuccess);
        this.reason = reason;
    }

    public FailureResponseMessage() {
        super(HttpStatus.BAD_REQUEST.value(), false);
    }

    public FailureResponseMessage(int statusCode, boolean result, D data, String reason) {
        super(statusCode, result);
        this.data = data;
        this.reason = reason;
    }

    public FailureResponseMessage(D data, String reason) {
        this(HttpStatus.OK.value(), true, data, reason);
    }

    public FailureResponseMessage(String reason) {
        this();
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
