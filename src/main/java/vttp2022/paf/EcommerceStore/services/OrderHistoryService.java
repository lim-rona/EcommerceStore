package vttp2022.paf.EcommerceStore.services;

import java.util.ArrayList;
// import java.util.Date;
import java.sql.Date;

import java.util.List;
import java.util.Optional;

import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.opencensus.common.Timestamp;
import vttp2022.paf.EcommerceStore.model.OrderHistory;
import vttp2022.paf.EcommerceStore.model.Product;
import vttp2022.paf.EcommerceStore.repositories.OrderHistoryRepository;
import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;
import vttp2022.paf.EcommerceStore.repositories.UsersRepository;

@Service
public class OrderHistoryService {

    @Autowired
    private OrderHistoryRepository ordhistRepo;

    @Autowired
    private ProductsRepository productRepo;

    @Autowired
    private UsersRepository userRepo;

    @Transactional
    public boolean createOrdhist(java.sql.Timestamp date, int total, String firstName, String lastName,
    String mobile, String country, String shippingAddress, String username, List<Product> cartList){

        int user_id = userRepo.getUserIdByUsername(username);
        ordhistRepo.createOrdhist(date,total,firstName,lastName,mobile,country,shippingAddress,user_id);
        int orderHistory_id = ordhistRepo.getOrdhistidWithDate(date);
        System.out.println("Order History_id is: " + orderHistory_id);

        for(Product product:cartList){
        String productName = product.getProductName();
        double price = product.getPrice();
        int quantityPurchased = product.getQuantityPurchased();
        Product product1 = productRepo.returnIndividualProductByName(productName);
        int product_id = product.getProduct_id();

        ordhistRepo.addOrderItem(productName, price, quantityPurchased,product_id,orderHistory_id);
        }

        return true;
    }

    public List<OrderHistory> getOrderHistory(int user_id){
        OrderHistory orderHistory = new OrderHistory();
        List<OrderHistory> orderhistoryList = new ArrayList<OrderHistory>();

        orderhistoryList = ordhistRepo.getOrderHistory(user_id);

        return orderhistoryList;

    }

    
}
