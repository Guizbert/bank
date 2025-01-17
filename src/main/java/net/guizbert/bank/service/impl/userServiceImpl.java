package net.guizbert.bank.service.impl;

import net.guizbert.bank.dto.*;
import net.guizbert.bank.entity.Transaction;
import net.guizbert.bank.entity.User;
import net.guizbert.bank.repository.UserRepository;
import net.guizbert.bank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class userServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    TransactionService transactionService;

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
            return bankResponseBuilder(AccountUtils.ACCOUNT_EXIST_CODE, AccountUtils.ACCOUNT_EXIST_MESSAGE, null);
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

        // Send email alert
        EmailDto emailDetails = EmailDto.builder()
                .recipient(savedUser.getEmail())
                .subject("Account creation")
                .messageBody("Account created successfully\n" +
                        "Your account detail :\n" +"Account name : " + savedUser.getFirstName()+ " "+savedUser.getLastName() + "\n"+
                        "Account number : " + savedUser.getAccountNumber() + " Balance : "+ savedUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailDetails);
        // Return the response
        return bankResponseBuilder(AccountUtils.ACCOUNT_CREATED_SUCCESS, AccountUtils.ACCOUNT_CREATED_MESSAGE, savedUser);
    }

    @Override
    public List<User> users() {
        return userRepository.findAll();
    }

    @Override
    public BankResponseDto balanceEnquiry(EnquiryRequestDto enquiryRequestDto) {

        //check if user exist based on the acc number;
        boolean exist = userRepository.existsByAccountNumber(enquiryRequestDto.getAccountNumber());
        if(!exist) {
            return bankResponseBuilder(AccountUtils.ACCOUNT_DOESNT_EXIST_CODE, AccountUtils.ACCOUNT_DOESNT_EXIST_MESSAGE, null);
        }
        User user = userRepository.findByAccountNumber(enquiryRequestDto.getAccountNumber());
        return bankResponseBuilder(AccountUtils.ACCOUNT_FOUND_CODE, AccountUtils.ACCOUNT_FOUND_MESSAGE, user);
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
            return bankResponseBuilder(AccountUtils.ACCOUNT_DOESNT_EXIST_CODE, AccountUtils.ACCOUNT_DOESNT_EXIST_MESSAGE, null);
        }
        // Retrieve user and update balance
        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        // Save transaction :
        TransactionDto transactionDto = transactionBuilder(userToCredit, "CREDIT" ,"SUCCESS", request.getAmount());

        transactionService.saveTransaction(transactionDto);
        // Return updated balance
        return bankResponseBuilder(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE, AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE, userToCredit);
    }

    @Override
    public BankResponseDto debitAccount(CreditDebitRequestDto request) {
        // check if account exist :
        boolean exist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!exist) {
            return bankResponseBuilder(AccountUtils.ACCOUNT_DOESNT_EXIST_CODE,
                    AccountUtils.ACCOUNT_DOESNT_EXIST_MESSAGE, null);
        }
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        // Check if the account has sufficient balance
        if(userToDebit.getAccountBalance().compareTo(request.getAmount()) < 0)
        {
            return bankResponseBuilder(AccountUtils.ACCOUNT_DEBITED_ERROR_CODE,AccountUtils.ACCOUNT_DEBITED_ERROR_MESSAGE,null);
        }
        // Update the balance after debit
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(userToDebit);

        // Save transaction :
        TransactionDto transactionDto = transactionBuilder(userToDebit, "DEBIT" ,"SUCCESS", request.getAmount());

        transactionService.saveTransaction(transactionDto);
        // Return updated balance
        return bankResponseBuilder(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE,
                    AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE, userToDebit );
    }


    @Override
    public BankResponseDto transfer(TransfertRequestDto request) {
        /*
            - check both acc exist                              checked
            - check if amount to transfert is possible          checked
            - debit the account                                 checked
            - credit the other account                          checked
            - return bank response
         */

        if(!accountExist(request.getReceiverAccountNumber())
            || !accountExist(request.getSenderAccountNumber()))
        {
            return bankResponseBuilder(AccountUtils.ACCOUNT_DOESNT_EXIST_CODE,
                            AccountUtils.ACCOUNT_DOESNT_EXIST_MESSAGE, null);
        }
        User sender = userRepository.findByAccountNumber(request.getSenderAccountNumber());
        if(!canDebitAccount(sender, request.getAmount()))
        {
            return bankResponseBuilder(AccountUtils.ACCOUNT_DEBITED_ERROR_CODE,
                    AccountUtils.ACCOUNT_DEBITED_ERROR_MESSAGE, null);
        }
        // Sending an email to the sender & debit the account
        String body = "You sent : " + request.getAmount() + " euros to the account : " + request.getReceiverAccountNumber();
        sendEmail("Debit alert", sender.getEmail(),body);
        debitAccount(sender, request.getAmount());

        User receiver = userRepository.findByAccountNumber(request.getReceiverAccountNumber());
        body = "You received : " + request.getAmount() + " euros from : " + request.getSenderAccountNumber();
        sendEmail("Credit alert", receiver.getEmail(),body);
        creditAccount(receiver, request.getAmount());

        // Save transaction :
        TransactionDto transactionDto = transactionBuilder(receiver, "CREDIT" ,"SUCCESS", request.getAmount());

        transactionService.saveTransaction(transactionDto);

        return bankResponseBuilder(AccountUtils.TRANSFER_SUCCESS_CODE, AccountUtils.TRANSFER_SUCCES_MESSAGE, null );
    }


    void sendEmail(String subject, String recipient, String body){
        EmailDto emailDetails = EmailDto.builder()
                .recipient(recipient)
                .subject(subject)
                .messageBody(body)
                .build();
        emailService.sendEmailAlert(emailDetails);
    }
    boolean accountExist(String accountNumber){
        return userRepository.existsByAccountNumber(accountNumber);

    }
    boolean canDebitAccount(User user, BigDecimal amount)
    {
        return user.getAccountBalance().compareTo(amount) >= 0 ;
    }
    void creditAccount(User user, BigDecimal amount)
    {
        user.setAccountBalance(user.getAccountBalance().add(amount));
        userRepository.save(user);
    }
    void debitAccount(User user, BigDecimal amount)
    {
        // Update the balance after debit
        user.setAccountBalance(user.getAccountBalance().subtract(amount));
        userRepository.save(user);
    }

    // Method to generate the BankResponseDto consistently
    BankResponseDto bankResponseBuilder(String code, String message, User user )
    {
        if(user == null)
        {
            return BankResponseDto.builder()
                    .responseCode(code)
                    .responseMessage(message)
                    .accInfo(null)
                    .build();
        }
        return BankResponseDto.builder()
                .responseCode(code)
                .responseMessage(message)
                .accInfo(AccountInformationDto.builder()
                        .accountName(user.getFirstName() + " "+ user.getLastName().toUpperCase())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }

    TransactionDto transactionBuilder(User u, String type, String status, BigDecimal amount)
    {
        return TransactionDto.builder()
                .transactionType(type)
                .amount(amount)
                .accountNumber(u.getAccountNumber())
                .status(status)
                .build();
    }
}
