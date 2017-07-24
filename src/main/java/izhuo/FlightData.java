package izhuo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.time.nlp.TimeNormalizer;
import com.time.nlp.TimeUnit;
import com.time.util.DateUtil;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by xunixhuang on 24/07/2017.
 */
public class FlightData {
    public FlightDataItem getData(String sentence) {
        sentence = changeNum(sentence);
        FlightDataItem dataItem = new FlightDataItem();

        //处理城市部分
        List<String> citys = getCity(sentence);
        if (citys.size() == 1) {
            dataItem.setCityTo(citys.get(0));
        } else if (citys.size() >= 2) {
            dataItem.setCityFrom(citys.get(0));
            dataItem.setCityTo(citys.get(1));
        }
        //处理航空公司部分
        dataItem.setAirLine(getAirline(sentence));

        //处理舱位部分
        SensitivewordFilter filterCabin = new SensitivewordFilter(SensitiveWordInit.CABIN);
        Set<String> setCabin = filterCabin.getSensitiveWord(sentence, 1);
        dataItem.setCabin(new ArrayList<>(setCabin));

        //处理机场部分
        SensitivewordFilter filterPort = new SensitivewordFilter(SensitiveWordInit.PORT);
        Set<String> setPort = filterPort.getSensitiveWord(sentence, 1);
        List<String> ports = new ArrayList<>(setPort);
        if (ports.size() == 1) {
            dataItem.setPortFrom(ports.get(0));
        } else if (ports.size() >= 2) {
            dataItem.setPortFrom(ports.get(0));
            dataItem.setPortTo(ports.get(1));
        }

        //处理折扣部分
        dataItem.setRate(getRate(sentence));

        //处理时间部分
        List<Date> dates = getDate(sentence);
        if (dates.size() == 1) {
            dataItem.setTimeBegin(dates.get(0));
        } else if (dates.size() >= 2) {
            dataItem.setTimeBegin(dates.get(0));
            dataItem.setTimeEnd(dates.get(1));
        }

        //处理价格部分
        List<Double> prices = getPrice(sentence);
        if (prices.size() == 1) {
            dataItem.setPriceRight(prices.get(0));
        } else if (dates.size() >= 2) {
            dataItem.setPriceLeft(prices.get(0));
            dataItem.setPriceRight(prices.get(1));
        }

        return dataItem;
    }


    private List<String> getCity(String sentence) {
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        List<Term> termList = segment.seg(sentence);
        List<String> words = termList.stream().map(Term::toString).collect(Collectors.toList());
        List<String> results = new ArrayList<>();

        String pattern = "/ns$";
        Pattern r = Pattern.compile(pattern);
        for (String word : words) {
            Matcher m = r.matcher(word);
            if (m.find()) {
                results.add(word.split("/")[0]);
            }
        }
        return results;
    }

    private String getAirline(String sentence) {
        Segment segment = HanLP.newSegment().enableOrganizationRecognize(true);
        List<Term> termList = segment.seg(sentence);
        List<String> words = termList.stream().map(Term::toString).collect(Collectors.toList());
        String pattern = ".*航.*/j$|.*航.*/nt$";
        Pattern r = Pattern.compile(pattern);
        for (String word : words) {
            Matcher m = r.matcher(word);
            if (m.find()) {
                return word.split("/")[0];
            }
        }
        return null;
    }

    private Double getRate(String sentence) {
        Segment segment = HanLP.newSegment();
        List<Term> termList = segment.seg(sentence);
        List<String> words = termList.stream().map(Term::toString).collect(Collectors.toList());
        String pattern1 = ".*/m";
        String pattern2 = "折.*";
        Pattern r1 = Pattern.compile(pattern1);
        Pattern r2 = Pattern.compile(pattern2);
        for (int i = 0; i + 1 < words.size(); i++) {
            Matcher m1 = r1.matcher(words.get(i));
            Matcher m2 = r2.matcher(words.get(i + 1));
            if (m1.find() && m2.find()) {
                return new Double(words.get(i).split("/")[0]);
            }
        }
        return null;
    }

    private List<Date> getDate(String sentence) {
        URL url = TimeNormalizer.class.getClassLoader().getResource("TimeExp.m");
        TimeNormalizer normalizer = new TimeNormalizer(url.getPath());
        normalizer.setPreferFuture(true);
        normalizer.parse(sentence);
        TimeUnit[] units = normalizer.getTimeUnit();
        List<Date> result = new ArrayList<>();
        for (TimeUnit unit : units) {
            result.add(unit.getTime());
        }
        return result;
    }

    private List<Double> getPrice(String sentence) {
        Segment segment = HanLP.newSegment();
        List<Term> termList = segment.seg(sentence);
        List<String> words = termList.stream().map(Term::toString).collect(Collectors.toList());
        String pattern = "[0-9]*/m$";
        Pattern r = Pattern.compile(pattern);
        List<Double> result = new ArrayList<>();
        for (String word : words) {
            Matcher m = r.matcher(word);
            if (m.find()) {
                Double number = new Double(word.split("/")[0]);
                if (number >= 100) {
                    result.add(number);
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    private String changeNum(String sentence) {
        Segment segment = HanLP.newSegment();
        List<Term> termList = segment.seg(sentence);
        List<String> words = termList.stream().map(Term::toString).collect(Collectors.toList());
        String pattern = "^((?![0-9]).)*/m$";
        StringBuilder result = new StringBuilder();
        Pattern r = Pattern.compile(pattern);
        for (String word : words) {
            Matcher m = r.matcher(word);
            if (m.find()) {
                int wordNumber = NumberParser.parseChineseNumber(word.split("/")[0]);
                result.append(wordNumber);
            } else {
                result.append(word.split("/")[0]);
            }
        }
        return result.toString();
    }

    class FlightDataItem {

        private String cityFrom;
        private String portFrom;
        private String cityTo;
        private String portTo;
        private Date timeBegin;
        private Date timeEnd;
        private String airLine;
        private Double rate;
        private Double priceLeft;
        private Double priceRight;
        private List<String> cabin;

        public FlightDataItem() {
            cityFrom = null;
            portFrom = null;
            cityTo = null;
            portTo = null;
            timeBegin = null;
            timeEnd = null;
            airLine = null;
            rate = null;
            priceLeft = 0.0;
            priceRight = Double.MAX_VALUE;
            cabin = new ArrayList<>();
        }

        public String getCityFrom() {
            return cityFrom;
        }

        public void setCityFrom(String cityFrom) {
            this.cityFrom = cityFrom;
        }

        public String getPortFrom() {
            return portFrom;
        }

        public void setPortFrom(String portFrom) {
            this.portFrom = portFrom;
        }

        public String getCityTo() {
            return cityTo;
        }

        public void setCityTo(String cityTo) {
            this.cityTo = cityTo;
        }

        public String getPortTo() {
            return portTo;
        }

        public void setPortTo(String portTo) {
            this.portTo = portTo;
        }

        public Date getTimeBegin() {
            return timeBegin;
        }

        public void setTimeBegin(Date timeBegin) {
            this.timeBegin = timeBegin;
        }

        public Date getTimeEnd() {
            return timeEnd;
        }

        public void setTimeEnd(Date timeEnd) {
            this.timeEnd = timeEnd;
        }

        public String getAirLine() {
            return airLine;
        }

        public void setAirLine(String airLine) {
            this.airLine = airLine;
        }

        public Double getRate() {
            return rate;
        }

        public void setRate(Double rate) {
            this.rate = rate;
        }

        public List<String> getCabin() {
            return cabin;
        }

        public void setCabin(List<String> cabin) {
            this.cabin = cabin;
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
    }

}
