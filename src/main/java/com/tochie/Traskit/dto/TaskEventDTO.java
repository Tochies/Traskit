package com.tochie.Traskit.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tochie.Traskit.enums.TaskType;
import com.tochie.Traskit.exception.constraints.EnumValidator;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskEventDTO {

    @NotEmpty(message = "Please input a valid task reference")
    private String taskReference;

    private String taskEventDescription;

    @NotEmpty
    @EnumValidator(enumClass = TaskType.class, message = "Please enter a valid task type")
    private String taskType;

    private Boolean taskEventBoolean;


}
