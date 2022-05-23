package vttp2022.paf.EcommerceStore.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2022.paf.EcommerceStore.model.Product;
import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;

@Service
public class ProductsService {
    
    @Autowired
    private ProductsRepository productsRepo;

    public List<Product> getCartWithUsername(String username){
        List<Product> userCartList = new ArrayList<>();
        int user_id = productsRepo.getUserIdWithUsername(username);
        int cart_id = productsRepo.getCartIdWithUserId(user_id);
        userCartList = productsRepo.getCartItemswithCartId(cart_id);
        return userCartList;
    }
    
    public boolean addItemIntoUserCart(String productName, int quantityPurchased, double price,String username){
        List<Product> cartList = getCartWithUsername(username);
        int user_id = productsRepo.getUserIdWithUsername(username);
        int cart_id = productsRepo.getCartIdWithUserId(user_id);
        Product product = new Product();
        product = productsRepo.returnIndividualProductByName(productName);
        int product_id = product.getProduct_id();
        System.out.println("it went here");
        for(Product product1: cartList){
            System.out.println("tester" + product1.getProductName());
            String cartProductName = product1.getProductName();
            if(cartProductName.equals(productName)){
                System.out.println("Something should update");
                int qtyPurchased = product1.getQuantityPurchased()+quantityPurchased;
                return productsRepo.updateQuantityPurchased(product_id, cart_id, qtyPurchased); 
            }
        }

        System.out.println("It shouldn't go here");
        price = product.getPrice();

        return productsRepo.addItemIntoUserCart(productName,cart_id, product_id, price, quantityPurchased);
    }

    public boolean deleteItemFromUserCart(String username, String productName, int quantityPurchased){
        int user_id = productsRepo.getUserIdWithUsername(username);
        int cart_id = productsRepo.getCartIdWithUserId(user_id);
        
        return productsRepo.deleteItemFromUserCart(cart_id,productName,quantityPurchased);
    }

    public List<Product> retrieveProductsWithCategoryName(String categoryName){
        List<Product> productList = new ArrayList<>();
        int category_id = productsRepo.getCategoryIdWithCategoryName(categoryName);
        productList = productsRepo.getProductsWithCategoryId(category_id);

        return productList;
        
    }
}


