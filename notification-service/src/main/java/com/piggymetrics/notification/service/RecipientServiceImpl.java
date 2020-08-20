package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import com.piggymetrics.notification.repository.RecipientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

@Service
public class RecipientServiceImpl implements RecipientService {

	//日志

	private final Logger log = LoggerFactory.getLogger(getClass());

	//数据库

	@Autowired
	private RecipientRepository repository;

	//调用数据库根据accountName来查找用户信息

	@Override
	public Recipient findByAccountName(String accountName) {
		//判断accountName是长度不为0,为0则抛出异常
		Assert.hasLength(accountName);
		return repository.findByAccountName(accountName);
	}

	/**
	 * {@inheritDoc}
	 */
	//存储收件人信息

	@Override
	public Recipient save(String accountName, Recipient recipient) {
		//设置收件人表的账户名
		recipient.setAccountName(accountName);
		//更新收件人上一次提醒日期
		recipient.getScheduledNotifications().values()
				.forEach(settings -> {
					if (settings.getLastNotified() == null) {
						settings.setLastNotified(new Date());
					}
				});
		//将收件人信息存入数据库
		repository.save(recipient);

		log.info("recipient {} settings has been updated", recipient);

		return recipient;
	}

	/**
	 * {@inheritDoc}
	 */
	//作用是字面意思
	@Override
	public List<Recipient> findReadyToNotify(NotificationType type) {
		switch (type) {
			case BACKUP:
				return repository.findReadyForBackup();
			case REMIND:
				return repository.findReadyForRemind();
			default:
				throw new IllegalArgumentException();
		}
	}

	/**
	 * {@inheritDoc}
	 */

	//Email提醒后更新参数LastNotified(上次提醒日期),然后存入数据库

	@Override
	public void markNotified(NotificationType type, Recipient recipient) {
		recipient.getScheduledNotifications().get(type).setLastNotified(new Date());
		repository.save(recipient);
	}
}
