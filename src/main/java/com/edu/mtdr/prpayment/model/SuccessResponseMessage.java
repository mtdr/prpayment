package com.edu.mtdr.prpayment.model;

import io.swagger.annotations.ApiModel;

/**
 * Success response message
 * @param <D> message data
 */
@ApiModel(description = "Success response message")
public class SuccessResponseMessage<D> extends BaseResponseMessage<D> {

    public SuccessResponseMessage(int statusCode, boolean isSuccess) {
        super(statusCode, isSuccess);
    }

    public SuccessResponseMessage() {
        super(200, true);
    }

    public SuccessResponseMessage(int statusCode, boolean result, D data) {
        super(statusCode, result);
        this.data = data;
    }

    public SuccessResponseMessage(D data) {
        this(200, true, data);
    }
}
