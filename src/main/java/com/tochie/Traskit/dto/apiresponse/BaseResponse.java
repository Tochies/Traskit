package com.tochie.Traskit.dto.apiresponse;

import lombok.Data;

@Data
public abstract class BaseResponse {
    private Integer code;
    private String description;
}
