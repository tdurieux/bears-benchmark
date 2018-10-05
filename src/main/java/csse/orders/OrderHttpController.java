package csse.orders;

import csse.IOrderServicesFacade;
import csse.OrderServicesFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Damsith Karunaratna(dammakaru@gmail.com) on 9/27/2018.
 */
@RestController
public class OrderHttpController {

    /**
     * The Facade class 'OrderServicesFacade' is injected using constructor injection via the spring IoC container
     * It provides the HttpController with access to a simplified interface to
     * the underlying services.
     */
    private final IOrderServicesFacade orderService;

    @Autowired
    public OrderHttpController(OrderServicesFacade orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public List<PurchaseOrder> getAllOrdersEndpoint() {
        return orderService.getAllOrders();
    }

    @PostMapping("/orders")
    public ResponseEntity createOrderEndpoint(@RequestBody PurchaseOrder order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createPurchaseOrder(order));
    }

}
