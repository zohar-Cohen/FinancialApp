package com.zoharc.persistence;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the security table.
 * 
 */
@Entity 
@Table(name="security")
@Builder @AllArgsConstructor @Getter @Setter @NoArgsConstructor
//@EntityListeners(SecurityTransactionLog.class)
public class Security extends AuditEntity  {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(unique=true, nullable=false, length=13)
	private String isin;

	@Column(name="EXTERNAL_KEY1")
	private int externalKey1;

	@Column(name="EXTERNAL_KEY2")
	private int externalKey2;

	@Column(name="primary_exchange", length=100)
	private String primaryExchange;
    
	@Column(name="SECURITY_SEQ_NUM", unique=true, nullable=false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int securitySeqNum;

	@Column(name="SECURITY_TYPE", length=4)
	private String securityType;

	@Column(length=1)
	private String status;

	@Column(length=9)
	private String uscode;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INDUSTRY_CODE")
	private Industry industry;
	
	@OneToMany(mappedBy="security"  ,fetch= FetchType.LAZY, cascade= CascadeType.ALL)
	private Set<Stock> stocks;
  
	@Transient
	private Security previousState;
	
	@Transient
	private int industryCode;
	
	public Set<Stock> getStocks() {
		if(this.stocks == null) {
			this.stocks = new HashSet<>();
		}
		return this.stocks;
	}

	public void setStocks(Set<Stock> stocks) {
		this.stocks = stocks;
	}

	public Stock addStock(Stock stock) {
		if(getStocks() == null) {
			setStocks(new HashSet<>(Arrays.asList(stock)));
		}else {
		getStocks().add(stock);
		stock.setSecurity(this);
		}
		return stock;
	}

	public Stock removeStock(Stock stock) {
		getStocks().remove(stock);
		stock.setSecurity(null);

		return stock;
	}

}