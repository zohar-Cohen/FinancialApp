package com.zoharc.persistence;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the exchange database table.
 * 
 */
@Entity
public class Exchange implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EXCHANGE_CODE")
	private String exchangeCode;

	@Column(name="EXCHANGE_COUNTRY")
	private String exchangeCountry;

	@Column(name="EXCHANGE_NAME")
	private String exchangeName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SYS_CREATION_DATE")
	private Date sysCreationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SYS_UPDATE_DATE")
	private Date sysUpdateDate;

	//bi-directional many-to-one association to Stock
	@OneToMany(mappedBy="exchange")
	//@JsonBackReference
	//@JsonManagedReference
	private List<Stock> stocks;

	public Exchange() {
	}

	public String getExchangeCode() {
		return this.exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	public String getExchangeCountry() {
		return this.exchangeCountry;
	}

	public void setExchangeCountry(String exchangeCountry) {
		this.exchangeCountry = exchangeCountry;
	}

	public String getExchangeName() {
		return this.exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
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

	public List<Stock> getStocks() {
		return this.stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public Stock addStock(Stock stock) {
		getStocks().add(stock);
		stock.setExchange(this);

		return stock;
	}

	public Stock removeStock(Stock stock) {
		getStocks().remove(stock);
		stock.setExchange(null);

		return stock;
	}

}