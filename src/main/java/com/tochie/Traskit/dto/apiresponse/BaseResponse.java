package com.tochie.Traskit.dto.apiresponse;

import com.tochie.Traskit.enums.ResponseCodeEnum;
import lombok.Data;

@Data
public abstract class BaseResponse {
    private Integer code;
    private String description;

    public void assignResponseCode(ResponseCodeEnum responseCodeEnum) {
        setCode(responseCodeEnum.getCode());
        setDescription(responseCodeEnum.getDescription());
    }

    public void assignResponseCode(int code, String description) {
        setCode(code);
        setDescription(description);
    }
}
