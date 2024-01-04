package com.tochie.Traskit.service;

import com.tochie.Traskit.dto.EditTaskDTO;
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
import com.tochie.Traskit.utils.GeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class TaskService {


    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskDetailsRepository taskDetailsRepository;

    @Autowired
    GeneratorUtil generatorUtil;

    public BaseResponse createTask(TaskCreationDTO taskCreationDTO, UserDetails userDetails){

        BaseResponse response = new ResponseData();

        try {
            createNewTask(taskCreationDTO, (User) userDetails);
        } catch (Exception e){
            log.warn("Error creating task : {}", e.getMessage());
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
        task.setTaskReference(generatorUtil.generateUniqueString());

        TaskDetails taskDetails = new TaskDetails();
        taskDetails.setTaskFk(task);
        taskDetails.setTaskContent(taskCreationDTO.getTaskContent());
        taskDetails.setTitle(taskCreationDTO.getTaskName());

        taskRepository.save(task);
        taskDetailsRepository.save(taskDetails);
    }


    public BaseResponse editTask(EditTaskDTO editTaskDTO, UserDetails userDetails){
        BaseResponse response = new ResponseData();

        // validate valid taskReference
        Task task = taskRepository.getTaskByReference(editTaskDTO.getTaskReference());

        if (task == null){
           response.assignResponseCode(ResponseCodeEnum.NO_RECORDS_FOUND.getCode(), "No Tasks found with provided task reference");
           return response;
        }

        // update db with new details
        task.setLastModified(new Date());

        TaskDetails taskDetails = taskDetailsRepository.getTaskDetailsByTaskFk(task.getId());
        if(StringUtils.isNotEmpty(editTaskDTO.getTaskName())) {taskDetails.setTitle(editTaskDTO.getTaskName());}
        if(StringUtils.isNotEmpty(editTaskDTO.getTaskContent())) {taskDetails.setTaskContent(editTaskDTO.getTaskContent());}

        taskDetails.setLastModified(new Date());
        taskRepository.save(task);
        taskDetailsRepository.save(taskDetails);

        response.assignResponseCode(ResponseCodeEnum.SUCCESS.getCode(), editTaskDTO.getTaskName() +" edited successfully");

        return response;
    }


}
