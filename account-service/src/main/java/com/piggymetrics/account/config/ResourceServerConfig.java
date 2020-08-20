package com.piggymetrics.account.config;

import com.piggymetrics.account.service.security.CustomUserInfoTokenServices;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author cdov
 */

//配置资源服务器(就是告诉资源服务器怎么去解码传过来的access token)

@Configuration
@EnableResourceServer       //开启资源服务器配置，打开这个注解，会自动添加一个类型为OAuth2AuthenticationProcessingFilter的过滤链
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /*为什么这里是写private final ResourceServerProperties sso,然后通过自动写入构造器的方法来实现sso的初始化呢
    而不是通过:
        @Autowired
        private final ResourceServerProperties sso;
        原因是@Autowired和final是互斥的，他们的对参数的初始化时间段不同，详细可以看：
        https://stackoverflow.com/questions/34580033/spring-io-autowired-the-blank-final-field-may-not-have-been-initializedS
        ResourceServerProperties的作用是将配置文件中的security.oauth2.resource前缀的属性注入到类中.
     */

    private final ResourceServerProperties sso;

    @Autowired
    public ResourceServerConfig(ResourceServerProperties sso) {
        this.sso = sso;
    }

    //将配置文件中security.oauth2.client.*自动装配入ClientCredentialsResourceDetails

    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    //这个拦截器是用来装配Feign请求的Header,OAuth2FeignRequestInterceptor是用来在请求头上加上
    //Authorization = Bearer + Token，按照Oauth2.0协议要求，请求资源需要携带token,这个应该是为了请求资源服务器
    //statistics-service微服务的时候用到

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(){
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
    }

    //这个Bean能用提供的资源(clientCredentialsResourceDetails())发起OAuth2.0的认证请求(资源服务器利用
    // ResourceServerTokenServices接口内的方法发送请求的时候需要一个RestTemplate,
    // 我们定义这个Oauth2RestTemplate就是RestTemplate的子类,ResourceServerTokenServices接口的实现类
    // 我们自定义了一个CustomUserInfoTokenServices)

    @Bean
    public OAuth2RestTemplate clientCredentialsRestTemplate() {
        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
    }

    //设置与匹配授权服务的ResourceServerTokenServices,它知道如何对令牌进行解码
    //CustomUserInfoTokenServices是自定义的ResourceServerTokenServices实现类
    //sso.getUserInfoUri:认证服务其的用户信息端点的URL,(配置文件里貌似没有指定)
    @Bean
    public ResourceServerTokenServices tokenServices() {
        return new CustomUserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
    }

    //安全拦截机制

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //所有"/","/demo"的请求可以访问，其他请求要认证之后才能访问
        http.authorizeRequests()
                .antMatchers("/" , "/demo").permitAll()
                .anyRequest().authenticated();
    }
}
