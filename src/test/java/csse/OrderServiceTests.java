package csse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import csse.orders.OrderService;
import csse.orders.PurchaseOrder;
import org.junit.After;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Damsith Karunaratna(dammakaru@gmail.com) on 10/2/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class OrderServiceTests {

    @Autowired
    private OrderService orderService;

    private Logger logger = LoggerFactory.getLogger(OrderServiceTests.class);
    private List<PurchaseOrder> testOrdersList = new ArrayList<>();
    private PurchaseOrder testOrder;

    @Before
    public void setUp() throws IOException {

        // Load dummy order data from json file
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<PurchaseOrder> typeReference = new TypeReference<PurchaseOrder>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/order.json");
        testOrder = mapper.readValue(inputStream, typeReference);
        logger.info("Loaded dummy order data");

    }

    @After
    public void tearDown() {
        // Clean orders database
        orderService.cleanDatabase();
    }

    @Test
    public void setsOrderIdOnSave() throws Exception {
        logger.info("Running setsOrderIdOnSave()");
        PurchaseOrder savedOrder = orderService.saveOrder(testOrder);
        Assert.assertNotNull("After creation OrderID should not be null", savedOrder.get_id());
    }

}
