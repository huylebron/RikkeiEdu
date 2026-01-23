package phoneStore.utils;

import phoneStore.constant.RegexConstant;
import phoneStore.exception.BackToMenuException;
import phoneStore.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Scanner;

public final class InputUtil {

    private InputUtil() {
    }

    // scanner dung chung - > tranh bug doc dong
    private static final Scanner SCANNER = new Scanner(System.in);
    private static boolean allowCancel = false;

    public static void setAllowCancel(boolean allow) {
        allowCancel = allow;
    }

    public static void executeWithCancel(Runnable action) {
        boolean originalState = allowCancel;
        allowCancel = true;
        try {
            action.run();
        } finally {
            allowCancel = originalState;
        }
    }

    public static String readString(String prompt) {
        if (allowCancel) {
            System.out.print(prompt + " (nhập 'exit' để hủy): ");
        } else {
            System.out.print(prompt);
        }

        String s = SCANNER.nextLine();
        String result = s == null ? "" : s.trim();

        if (allowCancel && "exit".equalsIgnoreCase(result)) {
            throw new BackToMenuException();
        }
        return result;
    }

    public static String readNotNull(String prompt) {
        while (true) {
            String s = readString(prompt);
            if (!s.isEmpty())
                return s;
            System.out.println(prompt + " khong duoc de trong.");
        }

    }

    public static int readInt(String prompt) {
        while (true) {
            String s = readString(prompt);
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println(" Vui long nhap so nguyen .");
            }
        }
    }

    public static int readIntInRange(String prompt, int minRange, int maxRange) {
        while (true) {
            int v = readInt(prompt);
            if (v >= minRange && v <= maxRange)
                return v;
            System.out.printf(" Vui long nhap trong khoang [%d - %d].%n", minRange, maxRange);
        }

    }

    public static long readLong(String prompt) {
        while (true) {
            String s = readString(prompt);
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so  hop le.");
            }
        }
    }

    public static BigDecimal readBigDecimal(String prompt) {
        while (true) {
            String s = readString(prompt);
            try {
                BigDecimal v = new BigDecimal(s);
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so tien hop le( so thuc ) .");
            }
        }
    }

    public static BigDecimal readBigDecimalPositive(String prompt) {
        while (true) {
            BigDecimal v = readBigDecimal(prompt);
            if (v.compareTo(BigDecimal.ZERO) > 0)
                return v;
            System.out.println("Gia tri phai > 0.");
        }
    }

    public static int readIntNonNegative(String prompt) {
        while (true) {
            int v = readInt(prompt);
            if (v >= 0)
                return v;
            System.out.println("Gia tri khong duoc am.");
        }
    }

    public static LocalDate readLocalDate(String prompt, String pattern) {
        Objects.requireNonNull(pattern, "khong dc null ");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);

        while (true) {
            String s = readString(prompt + " (" + pattern + "): ");
            try {
                return LocalDate.parse(s, fmt);
            } catch (DateTimeParseException e) {
                System.out.println("Ngay khong hop le. Vui long nhap dung " + pattern + ".");
            }
        }
    }

    public static String readPhone(String prompt) {
        while (true) {
            String s = readString(prompt);
            if (s.matches(RegexConstant.PHONE_REGEX)) {
                return s;
            }
            System.out.println(" Lỗi: Số điện thoại không hợp lệ (Phải có 10 số và bắt đầu bằng 0).");
        }
    }

    public static String readEmail(String prompt) {
        while (true) {
            String s = readString(prompt);
            if (s.isEmpty() || s.matches(RegexConstant.EMAIL_REGEX)) {
                return s;
            }
            System.out.println(" Lỗi: Email không hợp lệ (Ví dụ: huy@gmail.com).");
        }
    }

    public static String readStringDefault(String prompt, String defaultValue) {
        System.out.print(prompt + " (Mặc định: " + defaultValue + "): ");
        String s = SCANNER.nextLine().trim();
        return s.isEmpty() ? defaultValue : s;
    }

    public static String readPhoneDefault(String prompt, String defaultValue) {
        while (true) {
            String s = readString(prompt + " (Mặc định: " + defaultValue + "): ");
            if (s.isEmpty())
                return defaultValue;
            if (s.matches(RegexConstant.PHONE_REGEX)) {
                return s;
            }
            System.out.println(" Lỗi: Số điện thoại không hợp lệ.");
        }
    }

    public static String readEmailDefault(String prompt, String defaultValue) {
        while (true) {
            String s = readString(prompt + " (Mặc định: " + defaultValue + "): ");
            if (s.isEmpty())
                return defaultValue;
            if (s.matches(RegexConstant.EMAIL_REGEX)) {
                return s;
            }
            System.out.println(" Lỗi: Email không hợp lệ.");
        }
    }

    public static boolean readConfirm(String prompt) {
        System.out.print(prompt + " (y/n): ");
        String s = SCANNER.nextLine().trim().toLowerCase();
        return s.equals("y");
    }

    // public static void require(boolean condition, String messageIfFalse) {
    // if (!condition)
    // throw new ValidationException(messageIfFalse);
    // }
}
