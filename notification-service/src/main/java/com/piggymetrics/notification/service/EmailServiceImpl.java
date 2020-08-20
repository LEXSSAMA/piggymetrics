package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.MessageFormat;

@Service
@RefreshScope
public class EmailServiceImpl implements EmailService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private JavaMailSender mailSender;

	//微服务运行时的系统环境

	@Autowired
	private Environment env;


	//这个方法用来发送Email
	@Override
	public void send(NotificationType type, Recipient recipient, String attachment) throws MessagingException, IOException {

		//取得配置文件中的Subject信息
		final String subject = env.getProperty(type.getSubject());
		//编写发送的内容
		final String text = MessageFormat.format(env.getProperty(type.getText()), recipient.getAccountName());

		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		//设置收件人
		helper.setTo(recipient.getEmail());
		//设置主题
		helper.setSubject(subject);
		//设置内容
		helper.setText(text);

		if (StringUtils.hasLength(attachment)) {
			/*参数１：传入附件的名字
			　参数２:获取资源内容的地方
			 */
			helper.addAttachment(env.getProperty(type.getAttachment()), new ByteArrayResource(attachment.getBytes()));
		}

		//发送内容
		mailSender.send(message);

		log.info("{} email notification has been send to {}", type, recipient.getEmail());
	}
}
