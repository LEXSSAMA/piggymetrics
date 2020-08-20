package com.piggymetrics.notification.domain;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

/*注意这个＠Document是来自mongodb包的这个注解表示这个类是用来与mongodb中数据库集合account建立映射
 *一般要使用＠Field()来指定类中对象名和mongodb中的每个field名建立映射,但是如果类中每个对象名字和mongodb中的名字一样的话就可以省略
 * 这里的＠Document()就如spring data jpa 中的＠Entity,@Field就如＠column,都是用来建立java代码中类和数据库的映射关系
 */

@Document(collection = "recipients")
public class Recipient {

	//账户名字

	@Id
	private String accountName;

	//email地址

	@NotNull
	@Email
	private String email;

	@Valid
	private Map<NotificationType, NotificationSettings> scheduledNotifications;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<NotificationType, NotificationSettings> getScheduledNotifications() {
		return scheduledNotifications;
	}

	public void setScheduledNotifications(Map<NotificationType, NotificationSettings> scheduledNotifications) {
		this.scheduledNotifications = scheduledNotifications;
	}

	@Override
	public String toString() {
		return "Recipient{" +
				"accountName='" + accountName + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
