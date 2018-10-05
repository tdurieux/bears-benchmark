package csse.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Damsith Karunaratna(dammakaru@gmail.com) on 9/27/2018.
 */
@Service
public class OrderService {

    private final OrderDAO repository;

    @Autowired
    public OrderService(OrderDAO repository) {
        this.repository = repository;
    }

    public void cleanDatabase() {
        repository.deleteAll();
    }

    public PurchaseOrder saveOrder(PurchaseOrder purchaseOrder) {
        OrderStatus status = OrderStatus.PENDING_DELIVERY;
        purchaseOrder.setStatus(status.name());
        purchaseOrder.setCreatedOn(new Date());
        return repository.save(purchaseOrder);
    }

    public List<PurchaseOrder> fetchAll() {
        return  repository.findAll();
    }

    public PurchaseOrder updateOrder(PurchaseOrder purchaseOrder) {
        return repository.save(purchaseOrder);
    }

    public boolean allItemsReceived(PurchaseOrder order) {
        for (OrderLineItem item : order.getOrderItems()) {
            if (!item.isReceived()) return false;
        }
        return true;
    }

}