package com.kmutt.sit.jpa.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(schema = "public", name = "logistics_job_result")
public class LogisticsJobResult {

	@Id
	@Column(name="solution_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer solutionId;

	@Column(name="normalized_objective_1")
	private BigDecimal normalizedObjective1;

	@Column(name="normalized_objective_2")
	private BigDecimal normalizedObjective2;

	@Column(name="normalized_objective_3")
	private BigDecimal normalizedObjective3;

	@Column(name="objective_1")
	private BigDecimal objective1;

	@Column(name="objective_2")
	private BigDecimal objective2;

	@Column(name="objective_3")
	private BigDecimal objective3;

	@Column(name="problem_id")
	private Integer problemId;

	@Column(name="solution_detail")
	private String solutionDetail;

	@Column(name="solution_index")
	private Integer solutionIndex;
}
