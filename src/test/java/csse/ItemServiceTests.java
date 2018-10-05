package csse;

import csse.items.Item;
import csse.items.ItemService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class ItemServiceTests {

    @Autowired
    ItemService itemService;

    Item item1;

    @Before
    public void setUp() {
        itemService.cleanDatabase();
        System.out.println("Setup for tests");
        item1 = new Item("TestItem1", null, "Cat1", 200.00, "description");
    }

    @Test
    public void setsItemIdOnSave() {

        Item testItem = itemService.saveItem(item1);
        Assert.assertNotNull("After saving an item, the ObjectId should not be null ", testItem.get_id());
    }
}