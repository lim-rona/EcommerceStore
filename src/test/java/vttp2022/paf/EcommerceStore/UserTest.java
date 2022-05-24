package vttp2022.paf.EcommerceStore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import vttp2022.paf.EcommerceStore.model.User;
import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;
import vttp2022.paf.EcommerceStore.repositories.UsersRepository;
import vttp2022.paf.EcommerceStore.services.ProductsService;
import vttp2022.paf.EcommerceStore.services.UsersService;

@SpringBootTest
public class UserTest {

    @Autowired
    private UsersService usersSvc;

    @Autowired 
    private UsersRepository usersRepo;

    @Autowired
    private ProductsRepository productsRepo;

    @Autowired 
    private ProductsService productsSvc;

    @Autowired
    private JdbcTemplate template;
    

    @Test
    void verifyHohoExists(){
        //Here we authenticated with username, hoho
        Boolean verification1 = usersSvc.authenticate("hoho");
        //Authenticate with username and password
        Boolean verification2 = usersSvc.authenticate("hoho","hoho");
        //Authenticate with email
        Boolean verification3 = usersSvc.authenticateByEmail("hoho@hoho.com");

        
        assertTrue(verification1&&verification2&&verification3);
    }
    
    @Test
    void verifyAbleToRetrieveHohoEmail(){
        String username = usersSvc.getUserNameByEmail("hoho@hoho.com");
        assertEquals("hoho", username);
    }

    @Test
    void cantInsertUserWithDuplicateUsername(){
        //Since hoho is authenticated, we shouldnt be able to insert another user with username hoho
        User user = new User();
        user.setName("hoho");
        user.setUsername("hoho");
        user.setEmail("hoho@gmail.com");
        user.setPassword("hoho");
        Assertions.assertThrows(DuplicateKeyException.class, ()-> {
            usersSvc.insertNewUser(user);});
    }

    @Test
    void cantGetUnknownUserName(){
        Optional<String> username = usersRepo.getUserNameByEmail("reallyrandomstuff323232@gmail.com");
        assertEquals(Optional.empty(),username);
    }

    // @Test
    // void ableToInsertUser(){
    //     User user = new User();
    //     user.setName("bigtester");
    //     user.setUsername("bigtester");
    //     user.setEmail("bigtester.com");
    //     user.setPassword("bigtester");
    //     Boolean verification = usersSvc.insertNewUser(user);

    //     int user_id = usersRepo.getUserIdByUsername("bigtester");
        
    //     //To remove the user so it can be tested again, 
    //     template.update("Delete from Cart where user_id =?",user_id);
    //     template.update("Delete from user where user_id=?", user_id);

    //     assertEquals(true, verification);
        
    // }



    
}
