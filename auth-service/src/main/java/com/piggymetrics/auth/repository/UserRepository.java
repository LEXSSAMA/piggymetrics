package com.piggymetrics.auth.repository;

import com.piggymetrics.auth.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//数据库
@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
