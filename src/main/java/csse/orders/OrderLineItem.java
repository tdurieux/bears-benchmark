package csse.orders;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import csse.items.Item;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author Damsith Karunaratna(dammakaru@gmail.com) on 9/26/2018.
 */
public class OrderLineItem {

    @DBRef
    private Item item;
    private int quantity;
    private double orderLinePrice;
    private boolean received;

    @JsonCreator
    public OrderLineItem(
            @JsonProperty("item") Item item,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("orderLinePrice") double orderLinePrice,
            @JsonProperty("received") boolean received) {
        this.item = item;
        this.quantity = quantity;
        this.orderLinePrice = orderLinePrice;
        this.received = received;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getOrderLinePrice() {
        return orderLinePrice;
    }

    public void setOrderLinePrice(double orderLinePrice) {
        this.orderLinePrice = orderLinePrice;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }
}
