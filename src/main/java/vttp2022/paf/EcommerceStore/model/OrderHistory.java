package vttp2022.paf.EcommerceStore.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.time.LocalDateTime;
// import java.util.Date;

import com.google.api.client.util.DateTime;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.Timestamp;

public class OrderHistory {
    private int order_id;
    private Timestamp date;
    private double total; //cents
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private String country;
    private String shippingAddress;
    private int user_id;
    private String stringDate;

    //getters and setters
    public int getOrder_id() {
        return order_id;
    }
    public String getStringDate() {
        return stringDate;
    }
    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getShippingAddress() {
        return shippingAddress;
    }
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }
    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
    
    //Method to populate
    public static OrderHistory populate(SqlRowSet rs) throws SQLException {
        OrderHistory orderHistory = new OrderHistory();

        orderHistory.setCountry(rs.getString("country"));
        // Timestamp timestamp = rs.getTimestamp("dateAndTime");
        
        // Date date = rs.getDate("dateAndTime");
        // Date date = rs.getDate("dateAndTime");
        // LocalDateTime localDateTime = timestamp.toLocalDateTime();
        // rs.getTimestamp("dateAndTime");
        java.sql.Timestamp mysqlTimestamp = rs.getTimestamp("dateAndTime");
        orderHistory.setDate(mysqlTimestamp);

        // orderHistory.setDate(rs.getTimestamp("dateAndTime"));
        orderHistory.setEmail(rs.getString("email"));
        orderHistory.setFirstName(rs.getString("firstName"));
        orderHistory.setLastName(rs.getString("lastName"));
        orderHistory.setMobile(rs.getString("mobile"));
        orderHistory.setOrder_id(rs.getInt("order_id"));
        orderHistory.setShippingAddress(rs.getString("shippingAddress"));
        orderHistory.setTotal(rs.getDouble("total"));
        orderHistory.setUser_id(rs.getInt("user_id"));


        return orderHistory;

    }


    
    

}
