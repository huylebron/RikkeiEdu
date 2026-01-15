package phoneStore.entity;

import java.util.Objects;

public class InvoiceDetail {
    private int invoiceDetailId;
    private int invoiceId;
    private int productId;
    private int quantity;
    private double unitPrice;

    public InvoiceDetail() {}

    public InvoiceDetail(int id, int invoiceId, int productId, int quantity, double unitPrice) {
        this.invoiceDetailId = id;
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getter và Setter
    public int getId() { return invoiceDetailId; }
    public void setId(int id) { this.invoiceDetailId = id; }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof InvoiceDetail that)) return false;
        return invoiceDetailId == that.invoiceDetailId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(invoiceDetailId);
    }

}
