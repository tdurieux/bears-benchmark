package csse.grn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GrnService  {

    private final GrnDAO repository;

    @Autowired
    public GrnService(GrnDAO repository) {
        this.repository = repository;
    }

    void cleanDatabase() {
        repository.deleteAll();
    }

    public void deleteGrns(List<Grn> grns) {
        this.repository.deleteAll(grns);
    }

    public Grn saveGrn(Grn grn) {
        return repository.save(grn); }

    public List<Grn> fetchAll() {
        return  repository.findAll();
    }
}
