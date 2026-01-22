package phoneStore.utils;

import org.mindrot.jbcrypt.BCrypt;
import phoneStore.exception.ValidationException;

public class PasswordUtil {

    private PasswordUtil() {}

    public  static String hash ( String plainPassword) {
//        if ( plainPassword == null || plainPassword.trim().isEmpty()) {
//            throw new ValidationException(" mat khau khong dc de trong ") ;
//
//        }
//        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
        return plainPassword;
    }
 // so sanhs plain
    public static boolean verify(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) return false;
        //return BCrypt.checkpw(plainPassword, hashedPassword);
        return plainPassword.equals(hashedPassword);
    }
}
