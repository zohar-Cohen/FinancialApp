package com.zoharc.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the stock database table.
 * 
 */
@Entity
@Table(name="stock")
public class Stock implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private StockPK id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EXPIRE_DATE")
	private Date expireDate;

	@Column(name="EXTERNAL_KEY1")
	private int externalKey1;

	@Column(name="EXTERNAL_KEY2")
	private int externalKey2;

	@Column(name="LIST_STATUS", length=1)
	private String listStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LISTED_DATE")
	private Date listedDate;

	@Column(name="PRIMARY_EXCHANGE", length=1)
	private String primaryExchange;

	@Column(name="RAW_STOCK", nullable=false, length=32)
	private String rawStock;

	@Column(length=7)
	private String sedol;

	@Column(name="STOCK_NAME", nullable=false, length=160)
	private String stockName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SYS_CREATION_DATE")
	private Date sysCreationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SYS_UPDATE_DATE")
	private Date sysUpdateDate;

	//bi-directional many-to-one association to Exchange
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="EXCHANGE_CODE", nullable=false, insertable=false, updatable=false)
	//@JsonManagedReference
	@JsonBackReference(value = "exchange")
	private Exchange exchange;

	//bi-directional many-to-one association to Security
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="SECURITY_ID", nullable=false, insertable=false, updatable=false)
	//@JsonManagedReference
	@JsonBackReference(value = "security")
	private Security security;

	public Stock() {
	}

	public StockPK getId() {
		return this.id;
	}

	public void setId(StockPK id) {
		this.id = id;
	}

	public Date getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public int getExternalKey1() {
		return this.externalKey1;
	}

	public void setExternalKey1(int externalKey1) {
		this.externalKey1 = externalKey1;
	}

	public int getExternalKey2() {
		return this.externalKey2;
	}

	public void setExternalKey2(int externalKey2) {
		this.externalKey2 = externalKey2;
	}

	public String getListStatus() {
		return this.listStatus;
	}

	public void setListStatus(String listStatus) {
		this.listStatus = listStatus;
	}

	public Date getListedDate() {
		return this.listedDate;
	}

	public void setListedDate(Date listedDate) {
		this.listedDate = listedDate;
	}

	public String getPrimaryExchange() {
		return this.primaryExchange;
	}

	public void setPrimaryExchange(String primaryExchange) {
		this.primaryExchange = primaryExchange;
	}

	public String getRawStock() {
		return this.rawStock;
	}

	public void setRawStock(String rawStock) {
		this.rawStock = rawStock;
	}

	public String getSedol() {
		return this.sedol;
	}

	public void setSedol(String sedol) {
		this.sedol = sedol;
	}

	public String getStockName() {
		return this.stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Date getSysCreationDate() {
		return this.sysCreationDate;
	}

	public void setSysCreationDate(Date sysCreationDate) {
		this.sysCreationDate = sysCreationDate;
	}

	public Date getSysUpdateDate() {
		return this.sysUpdateDate;
	}

	public void setSysUpdateDate(Date sysUpdateDate) {
		this.sysUpdateDate = sysUpdateDate;
	}

	public Exchange getExchange() {
		return this.exchange;
	}

	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}

	public Security getSecurity() {
		return this.security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	
	

}