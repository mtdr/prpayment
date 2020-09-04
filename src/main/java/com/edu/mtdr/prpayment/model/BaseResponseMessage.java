package com.edu.mtdr.prpayment.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Base class for REST-response messages
 * @param <D> message data
 */
@ApiModel(description = "Base JSON response message")
public abstract class BaseResponseMessage<D> {
    @ApiModelProperty("Http result code")
    private final int statusCode;
    @ApiModelProperty("Flag of operation success")
    private final boolean isSuccess;
    @ApiModelProperty(value = "Result data")
    protected D data;

    public BaseResponseMessage(int statusCode, boolean isSuccess) {
        this.statusCode = statusCode;
        this.isSuccess = isSuccess;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public D getData() {
        return data;
    }
}
