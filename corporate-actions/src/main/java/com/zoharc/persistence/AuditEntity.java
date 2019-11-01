package com.zoharc.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 *  The class contains common auditing related fields 
 *  @author Zohar Cohen Date: 27-Aug-2019
 *
 */
@Getter @Setter
@MappedSuperclass 
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public abstract class AuditEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SYS_CREATION_DATE", updatable = false)
	@CreatedDate
	private Date sysCreationDate;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SYS_UPDATE_DATE")
	@LastModifiedDate
	private Date sysUpdateDate;


}
