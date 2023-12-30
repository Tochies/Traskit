package com.tochie.Traskit.service;

import com.tochie.Traskit.dto.TaskCreationDTO;
import com.tochie.Traskit.dto.apiresponse.BaseResponse;
import com.tochie.Traskit.dto.apiresponse.ResponseData;
import com.tochie.Traskit.enums.ResponseCodeEnum;
import com.tochie.Traskit.enums.ScheduleType;
import com.tochie.Traskit.enums.TaskType;
import com.tochie.Traskit.model.Task;
import com.tochie.Traskit.model.TaskDetails;
import com.tochie.Traskit.model.User;
import com.tochie.Traskit.repository.TaskDetailsRepository;
import com.tochie.Traskit.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskService {


    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskDetailsRepository taskDetailsRepository;

    public BaseResponse createTask(TaskCreationDTO taskCreationDTO, UserDetails userDetails){

        BaseResponse response = new ResponseData();

        try {
            createNewTask(taskCreationDTO, (User) userDetails);
        } catch (Exception e){
            response.assignResponseCode(ResponseCodeEnum.PROCESSING_ERROR);

            return response;
        }

        response.assignResponseCode(ResponseCodeEnum.SUCCESS.getCode(), "Task created successfully");

        return response;
    }

    private void createNewTask(TaskCreationDTO taskCreationDTO, User userDetails) {

        Task task = new Task();
        if (taskCreationDTO.getTaskType() != null){
            task.setTaskType(EnumUtils.getEnum(TaskType.class, taskCreationDTO.getTaskType()));
        }
        task.setOwner(userDetails);
        if (taskCreationDTO.getTaskScheduleType() != null){
            task.setScheduleType(EnumUtils.getEnum(ScheduleType.class, taskCreationDTO.getTaskScheduleType()));
        }

        TaskDetails taskDetails = new TaskDetails();
        taskDetails.setTaskFk(task);
        taskDetails.setTaskContent(taskCreationDTO.getTaskContent());
        taskDetails.setTitle(taskCreationDTO.getTaskName());

        taskRepository.save(task);
        taskDetailsRepository.save(taskDetails);
    }


}
