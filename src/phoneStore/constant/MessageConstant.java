package phoneStore.constant;

public final  class MessageConstant {

    private MessageConstant() {} ;

 // normal
    public static final String MSG_SUCCESS = "thao thành công.";
    public static final String MSG_FAILED = "thao tác thất bại.";
    public static final String MSG_INVALID_INPUT = "du liệu nhập không hợp lệ.";
    public static final String MSG_NOT_FOUND = "Ko tìm thấy dữ liệu.";
    public static final String MSG_SYSTEM_ERROR = "loi hệ thống mời thử lại ";

    // authent
    public static final String MSG_LOGIN_SUCCESS = "đăng nhập thành công.";
    public static final String MSG_LOGIN_FAILED = "sai tài khoản hoặc mật khẩu";

    // Product
    public static final String MSG_PRODUCT_CREATED = "thêm sản phẩm thành công.";
    public static final String MSG_PRODUCT_UPDATED = "cập nhật sản phẩm thành công.";
    public static final String MSG_PRODUCT_DELETED = "xóa sản phẩm thành công.";
    // Customer
    public static final String MSG_CUSTOMER_CREATED = "thêm khách hàng thành công.";
    public static final String MSG_CUSTOMER_UPDATED = "cập nhật khách hàng thành công.";
    public static final String MSG_CUSTOMER_DELETED = "xóa khách hàng thành công.";

    // Invoice
    public static final String MSG_INVOICE_CREATED = "tạo hóa đơn thành công.";

    // Validation
    public static final String ERR_REQUIRED = " hông được để trống.";
    public static final String ERR_POSITIVE = "phải >  0.";
    public static final String ERR_NON_NEGATIVE = " ko âm .";
    public static final String ERR_INVALID_EMAIL = "Email không hợp lệ.";
    public static final String ERR_INVALID_PHONE = "Số điện thoại không hợp lệ.";




}

