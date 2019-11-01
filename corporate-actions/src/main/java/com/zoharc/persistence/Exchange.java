package com.zoharc.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the exchange database table.
 * 
 */
@Entity
@Table(name="exchange")
@Builder @AllArgsConstructor @Getter @Setter @NoArgsConstructor 
public class Exchange implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EXCHANGE_CODE", unique=true, nullable=false, length=32)
	private String exchangeCode;

	@Column(name="EXCHANGE_COUNTRY", length=100)
	private String exchangeCountry;

	@Column(name="EXCHANGE_NAME", nullable=false, length=400)
	private String exchangeName;

	@Column(name="MARKET_CODE", length=15)
	private String marketCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SYS_CREATION_DATE")
	private Date sysCreationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SYS_UPDATE_DATE")
	private Date sysUpdateDate;

	@OneToMany(mappedBy="exchange")
	private Set<Stock> stocks;

	public Set<Stock> getStocks() {
		return this.stocks;
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