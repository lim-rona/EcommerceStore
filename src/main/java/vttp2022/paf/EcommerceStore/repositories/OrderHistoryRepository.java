package vttp2022.paf.EcommerceStore.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import io.opencensus.common.Timestamp;
import vttp2022.paf.EcommerceStore.model.OrderHistory;
import vttp2022.paf.EcommerceStore.model.Product;

import static vttp2022.paf.EcommerceStore.repositories.Queries.*;

import java.util.ArrayList;
// import java.util.Date;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.mysql.cj.x.protobuf.MysqlxCrud.Order;


@Repository
public class OrderHistoryRepository {

    @Autowired
    private JdbcTemplate template;

    public boolean createOrdhist(java.sql.Timestamp date, int total, String firstName, String lastName,
        String mobile, String country, String shippingAddress, int user_id){

            int count = template.update(SQL_CREATE_ORDHIST,date,total,firstName,lastName,mobile,country,shippingAddress,user_id);

            return 1==count;
    }

    public int getOrdhistidWithDate(java.sql.Timestamp date){
        String a = date.toString();   
        String b = a.substring(0,19);
        System.out.println("formatted time is: " + a);
        System.out.println("formatted time is: " + b);

        SqlRowSet rs = template.queryForRowSet(SQL_GET_ORDHISTID_WITH_DATE, b+"%");
        if(!rs.next())
            return 0;
        System.out.println("Managed to obtain order_id, it is: "+rs.getInt("order_id"));
        return rs.getInt("order_id");
    }

    //This one deals with Order History Items table
    public boolean addOrderItem(String productName, double price, int quantityPurchased, 
        int product_id, int orderHistory_id){

        int count = template.update(SQL_ADD_ORDERITEM,productName, price, quantityPurchased,
        product_id,orderHistory_id);

        return 1==count;

    }

    public List<OrderHistory> getOrderHistory(int user_id){
        List<OrderHistory> orderhistoryList = new ArrayList<OrderHistory>();
        OrderHistory orderHistory = new OrderHistory();
        SqlRowSet rs = template.queryForRowSet(SQL_GET_ORDERHISTORY, user_id);

        while(rs.next()){
            try{
                orderHistory = OrderHistory.populate(rs);
                orderhistoryList.add(orderHistory);
                
            } catch(Exception ex){
                ex.printStackTrace();
            }
          
        }
        
        return orderhistoryList;


    }

    public List<Product> getOrderHistoryItem(int order_id){
        List<Product> orderHistoryItemList = new ArrayList<>();
        
        SqlRowSet rs = template.queryForRowSet(SQL_GET_ORDERHISTORYITEM, order_id);

        while(rs.next()){

            Product product = new Product();
            product.setProductName(rs.getString("ProductName"));
            product.setPrice(rs.getDouble("Price"));
            product.setQuantityPurchased(rs.getInt("QuantityPurchased"));
            System.out.println("TO PROVE SMTH RAN");
            orderHistoryItemList.add(product);
        }

        return orderHistoryItemList;
    }

    public OrderHistory getOneOrderHistory(int order_id){
        SqlRowSet rs = template.queryForRowSet(SQL_GET_ONE_ORDERHISTORY, order_id);
        OrderHistory orderHistory = new OrderHistory();
        if(rs.next()){
            try{
                orderHistory = OrderHistory.populate(rs);
            } catch(Exception ex){
                ex.printStackTrace();
            }
        } 

        return orderHistory;

    }
    
}
