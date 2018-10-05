package csse.grn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import csse.items.Item;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class GrnLineItem {
    @DBRef
    private Item item;
    private int quantity;

    @JsonCreator
    public GrnLineItem(
            @JsonProperty("item") Item item,
            @JsonProperty("quantity") int quantity) {
        this.item = item;
        this.quantity = quantity;
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

}
