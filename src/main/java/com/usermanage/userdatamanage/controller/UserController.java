package com.usermanage.userdatamanage.controller;

import com.usermanage.userdatamanage.dto.UserDTO;
import com.usermanage.userdatamanage.entity.UserDetails;
import com.usermanage.userdatamanage.service.UserDetailsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    Log log = LogFactory.getLog(UserController.class);
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody @Valid UserDetails userDTOs) {
        return ResponseEntity.ok(userDetailsService.saveUserCredentials(userDTOs.getUserName(), userDTOs.getPassWord()));
    }

    @GetMapping("/get")
    public ResponseEntity<String> get(@Param("userName") String userName, @Param("passWord") String passWord) {
        return ResponseEntity.ok(userDetailsService.getUserCredentials(userName, passWord));
    }
}
