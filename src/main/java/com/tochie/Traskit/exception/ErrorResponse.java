package com.tochie.Traskit.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tochie.Traskit.dto.apiresponse.BaseResponse;
import com.tochie.Traskit.enums.ResponseCodeEnum;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse extends BaseResponse {

    private String errorCategory;

    public static class ErrorResponseBuilder{

        private int code;
        private String description;
        private String errorCategory;

        private ErrorResponseBuilder(){}

        public static ErrorResponseBuilder anErrorResponse(){
            return new ErrorResponseBuilder();
        }


        public ErrorResponseBuilder withResponseCode(int code){
            this.code = code;
            return this;
        }

        public ErrorResponseBuilder withDescription(String description){
            this.description = description;
            return this;
        }

        public ErrorResponseBuilder withErrorCategory(String errorCategory){
            this.errorCategory = errorCategory;
            return this;
        }

        public ErrorResponse build(){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(code);
            errorResponse.setDescription(description);
            errorResponse.setErrorCategory(errorCategory);

            return errorResponse;
        }

        public ErrorResponse build(ResponseCodeEnum responseCodeEnum){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(responseCodeEnum.getCode());
            errorResponse.setDescription(responseCodeEnum.getDescription());
            errorResponse.setErrorCategory(errorCategory);

            return errorResponse;
        }

    }


}
