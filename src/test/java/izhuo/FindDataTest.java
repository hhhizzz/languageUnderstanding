package izhuo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xunixhuang on 25/07/2017.
 */
public class FindDataTest {
    @Test
    public void findData(){
        FlightData.FlightDataItem dataItem = new FlightData().getData("预订一个下周一上午8点到12点之间从北京首都国际机场到上海虹桥机场的头等舱或商务舱航班，要南方航空的，价格8折以内,两千到三千元");
        System.out.println(FindData.findDataFlight(dataItem));
    }

}