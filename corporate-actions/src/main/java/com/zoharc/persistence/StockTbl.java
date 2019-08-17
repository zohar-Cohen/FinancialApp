package com.zoharc.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.zoharc.persistence.identity.StockPK;

import lombok.Builder;
import lombok.Data;

/**
 * @author Zohar Cohen Date: 14-Aug-2019
 *
 */
@Builder @Data
@Entity @Table(name = "stock")
@EntityListeners(AuditingEntityListener.class)
public class StockTbl {

	@EmbeddedId
	private StockPK stockPK;
	
	@Column(name = "sys_creation_date", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date sysCreationDate;

	@Column(name = "sys_update_date", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date sysUpdateDate;

	@Column(name = "raw_stock")
	private String rawStock;

	@Column(name = "stock_name")
	private String stockName;

	@Column(name = "list_status")
	private String listStatus;

	@Column(name = "listed_date")
	private Date listedDate;

	@Column(name = "expire_date")
	private Date expireDate;

	@Column(name = "external_key1")
	private int externalKeyOne;

	@Column(name = "external_key2")
	private int externalKeyTwo;

}



