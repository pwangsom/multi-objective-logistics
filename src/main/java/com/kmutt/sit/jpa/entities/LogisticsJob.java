package com.kmutt.sit.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(schema = "public", name = "logistics_job")
public class LogisticsJob {
	@Id
	@Column(name="job_id")
	private String jobId;

	@Column(name="max_iteration")
	private Integer maxIteration;

	@Column(name="max_run")
	private Integer maxRun;

	@Column(name="vehicle_config")
	private String vehicleConfig;
}
