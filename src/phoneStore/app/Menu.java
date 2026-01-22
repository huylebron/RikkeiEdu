package phoneStore.app;

import phoneStore.constant.MenuConstant;
import phoneStore.dao.customer.CustomerDaoImpl;
import phoneStore.dao.invoice.InvoiceDaoImpl;
import phoneStore.dao.product.ProductDaoImpl;
import phoneStore.exception.ConsoleErrorHandler;
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

    public Menu() {
        IProductService productService = new ProductServiceImpl(new ProductDaoImpl());
        ICustomerService customerService = new CustomerServiceImpl(new CustomerDaoImpl());
        IInvoiceService invoiceService = new InvoiceServiceImpl(new InvoiceDaoImpl());

        this.productHandler = new ProductMenuHandler(productService);
        this.customerHandler = new CustomerMenuHandler(customerService);
        this.invoiceHandler = new InvoiceMenuHandler(invoiceService, customerService, productService);
    }


    public void mainMenu() {
        while (true) {
            try {
                System.out.println(MenuConstant.MAIN_MENU);
                int choice = InputUtil.readIntInRange("Chon: ", 0, 3);

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
                    case 0:
                        System.out.println("Thoat chuong trinh.");
                        return;
                    default:
                        break;
                }
            } catch (Exception e) {
                ConsoleErrorHandler.handle(e);
            }
        }
    }
}
