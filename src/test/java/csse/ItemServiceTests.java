package csse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import csse.items.Item;
import csse.items.ItemService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class ItemServiceTests {

    @Autowired
    private ItemService itemService;
    private Logger logger = LoggerFactory.getLogger(ItemServiceTests.class);
    private List<Item> testItemList = new ArrayList<>();
    private Item item1;
    private Item item2;

    @Before
    public void setUp() {
        itemService.cleanDatabase();
        System.out.println("Setup for tests");
        item1 = new Item("TestItem1", null, "testing1", 200.00, "description");
        item2 = new Item("TestItem2", null, "testing2", 300.00, "description2");
    }

    @Test
    public void setsItemIdOnSave() {

        Item testItem = itemService.saveItem(item1);
        Assert.assertNotNull("After saving an item, the ObjectId should not be null ", testItem.get_id());
    }
    
    @Test
    public void allItems() throws Exception {
    	logger.info("Running setsItemIdOnSave");
    	Item savedItem1 =itemService.saveItem(item1);
    	Item savedItem2 =itemService.saveItem(item2);
    	List<Item> allItems = itemService.fetchAll();
    	Assert.assertEquals("Approved item size should be 2", 2, allItems.size());
    }
}
