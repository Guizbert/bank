package net.guizbert.bank.utils;

import java.security.SecureRandom;
import java.time.Year;
import java.util.List;

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


    private static final String[][] COUNTRY_IBAN_LENGTHS = {
            {"FR", "27"}, // France
            {"BE", "16"}, // Belgium
            {"PL", "28"}, // Poland
            {"ES", "24"}, // Spain
            {"IE", "22"}, // Ireland
            {"SE", "24"}  // Sweden
    };

    private static final SecureRandom random = new SecureRandom();

    public static String generateAccNumber() {
        StringBuilder accountNumber = new StringBuilder();

        // Randomly choose a country from the possible prefixes
        String[] possiblePrefix = {"FR", "BE", "PL", "ES", "IE", "SE"};
        String countryCode = possiblePrefix[random.nextInt(possiblePrefix.length)];

        // Find the account number length for the selected country
        String accountLengthStr = getAccountLengthForCountry(countryCode);
        int accountLength = Integer.parseInt(accountLengthStr);

        // Generate the IBAN-like account number
        accountNumber.append(countryCode);  // Add country code
        accountNumber.append(generateRandomCheckDigits());

        // Generate the remaining account number (random digits)
        int accountNumberLength = accountLength - 4; // Subtract country code and check digits
        accountNumber.append(generateRandomAccountNumber(accountNumberLength));

        return accountNumber.toString();
    }

    private static String getAccountLengthForCountry(String countryCode) {
        for (String[] countryInfo : COUNTRY_IBAN_LENGTHS) {
            if (countryInfo[0].equals(countryCode)) {
                return countryInfo[1];
            }
        }
        throw new IllegalArgumentException("Unsupported country code: " + countryCode);
    }

    private static String generateRandomCheckDigits() {
        // In real scenarios, the check digits are calculated based on the IBAN format.
        // For this project, generate two random digits as placeholders.
        return String.format("%02d", random.nextInt(100));  // Generates two random digits (00-99)
    }

    private static String generateRandomAccountNumber(int length) {
        // Generate a random account number with the specified length
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < length; i++) {
            accountNumber.append(random.nextInt(10));  // Generate a random digit (0-9)
        }
        return accountNumber.toString();
    }
}
