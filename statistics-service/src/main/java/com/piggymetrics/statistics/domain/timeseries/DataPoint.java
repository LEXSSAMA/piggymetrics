package com.piggymetrics.statistics.domain.timeseries;

import com.piggymetrics.statistics.domain.Currency;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * Represents daily time series data point containing
 * current account state
 */

/*注意这个＠Document是来自mongodb包的这个注解表示这个类是用来与mongodb中数据库集合datapoints建立映射
*一般要使用＠Field()来指定类中对象名和mongodb中的每个field名建立映射,但是如果类中每个对象名字和mongodb中的名字一样的话就可以省略
* 这里的＠Document()就如spring data jpa 中的＠Entity,@Field就如＠column,都是用来建立java代码中类和数据库的映射关系
* */
//数据点（DataPoint)，每日是一个数据点，这个类记录当日(DataPointId)的支出消费(incomes,expenses)，汇率信息(rates)，以及一些消费的统计信息(statistics)

@Document(collection = "datapoints")
public class DataPoint {

	/*@Id用来表明数据库中主键*/

	@Id
	private DataPointId id;

	private Set<ItemMetric> incomes;

	private Set<ItemMetric> expenses;

	private Map<StatisticMetric, BigDecimal> statistics;

	private Map<Currency, BigDecimal> rates;

//	----------------------------------------------------------------------

	public DataPointId getId() {
		return id;
	}

	public void setId(DataPointId id) {
		this.id = id;
	}

	public Set<ItemMetric> getIncomes() {
		return incomes;
	}

	public void setIncomes(Set<ItemMetric> incomes) {
		this.incomes = incomes;
	}

	public Set<ItemMetric> getExpenses() {
		return expenses;
	}

	public void setExpenses(Set<ItemMetric> expenses) {
		this.expenses = expenses;
	}

	public Map<StatisticMetric, BigDecimal> getStatistics() {
		return statistics;
	}

	public void setStatistics(Map<StatisticMetric, BigDecimal> statistics) {
		this.statistics = statistics;
	}

	public Map<Currency, BigDecimal> getRates() {
		return rates;
	}

	public void setRates(Map<Currency, BigDecimal> rates) {
		this.rates = rates;
	}
}
