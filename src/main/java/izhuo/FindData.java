package izhuo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xunixhuang on 25/07/2017.
 */
public class FindData {
    public static String findDataFlight(FlightData.FlightDataItem dataItem) {
        String portFrom = dataItem.getPortFrom();
        String portTo = dataItem.getPortTo();
        String airline = dataItem.getAirLine();
        String cityFrom = dataItem.getCityFrom();
        String cityTo = dataItem.getCityTo();
        List<String> cabin = dataItem.getCabin();
        Double priceLeft = dataItem.getPriceLeft();
        Double priceRight = dataItem.getPriceRight();
        Double rate = dataItem.getRate();
        Date timeBegin = dataItem.getTimeBegin();
        Date timeEnd = dataItem.getTimeEnd();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Segment segment = HanLP.newSegment();


        boolean and = false;

        String mainSQL = "SELECT * FROM flight WHERE ";
        if (portFrom != null) {
            //处理出发机场简写
            List<Term> termList2 = segment.seg(portFrom);
            List<String> words2 = termList2.stream().map(Term::toString).collect(Collectors.toList());
            StringBuilder builder2 = new StringBuilder();
            for (String word : words2) {
                builder2.append(word.split("/")[0] + "%");
            }
            portFrom = builder2.toString();
            mainSQL += String.format("dPort LIKE \'%s\' ", "%" + portFrom);
            and = true;
        }
        if (portTo != null) {
            //处理到达机场简写
            List<Term> termList3 = segment.seg(portTo);
            List<String> words3 = termList3.stream().map(Term::toString).collect(Collectors.toList());
            StringBuilder builder3 = new StringBuilder();
            for (String word : words3) {
                builder3.append(word.split("/")[0] + "%");
            }
            portTo = builder3.toString();
            if (and) {
                mainSQL += String.format("AND aPort LIKE \'%s\' ", "%" + portTo);
            } else {
                mainSQL += String.format("aPort LIKE \'%s\' ", "%" + portTo);
                and = true;
            }
        }
        if (airline != null) {
            //处理航空公司简写
            List<Term> termList = segment.seg(airline);
            List<String> words = termList.stream().map(Term::toString).collect(Collectors.toList());
            StringBuilder builder = new StringBuilder();
            for (String word : words) {
                builder.append(word.split("/")[0] + "%");
            }
            airline = builder.toString();
            if (and) {
                mainSQL += String.format("AND airline LIKE \'%s\' ", "%" + airline);
            } else {
                mainSQL += String.format("airline LIKE \'%s\' ", "%" + airline);
                and = true;
            }
        }
        if (cityFrom != null) {
            if (and) {
                mainSQL += String.format("AND departCity = \'%s\' ", cityFrom);
            } else {
                mainSQL += String.format("departCity = \'%s\' ", cityFrom);
                and = true;
            }
        }
        if (cityTo != null) {
            if (and) {
                mainSQL += String.format("AND arriveCity = \'%s\' ", cityTo);
            } else {
                mainSQL += String.format("arriveCity = \'%s\' ", cityTo);
                and = true;
            }
        }
        if (cabin.size() != 0) {
            if (and) {
                if (cabin.size() == 1) {
                    mainSQL += String.format("AND cabinInfo = \'%s\' ");
                } else {
                    mainSQL += "AND (cabinInfo = ";
                    for (int i = 0; i < cabin.size(); i++) {
                        if (i == 0) {
                            mainSQL += String.format(" \'%s\' ", cabin.get(i));
                        } else {
                            mainSQL += String.format("OR cabinInfo = \'%s\' ", cabin.get(i));
                        }
                    }
                    mainSQL += ") ";
                }
            } else {
                if (cabin.size() == 1) {
                    mainSQL += String.format("cabinInfo = \'%s\' ");
                } else {
                    mainSQL += "(cabinInfo = ";
                    for (int i = 0; i < cabin.size(); i++) {
                        if (i == 0) {
                            mainSQL += String.format(" \'%s\' ", cabin.get(i));
                        } else {
                            mainSQL += String.format("OR cabinInfo = \'%s\' ", cabin.get(i));
                        }
                    }
                    mainSQL += ") ";
                }
                and = true;
            }
        }
        if (rate != null) {
            while (rate > 1) {
                rate /= 10;
            }
            if (and) {
                mainSQL += String.format("AND rate <= %f ", rate);
            } else {
                mainSQL += String.format("rate <= %f ", rate);
                and = true;
            }
        }
        if (timeBegin != null) {
            if (and) {
                mainSQL += String.format("AND takeOffTime >= \'%s\' ", format.format(timeBegin));
            } else {
                mainSQL += String.format("takeOffTime >= \'%s\' ", format.format(timeBegin));
                and = true;
            }
        }
        if (timeEnd != null) {
            if (and) {
                mainSQL += String.format("AND takeOffTime <= \'%s\' ", format.format(timeEnd));
            } else {
                mainSQL += String.format("takeOffTime <= \'%s\' ", format.format(timeEnd));
                and = true;
            }
        }
        if (and) {
            mainSQL += String.format("AND price >= %f ", priceLeft);
            mainSQL += String.format("AND price <= %f ", priceRight);
        } else {
            mainSQL += String.format("price >= %f ", priceLeft);
            mainSQL += String.format("price <= %f ", priceRight);
        }
        return mainSQL;
    }
}
