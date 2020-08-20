package com.piggymetrics.statistics;

import com.piggymetrics.statistics.repository.converter.DataPointIdReaderConverter;
import com.piggymetrics.statistics.repository.converter.DataPointIdWriterConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.Arrays;


@SpringBootApplication
@EnableDiscoveryClient			//开启服务发现

/*这个微服务是资源服务器同时也是Oauth2.0客户端，这个注解用来开启Oauth2.0客户端的配置,打开j注解这个会干两件事情
* １．会启动一个叫做oauth2ClientContextFilter的过滤器，用来存储当前的请求和上下文，这个用来管理
* Oauth2.0认证过Oauth2.0认证过程中重定向的问题
* 2.会创建一个AccessTokenRequest类型的bean,这个bean是用来授权代码和授权客户端，防止单个客户端不同状态下的冲突(不是很理解，官方文档上这么说)
* */
@EnableOAuth2Client
@EnableFeignClients				//开启feign服务
@EnableGlobalMethodSecurity(prePostEnabled = true) 	//开启Spring Security 的方法授权，开启后可以使用@PreAuthorize()等注解
public class StatisticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatisticsApplication.class, args);
	}

	@Configuration
	static class CustomConversionsConfig {

		@Bean
		public CustomConversions customConversions() {
			return new CustomConversions(Arrays.asList(new DataPointIdReaderConverter(),
					new DataPointIdWriterConverter()));
		}
	}
}
