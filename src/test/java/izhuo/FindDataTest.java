package izhuo;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by xunixhuang on 25/07/2017.
 */
public class FindDataTest {
    @Test
    public void findData() throws Exception {
        FlightData.FlightDataItem dataItem = new FlightData().getData("预订一个下周三上午8点到12点之间从北京到上海的头等舱或公务舱航班，要南方航空的，价格8折以内,一千到三千元");
        System.out.println(FindData.findDataFlight(dataItem));
    }

}