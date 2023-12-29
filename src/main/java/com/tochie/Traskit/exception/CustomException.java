package com.tochie.Traskit.exception;


import lombok.Data;

import java.io.Serial;

@Data
public class CustomException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private int code;
    private String description;
    private String errorCategory;

    public CustomException(int code, String description){
        super(description);
        this.code = code;
        this.description = description;
    }


}
