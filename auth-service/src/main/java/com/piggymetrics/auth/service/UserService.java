package com.piggymetrics.auth.service;

import com.piggymetrics.auth.domain.User;

public interface UserService { //封装的一个接口，实现在UserServicelmpl中

	void create(User user);

}
