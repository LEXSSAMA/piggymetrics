package com.piggymetrics.statistics.client;

import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ExchangeRatesClientFallback implements ExchangeRatesClient {



    //这里的重写方法是用来做Feign里集成的Hystrix的熔断回调的

    @Override
    public ExchangeRatesContainer getRates(Currency base) {
        ExchangeRatesContainer container = new ExchangeRatesContainer();
        //设置基准货币
        container.setBase(Currency.getBase());
        //设置汇率
        //Collections.emptyMap: 用来设置一个一成不变的Map空集合(不可改变)，等到要用的时候再重新通过new HashMap()来初始化
        //为什么要这样做？　是为了不要明确地键入Map集合的范型(方便),还可以重复利用这个对象不用再麻烦的创建新对象
        //可能讲的不好具体可以看这个回答:https://stackoverflow.com/questions/14846920/collections-emptymap-vs-new-hashmap
        container.setRates(Collections.emptyMap());
        return container;
    }
}
