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

    //account exist
    public static final String ACCOUNT_EXIST_CODE = "101";
    public static final String ACCOUNT_EXIST_MESSAGE = "This user already exist and have an account.";

    //account creation
    public static final String ACCOUNT_CREATED_SUCCESS = "201";
    public static final String ACCOUNT_CREATED_MESSAGE = "Account created successfully.";

    //account found
    public static final String ACCOUNT_FOUND_CODE = "202";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account found.";

    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "203";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User account credited successfully";

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
