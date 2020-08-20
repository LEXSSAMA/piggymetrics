package com.piggymetrics.auth.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


/*注意这个＠Document是来自mongodb包的这个注解表示这个类是用来与mongodb中数据库集合users建立映射
 *一般要使用＠Field()来指定类中对象名和mongodb中的每个field名建立映射,但是如果类中每个对象名字和mongodb中的名字一样的话就可以省略
 * 这里的＠Document()就如spring data jpa 中的＠Entity,@Field就如＠column,都是用来建立java代码中类和数据库的映射关系
 */

@Document(collection = "users")
public class User implements UserDetails {

	@Id
	private String username;

	private String password;

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public List<GrantedAuthority> getAuthorities() {
		return null;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
