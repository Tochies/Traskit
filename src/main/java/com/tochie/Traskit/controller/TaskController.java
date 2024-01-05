package com.tochie.Traskit.controller;

import com.tochie.Traskit.dto.EditTaskDTO;
import com.tochie.Traskit.dto.TaskCreationDTO;
import com.tochie.Traskit.dto.TaskEventDTO;
import com.tochie.Traskit.dto.apiresponse.BaseResponse;
import com.tochie.Traskit.dto.apiresponse.ResponseData;
import com.tochie.Traskit.enums.ResponseCodeEnum;
import com.tochie.Traskit.service.TaskService;
import com.tochie.Traskit.service.UserAuth;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/task")
public class TaskController {


    @Autowired
    UserAuth userAuth;

    @Autowired
    TaskService taskService;


    @GetMapping("/admin/adminProfile")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> adminProfile() {

        ResponseEntity<BaseResponse> jwtSession = userAuth.validatedUserSession();
        if (jwtSession != null) return jwtSession;

        BaseResponse response = new ResponseData();
        response.setCode(ResponseCodeEnum.SUCCESS.getCode());
        response.setDescription("Welcome to Admin Profile, " + userAuth.userDetails.getUsername());
      return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create-task")
    @ResponseBody
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskCreationDTO taskCreationDTO) {

        ResponseEntity<BaseResponse> jwtSession = userAuth.validatedUserSession();
        if (jwtSession != null) return jwtSession;


        return new ResponseEntity<>(taskService.createTask(taskCreationDTO, userAuth.userDetails), HttpStatus.OK);
    }

    @PostMapping("/edit-task")
    @ResponseBody
    public ResponseEntity<?> editTask(@Valid @RequestBody EditTaskDTO editTaskDTO) {

        ResponseEntity<BaseResponse> jwtSession = userAuth.validatedUserSession();
        if (jwtSession != null) return jwtSession;


        return new ResponseEntity<>(taskService.editTask(editTaskDTO, userAuth.userDetails), HttpStatus.OK);
    }

    @PostMapping("/create-task-event")
    @ResponseBody
    public ResponseEntity<?> createTaskEvent(@Valid @RequestBody TaskEventDTO taskEventDTO) {

        ResponseEntity<BaseResponse> jwtSession = userAuth.validatedUserSession();
        if (jwtSession != null) return jwtSession;


        return new ResponseEntity<>(taskService.createTaskEvent(taskEventDTO, userAuth.userDetails), HttpStatus.OK);
    }


}
