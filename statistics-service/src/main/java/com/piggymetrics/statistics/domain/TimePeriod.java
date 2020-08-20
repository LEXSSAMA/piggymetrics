package com.piggymetrics.statistics.domain;

import java.math.BigDecimal;

public enum TimePeriod {

	//这里是以一天为基准,每小时赚200元就表示一天赚了200/0.0416＝4807.69元,一个月赚10000元就表示一天赚10000/30.4368=328.54元
	YEAR(365.2425), QUARTER(91.3106), MONTH(30.4368), DAY(1), HOUR(0.0416);

	private double baseRatio;

	TimePeriod(double baseRatio) {
		this.baseRatio = baseRatio;
	}

	public BigDecimal getBaseRatio() {
		return new BigDecimal(baseRatio);
	}

	public static TimePeriod getBase() {
		return DAY;
	}
}
