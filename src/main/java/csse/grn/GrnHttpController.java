package csse.grn;

import csse.IOrderServicesFacade;
import csse.OrderServicesFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GrnHttpController {

    private final IOrderServicesFacade grnService;

    @Autowired
    public GrnHttpController(OrderServicesFacade grnService) {
        this.grnService = grnService;
    }

    @GetMapping("/Grn")
    public List<Grn> getAllGrnEndpoint() {
        return grnService.getAllGrns();
    }

    @DeleteMapping("/Grn")
    public void deleteGrnEndpoint(@RequestBody List<Grn> grns) {
        grnService.deleteGrns(grns);
    }

    @PostMapping("/Grn")
    public ResponseEntity createGrnEndpoint(@RequestBody Grn grn) {
        return ResponseEntity.status(HttpStatus.CREATED).body(grnService.createGrn(grn));
    }
}
