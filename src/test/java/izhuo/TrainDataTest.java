package izhuo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xunixhuang on 25/07/2017.
 */
public class TrainDataTest {
    @Test
    public void getDataItem() throws Exception {
        TrainData.TrainDataItem dataItem = new TrainData().getDataItem("预订一张从南京到兰州的硬卧，明天中午12点到下午三点之间发车");
        System.out.println("出发城市："+dataItem.getCityFrom());
        System.out.println("到达城市："+dataItem.getCityTo());
        System.out.println("出发时间："+dataItem.getDates());
        System.out.println("座位类型："+dataItem.getSeatType());
    }

}