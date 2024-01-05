package com.tochie.Traskit.service;

import com.tochie.Traskit.dto.EditTaskDTO;
import com.tochie.Traskit.dto.TaskCreationDTO;
import com.tochie.Traskit.dto.TaskEventDTO;
import com.tochie.Traskit.dto.apiresponse.BaseResponse;
import com.tochie.Traskit.dto.apiresponse.ResponseData;
import com.tochie.Traskit.enums.ResponseCodeEnum;
import com.tochie.Traskit.enums.ScheduleFrequency;
import com.tochie.Traskit.enums.ScheduleType;
import com.tochie.Traskit.enums.TaskType;
import com.tochie.Traskit.model.*;
import com.tochie.Traskit.repository.TaskDetailsRepository;
import com.tochie.Traskit.repository.TaskEventRepository;
import com.tochie.Traskit.repository.TaskRepository;
import com.tochie.Traskit.repository.TaskScheduleRepository;
import com.tochie.Traskit.utils.GeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
public class TaskService {


    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskDetailsRepository taskDetailsRepository;

    @Autowired
    TaskScheduleRepository taskScheduleRepository;

    @Autowired
    TaskEventRepository taskEventRepository;

    @Autowired
    GeneratorUtil generatorUtil;

    public BaseResponse createTask(TaskCreationDTO taskCreationDTO, UserDetails userDetails){

        BaseResponse response = new ResponseData();

        if(taskCreationDTO.getScheduleFrequency() != null &&
                (taskCreationDTO.getTaskScheduleType() == null || taskCreationDTO.getTaskScheduleType().equals(ScheduleType.ONE_OFF.name())) ){
            response.assignResponseCode(ResponseCodeEnum.VALIDATION_ERROR.getCode(), "A valid Task schedule is required for task with schedule frequency");
            return response;
        }

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

        if(taskCreationDTO.getScheduleFrequency() != null){
            TaskSchedule taskSchedule = new TaskSchedule();

            taskSchedule.setTaskFk(task);
            taskSchedule.setScheduleFrequency(EnumUtils.getEnum(ScheduleFrequency.class, taskCreationDTO.getScheduleFrequency()));

            if((ScheduleFrequency.RANDOM.name().equals(taskCreationDTO.getScheduleFrequency()))){
                taskSchedule.setContinuousRun(Boolean.TRUE);
            } else {
                taskSchedule.setRecurring(Boolean.TRUE);
            }

            taskScheduleRepository.save(taskSchedule);
        }
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


    public BaseResponse createTaskEvent(TaskEventDTO taskEventDTO, UserDetails userDetails){
        // TODO : Validate if only the users who created a task can be able to create task events or make this a setting

        BaseResponse response = new ResponseData();

        // validate valid taskReference
        Task task = taskRepository.getTaskByReference(taskEventDTO.getTaskReference());

        if (task == null){
            response.assignResponseCode(ResponseCodeEnum.NO_RECORDS_FOUND.getCode(), "No Tasks found with provided task reference");
            return response;
        }

        if(!StringUtils.equalsIgnoreCase(taskEventDTO.getTaskType(), task.getTaskType().name())){
            response.assignResponseCode(ResponseCodeEnum.NO_RECORDS_FOUND.getCode(), "Could not find a matching task category type for provided reference");
            return response;
        }

        if(taskEventDTO.getTaskEventBoolean() != null && TaskType.COUNTER_BOOLEAN != task.getTaskType() ){
            response.assignResponseCode(ResponseCodeEnum.VALIDATION_ERROR.getCode(), "Invalid Task type for task with boolean event");
            return response;
        }

        try {
            createNewTaskEvent(taskEventDTO, (User) userDetails, task);
        } catch (Exception e){
            log.warn("Error creating task event : {}", e.getMessage());
            response.assignResponseCode(ResponseCodeEnum.PROCESSING_ERROR);

            return response;
        }

        response.assignResponseCode(ResponseCodeEnum.SUCCESS.getCode(), "Task event created successfully");

        return response;

    }

    private void createNewTaskEvent(TaskEventDTO taskEventDTO, User userDetails, Task task) {
        TaskEvents taskEvents = new TaskEvents();
        taskEvents.setTaskFk(task);
        taskEvents.setCreatedBy(userDetails) ;
        taskEvents.setTaskEventDescription(taskEventDTO.getTaskEventDescription());
        taskEvents.setTaskEventBoolean(taskEventDTO.getTaskEventBoolean());
        taskEvents.setTaskEventsCounter(taskEventRepository.getTaskEventCount(task.getId()) + 1);


        TaskSchedule taskSchedule = taskScheduleRepository.getTaskScheduleByTaskFk(task.getId());

        if(taskSchedule != null){

            if(taskSchedule.getFirstRun() == null){
                taskSchedule.setFirstRun(Timestamp.from(Instant.now()));
            }
            taskSchedule.setLastRun(Timestamp.from(Instant.now()));
            taskSchedule.setLastModified(new Date());

        } else {
            taskSchedule = new TaskSchedule();
            taskSchedule.setTaskFk(task);
            taskSchedule.setScheduleFrequency(ScheduleFrequency.RANDOM);
            taskSchedule.setContinuousRun(true);
            taskSchedule.setFirstRun(Timestamp.from(Instant.now()));
            taskSchedule.setLastRun(Timestamp.from(Instant.now()));
        }

        taskEventRepository.save(taskEvents);
        taskScheduleRepository.save(taskSchedule);
    }


}
