package net.guizbert.bank.service.impl;

import net.guizbert.bank.dto.*;
import net.guizbert.bank.entity.User;

import java.util.List;

public interface UserService {

    //create an account :
    BankResponseDto createAccount(UserDto userDto);
    BankResponseDto login(LoginDto request);
    List<User> users();
    BankResponseDto balanceEnquiry(EnquiryRequestDto enquiryRequestDto);
    String nameEnquiry(EnquiryRequestDto enquiryRequestDto);
    BankResponseDto creditAccount(CreditDebitRequestDto request);
    BankResponseDto debitAccount(CreditDebitRequestDto request);
    BankResponseDto transfer(TransfertRequestDto request);



}
