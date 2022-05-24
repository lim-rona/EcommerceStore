package vttp2022.paf.EcommerceStore;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;
import vttp2022.paf.EcommerceStore.services.ProductsService;

@SpringBootTest
public class ProductTest {
    
    @Autowired
    private ProductsRepository productsRepo;

    @Autowired 
    private ProductsService productsSvc;

    @Autowired
    private JdbcTemplate template;


    
    @Test
    void ableToAddItemIntoUserCart(){
        Boolean verify = productsSvc.addItemIntoUserCart("cute4", 1, 4.0, "hoho");

        //Delete the cartItem
        productsSvc.deleteItemFromUserCart("hoho", "cute4", 1);

        assertTrue(verify);
    }
}
