package br.com.patiolegal.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.patiolegal.domain.Protocol;

@Repository
public interface EntranceRepository extends MongoRepository<Protocol, String>, QuerydslPredicateExecutor<Protocol> {

	@Query(value = "{'entrance.vehicle.originalPlate': :#{#originalPlate}, 'exit': null}")
	List<Protocol> findOriginalPlateWithoutExit(@Param("originalPlate") String originalPlate);

	@Query(value = "{'entrance.vehicle.chassis': :#{#chassis}, 'exit': null}")
	List<Protocol> findChassisWithoutExit(@Param("chassis") String chassis);

	List<Protocol> findByProtocol(String protocol);

	@Query(value = "{'entrance.location.shed.initials': :#{#shed}, 'entrance.location.row': :#{#row}, 'entrance.location.floor': :#{#floor}, 'entrance.location.column': :#{#column}, 'exit': null }")
	List<Protocol> findLocationWithoutExit(@Param("shed") String shed, @Param("row") String row,
			@Param("floor") String floor, @Param("column") String column);

}
