package com.piggymetrics.notification.config;

import feign.RequestInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author cdov
 */
@Configuration
@EnableResourceServer   //开启资源服务器
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    //将配置文件中的security.oauth2.client前缀的配置写入
    //ClientCredentialsResourceDetails
    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    //这个拦截器是用来装配Feign请求的Header,OAuth2FeignRequestInterceptor是用来在请求头上加上
    //Authorization = Bearer + Token，按照Oauth2.0协议要求，请求资源需要携带token,这个应该是为了请求资源服务器准备的

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(){
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
    }

    //这个Bean能用提供的资源(clientCredentialsResourceDetails())发起OAuth2.0的认证请求(资源服务器利用
    // ResourceServerTokenServices接口内的方法发送请求的时候需要一个RestTemplate,
    // 我们定义这个Oauth2RestTemplate就是RestTemplate的子类.

    @Bean
    public OAuth2RestTemplate clientCredentialsRestTemplate() {
        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
    }
}
