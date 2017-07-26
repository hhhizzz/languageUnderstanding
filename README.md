## 简单的语义解析

现在可以航班、旅店和火车票信息解析：
设置默认今天为`2017-04-18`

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
出发时间：Mon Apr 24 08:00:00 CST 2017
到达时间：Mon Apr 24 12:00:00 CST 2017
最低价格要求：2000.0
最高价格要求：3000.0
```
旅店样例(在`HotelDataTest`中)：
```Java
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
开始时间：Thu Apr 20 00:00:00 CST 2017
结束时间：Sat Apr 22 00:00:00 CST 2017
位置：郑州市西大街二七广场
```
火车票样例(在`TrainDataTest`中):
```Java
//测试时间为2017-07-25
TrainData.TrainDataItem dataItem = new TrainData().getDataItem("预订一张从南京到兰州的硬卧，明天中午12点到下午三点之间发车");
System.out.println("出发城市："+dataItem.getCityFrom());
System.out.println("到达城市："+dataItem.getCityTo());
System.out.println("出发时间："+dataItem.getDates());
System.out.println("座位类型："+dataItem.getSeatType());
```

结果为：
```
出发城市：南京
到达城市：兰州
出发时间：[Wed Apr 19 12:00:00 CST 2017, Wed Apr 19 15:00:00 CST 2017]
座位类型：[硬卧]
```
## 查询数据库的功能
目前现只有机票和酒店查询
可以从`dump.sql.zip`获取mysql数据
在`FindDataTest`设置数据库url
样例在`FindDataTest`中：
查询飞机票
```Java
FlightData.FlightDataItem dataItem = new FlightData().getData("预订一个下周三上午8点到12点之间从北京到上海的头等舱或公务舱航班，要南方航空的，价格8折以内,一千到三千元");
System.out.println(FindData.findDataFlight(dataItem));
```
返回结果
```Json
{"data":[{"flight":"CZ3907","dPort":"首都国际机场","quantity":"3","departCity":"北京","standardPrice":"3470.0","arriveCity":"上海","arriveTime":"2017-04-26 10:30:00","takeOffTime":"2017-04-26 08:20:00","rate":"0.0","price":"1670","airline":"中国南方航空股份有限公司","aPort":"虹桥国际机场","cabinInfo":"公务舱"},{"flight":"CZ3907","dPort":"首都国际机场","quantity":"3","departCity":"北京","standardPrice":"3470.0","arriveCity":"上海","arriveTime":"2017-04-26 10:30:00","takeOffTime":"2017-04-26 08:20:00","rate":"0.0","price":"1740","airline":"中国南方航空股份有限公司","aPort":"虹桥国际机场","cabinInfo":"公务舱"},{"flight":"CZ3907","dPort":"首都国际机场","quantity":"5","departCity":"北京","standardPrice":"3470.0","arriveCity":"上海","arriveTime":"2017-04-26 10:30:00","takeOffTime":"2017-04-26 08:20:00","rate":"0.0","price":"2850","airline":"中国南方航空股份有限公司","aPort":"虹桥国际机场","cabinInfo":"公务舱"}]}
```
查询酒店：
```Java
HotelData.HotelDataItem dataItem = new HotelData().getData("预订郑州西大街二七广场附近的酒店两晚，后天入住，价格200-400");
System.out.println(FindData.findDataHotel(dataItem));
```
返回结果
```Json
{"data":[{"address":"管城回族区西大街198号(近二七广场,地铁1号线二七广场站D出口东700米)","city":"郑州","price":"399","name":"河南瑞贝卡大酒店"}]}
```
查询火车
```Java
TrainData.TrainDataItem dataItem = new TrainData().getDataItem("预订一个明天或者后天从长春出发到杭州的火车硬卧");
System.out.println(FindData.findDataTrain(dataItem));
```
返回结果
```Json
{"data":[{"arriveTime":"2017-04-20 13:00:00","originStation":"长春","trainType":"","trainNo":"Z178","startTime":"2017-04-19 13:00:00","terminalStation":"杭州"},{"price":[{"name":"硬卧","value":501}]},{"arriveTime":"2017-04-21 13:00:00","originStation":"长春","trainType":"","trainNo":"K75","startTime":"2017-04-19 13:00:00","terminalStation":"杭州东"},{"price":[{"name":"硬卧","value":541}]},{"arriveTime":"2017-04-21 13:00:00","originStation":"长春","trainType":"","trainNo":"K554","startTime":"2017-04-19 13:00:00","terminalStation":"杭州"},{"price":[{"name":"硬卧","value":491}]}]}
```

## 参考资料和项目

[HanLP](https://github.com/hankcs/HanLP)

[Time-NLP](https://github.com/shinyke/Time-NLP)

[Java实现敏感词过滤](http://cmsblogs.com/?p=1031)

[Create json Object by java from data of mysql](https://stackoverflow.com/questions/17160351/create-json-object-by-java-from-data-of-mysql)

