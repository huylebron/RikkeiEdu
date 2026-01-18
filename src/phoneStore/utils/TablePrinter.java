package phoneStore.utils;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public class TablePrinter {

    private TablePrinter() {}

    private static final String SEP = " | ";

    public static <T> void printTable(
            String title,
            String[] headers,
            List<T> rows,
            Function<T, String[]> rowMapper
    ) {
        if (title != null && !title.trim().isEmpty()) {
            System.out.println("\n" + title);
        }

        if (rows == null || rows.isEmpty()) {
            System.out.println("(khong co du lieu)");
            return;
        }

        int colCount = headers.length;
        int[] widths = new int[colCount];

        // init widths with header lengths
        for (int i = 0; i < colCount; i++) {
            widths[i] = headers[i].length();
        }


        for (T r : rows) {
            String[] cols = rowMapper.apply(r);
            for (int i = 0; i < colCount; i++) {
                String cell = safe(cols[i]);
                widths[i] = Math.max(widths[i], cell.length());
            }
        }

        printLine(widths);
        printRow(headers, widths);
        printLine(widths);

        for (T r : rows) {
            printRow(rowMapper.apply(r), widths);
        }

        printLine(widths);
    }

    private static void printRow(String[] cols, int[] widths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.length; i++) {
            if (i > 0) sb.append(SEP);
            String cell = safe(cols[i]);
            sb.append(padRight(cell, widths[i]));
        }
        System.out.println(sb.toString());
    }

    private static void printLine(int[] widths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < widths.length; i++) {
            if (i > 0) sb.append("-+-");
            sb.append(repeat("-", widths[i]));
        }
        System.out.println(sb.toString());
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static String padRight(String s, int width) {
        if (s.length() >= width) return s;
        return s + repeat(" ", width - s.length());
    }

    private static String repeat(String s, int times) {
        StringBuilder sb = new StringBuilder(times * s.length());
        for (int i = 0; i < times; i++) sb.append(s);
        return sb.toString();
    }

    // ===== Helpers format commonly used types =====
    public static String fmtMoney(BigDecimal v) {
        if (v == null) return "0";
        return v.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    public static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
}
