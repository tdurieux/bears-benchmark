package br.com.patiolegal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.patiolegal.domain.ProtocolRecord;

@Repository
public interface ProtocolRecordRepository extends MongoRepository<ProtocolRecord, String> {

}
