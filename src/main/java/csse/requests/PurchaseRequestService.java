package csse.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseRequestService {

    private final RequestDAO repository;

    @Autowired
    public PurchaseRequestService(RequestDAO repository) {
        this.repository = repository;
    }

    public void cleanDatabase() {
        repository.deleteAll();
    }

    public PurchaseRequest createRequest(PurchaseRequest purchaseRequest) {
        RequestStatus status = RequestStatus.PENDING;
        purchaseRequest.setRequestStatus(status.name());
        purchaseRequest.setCreatedOn(new Date());
        return repository.save(purchaseRequest);
    }

    public PurchaseRequest updateRequest(PurchaseRequest purchaseRequest) {
        return repository.save(purchaseRequest);
    }

    public List<PurchaseRequest> getApprovedRequests() {
        List<String> statusList = new ArrayList<>();
        statusList.add(RequestStatus.APPROVED.name());
        statusList.add(RequestStatus.PROCESSING.name());
        return repository.getAllByRequestStatusIn(statusList);
    }

    public List<PurchaseRequest> fetchAll() {
        return  repository.findAll();
    }

    public List<PurchaseRequest> approveRequests(List<PurchaseRequest> purchaseRequests) {
        return repository.saveAll(purchaseRequests);
    }

    public boolean ordersCreatedForAllRequestItems(PurchaseRequest request) {
        for (RequestItem item : request.getRequestLineItems()) {
            if (!item.isPOCreated()) return false;
        }
        return true;
    }

}
