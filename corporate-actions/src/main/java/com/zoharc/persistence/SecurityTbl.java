package com.zoharc.persistence;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zohar Cohen Date: 14-Aug-2019
 *
 */
@Data @NoArgsConstructor
@Entity @Table(name = "security")
@EntityListeners(AuditingEntityListener.class)
public class SecurityTbl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "security_seq_num")
	private Long securitySrqNumber;

	@Column(name = "sys_creation_date", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date sysCreationDate;

	@Column(name = "sys_update_date", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date sysUpdateDate;

	@Column(name = "isin")
	private String isin;

	@Column(name = "uscode")
	private String usCode;

	@Column(name = "status")
	private char status;

	@Column(name = "security_type")
	private String securityType;

	@Column(name = "industry_code")
	private int industryCode;

	@Column(name = "external_key1")
	private int externalKeyOne;

	@Column(name = "external_key2")
	private int externalKeyTwo;

	@OneToMany(mappedBy = "stockPK.securityId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<StockTbl> stocks;

	@ManyToOne
	@JoinColumn(name = "industry_code", insertable = false, updatable = false )
	private IndustryTbl industry;


	public void setStocks(StockTbl ... stocks) {
		this.stocks = Stream.of(stocks).collect(Collectors.toSet());
		this.stocks.forEach(stock -> stock.getStockPK().setSecurityId(this));
	}



}
