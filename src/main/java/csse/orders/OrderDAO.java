package csse.orders;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This DAO interface is used by spring-data-mongodb internally to create a Data access object
 * @author Damsith Karunaratna(dammakaru@gmail.com) on 9/26/2018.
 */
public interface OrderDAO extends MongoRepository<PurchaseOrder, String> {

}
