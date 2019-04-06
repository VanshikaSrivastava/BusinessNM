package tieup.business_network.appmart;

import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;



public class Validation {
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "[0-9]{10}";
//    private static final String NAME_REGEX="[a-zA-Z]+\\.?";
    private static final String NAME_REGEX="^[\\p{L} .'-]+$";
    private static final String Pin_REGEX = "[0-9]{6}";
    private static final String CONSUMER_REGEX = "[0-9]{10}";
    private static final String BILLINGUNIT_REGEX = "[0-9]{4}";
    private static final String PASSWORD_REGEX = "[0-9]{4}";
    private static final String NUMBER_REGEX = "[0-9]{10}";

    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    private static final String Number_MSG = "Required";
    private static final String EMAIL_MSG = "Invalid email";
    private static final String PIN_MSG = "Invalid Pin ";
    private static final String PHONE_MSG = "Invalid number";
    private static final String NAME_MSG = "Invalid Name";
    private static final String password_Msg = "Invalid password";


    // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }
    public static boolean isPin(EditText editText, boolean required) {
        return isValid(editText, Pin_REGEX, PIN_MSG, required);
    }
    public static boolean isName(EditText editText, boolean required) {
        return isValid(editText, NAME_REGEX, NAME_MSG, required);
    }

    public static boolean isPassword(EditText editText, boolean required) {
    return isValid(editText, PASSWORD_REGEX, password_Msg, required);
    }



    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText, required) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        };

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText, boolean required) {

        String text = editText.getText().toString().trim();
        editText.setError(null);
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }
        return true;
    }



}
