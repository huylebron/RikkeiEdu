package phoneStore.exception;

// fix and show bug for conole UI
public final  class ConsoleErrorHandler {

    private ConsoleErrorHandler() {

    }

    public static void handle(Exception e) {

        if (e instanceof ValidationException) {
            System.err.println(" dữ liệu không đúng : " + e.getMessage());

        } else if (e instanceof NotFoundException) {
            System.out.println(" không tìm thấy dữ liệu: " + e.getMessage());

        } else if (e instanceof BusinessException) {
            System.out.println(" không thể thực hiện: " + e.getMessage());

        } else if (e instanceof AuthenticationException) {
            System.out.println("dăng nhập thất bại: " + e.getMessage());

        } else if (e instanceof DatabaseException) {
            System.out.println("lỗi hệ thống.");
            System.err.println("db error ");
            e.printStackTrace(System.err);

        } else {

            System.out.println("lỗi không xác định");
            e.printStackTrace(System.err);
        }

    }


}
