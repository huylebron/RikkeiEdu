package phoneStore.utils;

import org.mindrot.jbcrypt.BCrypt;
import phoneStore.exception.ValidationException;

/**
 * Utility class xử lý băm và kiểm tra mật khẩu sử dụng BCrypt
 */
public class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * Băm mật khẩu thô thành chuỗi mã băm
     */
    public static String hash(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new ValidationException("Mật khẩu không được để trống.");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }

    /**
     * Kiểm tra mật khẩu thô với chuỗi đã băm trong DB
     */
    public static boolean verify(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null)
            return false;
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Hàm Main để tạo mã băm cho admin lần đầu
     */
    public static void main(String[] args) {
        String pass = "123456";
        String hashed = hash(pass);
        System.out.println("Mật khẩu thô: " + pass);
        System.out.println("Chuỗi băm (Hashed): " + hashed);

    }
}
