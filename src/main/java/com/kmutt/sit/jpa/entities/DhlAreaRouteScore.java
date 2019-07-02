package com.kmutt.sit.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(schema = "public", name = "dhl_area_route_score")
public class DhlAreaRouteScore {

	@Id
	@Column(name="score_id")
	private Integer scoreId;

	@Column(name="area_code")
	private Integer areaCode;

	private String route;

	private Integer score;
	
}
