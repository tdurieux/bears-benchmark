package csse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import csse.requests.PurchaseRequest;
import csse.requests.PurchaseRequestService;
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
public class PurchaseRequestServiceTests {

    @Autowired
    private PurchaseRequestService purchaseRequestService;

    private Logger logger = LoggerFactory.getLogger(PurchaseRequestServiceTests.class);
    private List<PurchaseRequest> testPurchaseRequestList = new ArrayList<>();
    private PurchaseRequest purchaseRequestPending;
    private PurchaseRequest purchaseRequestApproved;

    @Before
    public void setUp() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<PurchaseRequest> typeReference = new TypeReference<PurchaseRequest>() {};
        InputStream inputStream1 = TypeReference.class.getResourceAsStream("/json/purchaseRequestCreate.json");
        purchaseRequestPending  = mapper.readValue(inputStream1, typeReference);
        logger.info("loaded create request dummy data");

        InputStream inputStream2 = TypeReference.class.getResourceAsStream("/json/purchaseRequestApprove.json");
        purchaseRequestApproved = mapper.readValue(inputStream2, typeReference);
    }

    @After
    public void tearDown() {
        purchaseRequestService.cleanDatabase();
        logger.info("database cleaned");
    }

    @Test
    public void setsPurchaseRequestIdOnSave() throws Exception {
        logger.info("Running setsPurchaseRequestIdOnSave");
        PurchaseRequest savedRequest = purchaseRequestService.createRequest(purchaseRequestPending);
        Assert.assertNotNull("After creation RequestID should not be null", savedRequest.getId());
    }

//    @Test
//    public void getOnlyApprovedRequests() throws Exception {
//
//        logger.info("Running setsPurchaseRequestIdOnSave");
//        PurchaseRequest savedRequest1 = purchaseRequestService.createRequest(purchaseRequestPending);
//        PurchaseRequest savedRequest2 = purchaseRequestService.createRequest(purchaseRequestApproved);
////        PurchaseRequest savedRequest3 = purchaseRequestService.createRequest(purchaseRequestApproved);
//
//        List<PurchaseRequest> approvedRequests = purchaseRequestService.getApprovedRequests();
//        Assert.assertEquals("approved request size should be 1", 1, approvedRequests.size());
//    }

    @Test
    public void getAllRequests() throws Exception {
        logger.info("Running setsPurchaseRequestIdOnSave");
        PurchaseRequest savedRequest1 = purchaseRequestService.createRequest(purchaseRequestApproved);
        PurchaseRequest savedRequest2 = purchaseRequestService.createRequest(purchaseRequestPending);
//        PurchaseRequest savedRequest3 = purchaseRequestService.createRequest(purchaseRequestApproved);

        List<PurchaseRequest> allRequests = purchaseRequestService.fetchAll();
        Assert.assertEquals("approved request size should be 2", 2, allRequests.size());

    }


}
