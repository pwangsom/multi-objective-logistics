package com.kmutt.sit.jpa.respositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmutt.sit.jpa.entities.DhlRoute;

public interface DhlRouteRespository extends JpaRepository<DhlRoute, Integer>{
	
	List<DhlRoute> findByVehicleTypeIn(List<String> vehicleTypes);
	
	List<DhlRoute> findByVehicleTypeInOrderByChromosomeIdAsc(List<String> vehicleTypes);	

	List<DhlRoute> findByVehicleTypeInOrderByChromosomeIdDesc(List<String> vehicleTypes);

}
