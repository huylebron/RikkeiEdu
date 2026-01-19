package phoneStore.services.invoice;

import phoneStore.entity.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IInvoiceService {

    Invoice createInvoiceService(Invoice invoice);

    List<Invoice> getAllInvoiceService();

    Invoice getDetailsInvoiceService(long invoiceId);

    List<Invoice> searchInvoiceServiceByDay(String customerKeyword, LocalDate day);

    List<Invoice> searchInvoiceServiceByMonth(String customerKeyword, int year, int month);

    List<Invoice> searchInvoiceServiceByYear(String customerKeyword, int year);

    BigDecimal revenueByDayInvoiceService(LocalDate day);

    BigDecimal revenueByMonthInvoiceService(int year, int month);

    BigDecimal revenueByYearInvoiceService(int year);
}
