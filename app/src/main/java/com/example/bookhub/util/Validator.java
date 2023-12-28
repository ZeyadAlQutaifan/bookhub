package com.example.bookhub.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static boolean isValidEmail(String email){
        // Define the email pattern
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(emailPattern);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(email);

        // Check if the email matches the pattern
        return matcher.matches();
    }
    public static boolean isValidPassword(String password){
        return password.length() > 7 ;
    }

    public static boolean isPasswordMatches(String password , String confirmPassword){
        return password.equals(confirmPassword);
    }
}
