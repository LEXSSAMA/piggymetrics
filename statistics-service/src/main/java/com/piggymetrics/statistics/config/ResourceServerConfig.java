package com.piggymetrics.statistics.config;

import com.piggymetrics.statistics.service.security.CustomUserInfoTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author cdov
 */

/*Oauth2 的内容 @EnableResourceServer 开启资源服务器
* sso = single-sign-on: 单点登陆
*配置资源服务器(就是告诉资源服务器怎么去解码传过来的access token)
* */

@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //ResourceServerProperties的作用是将配置文件中的security.oauth2.resource前缀的属性注入到类中.

    @Autowired
    private ResourceServerProperties sso;

    //设置与匹配授权服务的ResourceServerTokenServices,它知道如何对令牌进行解码
    //CustomUserInfoTokenServices是自定义的ResourceServerTokenServices实现类
    //sso.getUserInfoUri:认证服务其的用户信息端点的URL,(配置文件里貌似没有指定)

    @Bean
    public ResourceServerTokenServices tokenServices() {
        return new CustomUserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
    }
}
