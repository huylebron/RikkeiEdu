package phoneStore.utils;

import phoneStore.constant.MessageConstant;
import phoneStore.constant.RegexConstant;
import phoneStore.exception.ValidationException;

import java.math.BigDecimal;

public final class Validator {

    private Validator() {
    }

    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + ": " + MessageConstant.ERR_REQUIRED);
        }
    }

    public static void requirePositive(BigDecimal value, String fieldName) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(fieldName + ": " + MessageConstant.ERR_POSITIVE);
        }
    }

    public static void requireNonNegative(Integer value, String fieldName) {
        if (value == null || value < 0) {
            throw new ValidationException(fieldName + ": " + MessageConstant.ERR_NON_NEGATIVE);
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) return;
        if (!RegexConstant.EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException(MessageConstant.ERR_INVALID_EMAIL);
        }
    }

    public static void validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return;
        if (!RegexConstant.PHONE_VN_PATTERN.matcher(phone.trim()).matches()) {
            throw new ValidationException(MessageConstant.ERR_INVALID_PHONE);
        }
    }

    // Validate entity fields (không phụ thuộc entity  )
    public static void validateProductFields(String name, String brand, BigDecimal price, Integer stock) {
        requireNonBlank(name, "Ten san pham");
        requireNonBlank(brand, "Hang san xuat");
        requirePositive(price, "Gia");
        requireNonNegative(stock, "Ton kho");
    }

    public static void validateCustomerFields(String name, String phone, String email, String address) {
        requireNonBlank(name, "Ten khach hang");
        validatePhone(phone);
        validateEmail(email);
        // address optional: nếu muốn bắt buộc thì requireNonBlank
    }

    public static void validateInvoiceLine(Integer quantity, BigDecimal unitPrice) {
        if (quantity == null || quantity <= 0) {
            throw new ValidationException("So luong: " + MessageConstant.ERR_POSITIVE);
        }
        requirePositive(unitPrice, "Don gia");
    }
}
