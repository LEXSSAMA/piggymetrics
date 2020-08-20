package com.piggymetrics.account.client;

import com.piggymetrics.account.domain.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/*
*这里Feign和Eureka来配合,当调用updateStatistics方法时会就会发送PUT请求到statistics-service的
* /statistics/{accountName}端点,如果请求失败，超时啊什么之类的就会触发hystrix的熔断机制，触发回调机制，
* 执行StatisticsServiceClientFallback的updateStatistics方法
* */


@FeignClient(name = "statistics-service", fallback = StatisticsServiceClientFallback.class)
public interface StatisticsServiceClient {

	@RequestMapping(method = RequestMethod.PUT, value = "/statistics/{accountName}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	void updateStatistics(@PathVariable("accountName") String accountName, Account account);

}
