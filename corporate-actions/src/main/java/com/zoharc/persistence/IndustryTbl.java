package com.zoharc.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
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
@Entity @Table(name = "industry")
@EntityListeners(AuditingEntityListener.class)

public class IndustryTbl {

	@Column(name = "sys_creation_date", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date sysCreationDate;

	@Column(name = "sys_update_date", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date sysUpdateDate;

	@Id
	@Column(name = "industry_code")
	private Long industryCode;

	@Column(name = "industry_desc")
	private String industryDesc;
}


