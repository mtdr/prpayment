package com.edu.mtdr.prpayment.model;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "")
public class SuccessResponseMessage<D> extends BaseResponseMessage<D> {

    public SuccessResponseMessage(int statusCode, boolean isSuccess) {
        super(statusCode, isSuccess);
    }

    public SuccessResponseMessage() {
        super(200, true);
    }
}
