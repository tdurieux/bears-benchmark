package csse.suppliers;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierDAO  extends MongoRepository<Supplier, String> {

    public Supplier findBySupplierName (String supplierName);

}
