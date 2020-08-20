package com.piggymetrics.notification.domain;

import java.util.stream.Stream;

public enum Frequency {

	WEEKLY(7), MONTHLY(30), QUARTERLY(90);

	private int days;

	Frequency(int days) {
		this.days = days;
	}

	public int getDays() {
		return days;
	}

	public static Frequency withDays(int days) {
		return Stream.of(Frequency.values()) //Frequency(enum).values会返回所有Frequency(enum)中的元素
				.filter(f -> f.getDays() == days)	//过滤掉天数不等于传入days的Frequency中的元素
				.findFirst()	//找到第一个过滤出来的元素,没有就抛出异常
				.orElseThrow(IllegalArgumentException::new);
	}
}
