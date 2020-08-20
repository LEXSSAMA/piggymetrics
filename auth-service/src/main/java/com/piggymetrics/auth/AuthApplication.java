package com.piggymetrics.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer		//开启资源服务器，这个微服务即是认证服务器也是资源服务器
@EnableDiscoveryClient	//开启服务发现
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启Spring Security 的方法授权，开启后可以使用@PreAuthorize()等注解

public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}


