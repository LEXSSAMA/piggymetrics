package com.piggymetrics.statistics.repository.converter;

import com.mongodb.DBObject;
import com.piggymetrics.statistics.domain.timeseries.DataPointId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Date;

/*定制Mongodb中的转换器（converter)
 *这里是将DBObject类型转化为DataPointId类型再被程序读出
 */

@Component
public class DataPointIdReaderConverter implements Converter<DBObject, DataPointId> {

	@Override
	public DataPointId convert(DBObject object) {

		Date date = (Date) object.get("date");
		String account = (String) object.get("account");

		return new DataPointId(account, date);
	}
}
