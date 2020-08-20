package com.piggymetrics.statistics.client;

import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//这里调用了设置在statistics-service.yml中的rates.url这个关键字中的API接口
/* @FeignClient中参数
* url: 表示第三方API接口
* name: 因为指定了url,所以代表FeignClient的名字,如果没有指定url,那么name属性就代表其他微服务的名字(与eureka配合使用),name属性必须要写
* fallback: 用来给Hystrix做用熔断回调，调用接口getRates执行失败后执行ExchangeRatesClientFallback类中的重写的getRates
* */

@FeignClient(url = "${rates.url}", name = "rates-client", fallback = ExchangeRatesClientFallback.class)
public interface ExchangeRatesClient {

    //发送GET指令　到rate.url/latest?base=(Currency base)

    @RequestMapping(method = RequestMethod.GET, value = "/latest")
    ExchangeRatesContainer getRates(@RequestParam("base") Currency base);

}
