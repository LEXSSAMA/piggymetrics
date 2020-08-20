package com.piggymetrics.account.client;

import com.piggymetrics.account.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/*这里feign与Eureka配合
 *@FeignClient中参数
 * name: 要调用其他微服务的名字,因为没有指定url,如果指定了url属性,那就代表FeignClient的名字,name属性必须要使用
 * */

@FeignClient(name = "auth-service")
public interface AuthServiceClient {
	//uaa: User Account and Authentication
	//这个方法发送POST请求到auth-service的/uaa/users端点

	@RequestMapping(method = RequestMethod.POST, value = "/uaa/users", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	void createUser(User user);

}
