package com.piggymetrics.statistics.service;

import com.google.common.collect.ImmutableMap;
import com.piggymetrics.statistics.domain.*;
import com.piggymetrics.statistics.domain.timeseries.DataPoint;
import com.piggymetrics.statistics.domain.timeseries.DataPointId;
import com.piggymetrics.statistics.domain.timeseries.ItemMetric;
import com.piggymetrics.statistics.domain.timeseries.StatisticMetric;
import com.piggymetrics.statistics.repository.DataPointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	//用来记录日志的

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DataPointRepository repository;

	@Autowired
	private ExchangeRatesService ratesService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DataPoint> findByAccountName(String accountName) {
		//用来判断给入的字符串accountName不是NULL也不是空字符
		Assert.hasLength(accountName);
		//去DataPointRepository类调用findByIdAccount方法
		return repository.findByIdAccount(accountName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataPoint save(String accountName, Account account) {

		/*LocalDate.now(): 代表获取当前系统时钟所在时区的日期，return LocalDate 类 ，如:2020-08-01
		* LocalDate.atStartOfDay(): 返回一个当前日期（年月日)，午夜(0点)开始的时间 ,return LocalDateTime 类 ，如：2020-08-01T00:00
		* LocalDateTime.atZone(ZoneId.systemDefault)是用来设置系统的时区的，	return ZoneDateTime类,如：2020-08-01T00:00+08:00[Asia/Shanghai]
		* ZoneDateTime.toInstant 是用来调整时间的把当前设置的时间调整为UTC时间(协调世界时) return Instant ，如2020-07-31T16:00:00Z
		* 下面这个语句的意思是获取当前你系统设置时区的当前日期的０点调整为UTC时间．
		* 这里我觉得主要的目的不是为了转换时间而是为了把ZoneDateTime类型转化为Instant类型，方便下一步从Instant实例中提取日期时间的操作
		* */
		Instant instant = LocalDate.now().atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant();

		/*创建一个DatePointId实例,DataPointId内装的是账户名和时间日期
		* Date.from(instant),就是从上面创建的instant中提取获得我们设置的时间日期*/
		DataPointId pointId = new DataPointId(accountName, Date.from(instant));

		/* 这个操作目的是得到每天收入的数目(incomes)
		*account.getIncomes()返回一个list<Item>
		* List<Item>.stream有点像高级版的iterator(迭代器),单向的,并行的,用stream让代码变得简洁方便
		* 可以在.stream()后面加.map,.collect等操作,对stream中遍历的对象执行一些操作
		* :: 是方法引用操作符，this表示从流中拿到的每一个类Item,this::createItemMetric表示将从流中拿到的每一个Item类都作为
		* 参数执行createItemMetric,返回一个传入账单(Item)每日的收入支出类(ItemMetric)
		* .collect(Collectors.toSet)是把流中每个被处理得到的ItemMetric类组合成一个Set,也就是Set<ItemMetric>
		* 详细关于collect(),可以看这篇文章:https://www.java67.com/2018/06/java-8-streamcollect-example.html*/
		Set<ItemMetric> incomes = account.getIncomes().stream()
				.map(this::createItemMetric)
				.collect(Collectors.toSet());

		/*操作和上面incomes的一样,目的是得到每天花费的数目(expenses)*/
		Set<ItemMetric> expenses = account.getExpenses().stream()
				.map(this::createItemMetric)
				.collect(Collectors.toSet());

		Map<StatisticMetric, BigDecimal> statistics = createStatisticMetrics(incomes, expenses, account.getSaving());

		DataPoint dataPoint = new DataPoint();
		dataPoint.setId(pointId);
		dataPoint.setIncomes(incomes);
		dataPoint.setExpenses(expenses);
		dataPoint.setStatistics(statistics);
		dataPoint.setRates(ratesService.getCurrentRates());

		log.debug("new datapoint has been created: {}", pointId);

		//spring data Jpa , spring data mongodb的相关知识，只有接口没有实现类一样可以实现数据库的增删改查

		return repository.save(dataPoint);
	}

	private Map<StatisticMetric, BigDecimal> createStatisticMetrics(Set<ItemMetric> incomes, Set<ItemMetric> expenses, Saving saving) {

		//将用户选择的货币的数额转化为基准货币的数额（USD)

		BigDecimal savingAmount = ratesService.convert(saving.getCurrency(), Currency.getBase(), saving.getAmount());

		//计算出每日的支出总和

		BigDecimal expensesAmount = expenses.stream()
				.map(ItemMetric::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);


		//计算出每日的收入总和

		BigDecimal incomesAmount = incomes.stream()
				.map(ItemMetric::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		//返回一个不可以改变的Map

		return ImmutableMap.of(
				StatisticMetric.EXPENSES_AMOUNT, expensesAmount,
				StatisticMetric.INCOMES_AMOUNT, incomesAmount,
				StatisticMetric.SAVING_AMOUNT, savingAmount
		);
	}

	/**
	 * Normalizes given item amount to {@link Currency#getBase()} currency with
	 * {@link TimePeriod#getBase()} time period
	 */
	private ItemMetric createItemMetric(Item item) {

		/*这里是算出每天收入多少
		* 首先根据传入的货币种类和货币数目转化为基准货币(Currency.getBase())的货币数目
		* 然后除于传入的账单收入支出的单位时期来计算出这个账单每天赚多少（支出多少) 钱
		* */
		BigDecimal amount = ratesService
				.convert(item.getCurrency(), Currency.getBase(), item.getAmount())
				.divide(item.getPeriod().getBaseRatio(), 4, RoundingMode.HALF_UP);

		/*返回传入账单每日收入/支出*/
		return new ItemMetric(item.getTitle(), amount);
	}
}
