package csse.requests;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RequestDAO extends MongoRepository<PurchaseRequest, String> {
    public List<PurchaseRequest> getAllByRequestStatusIn(List<String> requestStatuses);
}
