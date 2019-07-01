package com.kmutt.sit.jpa.entities;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;


@Data
@Entity
@Table(schema = "public", name = "dhl_shipment")
public class DhlShipment  {

	@Id
	@Column(name="shipment_key")
	private Integer shipmentKey;

	@Column(name="act_base")
	private String actBase;

	@Column(name="act_ckpt_code")
	private String actCkptCode;

	@Temporal(TemporalType.DATE)
	@Column(name="act_date")
	private Date actDate;

	@Column(name="act_dt")
	private String actDt;

	@Column(name="act_tm")
	private Time actTm;

	@Column(name="act_tm_text")
	private String actTmText;

	private String address;

	@Column(name="ar_dtm")
	private Timestamp arDtm;

	@Column(name="ar_dtm_text")
	private String arDtmText;

	@Column(name="area_code")
	private Integer areaCode;

	@Column(name="awb_booking")
	private String awbBooking;

	private String city;

	private Time closed;

	@Column(name="closed_text")
	private String closedText;

	@Column(name="courier_id")
	private String courierId;

	@Column(name="courier_type")
	private String courierType;

	@Column(name="customer_name")
	private String customerName;
	
	@Column(name="cycle_operate")
	private String cycleOperate;

	@Column(name="delivery_type")
	private String deliveryType;

	//private Object geom;

	private BigDecimal lat;

	@Column(name="long")
	private BigDecimal long_;

	private Time opening;

	@Column(name="pallets_pcs")
	private Integer palletsPcs;

	@Column(name="parcel_pcs")
	private Integer parcelPcs;

	@Column(name="pickup_type")
	private String pickupType;

	@Column(name="prod_code")
	private String prodCode;

	@Column(name="prod_grp")
	private String prodGrp;

	@Column(name="pud_cycle")
	private String pudCycle;

	@Column(name="pud_fac")
	private String pudFac;

	@Column(name="pud_rte")
	private String pudRte;

	@Column(name="pud_svc_area")
	private String pudSvcArea;

	@Column(name="pud_type")
	private String pudType;

	@Column(name="shipment_count")
	private Integer shipmentCount;

	@Column(name="stop_code")
	private Integer stopCode;

	@Column(name="total_pcs")
	private Integer totalPcs;

	@Column(name="vehicle_type")
	private String vehicleType;

	private BigDecimal weight;

	private String zip;
}