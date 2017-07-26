package izhuo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.json.simple.JSONValue;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by xunixhuang on 25/07/2017.
 */
public class FindData {
    static String databaseURL = "jdbc:mysql://localhost/flight?useUnicode=true&characterEncoding=utf-8";
    static String username = "root";
    static String password = "123456";

    public static String findDataFlight(FlightData.FlightDataItem dataItem) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {

        Class.forName("com.mysql.jdbc.Driver").newInstance();
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
        Connection conn = DriverManager.getConnection(databaseURL, username, password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(mainSQL);

        return getJSONFromResultSet(rs, "data");
    }

    private static String getJSONFromResultSet(ResultSet rs, String keyName) {
        Map json = new HashMap();
        List list = new ArrayList();
        if (rs != null) {
            try {
                ResultSetMetaData metaData = rs.getMetaData();
                while (rs.next()) {
                    Map<String, Object> columnMap = new HashMap<String, Object>();
                    for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
                        if (rs.getString(metaData.getColumnName(columnIndex)) != null)
                            columnMap.put(metaData.getColumnLabel(columnIndex), rs.getString(metaData.getColumnName(columnIndex)));
                        else
                            columnMap.put(metaData.getColumnLabel(columnIndex), "");
                    }
                    list.add(columnMap);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            json.put(keyName, list);
        }
        return JSONValue.toJSONString(json);
    }

    public static String findDataHotel(HotelData.HotelDataItem dataItem) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String city = dataItem.getCity();
        String location = dataItem.getLocation();
        Double priceLeft = dataItem.getPriceLeft();
        Double priceRight = dataItem.getPriceRight();

        Segment segment = HanLP.newSegment();

        boolean and = false;

        String mainSQL = "SELECT * FROM hotel WHERE ";

        if (location != null) {
            //处理位置简写
            List<Term> termList1 = segment.seg(location);
            List<String> words1 = termList1.stream().map(Term::toString).collect(Collectors.toList());
            StringBuilder builder2 = new StringBuilder();
            for (String word : words1) {
                builder2.append(word.split("/")[0] + "%");
            }
            location = builder2.toString();
            mainSQL += String.format("address LIKE \'%s\' ", "%" + location);
            and = true;
        }
        if (city != null) {
            if (and) {
                mainSQL += String.format("AND city = \'%s\' ", city);
            } else {
                mainSQL += String.format("city = \'%s\' ", city);
            }
        }
        if (and) {
            mainSQL += String.format("AND price >= %f ", priceLeft);
            mainSQL += String.format("AND price <= %f ", priceRight);
        } else {
            mainSQL += String.format("price >= %f ", priceLeft);
            mainSQL += String.format("price <= %f ", priceRight);
        }
        Connection conn = DriverManager.getConnection(databaseURL, username, password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(mainSQL);
        return getJSONFromResultSet(rs, "data");
    }

    public static String findDataTrain(TrainData.TrainDataItem dataItem) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String cityFrom = dataItem.getCityFrom();
        String cityTo = dataItem.getCityTo();
        List<Date> dates = dataItem.getDates();
        List<String> seatType = dataItem.getSeatType();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Segment segment = HanLP.newSegment();

        boolean and = false;

        String mainSQL = "SELECT * FROM train WHERE ";
        if (cityFrom != null) {
            mainSQL += String.format("originStation LIKE \'%s\' ", "%" + cityFrom + "%");
            and = true;
        }
        if (cityTo != null) {
            if (and) {
                mainSQL += String.format("AND terminalStation LIKE \'%s\' ", "%" + cityTo + "%");
            } else {
                mainSQL += String.format("terminalStation LIKE \'%s\' ", "%" + cityTo + "%");
            }
        }
        if (dates.size() == 2) {
            if (and) {
                mainSQL += String.format("AND startTime >= \'%s\' ", format.format(dates.get(0)));
                mainSQL += String.format("AND startTime <= \'%s\' ", format.format(dates.get(1)));
            } else {
                mainSQL += String.format("startTime >= \'%s\' ", format.format(dates.get(0)));
                mainSQL += String.format("AND startTime <= \'%s\' ", format.format(dates.get(1)));
            }
        }
        if (seatType.size() >= 1) {
            if (and) {
                for (String seat : seatType) {
                    mainSQL += String.format("AND %s != -1 ", seat);
                }
            } else {
                for (int i = 0; i < seatType.size(); i++) {
                    if (i == 0) {
                        mainSQL += String.format("%s != -1 ", seatType.get(i));
                    } else {
                        mainSQL += String.format("AND %s != -1 ", seatType.get(i));
                    }
                }
            }
        }
        Connection conn = DriverManager.getConnection(databaseURL, username, password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(mainSQL);

        Map json = new HashMap();
        List list = new ArrayList();
        while (rs.next()) {
            Map<String, String> tempMap = new HashMap<>();
            tempMap.put("terminalStation", rs.getString("terminalStation"));
            tempMap.put("originStation", rs.getString("originStation"));
            tempMap.put("trainNo", rs.getString("trainNo"));
            tempMap.put("trainType", rs.getString("trainType"));
            tempMap.put("startTime", format.format(rs.getDate("startTime")));
            tempMap.put("arriveTime", format.format(rs.getDate("arriveTime")));
            list.add(tempMap);
            List<Map<String, Object>> seatList = new ArrayList<>();
            Map<String, List<Map<String, Object>>> priceMap = new HashMap<>();
            for (int i = 0; i < seatType.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", seatType.get(i));
                map.put("value", rs.getInt(seatType.get(i)));
                seatList.add(map);
            }
            priceMap.put("price", seatList);
            list.add(priceMap);
        }


        json.put("data", list);
        return JSONValue.toJSONString(json);
    }
}
