package net.guizbert.bank.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXIST_CODE = "101";
    public static final String ACCOUNT_EXIST_MESSAGE = "This user already exist and have an account.";
    public static final String ACCOUNT_CREATED_SUCCESS = "201";
    public static final String ACCOUNT_CREATED_MESSAGE = "Account created successfully.";

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
