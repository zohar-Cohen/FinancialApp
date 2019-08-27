package com.zoharc.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the stock database table.
 * 
 */
@Embeddable
public class StockPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="STOCK_SEQ_NUM", unique=true, nullable=false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int stockSeqNum;

	@Column(name="MAPPED_STOCK", unique=true, nullable=false, length=32)
	private String mappedStock;

	@Column(name="EXCHANGE_CODE", insertable=false, updatable=false, unique=true, nullable=false, length=32)
	private String exchangeCode;

	@Column(name="STOCK_CURRENCY", unique=true, nullable=false, length=160)
	private String stockCurrency;

	@Column(name="SECURITY_ID", insertable=false, updatable=false, unique=true, nullable=false)
	private int securityId;

	public StockPK() {
	}
	public int getStockSeqNum() {
		return this.stockSeqNum;
	}
	public void setStockSeqNum(int stockSeqNum) {
		this.stockSeqNum = stockSeqNum;
	}
	public String getMappedStock() {
		return this.mappedStock;
	}
	public void setMappedStock(String mappedStock) {
		this.mappedStock = mappedStock;
	}
	public String getExchangeCode() {
		return this.exchangeCode;
	}
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}
	public String getStockCurrency() {
		return this.stockCurrency;
	}
	public void setStockCurrency(String stockCurrency) {
		this.stockCurrency = stockCurrency;
	}
	public int getSecurityId() {
		return this.securityId;
	}
	public void setSecurityId(int securityId) {
		this.securityId = securityId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof StockPK)) {
			return false;
		}
		StockPK castOther = (StockPK)other;
		return 
			(this.stockSeqNum == castOther.stockSeqNum)
			&& this.mappedStock.equals(castOther.mappedStock)
			&& this.exchangeCode.equals(castOther.exchangeCode)
			&& this.stockCurrency.equals(castOther.stockCurrency)
			&& (this.securityId == castOther.securityId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.stockSeqNum;
		hash = hash * prime + this.mappedStock.hashCode();
		hash = hash * prime + this.exchangeCode.hashCode();
		hash = hash * prime + this.stockCurrency.hashCode();
		hash = hash * prime + this.securityId;
		
		return hash;
	}
}