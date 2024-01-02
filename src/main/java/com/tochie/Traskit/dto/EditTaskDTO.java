package com.tochie.Traskit.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditTaskDTO {


    @NotEmpty(message = "Please input name for your task")
    private String taskReference;

    @NotEmpty(message = "Please input name for your task")
    private String taskName;

    private String taskContent;
}
