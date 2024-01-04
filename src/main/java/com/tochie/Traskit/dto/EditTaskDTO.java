package com.tochie.Traskit.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditTaskDTO {


    @NotEmpty(message = "Please input a valid task reference")
    private String taskReference;

    // @NotEmpty(message = "Please input name for your task")   // changing name should be optional as well
    private String taskName;

    private String taskContent;
}
