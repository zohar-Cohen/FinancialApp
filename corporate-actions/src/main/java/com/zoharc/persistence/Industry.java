package com.zoharc.persistence;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the industry database table.
 * 
 */
@Entity
@Table(name="industry")
@Builder @AllArgsConstructor @Getter @Setter @NoArgsConstructor 
public class Industry extends AuditEntity{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="INDUSTRY_CODE", unique=true, nullable=false)
	private int industryCode;

	@Column(name="INDUSTRY_DESC", nullable=false, length=400)
	private String industryDesc;

	@OneToMany(mappedBy="industry")
	private Set<Security> securities;

	
	public Set<Security> getSecurities() {
		return this.securities;
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