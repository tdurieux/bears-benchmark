package csse.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Udana Rathnayaka
 */
@Service
public class ItemService {

    private final ItemDAO repository;

    @Autowired
    public ItemService(ItemDAO repository) {
        this.repository = repository;
    }

    public void cleanDatabase() {
        repository.deleteAll();
    }

    public void deleteItems(List<Item> items) {
        this.repository.deleteAll(items);
    }

    public Item saveItem(Item i) {
        return repository.save(i);
    }

    public List<Item> fetchAll() {
        return  repository.findAll();
    }

    public Item fetchByItemName(String itemName) {
        return repository.findByItemName(itemName);
    }

    public List<Item> fetchByCategory(String category) {
        return repository.findByCategory(category);
    }

    public Item updateItem(Item item) {
        return repository.save(item);
    }
}
