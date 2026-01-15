package phoneStore.entity;

import java.sql.Timestamp;
import java.util.Objects;

public class Invoice {

    private int invoiceId;
    private int customerId;
    private Timestamp invoiceDate;
    private double totalAmount;

    public Invoice() {
    }


    public Invoice(int id, int customerId, Timestamp invoiceDate, double totalAmount) {
        this.invoiceId = id;
        this.customerId = customerId;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Timestamp getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Timestamp invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Invoice invoice)) return false;
        return invoiceId == invoice.invoiceId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(invoiceId);
    }

}
