package phoneStore.presentation;

import phoneStore.constant.MenuConstant;
import phoneStore.dao.customer.CustomerDaoImpl;
import phoneStore.dao.invoice.InvoiceDaoImpl;
import phoneStore.dao.product.ProductDaoImpl;
import phoneStore.entity.Admin;
import phoneStore.exception.ConsoleErrorHandler;
import phoneStore.services.authent.IAuthService;
import phoneStore.services.customer.CustomerServiceImpl;
import phoneStore.services.customer.ICustomerService;
import phoneStore.services.invoice.IInvoiceService;
import phoneStore.services.invoice.InvoiceServiceImpl;
import phoneStore.services.product.IProductService;
import phoneStore.services.product.ProductServiceImpl;
import phoneStore.utils.InputUtil;

public class Menu {

    private final ProductMenuHandler productHandler;
    private final CustomerMenuHandler customerHandler;
    private final InvoiceMenuHandler invoiceHandler;
    private final IAuthService authService;

    public Menu(IAuthService authService) {
        this.authService = authService;
        IProductService productService = new ProductServiceImpl(new ProductDaoImpl());
        ICustomerService customerService = new CustomerServiceImpl(new CustomerDaoImpl());
        IInvoiceService invoiceService = new InvoiceServiceImpl(new InvoiceDaoImpl());

        this.productHandler = new ProductMenuHandler(productService);
        this.customerHandler = new CustomerMenuHandler(customerService);
        this.invoiceHandler = new InvoiceMenuHandler(invoiceService, customerService, productService);
    }

    public void mainMenu(Admin loggedInAdmin) {
        try {
            while (true) {
                try {
                    System.out.println(MenuConstant.MAIN_MENU);
                    int choice = InputUtil.readIntInRange("Chọn: ", 0, 4);

                    switch (choice) {
                        case 1:
                            productHandler.run();
                            break;
                        case 2:
                            customerHandler.run();
                            break;
                        case 3:
                            invoiceHandler.run();
                            break;
                        case 4:
                            changePasswordMenu(loggedInAdmin);
                            break;
                        case 0:
                            System.out.println("Thoát chương trình.");
                            return;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    ConsoleErrorHandler.handle(e);
                }
            }
        } finally {
            InputUtil.setAllowCancel(false);
        }
    }

    private void changePasswordMenu(Admin admin) {
        System.out.println("\n--- ĐỔI MẬT KHẨU ---");
        String oldPass = InputUtil.readNotNull("Mật khẩu cũ: ");
        String newPass = InputUtil.readNotNull("Mật khẩu mới: ");
        String confirmPass = InputUtil.readNotNull("Xác nhận mật khẩu mới: ");

        if (!newPass.equals(confirmPass)) {
            System.err.println("Xác nhận mật khẩu không khớp!");
            return;
        }

        authService.changePassword(admin.getAdminId(), oldPass, newPass);
        System.out.println("Đổi mật khẩu thành công!");
    }
}
