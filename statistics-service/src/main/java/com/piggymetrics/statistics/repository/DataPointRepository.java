package com.piggymetrics.statistics.repository;

import com.piggymetrics.statistics.domain.timeseries.DataPoint;
import com.piggymetrics.statistics.domain.timeseries.DataPointId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*这个操作仓库的接口没有实现类依旧可以运行是因为spring data mongodb会给接口动态的分配一个实现类，具体可以了解spring data jpa
*和 spring data mongodb , 只要按照spring data的方法命名规范给方法命名就可以不用写实现类直接使用
* public interface DataPointRepository extends CrudRepository<T,ID> 中T是表示要操作的实体类，ID指的是实体类主键的类型
* 这篇文章是个不错的学习资料:http://springboot.gluecoders.com/mongodb-basics.html
*/

@Repository
public interface DataPointRepository extends CrudRepository<DataPoint, DataPointId> {

	/*猜测:这里表示根据DataPointId主键类中的account参数来查找*/

	List<DataPoint> findByIdAccount(String account);


}
