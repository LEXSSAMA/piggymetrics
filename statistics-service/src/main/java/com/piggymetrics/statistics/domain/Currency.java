package com.piggymetrics.statistics.domain;

/*enum是一种特殊的类，在java中代表一组常数*/

public enum Currency {

	USD, EUR, RUB;

	//用美元作为基准货币

	public static Currency getBase() {
		return USD;
	}
}
