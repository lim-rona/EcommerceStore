package vttp2022.paf.EcommerceStore.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import vttp2022.paf.EcommerceStore.model.ChargeRequest;
import vttp2022.paf.EcommerceStore.model.Product;
import vttp2022.paf.EcommerceStore.model.User;
import vttp2022.paf.EcommerceStore.model.ChargeRequest.Currency;
import vttp2022.paf.EcommerceStore.services.ProductsService;
import vttp2022.paf.EcommerceStore.services.StripeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PaymentsController {
    
    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    @Autowired
    StripeService stripeSvc;

    @Autowired
    private ProductsService productsSvc;
    
    String APIKEY = "sk_test_51L15EqI2mMYonepPF6nbVKhn5psN8HRX4iFh7cT22F7RtMvPKgGsyDx0xjTXnzutd6xkYNQVCh6cQqg5EY7z8sV800C7lbsTGg";

	@RequestMapping("/checkout")
    public ModelAndView checkout(HttpSession sess) {
        ModelAndView mvc = new ModelAndView();
        String username;
        List<Product> cartList = new ArrayList<>();
        double amount=0;
        int quantityPurchased;


        //Check if anon or user
        User user = (User)sess.getAttribute("user");
        if(user!=null){
            //If user,
            username = user.getUsername();
            cartList = productsSvc.getCartWithUsername(username);
            for(Product product:cartList){
                quantityPurchased=product.getQuantityPurchased();
                amount += product.getPrice()*quantityPurchased*100;
            }
            mvc.addObject("cartList", cartList);
            
        } else{ 
            //If it's anon,
            //Checking if cartList exists and updating
            cartList = (List<Product>)sess.getAttribute("cartList");
            for(Product product:cartList){
                quantityPurchased=product.getQuantityPurchased();
                amount += product.getPrice()*quantityPurchased*100;
            }
            mvc.addObject("cartList", cartList);
        }

        mvc.addObject("amount",amount); //in cents
        mvc.addObject("stripePublicKey", stripePublicKey);
        mvc.addObject("currency","SGD");
        
        mvc.setViewName("/checkout");
        return mvc;
    }
	
    @PostMapping("/charge")
    public String charge(ChargeRequest chargeRequest, Model model, HttpSession sess) throws StripeException {
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(Currency.SGD);
        Charge charge = stripeSvc.charge(chargeRequest);
        //If it went past here means transaction happened, delete everything from cart
        //(for user and anon)
        String username;
        List<Product> cartList = new ArrayList<>();
        User user = (User)sess.getAttribute("user");
        if(user!=null){
            //if user logged in, delete from database
            username = user.getUsername();
            cartList = productsSvc.getCartWithUsername(username);
            for(Product product:cartList){
                String productName = product.getProductName();
                int quantityPurchased = product.getQuantityPurchased();
                productsSvc.deleteItemFromUserCart(username,productName,quantityPurchased);
            }
            //and update new cartList to session
            cartList = productsSvc.getCartWithUsername(username);
            sess.setAttribute("cartList", cartList);

        }else{
            cartList = (List<Product>)sess.getAttribute("cartList");
            sess.removeAttribute("cartList");

        };


        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        return "result";
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "result";
    }

}

    


