package csse.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import csse.items.Item;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class RequestItem {

    @DBRef
    private Item item;
    private int quantity;
    private boolean POCreated;

    @JsonCreator
    public RequestItem(
            @JsonProperty("item") Item item,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("POCreated") boolean POCreated) {
        this.item = item;
        this.quantity = quantity;
        this.POCreated = POCreated;
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

    public boolean isPOCreated() {
        return POCreated;
    }

    public void setPOCreated(boolean POCreated) {
        this.POCreated = POCreated;
    }
}
