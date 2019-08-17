package com.zoharc.persistence.identity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.zoharc.persistence.ExchangeTbl;
import com.zoharc.persistence.SecurityTbl;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zohar Cohen Date: 14-Aug-2019
 
 */
@Data @NoArgsConstructor
@Embeddable
public class StockPK implements Serializable  {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_seq_num")
	private Long stockSeqNumber;

	@Column(name = "mapped_stock")
	private String mappedStock;

	@ManyToOne
	@JoinColumn(name = "exchange_code", nullable = false)
	private ExchangeTbl exchangeCode;

	@Column(name = "stock_currency")
	private String stockCurrency;

	@ManyToOne
	@JoinColumn(name = "security_id", nullable = false)
	private SecurityTbl securityId;

}
