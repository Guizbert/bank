package net.guizbert.bank.controller;

import net.guizbert.bank.dto.BankResponseDto;
import net.guizbert.bank.dto.UserDto;
import net.guizbert.bank.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping
    public BankResponseDto createAccount(@RequestBody UserDto userDto)
    {
        return userService.createAccount(userDto);
    }

}
