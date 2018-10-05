package csse;

import csse.grn.Grn;
import csse.grn.GrnService;
import csse.grn.GrnStatus;
import csse.orders.OrderService;
import csse.orders.OrderStatus;
import csse.orders.PurchaseOrder;
import csse.requests.PurchaseRequest;
import csse.requests.PurchaseRequestService;
import csse.requests.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Damsith Karunaratna(dammakaru@gmail.com) on 10/03/2018.
 */
@Service
public class OrderServicesFacade implements IOrderServicesFacade {

    private PurchaseRequestService requestService;
    private OrderService orderService;
    private GrnService grnService;

    @Autowired
    public OrderServicesFacade(GrnService grnService, OrderService orderService, PurchaseRequestService requestService) {
        this.orderService = orderService;
        this.requestService = requestService;
        this.grnService = grnService;
    }

    @Override
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        purchaseOrder.setStatus(OrderStatus.PENDING_DELIVERY.name());
        purchaseOrder.setCreatedOn(new Date());
        if (requestService.ordersCreatedForAllRequestItems(purchaseOrder.getPurchaseRequest())) {
            purchaseOrder.getPurchaseRequest().setRequestStatus(RequestStatus.ORDERED.name());
        } else {
            purchaseOrder.getPurchaseRequest().setRequestStatus(RequestStatus.PROCESSING.name());
        }
        this.requestService.updateRequest(purchaseOrder.getPurchaseRequest());
        return orderService.saveOrder(purchaseOrder);
    }

    @Override
    public List<PurchaseOrder> getAllOrders() {
        return orderService.fetchAll();
    }

    @Override
    public List<PurchaseRequest> getAllRequests() {
        return requestService.fetchAll();
    }

    @Override
    public List<PurchaseRequest> getApprovedRequests() {
        return requestService.getApprovedRequests();
    }

    @Override
    public PurchaseRequest createRequest(PurchaseRequest request) {
        return requestService.createRequest(request);
    }

    @Override
    public List<PurchaseRequest> approveRequests(List<PurchaseRequest> purchaseRequests) {
        return requestService.approveRequests(purchaseRequests);
    }

    @Override
    public Grn createGrn(Grn grn) {
        grn.setPaymentStatus(GrnStatus.PENDING_PAYMENT.name());
        grn.setRecievedOn(new Date());
        if (orderService.allItemsReceived(grn.getPurchaseOrder())) {
            grn.getPurchaseOrder().setStatus(OrderStatus.DELIVERY_COMPLETED.name());
        } else {
            grn.getPurchaseOrder().setStatus(OrderStatus.PARTIALLY_DELIVERED.name());
        }
        this.orderService.updateOrder(grn.getPurchaseOrder());
        return grnService.saveGrn(grn);
    }

    @Override
    public void deleteGrns(List<Grn> grns) {
        grnService.deleteGrns(grns);
    }

    @Override
    public List<Grn> getAllGrns() {
        return grnService.fetchAll();
    }


}
