package com.piggymetrics.account.domain;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

//用户空闲的钱

public class Saving {

	//用户空闲的钱数目

	@NotNull
	private BigDecimal amount;

	//货币类型

	@NotNull
	private Currency currency;

	//利息

	@NotNull
	private BigDecimal interest;

	//存款(暂时不知道是干嘛的)

	@NotNull
	private Boolean deposit;

	//股本(暂时不知道是干嘛的)

	@NotNull
	private Boolean capitalization;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public Boolean getDeposit() {
		return deposit;
	}

	public void setDeposit(Boolean deposit) {
		this.deposit = deposit;
	}

	public Boolean getCapitalization() {
		return capitalization;
	}

	public void setCapitalization(Boolean capitalization) {
		this.capitalization = capitalization;
	}
}
