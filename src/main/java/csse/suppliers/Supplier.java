package csse.suppliers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import csse.items.Item;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * @author Reeshma Hassen(hassenreeshma@gmail.com) on 9/17/2018.
 */
public class Supplier {

    @Id
    private String _id;

    private String supplierName;
    private String supplierAddress;
    private String supplierEmail;
    private String supplierContactNumber;

    @DBRef
    private List<Item> items;

    @JsonCreator
    public Supplier(
            @JsonProperty ("supplierName")String supplierName,
            @JsonProperty ("supplierAddress")String supplierAddress,
            @JsonProperty ("supplierContactNumber") String supplierContactNumber,
            @JsonProperty ("supplierEmail") String supplierEmail,
            @JsonProperty ("items") List<Item> items){
        this.supplierName=supplierName;
        this.supplierEmail=supplierEmail;
        this.supplierAddress=supplierAddress;
        this.supplierContactNumber=supplierContactNumber;
        this.items = items;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getEmail() {
        return supplierEmail;
    }

    public void setEmail(String email) {
        this.supplierEmail = email;
    }

    public String getAddress() {
        return supplierAddress;
    }

    public void setAddress(String address) {
        this.supplierAddress = address;
    }

    public String getContactNumber() {
        return supplierContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.supplierContactNumber = contactNumber;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
