package com.zoharc.reader;

import java.util.StringJoiner;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class SecurityMasterDTO {

	private int scexhID;
	private int sedolID;
	private String actflag;
	private String changed;
	private String created;
	private int secID;
	private int issID;
	private String iSIN;
	private String uSCode;
	private String issuerName;
	private String cntryofIncorp;
	private String sIC;
	private String cIK;
	private int indusID;
	private String sectyCD;
	private String securityDesc;
	private String parValue;
	private String pVCurrency;
	private String statusFlag;
	private String primaryExchgCD;
	private String sedol;
	private String sedolCurrency;
	private String defunct;
	private String sedolRegCntry;
	private String structCD;
	private String exchgCntry;
	private String exchgCD;
	private String mic;
	private String micseg;
	private String localCode;
	private String listStatus;


	public static String[] getFileHeader() {

		StringJoiner sj = new StringJoiner(",");

		sj.add("scexhid").add("sedolid").add("actflag").add("changed").add("created");
		sj.add("secid").add("issid").add("isin").add("uscode").add("issuername");
		sj.add("cntryofincorp").add("sic").add("cik").add("indusid").add("sectycd");
		sj.add("securitydesc").add("parvalue").add("pvcurrency").add("statusflag").add("primaryexchgcd");
		sj.add("sedol").add("sedolcurrency").add("defunct").add("sedolregcntry").add("structcd");
		sj.add("exchgcntry").add("exchgcd").add("mic").add("micseg").add("localcode").add("liststatus");

		return sj.toString().split(",");

	}
}
