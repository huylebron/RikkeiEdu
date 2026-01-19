package phoneStore.app;

import phoneStore.constant.MenuConstant;
import phoneStore.entity.Customer;
import phoneStore.exception.ConsoleErrorHandler;
import phoneStore.services.customer.ICustomerService;
import phoneStore.utils.InputUtil;
import phoneStore.utils.TablePrinter;

import java.util.List;

import static phoneStore.utils.TablePrinter.safe;

public class CustomerMenuHandler {

    private final ICustomerService customerService;

    public CustomerMenuHandler(ICustomerService customerService) {
        this.customerService = customerService;
    }

    public void run() {
        while (true) {
            try {
                System.out.println(MenuConstant.CUSTOMER_MENU);
                int choice = InputUtil.readIntInRange("Chon: ", 0, 4);

                switch (choice) {
                    case 1:
                        createCustomer();
                        break;
                    case 2:
                        updateCustomer();
                        break;
                    case 3:
                        deleteCustomer();
                        break;
                    case 4:
                        listAllCustomer();
                        break;
                    case 0:
                        return;
                    default:
                        break;
                }
            } catch (Exception e) {
                ConsoleErrorHandler.handle(e);
            }
        }




}

    private void listAllCustomer() {

        List<Customer> customers = customerService.getAllCustomerService();

        TablePrinter.printTable(
                "danh sách khách hàng ",
                new String[]{"ID", "tên : ", "sdt: ", "Email", "địa chỉ: "},
                customers,
                c -> new String[]{
                        String.valueOf(c.getId()),
                        c.getName(),
                        safe(c.getPhone()),
                        safe(c.getEmail()),
                        safe(c.getAddress())
                }
        );
    }

    private void deleteCustomer() {

        long id = InputUtil.readLong("Nhap ID khach hang can xoa: ");
        customerService.deleteCustomerService(id);
        System.out.println("Xoa thanh cong.");
    }

    private void updateCustomer() {


        long id = InputUtil.readLong(" nhập id khách hàng cần cập nhật  ");
        Customer exist = customerService.getByIdCustomerService(id);
        System.out.println("Khach hang hien tai: " + exist);

        // Không cho sửa ID
        String name = InputUtil.readNotNull("Ten moi: ");
        String phone = InputUtil.readString("So dien thoai moi");
        String email = InputUtil.readString("Email moi");
        String address = InputUtil.readString("Dia chi moi");

        Customer c = new Customer(id, name, phone, email, address);
        customerService.updateCustomerService(c);

        System.out.println("Cap nhat thanh cong.");
    }

    private void createCustomer() {

        String name = InputUtil.readNotNull("tên khách hàng :  ");
        String phone = InputUtil.readString("số điện thoại : " );
        String email = InputUtil.readString("Email : ");
        String address = InputUtil.readString("Dia chi : ");

        Customer c = new Customer(null, name, phone, email, address);
        Customer created = customerService.createCustomerService(c);

        System.out.println("đã thêm khách hàng thành công . ID=" + created.getId());
    }
    }



