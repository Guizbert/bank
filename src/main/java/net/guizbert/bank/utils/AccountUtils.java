package net.guizbert.bank.utils;

import java.time.Year;

public class AccountUtils {
    /*
        400 : error
        200 : success
        100 : already exist
     */

    //acc doesnt exist
    public static final String ACCOUNT_DOESNT_EXIST_CODE = "404";
    public static final String ACCOUNT_DOESNT_EXIST_MESSAGE = "The account number provided doesn't show in the DB";

    //acc doesnt exist
    public static final String ACCOUNT_DEBITED_ERROR_CODE = "405";
    public static final String ACCOUNT_DEBITED_ERROR_MESSAGE = "The amount provided is superior than the current amount in your account.";

    //account exist
    public static final String ACCOUNT_EXIST_CODE = "101";
    public static final String ACCOUNT_EXIST_MESSAGE = "This user already exist and have an account.";

    //account creation
    public static final String ACCOUNT_CREATED_SUCCESS = "201";
    public static final String ACCOUNT_CREATED_MESSAGE = "Account created successfully.";

    //account found
    public static final String ACCOUNT_FOUND_CODE = "202";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account found.";
    //account credited / debited
    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "203";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User account credited successfully";
    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "203";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "User account debited successfully";
    // Transfer
    public final static String TRANSFER_SUCCESS_CODE = "204";
    public final static String TRANSFER_SUCCES_MESSAGE = "Transfer done successfully";
    // Login
    public final static String LOGIN_CODE = "205";
    public final static String LOGIN_MESSAGE = "You logged in successfully";



    public static String generateAccNumber()
    {
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        int randomNumber = (int) Math.floor(Math.random() * (max - min +1) +(currentYear.getValue() *100));

        //We can use a String builder  but i'll just return it this way for now
        return String.valueOf(currentYear) + String.valueOf(randomNumber);
    }
}
