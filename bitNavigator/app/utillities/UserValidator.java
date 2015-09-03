package utillities;

import models.User;

/**
 * Created by ognje on 03-Sep-15.
 */
public class UserValidator {

    public static String[] isPasswordValid(String password) {
        if (password.length() < 8) {
            return new String[]{"error", "Password must be at least 8 characters long!"};
        } else if (password.length() > 25) {
            return new String[]{"error", "Password must be at most 25 characters long!"};
        }

        boolean valid = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            return new String[]{"error", "Password must contain at least one letter!"};
        }

        valid = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            return new String[]{"error", "Password must contain at least one digit!"};
        }

        return new String[]{"success", "Password is good!"};
    }

    public static boolean isEmailValid(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }




}
