package vttp2022.paf.EcommerceStore.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vttp2022.paf.EcommerceStore.model.Category;
import vttp2022.paf.EcommerceStore.model.Product;
import vttp2022.paf.EcommerceStore.model.User;
import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;
import vttp2022.paf.EcommerceStore.services.ProductsService;

@Controller
public class ProductsController {
    
    @Autowired
    private ProductsRepository productsRepo;

    @Autowired
    private ProductsService productsSvc;
    
    @GetMapping(path="/allProducts")
    public ModelAndView productsPage(HttpSession sess){
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("allProducts");


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

        //1. Retrieve Category List
        List<Category> categoryList = new ArrayList<Category>();
        categoryList = productsRepo.retrieveCategoryList();
        mvc.addObject("categoryList", categoryList);
        //2. Retrieve List of all items 
        List<Product> allProductsList = new ArrayList<Product>();
        allProductsList = productsRepo.retrieveAllProductsList();
        mvc.addObject("allProductsList", allProductsList);

        return mvc;
    }

    @GetMapping(path="/search/{productName}")
    public ModelAndView searchResultsPage(@RequestParam String productName,
    HttpSession sess){
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("searchResults");

        //Headers 
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

        //Querying and returning a list of items that match
        List<Product> returnedSearchList = new ArrayList<Product>();
        returnedSearchList = productsRepo.searchByProductName(productName);
        if(returnedSearchList.isEmpty()){
            mvc.setViewName("error");
        } else{
            mvc.addObject("returnedSearchList", returnedSearchList);
            mvc.setViewName("allProducts");

        }

        //need to get category list:
        List<Category> categoryList = new ArrayList<Category>();
        categoryList = productsRepo.retrieveCategoryList();
        mvc.addObject("categoryList", categoryList);

        //maybe if list.isempty(); then we return custom error/error ba
        

        return mvc;
    }

    @GetMapping(path="/product/{productName}")
    public ModelAndView individualProductPage(@PathVariable String productName, HttpSession sess){
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("individualProduct");
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

        Product product = new Product();
        product = productsRepo.returnIndividualProductByName(productName);
        int unitsInStock = product.getUnitsInStock();
        mvc.addObject("unitsInStock", unitsInStock);
        mvc.addObject("product", product);
        return mvc;
    }

    @GetMapping(path="/addToCart")
    public ModelAndView addIndividualItemToCart(@RequestParam String productName, @RequestParam int quantity,
    @RequestParam double price, HttpSession sess){
        ModelAndView mvc = new ModelAndView();
        List<Product> cartList = new ArrayList<>();
        List<Product> userCartList = new ArrayList<>();

        Product cartProduct = new Product();
        cartProduct = productsRepo.returnIndividualProductByName(productName);
        // cartProduct.setProductName(productName);
        cartProduct.setQuantityPurchased(quantity);
        cartProduct.setPrice(price);
        String username;


        //Check if anon or user
        User user = (User)sess.getAttribute("user");
        if(user!=null){
            //If it's a user, get cart from database and add this item into the cart
            username = user.getUsername();
            //Here we add the current item into the database cart
            productsSvc.addItemIntoUserCart(productName,quantity,price,username);
            //Then we retrieve it and add the list to mvc
            cartList = productsSvc.getCartWithUsername(username);
            
            mvc.addObject("cartList", cartList);
            
        } else{ 
            //If it's anon, then just update the normal cartList
            //Checking if cartList exists and updating
            if(sess.getAttribute("cartList")!=null){
                cartList = (List<Product>)sess.getAttribute("cartList");
            }

            boolean existsInCart = false;
            for(Product product:cartList) {
                if(product.getProduct_id() == cartProduct.getProduct_id()) {
                    product.setQuantityPurchased(product.getQuantityPurchased() + cartProduct.getQuantityPurchased());
                    existsInCart = true;
                }
            }

            if(!existsInCart) {
                cartList.add(cartProduct);
            }

            sess.setAttribute("cartList", cartList);
        }
        
        mvc = new ModelAndView("redirect:/cart");

        return mvc;
    }


    @PostMapping(path="/deleteFromCart")
    public ModelAndView deleteFromCart(@RequestBody MultiValueMap<String, String> payload, 
    HttpSession sess){
        ModelAndView mvc = new ModelAndView();
        List<Product> cartList = new ArrayList<>();
        String username;
        Product product = new Product();
        payload.get("productStat");
        int productStat = Integer.valueOf(payload.getFirst("productStat"));
        
        System.out.println("productStat is "+productStat);

        //You can't click on the delete button unless you already have something
        //in cart, so don't need to check if cartList is null
        User user = (User)sess.getAttribute("user");
        if(user!=null){
            //if user logged in, just delete from database
            username = user.getUsername();
            cartList = productsSvc.getCartWithUsername(username);
            product = cartList.get(productStat);
            String productName = product.getProductName();
            int quantityPurchased = product.getQuantityPurchased();
            productsSvc.deleteItemFromUserCart(username,productName,quantityPurchased);
            //and update new cartList to session
            cartList = productsSvc.getCartWithUsername(username);
            sess.setAttribute("cartList", cartList);

        }else{
            cartList = (List<Product>)sess.getAttribute("cartList");
            cartList.remove(productStat);
            sess.setAttribute("cartList", cartList);

        };

        mvc = new ModelAndView("redirect:/cart");

        return mvc;

    }

    @GetMapping(value="/{categoryName}")
    public ModelAndView getCategoryItems(@PathVariable String categoryName) {
        ModelAndView mvc = new ModelAndView();
        List<Product> productList = new ArrayList<>();

        productList = productsSvc.retrieveProductsWithCategoryName(categoryName);
        mvc.addObject("productList",productList);
        mvc.addObject("categoryName", categoryName);

        //add in list of category
        List<Category> categoryList = new ArrayList<Category>();
        categoryList = productsRepo.retrieveCategoryList();
        mvc.addObject("categoryList", categoryList);

        mvc.setViewName("allProducts");
        
        return mvc;
    }
    

    
}
