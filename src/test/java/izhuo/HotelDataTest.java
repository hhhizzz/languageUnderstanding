package izhuo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by xunixhuang on 25/07/2017.
 */
public class HotelDataTest {
    @Test
    public void getData() throws Exception {
        HotelData.HotelDataItem dataItem = new HotelData().getData("预订郑州市西大街二七广场附近的酒店两晚，后天入住，价格200-400");
        System.out.println("城市："+dataItem.getCity());
        System.out.println("价格最低："+dataItem.getPriceLeft());
        System.out.println("价格最高："+dataItem.getPriceRight());
        System.out.println("开始时间："+dataItem.getDateBegin());
        System.out.println("结束时间："+dataItem.getDateEnd());
        System.out.println("位置："+dataItem.getLocation());
    }

}