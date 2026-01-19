package phoneStore.utils;

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

    public static String readString(String prompt) {
        System.out.print(prompt);
        String s = SCANNER.nextLine();
        return s == null ? "" : s.trim();
    }

    public static String readNotNull(String prompt) {
        while (true) {
            String s = readString(prompt);
            if (!s.isEmpty()) return s;
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
            if (v >= minRange && v <= maxRange) return v;
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
            if (v.compareTo(BigDecimal.ZERO) > 0) return v;
            System.out.println("Gia tri phai > 0.");
        }
    }

    public static int readIntNonNegative(String prompt) {
        while (true) {
            int v = readInt(prompt);
            if (v >= 0) return v;
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


    public static void require(boolean condition, String messageIfFalse) {
        if (!condition) throw new ValidationException(messageIfFalse);
    }


}
