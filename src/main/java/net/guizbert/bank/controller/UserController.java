package net.guizbert.bank.controller;

import net.guizbert.bank.dto.BankResponseDto;
import net.guizbert.bank.dto.CreditDebitRequestDto;
import net.guizbert.bank.dto.EnquiryRequestDto;
import net.guizbert.bank.dto.UserDto;
import net.guizbert.bank.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/balanceEnquiry")
    public BankResponseDto balanceEnquiry(@RequestBody EnquiryRequestDto enquiryRequestDto)
    {
        return userService.balanceEnquiry(enquiryRequestDto);
    }

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequestDto enquiryRequestDto)
    {
        return userService.nameEnquiry(enquiryRequestDto);
    }

    @PostMapping("/credit")
    public BankResponseDto creditAccount(@RequestBody CreditDebitRequestDto request)
    {
        return userService.creditAccount(request);
    }
    @PostMapping("/debit")
    public BankResponseDto debitAccount(@RequestBody CreditDebitRequestDto request)
    {
        return userService.debitAccount(request);
    }

}
