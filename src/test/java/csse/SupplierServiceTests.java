package csse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import csse.suppliers.Supplier;
import csse.suppliers.SupplierService;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class SupplierServiceTests {

    @Autowired
    private SupplierService supplierService;

    private Logger logger = LoggerFactory.getLogger(SupplierServiceTests.class);
    private List<Supplier> testSupplierList = new ArrayList<>();
    private Supplier testSupplier;

    @Before
    public void setUp() throws IOException {

        // Load dummy supplier data from json file
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Supplier> typeReference = new TypeReference<Supplier>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/supplier.json");
        testSupplier = mapper.readValue(inputStream, typeReference);
        logger.info("Loaded dummy supplier data");

    }

    @After
    public void tearDown() {
        // Clean supplier database
        supplierService.cleanDatabase();
    }

    @Test
    public void setsSupplierIdOnSave() throws Exception {
        logger.info("Running setsOrderIdOnSave()");
        Supplier  saveSupplier = supplierService.saveSupplier(testSupplier);
        Assert.assertNotNull("After creation OrderID should not be null", saveSupplier.get_id());
    }
}
