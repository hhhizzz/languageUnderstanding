package izhuo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.time.nlp.TimeNormalizer;
import com.time.nlp.TimeUnit;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by xunixhuang on 25/07/2017.
 */
public class HotelData {
    public HotelDataItem getData(String sentence) {
        sentence = new FlightData().changeNum(sentence);
        HotelDataItem dataItem = new HotelDataItem();

        //处理价格部分
        List<Double> prices = new FlightData().getPrice(sentence);
        if (prices.size() == 1) {
            dataItem.setPriceRight(prices.get(0));
        } else if (prices.size() >= 2) {
            dataItem.setPriceLeft(prices.get(0));
            dataItem.setPriceRight(prices.get(1));
        }
        //处理时间部分
        List<Date> dates = getDate(sentence);
        if (dates.size() == 1) {
            dataItem.setDateBegin(dates.get(0));
        } else if (dates.size() >= 2) {
            dataItem.setDateBegin(dates.get(0));
            dataItem.setDateEnd(dates.get(1));
        }
        //处理城市部分
        List<String> citys = new FlightData().getCity(sentence);
        dataItem.setCity(citys.get(0));

        //处理地址部分
        String location = getAddress(sentence);
        location = location.replaceAll(citys.get(0),"");
        dataItem.setLocation(location);

        return dataItem;
    }

    List<Date> getDate(String sentence) {
        URL url = TimeNormalizer.class.getClassLoader().getResource("TimeExp.m");
        TimeNormalizer normalizer = new TimeNormalizer(url.getPath());
        normalizer.setPreferFuture(true);
        normalizer.parse(sentence);
        TimeUnit[] units = normalizer.getTimeUnit();
        List<Date> results = new ArrayList<>();
        for (TimeUnit unit : units) {
            results.add(unit.getTime());
        }
        if (results.size() == 1) {
            int day = 1;
            Segment segment = HanLP.newSegment();
            List<Term> termList = segment.seg(sentence);
            List<String> words = termList.stream().map(Term::toString).collect(Collectors.toList());
            String pattern1 = ".*/m";
            String pattern2 = ".*/q|.*/tg";
            Pattern r1 = Pattern.compile(pattern1);
            Pattern r2 = Pattern.compile(pattern2);
            for (int i = 0; i + 1 < words.size(); i++) {
                Matcher m1 = r1.matcher(words.get(i));
                Matcher m2 = r2.matcher(words.get(i + 1));
                if (m1.find() && m2.find()) {
                    day = new Integer(words.get(i).split("/")[0]);
                }
            }
            Calendar cd = Calendar.getInstance();
            cd.setTime(results.get(0));
            cd.add(Calendar.DATE, day);
            Date dateEnd = cd.getTime();
            results.add(dateEnd);
        }
        return results;
    }

    String getAddress(String sentence) {
        Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        List<Term> termList = nShortSegment.seg(sentence);
        List<String> words = termList.stream().map(Term::toString).collect(Collectors.toList());
        String pattern = ".*/n.|.*/a.";
        Pattern r = Pattern.compile(pattern);
        int currentLength = 0;
        int maxLength = 0;
        int currentStart = 0;
        int start = 0;
        for (int i = 0; i < words.size(); i++) {
            Matcher m = r.matcher(words.get(i));
            if (m.find()) {
                if (currentLength == 0) {
                    currentStart = i;
                }
                currentLength++;
            } else {
                if (currentLength > maxLength) {
                    maxLength = currentLength;
                    start = currentStart;
                    currentLength=0;
                }
            }
        }
        String result = null;
        if(maxLength>=2) {
            StringBuilder builder = new StringBuilder();
            for (int i = start; i < start + maxLength; i++){
                builder.append(words.get(i).split("/")[0]);
            }
            result=builder.toString();
        }
        return result;
    }


    class HotelDataItem {
        Date dateBegin;
        Date dateEnd;
        String city;
        String location;
        Double priceLeft;
        Double priceRight;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public Date getDateBegin() {
            return dateBegin;
        }

        public void setDateBegin(Date dateBegin) {
            this.dateBegin = dateBegin;
        }

        public Date getDateEnd() {
            return dateEnd;
        }

        public void setDateEnd(Date dateEnd) {
            this.dateEnd = dateEnd;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Double getPriceLeft() {
            return priceLeft;
        }

        public void setPriceLeft(Double priceLeft) {
            this.priceLeft = priceLeft;
        }

        public Double getPriceRight() {
            return priceRight;
        }

        public void setPriceRight(Double priceRight) {
            this.priceRight = priceRight;
        }

        public HotelDataItem() {
            dateBegin = null;
            dateEnd = null;
            location = null;
            city = null;
            priceLeft = 0.0;
            priceRight = Double.MAX_VALUE;
        }
    }
}
