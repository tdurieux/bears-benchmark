package csse.orders;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import csse.requests.PurchaseRequest;
import csse.suppliers.Supplier;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;
import java.util.List;

/**
 * @author Damsith Karunaratna(damamkaru@gmail.com)
 */
public class PurchaseOrder {

    @Id
    private String _id;
    @DBRef
    private PurchaseRequest purchaseRequest;
    @DBRef
    private Supplier supplier;
    private Date createdOn;
    private String status;
    private List<OrderLineItem> orderItems;
    private double totalPrice;

    @JsonCreator
    public PurchaseOrder(
            @JsonProperty("purchaseRequest") PurchaseRequest purchaseRequest,
            @JsonProperty("supplier") Supplier supplier,
            @JsonProperty("createdOn") Date createdOn,
            @JsonProperty("status") String status,
            @JsonProperty("orderItems") List<OrderLineItem> orderItems,
            @JsonProperty("totalPrice") double totalPrice
    ) {
        this.purchaseRequest = purchaseRequest;
        this.supplier = supplier;
        this.createdOn = createdOn;
        this.status = status;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public PurchaseRequest getPurchaseRequest() {
        return purchaseRequest;
    }

    public void setPurchaseRequest(PurchaseRequest purchaseRequest) {
        this.purchaseRequest = purchaseRequest;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderLineItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderLineItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}