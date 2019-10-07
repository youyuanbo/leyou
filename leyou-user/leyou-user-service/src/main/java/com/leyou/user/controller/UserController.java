package com.leyou.user.controller;


import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable String data,
                                             @PathVariable Integer type) {

        return ResponseEntity.ok(userService.checkData(data, type));
    }


    @PostMapping("code")
    public ResponseEntity<Void> sendCode(@RequestParam String phone) {

        userService.sendCode(phone);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("registry")
    public ResponseEntity<Void> registry(@Valid User user, @RequestParam String code, BindingResult result) {
        if (result.hasFieldErrors()){
            throw new RuntimeException(result.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("|")));
        }

        userService.registry(user, code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("query")
    public ResponseEntity<User> queryUserByUsernameAndPassword(
            @RequestParam String username,
            @RequestParam String password
    ){
        return ResponseEntity.ok(userService.queryUserByUsernameAndPassword(username, password));
    }


}

