package csse;

import csse.grn.Grn;
import csse.orders.PurchaseOrder;
import csse.requests.PurchaseRequest;

import java.util.List;

/**
 * @author Damsith Karunaratna(dammakaru@gmail.com) on 10/3/2018.
 */
public interface IOrderServicesFacade {
    PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder);
    List<PurchaseOrder> getAllOrders();
    List<PurchaseRequest> getAllRequests();
    List<PurchaseRequest> getApprovedRequests();
    PurchaseRequest createRequest(PurchaseRequest request);
    List<PurchaseRequest> approveRequests(List<PurchaseRequest> purchaseRequests);
    Grn createGrn(Grn grn);
    void deleteGrns(List<Grn> grns);
    List<Grn> getAllGrns();
}
