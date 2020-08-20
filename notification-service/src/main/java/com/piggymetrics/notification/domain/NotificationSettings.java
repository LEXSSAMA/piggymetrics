package com.piggymetrics.notification.domain;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class NotificationSettings {

	//是否执行提醒操作

	@NotNull
	private Boolean active;

	//提醒的频率是多少

	@NotNull
	private Frequency frequency;

	//上一次提醒的日期

	private Date lastNotified;

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Frequency getFrequency() {
		return frequency;
	}

	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;
	}

	public Date getLastNotified() {
		return lastNotified;
	}

	public void setLastNotified(Date lastNotified) {
		this.lastNotified = lastNotified;
	}
}
