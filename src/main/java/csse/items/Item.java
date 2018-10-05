package csse.items;

/**
 * @author Udana Rathnayaka on 9/17/2018.
 */
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import csse.suppliers.Supplier;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class Item {

    @Id
    private String _id;
    @Indexed(unique = true)
    private String itemName;
    @DBRef
    private Supplier supplier;
    private String category;
    private Double price;
    private String description;

    @JsonCreator
    public Item(
            @JsonProperty("itemName") String itemName,
            @JsonProperty("supplier") Supplier supplier,
            @JsonProperty("category") String category,
            @JsonProperty("price") Double price,
            @JsonProperty("description") String description
    ) {
        this.itemName = itemName;
        this.supplier = supplier;
        this.category = category;
        this.price = price;
        this.description = description;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
