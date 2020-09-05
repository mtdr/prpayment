package com.edu.mtdr.prpayment.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Model for request messages")
public class RequestMessage<D> {
    @ApiModelProperty(value = "Request data")
    protected D data;

    public RequestMessage() {
    }

    public RequestMessage(D data) {
        this.data = data;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
