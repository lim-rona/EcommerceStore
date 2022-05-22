package vttp2022.paf.EcommerceStore.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import vttp2022.paf.EcommerceStore.model.Product;
import vttp2022.paf.EcommerceStore.model.Test;
import vttp2022.paf.EcommerceStore.model.User;
import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;
import vttp2022.paf.EcommerceStore.services.ProductsService;
import vttp2022.paf.EcommerceStore.services.UsersService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
// @RequestMapping(path={"/","/home"})
public class EcommerceController {

    @Autowired
    private UsersService usersSvc;

    @Autowired
    private ProductsRepository productsRepo;

    @Autowired
    private ProductsService productsSvc;

    
    @RequestMapping(path={"/","/home"})
    public ModelAndView homePage(HttpSession sess){
        System.out.println("it did run here orig");
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
            System.out.println("User failed to authenticate on homepage");
            return mvc;

        } else {
            // Successful
            System.out.println("else was ran");

            user.setStatus(true);
            mvc.addObject("user", user);
            System.out.println("User authenticated on homepage");
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
            System.out.println("From Ourstory, the user's status is: "+ status);
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
    public ModelAndView myAccountPage(){
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("myAccount");
        
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
            mvc.addObject(user);
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

    @PostMapping(path="/UserSignUpInfo")
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
        

        if (!usersSvc.authenticate(username, password)) {
            // Means user not registered,
           try{
               usersSvc.insertNewUser(user); //also created a cart when u insert new user
               user.setStatus(true);
               mvc.addObject("user",user);
               mvc.addObject("message", "You have been added to the system successfully!");
               sess.setAttribute("username", user.getUsername());
               sess.setAttribute("user", user);
            //registered successfully and return
            } catch(Exception ex){ //Here can create custom exception
                mvc.setStatus(HttpStatus.BAD_REQUEST);
                ex.printStackTrace();
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

