package com.piggymetrics.statistics.service;

import com.google.common.collect.ImmutableMap;
import com.piggymetrics.statistics.client.ExchangeRatesClient;
import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

    //用来记录日志

	private static final Logger log = LoggerFactory.getLogger(ExchangeRatesServiceImpl.class);

	private ExchangeRatesContainer container;


	@Autowired
	private ExchangeRatesClient client;

	/**
	 * {@inheritDoc}
	 */
	/*该方法用来取得调用时那天的各国的货币汇率 */

	@Override
	public Map<Currency, BigDecimal> getCurrentRates() {

	    //更新汇率
		//client.getRates中getRates()这个方法一旦被调用就会给https://api.exchangeratesapi.ioAPI接口发送GET请求，取得请求时一个ExchangerRatesContainer对象，
    	//对象存有已传入参数Currency.getbase()为基准的各国货币的汇率
    	//这里用到了Spring Cloud OpenFegin,具体看接口ExchangeRatesClient

		if (container == null || !container.getDate().equals(LocalDate.now())) {
			container = client.getRates(Currency.getBase());
			log.info("exchange rates has been updated: {}", container);
		}
		/*ImutableMap：字面意思就是一成不变的Map,任何修改ImutableMap的操作都会throw UnsupportedOperationException
		* 这里的操作是返回不可改变的Map<Currency,BigDecimal>*/

		return ImmutableMap.of(
				Currency.EUR, container.getRates().get(Currency.EUR.name()),
				Currency.RUB, container.getRates().get(Currency.RUB.name()),
				//这里以美元为基准
				Currency.USD, BigDecimal.ONE
		);
	}

	/**
	 * {@inheritDoc}
     * 这个方法用来转换不同的币种
	 * Currency from : 币种１
	 * Currency from : 币种2
	 * BigDecimal amount : 转化数额
	 */

	@Override
	public BigDecimal convert(Currency from, Currency to, BigDecimal amount) {

		//判断传入amount不是Null,如果是Null就会抛出IllegalArgumentException()错误
		Assert.notNull(amount);
		//取得以USD为基准的EUR,RUB,USD的汇率
		Map<Currency, BigDecimal> rates = getCurrentRates();

		/* public BigDecimal divide(BigDecimal divisor, int scale, RoundingMode roundingMode)
		* divisor : BigDecimal要除以的值<也就是rate.get(to)要除以的值>
		* scale : 商的小数位数
		* roundingMode: 舍入的模式 RoundingMode.HALF_UP表示小数大于等于5就进一位
		* */
		BigDecimal ratio = rates.get(to).divide(rates.get(from), 4, RoundingMode.HALF_UP);
		//amount与ratio做乘法，小数点的位数是两个数小数点位数之和
		return amount.multiply(ratio);
	}
}
