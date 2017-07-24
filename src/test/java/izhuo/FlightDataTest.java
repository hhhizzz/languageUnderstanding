package izhuo;

import izhuo.FlightData;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by xunixhuang on 24/07/2017.
 */
public class FlightDataTest {
    /*
        完整测试
    */
    @Test
    public void getData() throws Exception{
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
    }

    /*
        对每个部分进行单元测试
     */
    @Test
    public void getCity() throws Exception {
        FlightData.FlightDataItem flightDataItem = new FlightData().getData("预订一个下周一上午8点到12点之间从北京到上海的头等舱机票，要南方航空的，价格8折以内,");
        String cityFrom = flightDataItem.getCityFrom();
        String cityTo = flightDataItem.getCityTo();
        String[] getWords = {cityFrom, cityTo};
        String[] expectWords = {"北京", "上海"};
        assertArrayEquals(expectWords, getWords);

        FlightData.FlightDataItem flightDataItem2 = new FlightData().getData("帮我看看怎么去成都");
        String cityTo2 = flightDataItem2.getCityTo();
        String[] getWords2 = {cityTo2};
        String[] expectWords2 = {"成都"};
        assertArrayEquals(expectWords2, getWords2);
    }

    @Test
    public void getAirline() throws Exception {
        FlightData.FlightDataItem flightDataItem = new FlightData().getData("预订一个下周一上午8点到12点之间从北京首都机场到上海虹桥机场的头等舱航班，要南方航空的，价格8折以内");
        String airline = flightDataItem.getAirLine();
        assertEquals("南方航空", airline);
    }

    @Test
    public void getCabin() throws Exception {
        FlightData.FlightDataItem flightDataItem = new FlightData().getData("预订一个下周一上午8点到12点之间从北京到上海的经济舱航班，要南方航空的，价格8折以内");
        List<String> words = flightDataItem.getCabin();
        String[] getWords = {words.get(0)};
        String[] expectWords = {"经济舱"};
        assertArrayEquals(expectWords, getWords);
    }

    @Test
    public void getPort() throws Exception {
        FlightData.FlightDataItem flightDataItem = new FlightData().getData("预订一个下周一上午8点到12点之间从北京首都国际机场到上海虹桥机场的头等舱航班，要南方航空的，价格8折以内");
        String fromPort = flightDataItem.getPortFrom();
        String toPort = flightDataItem.getPortTo();
        String[] getWords = {fromPort, toPort};
        String[] expectWords = {"首都国际机场", "虹桥机场"};
        assertArrayEquals(expectWords, getWords);
    }

    @Test
    public void getRate() throws Exception {
        FlightData.FlightDataItem flightDataItem = new FlightData().getData("预订一个下周一上午8点到12点之间从北京首都国际机场到上海虹桥机场的头等舱航班，要南方航空的，价格8折以内");
        Double getRate = flightDataItem.getRate();
        Double expectRate = 8.0;
        assertEquals(expectRate, getRate);
    }

    @Test
    public void getDate() throws Exception {
        FlightData.FlightDataItem flightDataItem = new FlightData().getData("预订一个下周一上午8点到12点之间从北京首都国际机场到上海虹桥机场的头等舱航班，要南方航空的，价格8折以内");
        Date begin = flightDataItem.getTimeBegin();
        Date end = flightDataItem.getTimeEnd();
        System.out.println(begin);
        System.out.println(end);
    }

}