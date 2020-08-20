package com.piggymetrics.statistics.domain;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/*@NotNull　带这个注释的元素不能为Null*/

/*这个类是用来记录用户在可选的指定时期(小时，日，月，季度，年)内的收入和支出的一份账单*/

public class Item {

	/* @Length用来限制title的长度*/

	@NotNull
	@Length(min = 1, max = 20)
	private String title;

	/*BigDecimal类用于大精度的数字计算,amount代表钱的数值*/

	@NotNull
	private BigDecimal amount;

	/*用来选择用什么纸币来结算（美元，欧元，等等)*/

	@NotNull
	private Currency currency;

	/*代表传入的账单是某个单位时期的收入/支出 (在用户填写账单的时候可以选择)
	* 例如: 一个小时的收入支出
	*		一个月的收入支出
	* 		一个季度的收入支出
	* 		一年的收入支出
	* */

	@NotNull
	private TimePeriod period;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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

	public TimePeriod getPeriod() {
		return period;
	}

	public void setPeriod(TimePeriod period) {
		this.period = period;
	}
}
