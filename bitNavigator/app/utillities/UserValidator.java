package utillities;

import controllers.UserController;

/**
 * Class used to validate user`s attributes (e.g. if email is in valid format).
 * Created by ognjen on 03-Sep-15.
 */
public class UserValidator {

    /**
     * Minimum characters in password.
     */
    public static final int PASSWORD_MIN_CHARACTERS = 8;
    /**
     * Maximum characters in password.
     */
    public static final int PASSWORD_MAX_CHARACTERS = 25;

    /**
     * Returns array of two Strings, first representing type (error or success) and second
     * representing message. Intended to use in Controller.flash.
     * @param password A password.
     * @return Array of two Strings, first representing type (error or success) and second
     * representing message. Intended to use in Controller.flash.
     */
    public static String[] isPasswordValid(String password) {
        if (password.length() < PASSWORD_MIN_CHARACTERS) {
            return new String[]{UserController.ERROR_MESSAGE, "Password must be at least " + PASSWORD_MIN_CHARACTERS + " characters long!"};
        } else if (password.length() > PASSWORD_MAX_CHARACTERS) {
            return new String[]{UserController.ERROR_MESSAGE, "Password must be at most " + PASSWORD_MAX_CHARACTERS + " characters long!"};
        }

        boolean valid = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            return new String[]{UserController.ERROR_MESSAGE, "Password must contain at least one letter!"};
        }

        valid = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            return new String[]{UserController.ERROR_MESSAGE, "Password must contain at least one digit!"};
        }

        return new String[]{UserController.SUCCESS_MESSAGE, "Password is good!"};
    }

    /**
     * Returns true if email is in valid format.
     * @param email An email.
     * @return True if email is in valid format.
     */
    public static boolean isEmailValid(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * Returns array of two Strings, first representing type (error or success) and second
     * representing message. Intended to use in Controller.flash.
     * @param firstName A first name.
     * @param lastName A last name.
     * @return Array of two Strings, first representing type (error or success) and second
     * representing message. Intended to use in Controller.flash.
     */
    public static String[] isNameValid(String firstName, String lastName) {
        for (char c : firstName.toCharArray()) {
            if (!Character.isLetter(c)) {
                return new String[]{UserController.ERROR_MESSAGE, "Name can only contain letters"};
            }
        }
        for (char c : lastName.toCharArray()) {
            if (!Character.isLetter(c)) {
                return new String[]{UserController.ERROR_MESSAGE, "Name can only contain letters"};
            }
        }
        return new String[]{UserController.SUCCESS_MESSAGE, "Name is good!"};
    }
}
