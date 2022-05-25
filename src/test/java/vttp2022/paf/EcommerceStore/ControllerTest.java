package vttp2022.paf.EcommerceStore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockitoSession;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;

import vttp2022.paf.EcommerceStore.controllers.EcommerceController;
import vttp2022.paf.EcommerceStore.controllers.ProductsController;
import vttp2022.paf.EcommerceStore.model.Category;
import vttp2022.paf.EcommerceStore.model.OrderHistory;
import vttp2022.paf.EcommerceStore.model.Product;
import vttp2022.paf.EcommerceStore.model.User;
import vttp2022.paf.EcommerceStore.repositories.OrderHistoryRepository;
import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;
import vttp2022.paf.EcommerceStore.repositories.UsersRepository;
import vttp2022.paf.EcommerceStore.services.OrderHistoryService;
import vttp2022.paf.EcommerceStore.services.ProductsService;
import vttp2022.paf.EcommerceStore.services.UsersService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private EcommerceController ecommerceController;

    @InjectMocks
    private ProductsController productsController;

    @MockBean
    private UsersRepository usersRepo;

    @MockBean
    private UsersService usersSvc;

    @MockBean
    private ProductsRepository productsRepo;

    @MockBean
    private ProductsService productsSvc;

    @MockBean
    private OrderHistoryService orderhistSvc;

    @MockBean
    private OrderHistoryRepository orderhistRepo;

    

    @Test
    void testHomePage(){
        MockHttpSession sess = new MockHttpSession();
        String username="username";

        User user = new User();
        user.setUsername(username);
        sess.setAttribute("user",user);
        

        // Mockito.when(usersRepo.countUsersByName(username)).thenReturn(1);
        // Mockito.when(usersSvc.authenticate(username)).thenReturn(true);
        ModelAndView mvc1 = ecommerceController.homePage(sess);
        doReturn(true).when(usersSvc).authenticate(username);

        
        assertEquals("index", mvc1.getViewName());
    }

    @Test
    void testOurStory(){
        MockHttpSession sess = new MockHttpSession();
        String username="username";

        User user = new User();
        user.setUsername(username);
        sess.setAttribute("user",user);
        
        ModelAndView mvc1 = ecommerceController.ourStoryPage(sess);
        doReturn(true).when(usersSvc).authenticate(username);

        
        assertEquals("ourStory", mvc1.getViewName());
    }

    @Test
    void signIn(){

  
        ModelAndView mvc1 = ecommerceController.signInPage();

        
        assertEquals("signIn", mvc1.getViewName());
    }

    @Test
    void testSaveNewGoogleUserAndBindToHttp(){
        MockHttpSession sess = new MockHttpSession();
        String username="test";
        String password="test";
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name","test");
        form.add("email","test@gmail.com");
        form.add("username",username);
        form.add("password",password);

        List<Product> cartList = new ArrayList<>();
        Product product = new Product();
        product.setProductName("test");
        product.setPrice(2.00);
        product.setQuantityPurchased(1);
        product.setProduct_id(999);
        cartList.add(product);
        sess.setAttribute("cartList", cartList);


        

        ModelAndView mvc1 = ecommerceController.saveNewGoogleUserAndBindToHttp(form, sess);

        Mockito.when(usersSvc.authenticate(username,password)).thenReturn(true);

        assertEquals("signUpSuccess", mvc1.getViewName());

    }

    @Test
    void testGetOrderHistoryItem(){
        MockHttpSession sess = new MockHttpSession();
        String username="username";

        User user = new User();
        user.setUsername(username);
        sess.setAttribute("user",user);

        String orderHistory="1";
        int order_id=1;
        String order_idString="1";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("1","1");

        List<Product> orderHistoryItemListPlaceholder = new ArrayList<>();

        Mockito.when(orderhistRepo.getOrderHistoryItem(order_id)).thenReturn(orderHistoryItemListPlaceholder);
        int user_id=1;
        Mockito.when(usersRepo.getUserIdByUsername(username)).thenReturn(user_id);
        OrderHistory soloOrderHistory = new OrderHistory();
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());

        soloOrderHistory.setDate(date);
        Mockito.when(orderhistRepo.getOneOrderHistory(order_id)).thenReturn(soloOrderHistory);
        

        List<Product> orderHistoryItemList = new ArrayList<>();
        

        ModelAndView mvc1 = ecommerceController.getOrderHistoryItems(form, sess, "1");

        assertEquals("orderHistoryItem", mvc1.getViewName());

    }

    @Test
    void testGetLoginDetails(){
        MockHttpSession sess = new MockHttpSession();
        String username="test";
        String password="test";


        User user = new User();
        user.setUsername(username);
        sess.setAttribute("user",user);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name","test");
        form.add("email","test@gmail.com");
        form.add("username","test");
        form.add("password",password);

        // List<Product> cartList = new ArrayList<>();
        // Product product = new Product();
        // product.setProductName("test");
        // product.setPrice(2.00);
        // product.setQuantityPurchased(1);
        // product.setProduct_id(999);
        // cartList.add(product);
        // sess.setAttribute("cartList", cartList);
        


        Mockito.when(!usersSvc.authenticate(username, password)).thenReturn(false);

        ModelAndView mvc1 = ecommerceController.getLoginDetails(form, sess);


        assertEquals("signIn", mvc1.getViewName());

    }

    @Test
    void testGetLoginDetails2(){
        MockHttpSession sess = new MockHttpSession();
        String username="test";
        String password="test";


        User user = new User();
        user.setUsername(username);
        sess.setAttribute("user",user);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name","test");
        form.add("email","test@gmail.com");
        form.add("username","test");
        form.add("password",password);

        List<Product> cartList = new ArrayList<>();
        Product product = new Product();
        product.setProductName("test");
        product.setPrice(2.00);
        product.setQuantityPurchased(1);
        product.setProduct_id(999);
        cartList.add(product);
        sess.setAttribute("cartList", cartList);
        


        Mockito.when(!usersSvc.authenticate(username, password)).thenReturn(true);

        ModelAndView mvc1 = ecommerceController.getLoginDetails(form, sess);


        assertEquals("redirect:/home", mvc1.getViewName());

    }

    @Test
    void testCartPage(){
        MockHttpSession sess = new MockHttpSession();
        String username="username";

        User user = new User();
        user.setUsername(username);
        sess.setAttribute("user",user);
        
        ModelAndView mvc1 = ecommerceController.cartPage(sess);
        doReturn(true).when(usersSvc).authenticate(username);

        
        assertEquals("cart", mvc1.getViewName());
    }

    @Test
    void testMyAccountPage(){
        MockHttpSession sess = new MockHttpSession();
        String username="username";

        User user = new User();
        user.setUsername(username);
        sess.setAttribute("user",user);
        
        ModelAndView mvc1 = ecommerceController.myAccountPage(sess);
        doReturn(true).when(usersSvc).authenticate(username);

        int user_id=1;
        Mockito.when(usersRepo.getUserIdByUsername(username)).thenReturn(user_id);
        List<OrderHistory> orderHistoryList = new ArrayList<>();
        Mockito.when(orderhistSvc.getOrderHistory(user_id)).thenReturn(orderHistoryList);
        OrderHistory soloOrderHistory = new OrderHistory();
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());

        soloOrderHistory.setDate(date);
        orderHistoryList.add(soloOrderHistory);


        
        assertEquals("myAccount", mvc1.getViewName());
    }

    //for products controller
    
    @Test
    void testProductsPage(){
        MockHttpSession sess = new MockHttpSession();
        String username="username";

        User user = new User();
        user.setUsername(username);
        sess.setAttribute("user",user);

        ModelAndView mvc1 = productsController.productsPage(sess);
        doReturn(true).when(usersSvc).authenticate(username);

        List<Category> categoryList = new ArrayList<Category>();
        Mockito.when(productsRepo.retrieveCategoryList()).thenReturn(categoryList);

        List<Product> allProductsList = new ArrayList<Product>();
        Mockito.when(productsRepo.retrieveAllProductsList()).thenReturn(allProductsList);


        
        assertEquals("allProducts", mvc1.getViewName());
    }
        

    

    

    
    
}
