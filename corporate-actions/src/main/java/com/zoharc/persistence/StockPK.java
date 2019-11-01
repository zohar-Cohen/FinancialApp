package com.zoharc.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The primary key class for the stock table.
 * @author Zohar Cohen Date: Sep 5th 2019
 * 
 * 
 */
@Embeddable 
@Builder @AllArgsConstructor @Getter @Setter @NoArgsConstructor 
public class StockPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="MAPPED_STOCK", unique=true, nullable=false, length=32)
	private String mappedStock;

	@Column(name="EXCHANGE_CODE", insertable=false, updatable=false, unique=true, nullable=false, length=32)
	private String exchangeCode;

	@Column(name="STOCK_CURRENCY", unique=true, nullable=false, length=160)
	private String stockCurrency;
	
	@Column(name="SECURITY_ID")
	private String securityId;
	
}