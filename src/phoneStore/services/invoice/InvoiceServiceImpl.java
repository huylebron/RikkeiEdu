package phoneStore.services.invoice;

import phoneStore.dao.invoice.IInvoiceDao;
import phoneStore.entity.Invoice;
import phoneStore.entity.InvoiceDetail;
import phoneStore.exception.DatabaseException;
import phoneStore.exception.NotFoundException;
import phoneStore.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InvoiceServiceImpl implements IInvoiceService {

    private final IInvoiceDao invoiceDao;

    public InvoiceServiceImpl(IInvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }


    /**
     * @param invoice
     * @return
     */
    @Override
    public Invoice createInvoiceService(Invoice invoice) {
        ValidateInvoiceCreate(invoice);
        try {
            return invoiceDao.createInvoiceDao(invoice);

        } catch (DatabaseException e) {
            throw new DatabaseException(" lỗi khi tạo hóa đơn ", e);
        }
    }

    private void ValidateInvoiceCreate(Invoice invoice) {

        if (invoice == null) throw new ValidationException(" hóa đơn không hợp lệ");
        if (invoice.getCustomer() == null || invoice.getCustomer().getId() == null) {
            throw new ValidationException("Vui lòng chọn khách hàng với id hợp lệ ");

        }
        List<InvoiceDetail> details = invoice.getDetails();
        if (details == null || details.isEmpty()) {
            throw new ValidationException("Hóa đơn phải có ít nhất 1 sản phẩm ");

        }
        //validate cho product

        details.forEach(detail -> {
            if (detail.getProduct() == null || detail.getProduct().getId() == null) {
                throw new ValidationException("thiếu id sản phẩm trong chi tiết hóa đơn");

            }
            if (detail.getQuantity() == null || detail.getQuantity() <= 0) {
                throw new ValidationException("số lượng phải > 0 ");

            }
        });

        // tránh trùng id trong 1 hóa đơn
        Map<Long, Long> countByProduct = details.stream()
                .collect(Collectors.groupingBy(detail -> detail.getProduct().getId(),
                        Collectors.counting()));

        boolean hasDuplicate = countByProduct.values().stream().anyMatch(count -> count > 1);
        if (hasDuplicate) {
            throw new ValidationException("Sản phẩm không được trùng nhau trong cùng một hóa đơn");
        }

    }

    /**
     * @return
     */
    @Override
    public List<Invoice> getAllInvoiceService() {
        return invoiceDao.findAllIvoiceDao();

    }

    /**
     * @param invoiceId
     * @return
     */
    @Override
    public Invoice getDetailsInvoiceService(long invoiceId) {
        return invoiceDao.findByIdDetail(invoiceId)
                .orElseThrow(() -> new NotFoundException("không tìm thấy hóa đơn id = " + invoiceId));


    }

    /**
     * @param customerKeyword
     * @param day
     * @return
     */
    @Override
    public List<Invoice> searchInvoiceServiceByDay(String customerKeyword, LocalDate day) {
        if (day == null) throw new ValidationException("Ngày không hợp lệ.");
        return invoiceDao.searchByCustomerAndDay(customerKeyword == null ? "" : customerKeyword, day);
    }

    /**
     * @param customerKeyword
     * @param year
     * @param month
     * @return
     */
    @Override
    public List<Invoice> searchInvoiceServiceByMonth(String customerKeyword, int year, int month) {
        if (year <= 0) throw new ValidationException("Năm không hợp lệ.");
        if (month < 1 || month > 12) throw new ValidationException("Tháng không hợp lệ .");
        return invoiceDao.searchByCustomerAndMonth(customerKeyword == null ? "" : customerKeyword, year, month);
    }

    /**
     * @param customerKeyword
     * @param year
     * @return
     */
    @Override
    public List<Invoice> searchInvoiceServiceByYear(String customerKeyword, int year) {
        if (year <= 0) throw new ValidationException("Năm không hợp lệ.");
        return invoiceDao.searchByCustomerAndYear(customerKeyword == null ? "" : customerKeyword, year);
    }


    //TODO statistic revenue by day , month , year

    // revenue day

    /**
     * @param day
     * @return
     */
    @Override
    public BigDecimal revenueByDayInvoiceService(LocalDate day) {

        if (day == null) throw new ValidationException("Ngày không hợp lệ.");
        return invoiceDao.getRevenueByDay(day);
    }

    // revenue month

    /**
     * @param year
     * @param month
     * @return
     */
    @Override
    public BigDecimal revenueByMonthInvoiceService(int year, int month) {

        if (year <= 0) throw new ValidationException("Năm không hợp lệ.");
        if (month < 1 || month > 12) throw new ValidationException("Tháng không hợp lệ .");
        return invoiceDao.getRevenueByMonth(year, month);

    }

    // revenue year

    /**
     * @param year
     * @return
     */
    @Override
    public BigDecimal revenueByYearInvoiceService(int year) {
        if (year <= 0) throw new ValidationException("Năm không hợp lệ.");
        return invoiceDao.getRevenueByYear(year);
    }
}



