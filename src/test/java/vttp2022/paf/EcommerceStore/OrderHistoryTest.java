package vttp2022.paf.EcommerceStore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import vttp2022.paf.EcommerceStore.model.OrderHistory;
import vttp2022.paf.EcommerceStore.model.Product;
import vttp2022.paf.EcommerceStore.repositories.OrderHistoryRepository;
import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;
import vttp2022.paf.EcommerceStore.repositories.UsersRepository;
import vttp2022.paf.EcommerceStore.services.OrderHistoryService;
import vttp2022.paf.EcommerceStore.services.ProductsService;

@SpringBootTest
public class OrderHistoryTest {
    
    @MockBean
    private OrderHistoryRepository ordhistRepo;

    @MockBean
    private ProductsRepository productRepo;

    @MockBean
    private UsersRepository userRepo;

    @Autowired
    private OrderHistoryService ordhistSvc;

    @Test
    void testCreateOrderHistory(){
    //     java.sql.Timestamp date, int total, String firstName, String lastName,
    // String mobile, String country, String shippingAddress, String username, List<Product> cartList
    String username="hoho";
    int total = 3;
    String firstName= "firstName";
    String lastName = "lastName";
    String mobile = "111";
    String country = "Singapore";
    String shippingAddress = "Singapore";
    List<Product> cartList = new ArrayList<>();
    int user_id=0;
    int orderHistory_id=99999;

    //Creating mock products for the cartList
    Product product = new Product();
    product.setProductName("test");
    product.setPrice(2.00);
    product.setQuantityPurchased(1);
    product.setProduct_id(999);
    cartList.add(product);

    Mockito.when(productRepo.returnIndividualProductByName("test")).thenReturn(product);
    Mockito.when(ordhistRepo.addOrderItem("test", 2.00, 1,999,orderHistory_id)).thenReturn(true);
        
   

    java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
    
    Mockito.when(userRepo.getUserIdByUsername(username)).thenReturn(7);
    Mockito.when(ordhistRepo.createOrdhist(date,total,firstName,lastName,mobile,country,shippingAddress,user_id)).thenReturn(true);
    Mockito.when(ordhistRepo.getOrdhistidWithDate(date)).thenReturn(orderHistory_id);

    Boolean verify = true;
    
    assertEquals(true, ordhistSvc.createOrdhist(date, total, firstName, lastName, mobile, country, shippingAddress, username, cartList));
    

    }

    @Test
    void testGetOrderHistory(){
        OrderHistory orderHistory = new OrderHistory();
        List<OrderHistory> orderhistoryList = new ArrayList<OrderHistory>();
        int user_id = 999;

        Mockito.when(ordhistRepo.getOrderHistory(user_id)).thenReturn(orderhistoryList);
        assertEquals(orderhistoryList, ordhistSvc.getOrderHistory(999));
    

    }

}