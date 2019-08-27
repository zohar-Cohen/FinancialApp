package com.zoharc.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the industry database table.
 * 
 */
@Entity
@Table(name="industry")
public class Industry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="INDUSTRY_CODE", unique=true, nullable=false)
	private Integer industryCode;

	@Column(name="INDUSTRY_DESC", nullable=false, length=400)
	private String industryDesc;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SYS_CREATION_DATE")
	private Date sysCreationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SYS_UPDATE_DATE")
	private Date sysUpdateDate;

	@OneToMany(mappedBy="industry" , fetch = FetchType.LAZY)
	//@JsonBackReference
	//@JsonManagedReference 
	private Set<Security> securities;

	public Industry() {
	}

	public int getIndustryCode() {
		return this.industryCode;
	}

	public void setIndustryCode(int industryCode) {
		this.industryCode = industryCode;
	}

	public String getIndustryDesc() {
		return this.industryDesc;
	}

	public void setIndustryDesc(String industryDesc) {
		this.industryDesc = industryDesc;
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

	public Set<Security> getSecurities() {
		return this.securities;
	}

	public void setSecurities(Set<Security> securities) {
		this.securities = securities;
	}

	public Security addSecurity(Security security) {
		getSecurities().add(security);
		security.setIndustry(this);

		return security;
	}

	public Security removeSecurity(Security security) {
		getSecurities().remove(security);
		security.setIndustry(null);

		return security;
	}

}