package vttp2022.paf.EcommerceStore;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLIntegrityConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import vttp2022.paf.EcommerceStore.model.User;
import vttp2022.paf.EcommerceStore.repositories.UsersRepository;
import vttp2022.paf.EcommerceStore.services.UsersService;

@SpringBootTest
public class UserTest {

    @Autowired
    private UsersService usersSvc;

    @Autowired 
    private UsersRepository usersRepo;
    

    @Test
    void verifyHohoExists(){
        //Here we authenticated with username, hoho
        Boolean verification = usersSvc.authenticate("hoho");
        assertTrue(verification);
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



    
}
