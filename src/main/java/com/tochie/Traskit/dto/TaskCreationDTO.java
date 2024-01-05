package com.tochie.Traskit.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tochie.Traskit.enums.ScheduleFrequency;
import com.tochie.Traskit.enums.ScheduleType;
import com.tochie.Traskit.enums.TaskType;
import com.tochie.Traskit.exception.constraints.EnumValidator;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskCreationDTO {

    @NotEmpty(message = "Please input name for your task")
    private String taskName;

    private String taskContent;

    @EnumValidator(enumClass = TaskType.class, message = "Please enter a valid task type")
    private String taskType;

    @EnumValidator(enumClass = ScheduleType.class, message = "Please enter a valid task schedule type")
    private String taskScheduleType;

    @EnumValidator(enumClass = ScheduleFrequency.class, message = "Please enter a valid task schedule frequency")
    private String scheduleFrequency;


}
