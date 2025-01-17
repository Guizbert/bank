package net.guizbert.bank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import net.guizbert.bank.dto.*;
import net.guizbert.bank.entity.User;
import net.guizbert.bank.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.SequencedCollection;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Account Management APIs")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(
            summary = "Create a new Account",
            description = "Creating a new user and generates an Account number"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201"
    )
    @PostMapping
    public BankResponseDto createAccount(@RequestBody UserDto userDto)
    {
        return userService.createAccount(userDto);
    }

    @GetMapping("/getUsers")
    public SequencedCollection<User> UserDto(){
        return userService.users();
    }


    @Operation(
            summary = "Find an account to show his balance ",
            description = "Find an Account based on the account number" +
                    "it will return informations about the account"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201"
    )
    @GetMapping("/balanceEnquiry")
    public BankResponseDto balanceEnquiry(@RequestBody EnquiryRequestDto enquiryRequestDto)
    {
        return userService.balanceEnquiry(enquiryRequestDto);
    }

    @Operation(
            summary = "Find a user",
            description = "Return the full name of a user based on his account number"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201"
    )
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequestDto enquiryRequestDto)
    {
        return userService.nameEnquiry(enquiryRequestDto);
    }

    @Operation(
            summary = "Credit : add money to an account",
            description = "Add money to an account."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201"
    )
    @PostMapping("/credit")
    public BankResponseDto creditAccount(@RequestBody CreditDebitRequestDto request)
    {
        return userService.creditAccount(request);
    }


    @Operation(
            summary = "Debit : Withdraw money from an account",
            description = "Withdraw money from an account."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201"
    )
    @PostMapping("/debit")
    public BankResponseDto debitAccount(@RequestBody CreditDebitRequestDto request)
    {
        return userService.debitAccount(request);
    }


    @Operation(
            summary = "Transfer 'money' between two account from the database",
            description = "find both account and debit the sender and credit the receiver."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201"
    )
    @PostMapping("/transfer")
    public BankResponseDto transfer(@RequestBody TransfertRequestDto requestDto)
    {
        return userService.transfer(requestDto);
    }
}
