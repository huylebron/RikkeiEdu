package phoneStore.constant;

import java.util.regex.Pattern;

public final  class RegexConstant {

    private RegexConstant() {

    }

    //regex for phone

    public static final String PHONE_REGEX  = "^0\\d{9}$" ;
// regex email

    public static final String EMAIL_REGEX =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
 // regex username

    public static final String USERNAME_REGEX =
            "^[A-Za-z0-9._-]{4,30}$";
 // regex password
    public static final String PASSWORD_REGEX =
            "^(?=.*[A-Za-z])(?=.*\\d).{6,}$";

    public static final Pattern PHONE_VN_PATTERN = Pattern.compile(PHONE_REGEX);
    public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX );
    public static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);
    public static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);





}
