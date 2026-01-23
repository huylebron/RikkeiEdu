package phoneStore.presentation;

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
                        InputUtil.executeWithCancel(this::createCustomer);
                        break;
                    case 2:
                        InputUtil.executeWithCancel(this::updateCustomer);
                        break;
                    case 3:
                        InputUtil.executeWithCancel(this::deleteCustomer);
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
                new String[] { "ID", "tên : ", "sdt: ", "Email", "địa chỉ: " },
                customers,
                c -> new String[] {
                        String.valueOf(c.getId()),
                        c.getName(),
                        safe(c.getPhone()),
                        safe(c.getEmail()),
                        safe(c.getAddress())
                });
    }

    private void deleteCustomer() {
        long id = InputUtil.readLong("Nhap ID khach hang can xoa: ");
        try {
            Customer exist = customerService.getByIdCustomerService(id);
            System.out.println("Sẽ xóa khách hàng: " + exist);
            if (InputUtil.readConfirm("Bạn có chắc chắn muốn xóa không?")) {
                customerService.deleteCustomerService(id);
                System.out.println("Xóa thành công. ID: " + id);
            } else {
                System.out.println("Đã hủy thao tác xóa.");
            }
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private void updateCustomer() {

        long id = InputUtil.readLong(" nhập id khách hàng cần cập nhật  ");
        Customer exist = customerService.getByIdCustomerService(id);
        System.out.println("Khach hang hien tai: " + exist);

        // Không cho sửa ID
        // Sử dụng giá trị mặc định nếu người dùng để trống
        String name = InputUtil.readStringDefault("Tên mới", exist.getName());
        String phone = InputUtil.readPhoneDefault("Số điện thoại mới", exist.getPhone());
        String email = InputUtil.readEmailDefault("Email mới", exist.getEmail());
        String address = InputUtil.readStringDefault("Địa chỉ mới", exist.getAddress());

        Customer c = new Customer(id, name, phone, email, address);
        customerService.updateCustomerService(c);

        System.out.println("Cap nhat thanh cong.");
    }

    private void createCustomer() {

        String name = InputUtil.readNotNull("tên khách hàng :  ");
        String phone = InputUtil.readPhone("số điện thoại : ");
        String email = InputUtil.readEmail("Email : ");
        String address = InputUtil.readString("Dia chi : ");

        Customer c = new Customer(null, name, phone, email, address);
        Customer created = customerService.createCustomerService(c);

        System.out.println("đã thêm khách hàng thành công . ID=" + created.getId() + "\n"
                + c.toString());
    }
}
