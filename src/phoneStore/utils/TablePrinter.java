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

        // Calculate max width for each column using streams
        rows.forEach(r -> {
            String[] cols = rowMapper.apply(r);
            for (int i = 0; i < colCount; i++) {
                String cell = safe(cols[i]);
                widths[i] = Math.max(widths[i], cell.length());
            }
        });

        printLine(widths);
        printRow(headers, widths);
        printLine(widths);

        // Print each row using streams
        rows.stream()
                .map(rowMapper)
                .forEach(row -> printRow(row, widths));

        printLine(widths);
    }

    private static void printRow(String[] cols, int[] widths) {
        String result = java.util.stream.IntStream.range(0, cols.length)
                .mapToObj(i -> (i > 0 ? SEP : "") + padRight(safe(cols[i]), widths[i]))
                .reduce("", (acc, curr) -> acc + curr);
        System.out.println(result);
    }

    private static void printLine(int[] widths) {
        String result = java.util.stream.IntStream.range(0, widths.length)
                .mapToObj(i -> (i > 0 ? "-+-" : "") + repeat("-", widths[i]))
                .reduce("", (acc, curr) -> acc + curr);
        System.out.println(result);
    }

    public static String safe(String s) {
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

    // helper method format money
    public static String fmtMoney(BigDecimal v) {
        if (v == null) return "0";
        return String.format("%,.0f", v.doubleValue()).replace(",", ".");
        //return v.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    public static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
}
