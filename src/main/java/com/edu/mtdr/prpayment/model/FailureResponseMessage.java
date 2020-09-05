package com.edu.mtdr.prpayment.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
        super(400, false);
    }

    public FailureResponseMessage(int statusCode, boolean result, D data, String reason) {
        super(statusCode, result);
        this.data = data;
        this.reason = reason;
    }

    public FailureResponseMessage(D data, String reason) {
        this(200, true, data, reason);
    }

    public String getReason() {
        return reason;
    }
}
