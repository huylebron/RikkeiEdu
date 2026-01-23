package phoneStore.dao.invoice;

import phoneStore.database.DBConnection;
import phoneStore.entity.Customer;
import phoneStore.entity.Invoice;
import phoneStore.entity.InvoiceDetail;
import phoneStore.entity.Product;
import phoneStore.exception.DatabaseException;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceDaoImpl  implements IInvoiceDao{
    /**
     * @param invoice
     * @return
     */
    @Override
    public Invoice createInvoiceDao(Invoice invoice) {

        if ( invoice.getCustomer() == null || invoice.getCustomer().getId() == null) {
            throw new IllegalArgumentException(" invoice phải có customer và id đúng ") ;

        }
        String sql = "call sp_invoice_create(?, ?, ?, ?, ?)";

        List<InvoiceDetail> details = invoice.getDetails() ;
        Long[] productIds = details.stream()
                .map(detail -> detail.getProduct().getId())
                .toArray(Long[]::new);

        Integer[] quantities = details.stream()
                .map(InvoiceDetail::getQuantity)
                .toArray(Integer[]::new) ;

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, invoice.getCustomer().getId());

            cs.setArray(2, conn.createArrayOf("bigint", productIds));
            cs.setArray(3, conn.createArrayOf("integer", quantities));

            cs.registerOutParameter(4, Types.BIGINT);
            cs.registerOutParameter(5, Types.NUMERIC);

            cs.execute();

            invoice.setId(cs.getLong(4));
            invoice.setTotalAmount(cs.getBigDecimal(5));

            return invoice;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi sp_invoice_create", e);
        }




    }

    /**
     * @return
     */
    @Override
    public List<Invoice> findAllIvoiceDao() {
        String sql = "SELECT * FROM fn_invoice_list_summary()";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            List<Invoice> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapInvoiceRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_invoice_list", e);
        }

    }

    private Invoice mapInvoiceRow(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getLong("invoice_id"));
        Customer customer = new Customer();
        customer.setId(rs.getLong("customer_id"));
        customer.setName(rs.getString("customer_name"));
        invoice.setCustomer(customer);

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            LocalDateTime createdAt = ts.toLocalDateTime();
            invoice.setCreatedAt(createdAt);
        }

        invoice.setTotalAmount(rs.getBigDecimal("total_amount"));
        return invoice;


    }

    /**
     * @param invoiceId
     * @return
     */
    @Override
    public Optional<Invoice> findByIdDetail(Long invoiceId) {
        Invoice invoice = findAllIvoiceDao().stream()
                .filter(invoice1 -> invoice1.getId() != null && invoice1.getId() == invoiceId)
                .findFirst()
                .orElse(null);

        if ( invoice == null) {
            return Optional.empty();
        }

        // show detail
        String sql = "SELECT * FROM fn_invoice_get_details(?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, invoiceId);
            try (ResultSet rs = cs.executeQuery()) {
                List<InvoiceDetail> details = new ArrayList<>();
                while (rs.next()) {
                    details.add(mapInvoiceDetailRow(rs));
                }
                invoice.setDetails(details);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_invoice_get_details", e);
        }

        return Optional.of(invoice);



    }

    /**
     * @param rs
     * @return invoiceDetail
     */
    private InvoiceDetail mapInvoiceDetailRow(ResultSet rs) throws SQLException {

        InvoiceDetail detail = new InvoiceDetail() ;
        detail.setId(rs.getLong("detail_id"));

        Product product = new Product();
        product.setId(rs.getLong("product_id"));
        product.setName(rs.getString("product_name"));
        detail.setProduct(product);

        detail.setQuantity(rs.getInt("quantity"));
        detail.setUnitPrice(rs.getBigDecimal("unit_price"));
        return detail;


    }

    /**
     * @param customerKeyword
     * @param day
     * @return
     */
    @Override
    public List<Invoice> searchByCustomerAndDay(String customerKeyword, LocalDate day) {
        String sql = "SELECT * FROM fn_invoice_search_by_customer_and_day(?, ?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, customerKeyword == null ? "" : customerKeyword);
            cs.setDate(2, Date.valueOf(day));

            try (ResultSet rs = cs.executeQuery()) {
                List<Invoice> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapInvoiceRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_invoice_search_by_customer_and_day", e);
        }
    }

    /**
     * @param customerKeyword
     * @param year
     * @param month
     * @return
     */
    @Override
    public List<Invoice> searchByCustomerAndMonth(String customerKeyword, int year, int month) {
        String sql = "SELECT * FROM fn_invoice_search_by_customer_and_month(?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, customerKeyword == null ? "" : customerKeyword);
            cs.setInt(2, year);
            cs.setInt(3, month);

            try (ResultSet rs = cs.executeQuery()) {
                List<Invoice> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapInvoiceRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_invoice_search_by_customer_and_month", e);
        }
    }

    /**
     * @param customerKeyword
     * @param year
     * @return
     */
    @Override
    public List<Invoice> searchByCustomerAndYear(String customerKeyword, int year) {
        String sql = "SELECT * FROM fn_invoice_search_by_customer_and_year(?, ?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, customerKeyword == null ? "" : customerKeyword);
            cs.setInt(2, year);

            try (ResultSet rs = cs.executeQuery()) {
                List<Invoice> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapInvoiceRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_invoice_search_by_customer_and_year", e);
        }
    }

    // TODO implement revenueDao  ở đây


    /**
     * @param day
     * @return
     */
    @Override
    public BigDecimal getRevenueByDay(LocalDate day) {
        String sql = "{ ? = call fn_revenue_by_day(?) }";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.registerOutParameter(1, Types.NUMERIC);
            cs.setDate(2, java.sql.Date.valueOf(day));

            cs.execute();

            BigDecimal v = cs.getBigDecimal(1);
            return v != null ? v : BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_revenue_by_day", e);
        }
    }

    /**
     * @param year
     * @param month
     * @return
     */
    @Override
    public BigDecimal getRevenueByMonth(int year, int month) {
        String sql = "{ ? = call fn_revenue_by_month(?, ?) }";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.registerOutParameter(1, Types.NUMERIC);
            cs.setInt(2, year);
            cs.setInt(3, month);

            cs.execute();

            BigDecimal v = cs.getBigDecimal(1);
            return v != null ? v : BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_revenue_by_month", e);
        }


    }

    /**
     * @param year
     * @return
     */
    @Override
    public BigDecimal getRevenueByYear(int year) {
        String sql = "{ ? = call fn_revenue_by_year(?) }";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.registerOutParameter(1, Types.NUMERIC);
            cs.setInt(2, year);

            cs.execute();

            BigDecimal v = cs.getBigDecimal(1);
            return v != null ? v : BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_revenue_by_year", e);
        }
    }
}
