package csse.items;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Udana Rathnayaka on 9/17/2018.
 */
public interface ItemDAO extends MongoRepository<Item, String> {

    Item findByItemName(String itemName);
    List<Item> findByCategory(String category);

}
