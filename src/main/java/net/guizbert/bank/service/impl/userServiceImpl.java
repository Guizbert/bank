package net.guizbert.bank.service.impl;

import net.guizbert.bank.dto.*;
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
    @Autowired
    EmailService emailService;

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

        //send mail alert
        EmailDto emailDetails = EmailDto.builder()
                .recipient(savedUser.getEmail())
                .subject("Account creation")
                .messageBody("Account created successfully\n" +
                        "Your account detail :\n" +"Account name : " + savedUser.getFirstName()+ " "+savedUser.getLastName() + "\n"+
                        "Account number : " + savedUser.getAccountNumber() + " Balance : "+ savedUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponseDto.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
                .accInfo(AccountInformationDto.builder()
                        .accountName(savedUser.getLastName().toUpperCase() + " " + savedUser.getFirstName())
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())

                        .build())
                .build();
    }

    @Override
    public BankResponseDto balanceEnquiry(EnquiryRequestDto enquiryRequestDto) {

        //check if user exist based on the acc number;
        boolean exist = userRepository.existsByAccountNumber(enquiryRequestDto.getAccountNumber());
        if(!exist) {
            return BankResponseDto.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOESNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOESNT_EXIST_MESSAGE)
                    .accInfo(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(enquiryRequestDto.getAccountNumber());
        return BankResponseDto.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accInfo(AccountInformationDto.builder()
                        .accountName(user.getFirstName() + " "+ user.getLastName().toUpperCase())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequestDto enquiryRequestDto) {
        boolean exist = userRepository.existsByAccountNumber(enquiryRequestDto.getAccountNumber());
        if(!exist) {
            return AccountUtils.ACCOUNT_DOESNT_EXIST_MESSAGE;
        }
        User user = userRepository.findByAccountNumber(enquiryRequestDto.getAccountNumber());

        return user.getLastName().toUpperCase() + " "+ user.getFirstName();

    }

    @Override
    public BankResponseDto creditAccount(CreditDebitRequestDto request) {
        // check account exist :
        boolean exist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!exist) {
            return BankResponseDto.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOESNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOESNT_EXIST_MESSAGE)
                    .accInfo(null)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        //update the user balance :
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        //saving the update
        userRepository.save(userToCredit);

        return BankResponseDto.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accInfo(AccountInformationDto.builder()
                        .accountName(userToCredit.getFirstName() + " "+ userToCredit.getLastName().toUpperCase())
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }


}
