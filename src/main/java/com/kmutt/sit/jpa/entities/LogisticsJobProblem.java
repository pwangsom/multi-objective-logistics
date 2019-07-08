package com.kmutt.sit.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(schema = "public", name = "logistics_job_problem")
public class LogisticsJobProblem {

	@Id
	@Column(name="problem_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer problemId;

	@Column(name="job_id")
	private String jobId;

	@Column(name="no_of_solutions")
	private Integer noOfSolutions;

	@Column(name="route_list")
	private String routeList;

	@Column(name="shipment_date")
	private String shipmentDate;

	@Column(name="shipment_list")
	private String shipmentList;

	@Column(name="vehicle_type")
	private String vehicleType;
	
}
