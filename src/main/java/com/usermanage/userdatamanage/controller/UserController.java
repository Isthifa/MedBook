package com.usermanage.userdatamanage.controller;

import com.usermanage.userdatamanage.entity.UserDetail;
import com.usermanage.userdatamanage.service.UserDetailService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    private UserDetailService userDetailService;

    @PostMapping("/new/user")
    public ResponseEntity<String> save(@RequestBody @Valid UserDetail userDTOs) {
        log.info("UserDetail: " + userDTOs);
        return ResponseEntity.ok(userDetailService.saveUserCredentials(userDTOs.getUserName(), userDTOs.getPassWord()));
    }

    @GetMapping("/get")
    public ResponseEntity<String> get(@Param("userName") String userName, @Param("passWord") String passWord) {
        return ResponseEntity.ok(userDetailService.getUserCredentials(userName, passWord));
    }
}
