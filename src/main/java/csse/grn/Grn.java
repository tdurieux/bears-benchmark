package csse.grn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import csse.orders.PurchaseOrder;
import csse.suppliers.Supplier;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;
import java.util.List;

public class Grn {

    @Id
    private String _id;
    @DBRef
    private PurchaseOrder purchaseOrder;
    @DBRef
    private Supplier supplier;
    private Date recievedOn;
    private String paymentStatus;
    private List<GrnLineItem> grnItems;
    private double totalPrice;


    @JsonCreator
    public Grn(
            @JsonProperty("purchaseOrder") PurchaseOrder purchaseOrder,
            @JsonProperty("supplier") Supplier supplier,
            @JsonProperty("recievedOn") Date recievedOn,
            @JsonProperty("paymentStatus") String paymentStatus,
            @JsonProperty("orderItems") List<GrnLineItem> grnItems,
            @JsonProperty("totalPrice") double totalPrice
    ) {
        this.purchaseOrder = purchaseOrder;
        this.supplier = supplier;
        this.recievedOn = recievedOn;
        this.paymentStatus = paymentStatus;
        this.grnItems = grnItems;
        this.totalPrice = totalPrice;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Date getRecievedOn() {
        return recievedOn;
    }

    public void setRecievedOn(Date recievedOn) {
        this.recievedOn = recievedOn;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String status) {
        this.paymentStatus = paymentStatus;
    }

    public List<GrnLineItem> getGrnItems() {
        return grnItems;
    }

    public void setGrnItems(List<GrnLineItem> grnItems) {
        this.grnItems = grnItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

