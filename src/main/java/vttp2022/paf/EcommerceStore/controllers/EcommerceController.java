package vttp2022.paf.EcommerceStore.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.nimbusds.oauth2.sdk.util.MultivaluedMapUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.spring5.expression.Mvc;

import vttp2022.paf.EcommerceStore.model.Category;
import vttp2022.paf.EcommerceStore.model.OrderHistory;
import vttp2022.paf.EcommerceStore.model.Product;
import vttp2022.paf.EcommerceStore.model.Test;
import vttp2022.paf.EcommerceStore.model.User;
import vttp2022.paf.EcommerceStore.repositories.OrderHistoryRepository;
import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;
import vttp2022.paf.EcommerceStore.repositories.UsersRepository;
import vttp2022.paf.EcommerceStore.services.OrderHistoryService;
import vttp2022.paf.EcommerceStore.services.ProductsService;
import vttp2022.paf.EcommerceStore.services.UsersService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
// @RequestMapping(path={"/","/home"})
public class EcommerceController {

    @Autowired
    private UsersRepository usersRepo;

    @Autowired
    private UsersService usersSvc;

    @Autowired
    private ProductsRepository productsRepo;

    @Autowired
    private ProductsService productsSvc;

    @Autowired
    private OrderHistoryService orderhistSvc;

    @Autowired
    private OrderHistoryRepository orderhistRepo;

    
    @RequestMapping(path={"/","/home"})
    public ModelAndView homePage(HttpSession sess){
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("index");
        String username;

        User user = (User)sess.getAttribute("user");
        if(user==null){
            username = null;
        } else{
        username = user.getUsername();
        }

        //returning different status 
        if (!usersSvc.authenticate(username)) {
            // Not successful
            return mvc;

        } else {
            // Successful

            user.setStatus(true);
            mvc.addObject("user", user);
            return mvc;
        }
        
    }

    @GetMapping(path="/ourStory")
    public ModelAndView ourStoryPage(HttpSession sess){
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("ourStory");

        
        User placeholder = (User)sess.getAttribute("user");
        User user = new User();
        if(placeholder==null){
            user.setStatus(false);
            mvc.addObject("user", user);
        } else{
            user = placeholder;
            Boolean status = user.isStatus();
            mvc.addObject("user", user);
        }
                
        return mvc;
    }

    @GetMapping(path="/signIn")
    public ModelAndView signInPage() {
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("signIn");
        
        return mvc;
    }

    @GetMapping(path="/myAccount")
    //Need to handle so that un-logged in can't enter this page, forbidden
    public ModelAndView myAccountPage(HttpSession sess){
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("myAccount");
        String username;

        //Retrieve username
        User user = (User)sess.getAttribute("user");
        if(user==null){
            username = null;
            user.setStatus(false);
            mvc.addObject("user", user);
        } else{
            username = user.getUsername();
            Boolean status = user.isStatus();
            mvc.addObject("user", user);
        }

        //Using username, get user_id to retrieve orderhistory and items
        int user_id = usersRepo.getUserIdByUsername(username);
        List<OrderHistory> orderhistoryList = new ArrayList<OrderHistory>();
        orderhistoryList = orderhistSvc.getOrderHistory(user_id);
        for(OrderHistory orderhistory: orderhistoryList){
            String stringDate = orderhistory.getDate().toString();
            orderhistory.setStringDate(stringDate);
        }

        
        // if(orderhistoryList!=null){
        //     for(OrderHistory orderHistory: orderhistoryList){
        //         int order_id = orderhistRepo.getOrdhistidWithUserid(user_id);
        //         List<Product> orderHistoryList = orderhistRepo.getOrderHistoryItem(order_id);
        //     }
            
        // }
        
        mvc.addObject("orderhistoryList", orderhistoryList);


        
        return mvc;
    }

    @PostMapping(path="/orderHistory/{orderHistory}")
    public ModelAndView getOrderHistoryItems(@RequestBody MultiValueMap<String, String> payload,
     HttpSession sess,@PathVariable String orderHistory){
        ModelAndView mvc = new ModelAndView();
        String username;

        //Retrieve username
        User user = (User)sess.getAttribute("user");
        if(user==null){
            username = null;
            user.setStatus(false);
            mvc.addObject("user", user);
        } else{
            username = user.getUsername();
            Boolean status = user.isStatus();
            mvc.addObject("user", user);

        }
        
        String order_idString = payload.getFirst(orderHistory);
        System.out.println("ordhist is: "+ orderHistory);
        int order_id = Integer.valueOf(order_idString);
        List<Product> orderHistoryItemListPlaceholder = orderhistRepo.getOrderHistoryItem(order_id);
        int user_id = usersRepo.getUserIdByUsername(username);
        OrderHistory soloOrderHistory = orderhistRepo.getOneOrderHistory(order_id);
        System.out.println("order_id= " + order_id);
        for(Product product:orderHistoryItemListPlaceholder){
            System.out.println("productname is: " + product.getProductName());
        }
        
        String stringDate = soloOrderHistory.getDate().toString();
        soloOrderHistory.setStringDate(stringDate);
        List<Product> orderHistoryItemList = new ArrayList<>();
        
        //To get the photo in, 
        for(Product product:orderHistoryItemListPlaceholder){
            int quantityPurchased=product.getQuantityPurchased();
            product = productsRepo.returnIndividualProductByName(product.getProductName());
            product.setQuantityPurchased(quantityPurchased);
            orderHistoryItemList.add(product);

        }
        
        
        mvc.addObject("orderHistory",soloOrderHistory);
        mvc.addObject("orderHistoryItemList",orderHistoryItemList);
        mvc.setViewName("orderHistoryItem");
        
        return mvc;
    }

    

    @PostMapping(path="/login")
    public ModelAndView getLoginDetails(@RequestBody MultiValueMap<String, String> payload, 
    HttpSession sess){
        String username = payload.getFirst("username");
        String password = payload.getFirst("password");

        System.out.printf("+++ username: %s, password: %s\n", username, password);

        ModelAndView mvc = new ModelAndView();
        User user = new User();
        List<Product> cartList = new ArrayList<>();

        //If a lists exists for the httpsession, set it to cartList
        if(sess.getAttribute("cartList")!=null){
            cartList = (List)sess.getAttribute("cartList");
        }

        if (!usersSvc.authenticate(username, password)) {
            // Not successful
            mvc.addObject("message", "(Incorrect username or password, please try again.)");
            mvc.setViewName("signIn");
            // mvc = new ModelAndView("redirect:/signIn");
            // mvc.setStatus(HttpStatus.FORBIDDEN);

        }else{
            // Successful
            user.setStatus(true);
            user.setUsername(username);
            // user.setPassword(password);
            sess.setAttribute("user", user);
            mvc = new ModelAndView("redirect:/home");

            //Upon successful login, we add what's in the httpsession's cart into
            //the user's cart
            for (int i = 0; i < cartList.size(); i++) {
                Product product = new Product();
                product = cartList.get(i);
                String productName = product.getProductName();
                int quantityPurchased = product.getQuantityPurchased();
                double price = product.getPrice();
                productsSvc.addItemIntoUserCart(productName, quantityPurchased, price, username);
            };

            //And now we remove the cartList from the httpSession
            sess.removeAttribute("cartList");
            
        }
        
        return mvc;
    }

    @GetMapping(path="/cart")
    public ModelAndView cartPage(HttpSession sess){ //Need to make them sign up when checkout?
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("cart");

        User placeholder = (User)sess.getAttribute("user");
        User user = new User();
        String username;
        List<Product> cartList = new ArrayList<>();

        System.out.println("The allProducts ran");
        if(placeholder==null){
            user.setStatus(false);
            mvc.addObject("user", user);
        } else{
            user = placeholder;
            Boolean status = user.isStatus();
            mvc.addObject("user", user);
        }

        //Retrieve list for login user
        if(placeholder!=null){
            username = user.getUsername();
            //Then we retrieve it and add the list to mvc
            cartList = productsSvc.getCartWithUsername(username);
            mvc.addObject("cartList", cartList);
        } else{
            //Retrieve list of products from non-login customer
            if(sess.getAttribute("cartList")!=null){
                cartList = (List)sess.getAttribute("cartList");
                mvc.addObject("cartList", cartList);
            }
        }
       
        return mvc;
    }
    

    @GetMapping(path="/success")
    public ModelAndView success(@AuthenticationPrincipal OAuth2User principal, HttpSession sess){
        ModelAndView mvc = new ModelAndView();
        String email = principal.getAttribute("email");
        
        User user = new User();
        user.setEmail(email);
        System.out.println("The OAUTH email is: "+ email);
        //Using email, we verify if user exists. 
        if(!usersSvc.authenticateByEmail(email)){
            //If don't exist, go to signup page
            mvc.setViewName("userSignUp");
            mvc.addObject("user",user);
        } else{
            //means user already exists, redirect to home page
            user.setStatus(true);
            String username = usersSvc.getUserNameByEmail(email);
            System.out.println("Username is:" + username);
            user.setUsername(username);
            sess.setAttribute("user",user);
            mvc.addObject("user",user);
            mvc = new ModelAndView("redirect:/home");
        }

        return mvc;
    }

    @PostMapping(path="/Signup/UserSignUpInfo")
    public ModelAndView saveNewGoogleUserAndBindToHttp(@RequestBody MultiValueMap<String, String> payload,
    HttpSession sess){
        //Need to insert a check here to ensure we don't anyhow save things, either 
        //non logged-in users cannot access this page, or that what they put in
        //we won't save
        String name = payload.getFirst("name");
        String email = payload.getFirst("email");
        String username = payload.getFirst("username");
        String password = payload.getFirst("password");

        System.out.println("The name is: "+ name);
        System.out.printf("+++ username: %s, password: %s\n", username, password);
        System.out.println("the email is:"+ email);

        ModelAndView mvc = new ModelAndView();
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        

        if(usersSvc.authenticate(username,password)){
            //means user already exists, 
            mvc.addObject("message","account already exists");
            mvc.setViewName("userSignUp");
            return mvc;
        }

        if (!usersSvc.authenticate(username, password)) {
            // Means user not registered,
           try{
               if(usersSvc.authenticate(username)){
                   //if username is already taken,
                   mvc.addObject("message", "Username taken");
                   mvc.setViewName("userSignUp");
                   return mvc;
               }
               //Now we try inserting the new user. I didn't put much requirements so should be fine
               usersSvc.insertNewUser(user); //also created a cart when u insert new user

                List<Product> cartList = new ArrayList<>();

                //If a lists exists for the httpsession, set it to cartList
                if(sess.getAttribute("cartList")!=null){
                    cartList = (List)sess.getAttribute("cartList");
                }

               //Now need to take the httpsession cart and add to user's cart:
               for (int i = 0; i < cartList.size(); i++) {
                Product product = new Product();
                product = cartList.get(i);
                String productName = product.getProductName();
                int quantityPurchased = product.getQuantityPurchased();
                double price = product.getPrice();
                productsSvc.addItemIntoUserCart(productName, quantityPurchased, price, username);
                };

                  //And now we remove the cartList from the httpSession
                sess.removeAttribute("cartList");

                user.setStatus(true);
                mvc.addObject("user",user);
                mvc.addObject("message", "You have been added to the system successfully!");
                sess.setAttribute("username", user.getUsername());
                sess.setAttribute("user", user);
            //registered successfully and return
            } catch(Exception ex){ //Here can create custom exception
                mvc.setStatus(HttpStatus.BAD_REQUEST);
                }
                
        } 
            
        

        mvc.setViewName("signUpSuccess");
        return mvc;
    }

    @GetMapping("/logout")
    public ModelAndView getLogout(HttpSession sess) {
        ModelAndView mvc = new ModelAndView();
        sess.invalidate();
        mvc = new ModelAndView("redirect:/home");
          
        return mvc;
    }
    
    @GetMapping("/userSignUp")
    public String userSignUp(){
        return "userSignUp";
    }
}

