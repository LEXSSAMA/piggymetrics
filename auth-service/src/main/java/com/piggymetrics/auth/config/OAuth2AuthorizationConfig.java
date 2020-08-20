package com.piggymetrics.auth.config;

import com.piggymetrics.auth.service.security.MongoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author cdov
 */

//这个类用来配置Oauth2.0授权服务器
@Configuration
//开启Oauth2.0认证服务器
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    //使用的是内存存储Token的方式

    private TokenStore tokenStore = new InMemoryTokenStore();

    private final String NOOP_PASSWORD_ENCODE = "{noop}";


    //@Qualifier指定需要注入的bean

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private MongoUserDetailsService userDetailsService;

    //代表application运行时的系统环境

    @Autowired
    private Environment env;

    //ClientDetailsServiceConfigurer 用来配置客户端的详情信息，可以把客户端信息写死在这里也可以通过数据库来调

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        // TODO persist clients details

        //配置了的四个客户端(browser,account-service,statistics-service,notification-service)能被认证服务器接受
        //浏览器用密码模式,其他3个客户端都是使用客户端模式,refresh_token表示来的请求是为了更新令牌
        //ACCOUNT_SERVICE_PASSWORD,STATISTICS_SERVICE_PASSWORD,NOTIFICATION_SERVICE_PASSWORD在应用启动时由dokcer设置
        // @formatter:off
        clients.inMemory()
                //clientId:（必须设定），用来标识客户ID
                .withClient("browser")
                //此客户端的授权类型，默认为空
                .authorizedGrantTypes("refresh_token", "password")
                //用来限制客户端的访问范围，如果为空(默认),那么客户端拥有全部的访问范围
                .scopes("ui")
                .and()
                .withClient("account-service")
                //client_secret:客户端密钥
                .secret(env.getProperty("ACCOUNT_SERVICE_PASSWORD"))
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .and()
                .withClient("statistics-service")
                .secret(env.getProperty("STATISTICS_SERVICE_PASSWORD"))
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .and()
                .withClient("notification-service")
                .secret(env.getProperty("NOTIFICATION_SERVICE_PASSWORD"))
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server");
        // @formatter:on
    }

    //配置令牌，以及令牌端点上的相关服务

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //配置授权服务器的令牌存储方式这里使用内存存储令牌
                .tokenStore(tokenStore)
                //配置授权服务提供者的容器
                .authenticationManager(authenticationManager)
                //配置自定义的对用户的认证(校验)方式,当"refresh_token"的时候，就会添加一个检查，用来确保这个帐号是否仍然有效
                .userDetailsService(userDetailsService);
    }

    //配置令牌端点上的安全约束

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                //tokenkey这个endpoint当使用jwtToken且使用非对称加密时，资源服务为了获取公钥而开放的，这里指自这个
                //endpoint完全公开
                .tokenKeyAccess("permitAll()")
                //checkToken这个endPoint需要认证过后才能访问
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

}
