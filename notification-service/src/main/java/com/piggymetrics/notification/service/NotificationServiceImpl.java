package com.piggymetrics.notification.service;

import com.piggymetrics.notification.client.AccountServiceClient;
import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificationServiceImpl implements NotificationService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private AccountServiceClient client;

	@Autowired
	private RecipientService recipientService;

	@Autowired
	private EmailService emailService;

	//暂时不知道BackUp Notification是用来干什么的
	@Override
	@Scheduled(cron = "${backup.cron}") //每天的12:00pm执行定时任务(backup.cron在配置文件中指定)
	public void sendBackupNotifications() {

		final NotificationType type = NotificationType.BACKUP;

		List<Recipient> recipients = recipientService.findReadyToNotify(type);
		log.info("found {} recipients for backup notification", recipients.size());

		recipients.forEach(recipient -> CompletableFuture.runAsync(() -> {
			try {
				String attachment = client.getAccount(recipient.getAccountName());
				emailService.send(type, recipient, attachment);
				recipientService.markNotified(type, recipient);
			} catch (Throwable t) {
				log.error("an error during backup notification for {}", recipient, t);
			}
		}));
	}

	@Override
	@Scheduled(cron = "${remind.cron}")  //每天0:00执行定时任务(remind.cron在配置文件中指定)
	public void sendRemindNotifications() {

		final NotificationType type = NotificationType.REMIND;

		//在数据库中找到需要发送提醒的用户
		List<Recipient> recipients = recipientService.findReadyToNotify(type);
		log.info("found {} recipients for remind notification", recipients.size());

		//使用多线程异步执行来发送email提醒,CompletableFuture里的runAsyns方法和supplyAsync方法
		//不同之处在于runAsyns不用写返回值，supplyAsync要写返回值 (多线程里面的知识)
		//详细可以看https://www.liaoxuefeng.com/wiki/1252599548343744/1306581182447650
		recipients.forEach(recipient -> CompletableFuture.runAsync(() -> {
			try {
				//发送邮件提醒
				emailService.send(type, recipient, null);
				//Email提醒后更新参数LastNotified(上次提醒日期),然后存入数据库
				recipientService.markNotified(type, recipient);
			} catch (Throwable t) {
				log.error("an error during remind notification for {}", recipient, t);
			}
		}));
	}
}
