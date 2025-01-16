package net.guizbert.bank.service.impl;

import net.guizbert.bank.dto.BankResponseDto;
import net.guizbert.bank.dto.CreditDebitRequestDto;
import net.guizbert.bank.dto.EnquiryRequestDto;
import net.guizbert.bank.dto.UserDto;

public interface UserService {

    //create an account :
    BankResponseDto createAccount(UserDto userDto);


    BankResponseDto balanceEnquiry(EnquiryRequestDto enquiryRequestDto);

    String nameEnquiry(EnquiryRequestDto enquiryRequestDto);

    BankResponseDto creditAccount(CreditDebitRequestDto request);
    BankResponseDto debitAccount(CreditDebitRequestDto request);





}
