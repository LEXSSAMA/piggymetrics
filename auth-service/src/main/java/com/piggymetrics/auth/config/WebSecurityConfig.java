package com.piggymetrics.auth.config;

import com.piggymetrics.auth.service.security.MongoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author cdov
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MongoUserDetailsService userDetailsService;

    //设置请求进入的安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                //所有进入的http请求都需要认证
                .authorizeRequests().anyRequest().authenticated()
                .and()
                //关闭csrf防护机制
                .csrf().disable();
        // @formatter:on
    }

    //这里是用来建立(build)一个AuthenticationManager,什么是AuthenticationManager？
    //AuthenticationManager - the API that defines how Spring Security’s Filters perform authentication.(来至官方文档)

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.
               //指定使用自定义的UserDetailsService来实现认证
                userDetailsService(userDetailsService)
                //指定认证时(即对比根据username从数据库中取出的password和用户传入的password的时候)使用的加密解密方式
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    //通过重写这个方法将上面AuthenticationManagerBuilder构建的AuthenticationManager公开为一个Bean，
    // 使之可以被Autowired

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}