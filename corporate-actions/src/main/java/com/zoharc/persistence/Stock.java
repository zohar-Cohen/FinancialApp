package com.zoharc.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the stock database table.
 * 
 */
@Entity
@Table(name="stock")
@Builder @AllArgsConstructor @Getter @Setter @NoArgsConstructor
//@EntityListeners(TransactionLogListnert.class)
public class Stock extends AuditEntity{
	
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

	@Column(name="STOCK_SEQ_NUM", nullable=false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int stockSeqNum;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EXCHANGE_CODE", nullable=false, insertable=false, updatable=false)
	private Exchange exchange;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SECURITY_ID", nullable=false, insertable=false, updatable=false)
	private Security security;
	
	@Transient
    private String mappedStock;
	
	@Transient
	private String exchangeCode;
	
	@Transient
	private String stockCurrency;
	
	@Transient
	private String securityId;
		
	
}