package vttp2022.paf.EcommerceStore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

import vttp2022.paf.EcommerceStore.model.Product;
import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;
import vttp2022.paf.EcommerceStore.services.ProductsService;

@SpringBootTest
public class ProductTest {
    
    @MockBean
    private ProductsRepository productsRepo;

    @Autowired 
    private ProductsService productsSvc;

    @Autowired
    private JdbcTemplate template;

    // @MockBean
    // private ProductsService mockProductsSvc;


    
    // @Test
    // void ableToAddItemIntoUserCart(){
    //     Boolean verify = productsSvc.addItemIntoUserCart("cute4", 1, 4.0, "hoho");

    //     //Delete the cartItem
    //     productsSvc.deleteItemFromUserCart("hoho", "cute4", 1);

    //     assertTrue(verify);
    // }

    @Test
    void testGetCartWithUsername(){
        List<Product> CartList = new ArrayList<>();
        //Making a product for userCartList
        Product product = new Product();
        product.setProduct_id(999);
        product.setPrice(9.99);
        CartList.add(product);

     

        
        String username="test";
        int user_id=999;
        int cart_id=999;

        Mockito.when(productsRepo.getUserIdWithUsername(username)).thenReturn(user_id);
        Mockito.when(productsRepo.getCartIdWithUserId(user_id)).thenReturn(cart_id);
        Mockito.when(productsRepo.getCartItemswithCartId(cart_id)).thenReturn(CartList);

        assertEquals(CartList, productsSvc.getCartWithUsername(username));

    }

    @Test
    void testAddItemIntoUserCart(){
        List<Product> cartList = new ArrayList<>();
        Product product = new Product();
        product.setProduct_id(999);
        product.setPrice(9.99);
        double price=9.99;

        String username="test";
        int user_id=999;
        int cart_id=999;
        
        String productName="test";

        // Mockito.when(mockProductsSvc.getCartWithUsername(username)).thenReturn(cartList);
        Mockito.when(productsRepo.getUserIdWithUsername(username)).thenReturn(user_id);
        Mockito.when(productsRepo.getCartIdWithUserId(user_id)).thenReturn(cart_id);

        Mockito.when(productsRepo.returnIndividualProductByName(productName)).thenReturn(product);

        //Creating mock products for the cartList
        Product product1 = new Product();
        product1.setProductName("test");
        product1.setPrice(2.00);
        product1.setQuantityPurchased(1);
        product1.setProduct_id(999);
        cartList.add(product1);
        int qtyPurchased = 1;
        int quantityPurchased=1;
        int product_id=999;

        Mockito.when(productsRepo.updateQuantityPurchased(product_id, cart_id, qtyPurchased)).thenReturn(true);
        Mockito.when(productsRepo.addItemIntoUserCart(productName,cart_id, product_id, price, quantityPurchased)).thenReturn(true);
        
        
        assertEquals(true, productsSvc.addItemIntoUserCart(productName, quantityPurchased, price, username));
        
    }

    @Test
    void testRetrieveProductsWithCategoryName(){
        List<Product> productList = new ArrayList<>();
        String categoryName="VTTP";
        int category_id=1;

        Mockito.when(productsRepo.getCategoryIdWithCategoryName(categoryName)).thenReturn(category_id);
        Mockito.when(productsRepo.getProductsWithCategoryId(category_id)).thenReturn(productList);

        assertEquals(productList, productsSvc.retrieveProductsWithCategoryName(categoryName));
        

    }
}
