package phoneStore.dao.invoice;

import phoneStore.entity.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IInvoiceDao {

    Invoice createInvoiceDao(Invoice invoice);
    List<Invoice> findAllINvoiceDao();
    Optional<Invoice> findByIdDetail( Long invoiceId) ;
// search invoice
    List<Invoice> searchByCustomerAndDay(String customerKeyword, LocalDate day);

    List<Invoice> searchByCustomerAndMonth(String customerKeyword, int year, int month);

    List<Invoice> searchByCustomerAndYear(String customerKeyword, int year);

    // Thống kê doanh thu
    BigDecimal getRevenueByDay(LocalDate day);

    BigDecimal getRevenueByMonth(int year, int month);

    BigDecimal getRevenueByYear(int year);


}
