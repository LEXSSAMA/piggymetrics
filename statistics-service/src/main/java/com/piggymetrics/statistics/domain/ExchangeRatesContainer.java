package com.piggymetrics.statistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/*这个注释可以用来禁止指定属性在序列化的时候，也可以在反序列化(读)的过程中忽略Json中的不存在的值
* ignoreUnknown=true代表在反序列化的过程中忽略Json中的不存在的值,
* 例如有一个Json格式的数据:
* {
*	"firstName":"Homer",
*	"middleName":"Jay",
*	"lastName":"Simpson"
* }
* 要被读入一个类中：
* public class Person {
*	public String firstName;
*	public String lastName;
}
* 这个类中没有middleName，如果在Person类上加了@JsonIgnoreProperties(ignoreUnknown = true)
* 那么middleName就会被忽略，没加就会报错.
* 而使用@JsonIgnoreProperties(value = {"firstName"})在类上那么在序列化成Json的过程中就会被忽略
* 例如就会得出:
* {
*	"lastName":"Simpson"
* }
* 没有了firstName.
* */

@JsonIgnoreProperties(ignoreUnknown = true, value = {"date"})
public class ExchangeRatesContainer {

    //表示被调用时的日期

	private LocalDate date = LocalDate.now();

	//基准货币

	private Currency base;

	//String:表示货币,BigDecimal:表示对应的汇率

	private Map<String, BigDecimal> rates;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Currency getBase() {
		return base;
	}

	public void setBase(Currency base) {
		this.base = base;
	}

	public Map<String, BigDecimal> getRates() {
		return rates;
	}

	public void setRates(Map<String, BigDecimal> rates) {
		this.rates = rates;
	}

	@Override
	public String toString() {
		return "RateList{" +
				"date=" + date +
				", base=" + base +
				", rates=" + rates +
				'}';
	}
}
