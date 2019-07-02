package com.kmutt.sit.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data
@Entity
@Table(schema = "public", name = "dhl_area")
public class DhlArea  {

	@Id
	@Column(name="area_code")
	private Integer areaCode;

	private String district;

	private String postcode;

	private String province;

	@Column(name="serviced_by_dhl")
	private String servicedByDhl;

}