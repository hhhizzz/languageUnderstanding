## 简单的语义解析

现在只有航班和旅店信息解析：

航班样例(在`FlightDataTest`类中：

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
旅店样例(在`HotelDataTest`中)：
```Java
//测试时间为2017-07-25
HotelData.HotelDataItem dataItem = new HotelData().getData("预订郑州市西大街二七广场附近的酒店两晚，后天入住，价格200-400");
System.out.println("城市："+dataItem.getCity());
System.out.println("价格最低："+dataItem.getPriceLeft());
System.out.println("价格最高："+dataItem.getPriceRight());
System.out.println("开始时间："+dataItem.getDateBegin());
System.out.println("结束时间："+dataItem.getDateEnd());
System.out.println("位置："+dataItem.getLocation());
```
结果为：
```
城市：郑州市
价格最低：200.0
价格最高：400.0
开始时间：Thu Jul 27 00:00:00 CST 2017
结束时间：Sat Jul 29 00:00:00 CST 2017
位置：郑州市西大街二七广场
```

## 参考资料和项目

[HanLP](https://github.com/hankcs/HanLP)

[Time-NLP](https://github.com/shinyke/Time-NLP)

[Java实现敏感词过滤](http://cmsblogs.com/?p=1031)

