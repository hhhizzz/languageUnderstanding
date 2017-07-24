## 简单的语义解析

现在只有航班信息解析：

样例(在`FlightDataTest`类中：

```Java
//测试时间为2017-07-24
FlightData.FlightDataItem flightDataItem = new FlightData().getData("预订一个下周一上午8点到12点之间从北京首都国际机场到上海虹桥机场的头等舱或商务舱航班，要南方航空的，价格8折以内,两千到三千元");
System.out.println("出发城市："+flightDataItem.getCityFrom());
System.out.println("到达城市："+flightDataItem.getCityTo());
System.out.println("航空公司："+flightDataItem.getAirLine());
System.out.println("舱位："+flightDataItem.getCabin());
System.out.println("出发机场："+flightDataItem.getPortFrom());
System.out.println("到达机场："+flightDataItem.getPortTo());
System.out.println("要求折扣："+flightDataItem.getRate());
System.out.println("出发时间："+flightDataItem.getTimeBegin());
System.out.println("到达时间："+flightDataItem.getTimeEnd());
System.out.println("最低价格要求："+flightDataItem.getPriceLeft());
System.out.println("最高价格要求："+flightDataItem.getPriceRight());
```

结果为：

```
出发城市：北京
到达城市：上海
航空公司：南方航空
舱位：[头等舱, 商务舱]
出发机场：首都国际机场
到达机场：虹桥机场
要求折扣：8.0
出发时间：Mon Jul 31 08:00:00 CST 2017
到达时间：Mon Jul 31 12:00:00 CST 2017
最低价格要求：2000.0
最高价格要求：3000.0
```

## 参考资料和项目

[HanLP](https://github.com/hankcs/HanLP)

[Time-NLP](https://github.com/shinyke/Time-NLP)

[Java实现敏感词过滤](http://cmsblogs.com/?p=1031)

