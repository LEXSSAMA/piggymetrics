package com.piggymetrics.auth.service;

import com.piggymetrics.auth.domain.User;
import com.piggymetrics.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	//记录日志
	private final Logger log = LoggerFactory.getLogger(getClass());

	//使用BCryptPasswordEncoder类来为密码加密
	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	//数据库
	@Autowired
	private UserRepository repository;

	@Override
	public void create(User user) {

		//首先在数据库中根据Id(也就是自定义User类中的username)查找,是否这个user已经存在
		Optional<User> existing = repository.findById(user.getUsername());
		//如果传入user已经存在那么就抛出错误
		existing.ifPresent(it-> {throw new IllegalArgumentException("user already exists: " + it.getUsername());});

		//数据库中没有传入user就使用BCryptPasswordEncoder来加密传入user密码
		String hash = encoder.encode(user.getPassword());
		//将传入user密码改成加密后的密码
		user.setPassword(hash);
		//将处理过后的user存入数据库
		repository.save(user);
		//记录日志
		log.info("new user has been created: {}", user.getUsername());
	}
}
