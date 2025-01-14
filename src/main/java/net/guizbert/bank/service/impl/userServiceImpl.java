package net.guizbert.bank.service.impl;

import net.guizbert.bank.dto.AccountInformationDto;
import net.guizbert.bank.dto.BankResponseDto;
import net.guizbert.bank.dto.UserDto;
import net.guizbert.bank.entity.User;
import net.guizbert.bank.repository.UserRepository;
import net.guizbert.bank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class userServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public BankResponseDto createAccount(UserDto userDto) {
        /*
        *   creating an acc :
        *   - check user already exist
        *   - new user
        * */
        if(userRepository.existsByEmail(userDto.getEmail()) ||
                userRepository.existsByPhoneNumber(userDto.getPhoneNumber()))
        {
            return BankResponseDto.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXIST_MESSAGE)
                    .accInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .gender(userDto.getGender())
                .adress(userDto.getAdress())
                .origin(userDto.getOrigin())
                .email(userDto.getEmail())
                .accountNumber(AccountUtils.generateAccNumber())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userDto.getPhoneNumber())
                .status("ACTIVE")
                .build();
        User savedUser = userRepository.save(newUser);

        return BankResponseDto.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
                .accInfo(AccountInformationDto.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getLastName().toUpperCase() + " " + savedUser.getFirstName())

                        .build())
                .build();
    }
}
