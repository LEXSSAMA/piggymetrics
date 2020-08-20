package com.piggymetrics.account.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/*注意这个＠Document是来自mongodb包的这个注解表示这个类是用来与mongodb中数据库集合account建立映射
 *一般要使用＠Field()来指定类中对象名和mongodb中的每个field名建立映射,但是如果类中每个对象名字和mongodb中的名字一样的话就可以省略
 * 这里的＠Document()就如spring data jpa 中的＠Entity,@Field就如＠column,都是用来建立java代码中类和数据库的映射关系
 */

/*@JsonIgnoreProperties(ignoreUnknown = true)这个注释可以用来禁止指定属性在序列化的时候，
*也可以在反序列化(读)的过程中忽略Json中的不存在的值
* ignoreUnknown=true代表在反序列化的过程中忽略Json中的不存在的值,
* 例如有一个Json格式的数据:
* {
*	"firstName":"Homer",
*	"middleName":"Jay",
*	"lastName":"Simpson"
* }
* 要被读入一个类中：
* public class Person {
*	public String firstName;
*	public String lastName;
}
* 这个类中没有middleName，如果在Person类上加了@JsonIgnoreProperties(ignoreUnknown = true)
* 那么middleName就会被忽略，没加就会报错.
* 而使用@JsonIgnoreProperties(value = {"firstName"})在类上那么在序列化成Json的过程中就会被忽略
* 例如就会得出:
* {
*	"lastName":"Simpson"
* }
* 没有了firstName.
* */

@Document(collection = "accounts")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

	@Id
	private String name;

	//最后一次登陆的日期

	private Date lastSeen;

	//@Valid是用来检验约束条件(@NotNull,@Length,之类)是否被满足,失败则报错,而且这个注释是递归执行的，Item类里面的约束条件也会被检验
	//收入

	@Valid
	private List<Item> incomes;

	//支出

	@Valid
	private List<Item> expenses;

	//用户空闲的钱

	@Valid
	@NotNull
	private Saving saving;

	//用来限制String的字符长度

	@Length(min = 0, max = 20_000)
	private String note;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}

	public List<Item> getIncomes() {
		return incomes;
	}

	public void setIncomes(List<Item> incomes) {
		this.incomes = incomes;
	}

	public List<Item> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Item> expenses) {
		this.expenses = expenses;
	}

	public Saving getSaving() {
		return saving;
	}

	public void setSaving(Saving saving) {
		this.saving = saving;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
