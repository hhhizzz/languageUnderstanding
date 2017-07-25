package izhuo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by xunixhuang on 25/07/2017.
 */
public class TrainData {
    public TrainDataItem getDataItem(String sentence){
        TrainDataItem dataItem = new TrainDataItem();
        //处理城市部分
        List<String> citys = new FlightData().getCity(sentence);
        if (citys.size() == 1) {
            dataItem.setCityTo(citys.get(0));
        } else if (citys.size() >= 2) {
            dataItem.setCityFrom(citys.get(0));
            dataItem.setCityTo(citys.get(1));
        }
        //处理时间部分
        List<Date> dates = new FlightData().getDate(sentence);
        dataItem.setDates(dates);

        //处理座位类型部分
        SensitivewordFilter filterCabin = new SensitivewordFilter(SensitiveWordInit.SEATS);
        Set<String> setSeat = filterCabin.getSensitiveWord(sentence, 1);
        dataItem.setSeatType(new ArrayList<>(setSeat));

        return dataItem;
    }
    class TrainDataItem {
        String cityFrom;
        String cityTo;
        List<Date> dates;
        List<String> seatType;

        public TrainDataItem() {
            cityFrom = null;
            cityTo = null;
            dates = null;
            seatType = null;
        }

        public String getCityFrom() {
            return cityFrom;
        }

        public void setCityFrom(String cityFrom) {
            this.cityFrom = cityFrom;
        }

        public String getCityTo() {
            return cityTo;
        }

        public void setCityTo(String cityTo) {
            this.cityTo = cityTo;
        }

        public List<Date> getDates() {
            return dates;
        }

        public void setDates(List<Date> dates) {
            this.dates = dates;
        }

        public List<String> getSeatType() {
            return seatType;
        }

        public void setSeatType(List<String> seatType) {
            this.seatType = seatType;
        }
    }
}
