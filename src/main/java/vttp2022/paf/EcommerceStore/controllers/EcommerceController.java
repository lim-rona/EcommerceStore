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
import org.springframework.web.bind.annotation.RequestMapping;
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
        //need to settle the case where people click our story from un log-in part
        User placeholder = (User)sess.getAttribute("user");
        User user = new User();
        if(placeholder==null){
            user.setStatus(false);
            mvc.addObject("user", user);
            return mvc;
        } else{
            user = placeholder;
            Boolean status = user.isStatus();
            System.out.println("From Ourstory, the user's status is: "+ status);
            mvc.addObject("user", user);
        }
                
        return mvc;
    }

    @GetMapping(path="/myAccount")
    public ModelAndView myAccountPage(){
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("myAccount");
        
        return mvc;
    }

    @GetMapping(path="/allProducts")
    public ModelAndView productsPage(){
        ModelAndView mvc = new ModelAndView();
        //1. Retrieve Category List
        List<Category> categoryList = new ArrayList<Category>();
        categoryList = productsRepo.retrieveCategoryList();
        mvc.addObject("categoryList", categoryList);
        //2. Retrieve List of all items 
        List<Product> allProductsList = new ArrayList<Product>();
        allProductsList = productsRepo.retrieveAllProductsList();
        mvc.addObject("allProductsList", allProductsList);

        mvc.setViewName("allProducts");
        return mvc;
    }

    @GetMapping(path="/signIn")
    public ModelAndView signInPage() {
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("signIn");
        
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

        if (!usersSvc.authenticate(username, password)) {
            // Not successful
            mvc.setViewName("error"); //maybe here can put the sign up page
            mvc.setStatus(HttpStatus.FORBIDDEN);

        } else {
            // Successful
            System.out.println("It did succeed");
            user.setStatus(true);
            user.setUsername(username);
            user.setPassword(password);
            sess.setAttribute("user", user); //binds an object to the session
            mvc = new ModelAndView("redirect:/home");
            //So I just put httpsession in /home and try, see if can have null anot 
        }
        
        return mvc;
    }

    @GetMapping(path="/cart")
    public ModelAndView cartPage(){
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("cart");
        
        return mvc;
    }
    

    @GetMapping(path="/success")
    public ModelAndView success(@AuthenticationPrincipal OAuth2User principal){
        ModelAndView mvc = new ModelAndView();
        String email = principal.getAttribute("email");
        
        User user = new User();
        user.setEmail(email);
        System.out.println("The OAUTH email is: "+ email);
        //Using email, we verify if user exists. 
        if(!usersSvc.authenticateByEmail(email)){
            //means not registered,
            mvc.setViewName("userSignUp");
            mvc.addObject(user);
        } else{
            //means user already exists, redirect to home page
            mvc.setViewName("index");
            user.setStatus(true);
            mvc.addObject(user);
            //Here need to make a user object and put status 
        }

        return mvc;
    }

    @PostMapping(path="/googleUserSignUpInfo")
    public ModelAndView saveNewGoogleUserAndBindToHttp(@RequestBody MultiValueMap<String, String> payload,
    HttpSession sess){
        //Need to insert a check here to ensure we don't anyhow save things, either 
        //non logged-in users cannot access this page, or that what they put in
        //we won't save
        String name = payload.getFirst("name");
        String email = payload.getFirst("email");
        String username = payload.getFirst("username");
        String password = payload.getFirst("password");

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
               usersSvc.insertNewUser(user);
               mvc.addObject("user",user);
               mvc.addObject("message", "You have been added to the system successfully!");
               sess.setAttribute("username", user.getUsername());
            //registered successfully and return
            } catch(Exception ex){ //Here can create custom exception
                mvc.setStatus(HttpStatus.BAD_REQUEST);
                ex.printStackTrace();
            }
        }
 

        mvc.setViewName("signUpSuccess");
        return mvc;
    }

    
}

