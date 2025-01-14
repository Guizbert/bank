package net.guizbert.bank.service.impl;

import net.guizbert.bank.dto.BankResponseDto;
import net.guizbert.bank.dto.UserDto;

public interface UserService {

    //create an account :
    BankResponseDto createAccount(UserDto userDto);

}
