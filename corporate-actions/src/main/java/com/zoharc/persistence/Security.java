package com.zoharc.persistence;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



/**
 * The persistent class for the security database table.
 * 
 */
@Entity
@Table(name="security")
@Getter @Setter @NoArgsConstructor
public class Security extends AuditEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SECURITY_SEQ_NUM", unique=true, nullable=false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long securitySeqNum;

	@Column(name="EXTERNAL_KEY1")
	private int externalKey1;

	@Column(name="EXTERNAL_KEY2")
	private int externalKey2;

	@Column(length=13)
	private String isin;

	@Column(name="PRIMARY_EXCHANGE", length=100)
	private String primaryExchange;

	@Column(name="SECURITY_TYPE", length=4)
	private String securityType;

	@Column(length=1)
	private String status;

	@Column(length=9)
	private String uscode;

	//bi-directional many-to-one association to Industry
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INDUSTRY_CODE")
	//@JsonManagedReference
	@JsonBackReference(value = "industry")
	private Industry industry;

	//bi-directional many-to-one association to Stock
	@OneToMany(mappedBy="security")
	//@JsonBackReference
	//@JsonManagedReference(value = "stocks")
	private Set<Stock> stocks;

	public Set<Stock> getStocks() {
		return this.stocks;
	}

	public void setStocks(Set<Stock> stocks) {
		this.stocks = stocks;
	}

	public Stock addStock(Stock stock) {
		getStocks().add(stock);
		stock.setSecurity(this);

		return stock;
	}

	public Stock removeStock(Stock stock) {
		getStocks().remove(stock);
		stock.setSecurity(null);

		return stock;
	}

}