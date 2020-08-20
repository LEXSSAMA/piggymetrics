package com.piggymetrics.statistics.domain.timeseries;

import java.io.Serializable;
import java.util.Date;

public class DataPointId implements Serializable {

	private static final long serialVersionUID = 1L;

	//帐号名
	private String account;

	//存储时间日期
	private Date date;

	public DataPointId(String account, Date date) {
		this.account = account;
		this.date = date;
	}

	public String getAccount() {
		return account;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "DataPointId{" +
				"account='" + account + '\'' +
				", date=" + date +
				'}';
	}
}
