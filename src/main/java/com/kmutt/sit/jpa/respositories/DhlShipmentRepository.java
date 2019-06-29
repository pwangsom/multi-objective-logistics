package com.kmutt.sit.jpa.respositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kmutt.sit.jpa.entities.DhlShipment;

public interface DhlShipmentRepository extends JpaRepository<DhlShipment, Integer>{
	
	@Query("SELECT s FROM DhlShipment s WHERE actDt = ?1")
    List<DhlShipment> findByActDt(String actDt);
	
	@Query("SELECT DISTINCT actDt FROM DhlShipment")
	List<String> findDistinctActDt();

}
