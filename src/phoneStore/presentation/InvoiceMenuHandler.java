package phoneStore.presentation;

import phoneStore.constant.MenuConstant;
import phoneStore.entity.Customer;
import phoneStore.entity.Invoice;
import phoneStore.entity.InvoiceDetail;
import phoneStore.entity.Product;
import phoneStore.exception.ConsoleErrorHandler;
import phoneStore.services.customer.ICustomerService;
import phoneStore.services.invoice.IInvoiceService;
import phoneStore.services.product.IProductService;
import phoneStore.utils.InputUtil;
import phoneStore.utils.TablePrinter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static phoneStore.utils.TablePrinter.safe;

public class InvoiceMenuHandler {

    private final IInvoiceService invoiceService;
    private final ICustomerService customerService;
    private final IProductService productService;

    public InvoiceMenuHandler(IInvoiceService invoiceService, ICustomerService customerService,
            IProductService productService) {
        this.invoiceService = invoiceService;
        this.customerService = customerService;
        this.productService = productService;
    }

    // ham hien thi menu invoice

    public void run() {

        while (true) {

            try {
                System.out.println(MenuConstant.INVOICE_MENU);
                int choice = InputUtil.readIntInRange("Chọn", 0, 4);

                switch (choice) {
                    case 1:
                        InputUtil.executeWithCancel(this::createInvoice);
                        break;
                    case 2:
                        listInvoice();
                        break;
                    case 3:
                        invoiceSearchMenu();
                        break;
                    case 4:
                        revenueMenu();
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

    // revenue

    private void revenueMenu() {

        while (true) {
            try {
                System.out.println(MenuConstant.REVENUE_MENU);
                int choice = InputUtil.readIntInRange("Chon: ", 0, 3);

                switch (choice) {
                    case 1:
                       InputUtil.executeWithCancel(this::revenueByDay);
                        break;
                    case 2:
                        InputUtil.executeWithCancel(this::revenueByMonth);
                        break;
                    case 3:
                        InputUtil.executeWithCancel(this::revenueByYear);
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

    private void revenueByYear() {
        int year = InputUtil.readInt("Nhap nam: ");
        BigDecimal v = invoiceService.revenueByYearInvoiceService(year);
        System.out.println("Doanh thu nam " + year + ": " + TablePrinter.fmtMoney(v));

    }

    // renenue by month

    private void revenueByMonth() {

        int year = InputUtil.readInt("Nhap nam: ");
        int month = InputUtil.readIntInRange("Nhap thang: ", 1, 12);
        BigDecimal v = invoiceService.revenueByMonthInvoiceService(year, month);
        System.out.println("Doanh thu thang " + month + "/" + year + ": " + TablePrinter.fmtMoney(v));
    }

    private void revenueByDay() {
        LocalDate day = InputUtil.readLocalDate("Nhap ngày ", "dd/MM/yyyy");
        BigDecimal v = invoiceService.revenueByDayInvoiceService(day);
        System.out.println("Doanh thu ngày  " + day + "  là  : " + TablePrinter.fmtMoney(v));

    }

    // seach invoice optional
    private void invoiceSearchMenu() {

        while (true) {
            try {
                System.out.println(MenuConstant.INVOICE_SEARCH_MENU);
                int choice = InputUtil.readIntInRange("Chọn: ", 0, 3);

                switch (choice) {
                    case 1:
                        InputUtil.executeWithCancel(this::searchInvoiceByDay);
                        break;
                    case 2:
                        InputUtil.executeWithCancel(this::searchInvoiceByMonth);
                        break;
                    case 3:
                        InputUtil.executeWithCancel(this::searchInvoiceByYear);
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

    private void searchInvoiceByYear() {
        String keyword = InputUtil.readString("nhập từ khóa tên khách : ");
        int year = InputUtil.readInt("Nhap năm: ");

        List<Invoice> list = invoiceService.searchInvoiceServiceByYear(keyword, year);
        printInvoiceSearchResult(list);
        showInvoiceDetails();

    }

    // search invoice by month

    private void searchInvoiceByMonth() {

        String keyword = InputUtil.readString(" nhập từ khóa tên khách :  ");
        int year = InputUtil.readInt("Nhap năm : ");
        int month = InputUtil.readIntInRange("Nhap tháng : ", 1, 12);

        List<Invoice> list = invoiceService.searchInvoiceServiceByMonth(keyword, year, month);
        printInvoiceSearchResult(list);
        showInvoiceDetails();

    }

    // search invoive by day

    private void searchInvoiceByDay() {

        String keyword = InputUtil.readString(" nhập từ khóa tên khách : ");
        LocalDate day = InputUtil.readLocalDate("Nhap ngày ", "dd/MM/yyyy");
        List<Invoice> list = invoiceService.searchInvoiceServiceByDay(keyword, day);
        printInvoiceSearchResult(list);
        showInvoiceDetails();

    }

    private void showInvoiceDetails() {
        long id = InputUtil.readLong(" nhập id hóa đơn để xem chi tiết ( nhập 0 để bỏ qua )  :  ");
        if (id == 0)
            return;

        Invoice full = invoiceService.getDetailsInvoiceService(id);
        printInvoice(full);

    }

    private void printInvoiceSearchResult(List<Invoice> list) {
        TablePrinter.printTable(
                "kết quả tìm kiếm hóa đơn : ",
                new String[] { "mã hóa đơn ", "khách hàng ", "ngày tạo ", "tổng tiền " },
                list,
                i -> new String[] {
                        String.valueOf(i.getId()),
                        i.getCustomer() != null ? i.getCustomer().getName() : "",
                        i.getCreatedAt() != null ? i.getCreatedAt().format(TablePrinter.DATETIME_FMT) : "",
                        TablePrinter.fmtMoney(i.getTotalAmount())
                });

    }

    // show all invoice
    private void listInvoice() {

        List<Invoice> invoices = invoiceService.getAllInvoiceService();

        TablePrinter.printTable(
                "danh sách hóa đơn tổng hợp : ",
                new String[] { "id hóa đơn ", "khách hàng ", "ngày tạo ", "tổng tiền " },
                invoices,
                i -> new String[] {
                        String.valueOf(i.getId()),
                        i.getCustomer() != null ? i.getCustomer().getName() : "",
                        i.getCreatedAt() != null ? i.getCreatedAt().format(TablePrinter.DATETIME_FMT) : "",
                        TablePrinter.fmtMoney(i.getTotalAmount())
                });

    }

    // create invoice
    private void createInvoice() {

        Customer customer = chooseCustomer();

        if (customer == null)
            return;

        List<InvoiceDetail> details = new ArrayList<>();
        while (true) {
            long pid = InputUtil.readLong(" Nhập ID sản phẩm (Nhấn 0 để hoàn tất hóa đơn): ");
            if (pid == 0)
                break;

            Product product = productService.getByIdProductService(pid);
            System.out.println(" Chọn: " + product.getName() + " -- Tồn kho hiện tại: " + product.getStock());

            int quantity = 0;
            boolean quantityAdd = false;

            while (true) {
                quantity = InputUtil.readIntInRange("  Số lượng mua: ", 1, Integer.MAX_VALUE);

                if (quantity > product.getStock()) {
                    System.out
                            .println(" Lỗi: Số lượng " + quantity + " vượt quá tồn kho (" + product.getStock() + ")!");
                    System.out.println("  1. Nhập lại số lượng khác");
                    System.out.println("  2. Đổi sang mã sản phẩm khác");
                    int subChoice = InputUtil.readIntInRange("  Lựa chọn: ", 1, 2);
                    if (subChoice == 2)
                        break;
                } else {
                    quantityAdd = true;
                    break;
                }
            }

            if (quantityAdd) {
                InvoiceDetail detail = new InvoiceDetail();
                detail.setProduct(product);
                detail.setQuantity(quantity);
                details.add(detail);
                System.out.println("  Đã thêm thành công.");
            }
        }

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setDetails(details);

        Invoice created = invoiceService.createInvoiceService(invoice);

        System.out.println("tạo hóa đơn thành công ! id hóa đơn : " + created.getId()
                + " tổng tiền = " + TablePrinter.fmtMoney(created.getTotalAmount()));

        Invoice full = invoiceService.getDetailsInvoiceService(created.getId());
        printInvoice(full);

    }

    private void printInvoice(Invoice invoice) {

        TablePrinter.printTable(
                "HÓA ĐƠN",
                new String[] { "Mã hóa đơn", "Khách hàng", "Ngày tạo", "Tổng cộng" },
                List.of(invoice),
                i -> new String[] {
                        String.valueOf(i.getId()),
                        i.getCustomer() != null ? i.getCustomer().getName() : "",
                        i.getCreatedAt() != null ? i.getCreatedAt().format(TablePrinter.DATETIME_FMT) : "",
                        TablePrinter.fmtMoney(i.getTotalAmount())
                });

        List<InvoiceDetail> details = invoice.getDetails() == null ? List.of() : invoice.getDetails();

        TablePrinter.printTable(
                "CHI TIẾT HÓA ĐƠN",
                new String[] { "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền" },
                details,
                d -> new String[] {
                        d.getProduct() != null ? String.valueOf(d.getProduct().getId()) : "",
                        d.getProduct() != null ? safe(d.getProduct().getName()) : "",
                        String.valueOf(d.getQuantity()),
                        TablePrinter.fmtMoney(d.getUnitPrice()),
                        TablePrinter.fmtMoney(d.getLineTotal())
                });

    }

    private Customer chooseCustomer() {

        System.out.println("\n--- CHỌN KHÁCH HÀNG ---");
        System.out.println("1. Nhập ID khách hàng");
        System.out.println("2. Tìm theo tên (hiển thị danh sách, chọn ID)");
        System.out.println("0. Hủy");

        int c = InputUtil.readIntInRange("Chọn: ", 0, 2);
        if (c == 0)
            return null;

        if (c == 1) {
            long id = InputUtil.readLong("Nhập ID khách hàng: ");
            return customerService.getByIdCustomerService(id);
        }

        String keyword = InputUtil.readString("Nhập từ khóa: ");
        List<Customer> all = customerService.getAllCustomerService();

        List<Customer> filtered = all.stream()
                .filter(customer -> customer.getName() != null &&
                        customer.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        TablePrinter.printTable(
                "DANH SÁCH KHÁCH HÀNG (LỌC)",
                new String[] { "ID", "Tên", "Số điện thoại", "Email" },
                filtered,
                x -> new String[] {
                        String.valueOf(x.getId()),
                        x.getName(),
                        safe(x.getPhone()),
                        safe(x.getEmail())
                });

        long id = InputUtil.readLong("Nhập ID khách hàng muốn chọn: ");
        return customerService.getByIdCustomerService(id);

    }

}
