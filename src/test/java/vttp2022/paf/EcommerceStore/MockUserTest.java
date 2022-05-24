package vttp2022.paf.EcommerceStore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import vttp2022.paf.EcommerceStore.model.User;
import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;
import vttp2022.paf.EcommerceStore.repositories.UsersRepository;
import vttp2022.paf.EcommerceStore.services.UsersService;

@SpringBootTest
public class MockUserTest {
    
    @MockBean
    private UsersRepository usersRepo;

    @MockBean
    private ProductsRepository productsRepo;

    @Autowired
    private UsersService usersSvc;
    @Test
    void testInsertNewUser(){
        String username="bigtester";
        User user = new User();
        user.setName("bigtester");
        user.setUsername(username);
        user.setEmail("bigtester.com");
        user.setPassword("bigtester");
        int user_id=999;
        
        Mockito.when(usersRepo.insertNewUser(user)).thenReturn(true);
        Mockito.when(usersRepo.getUserIdByUsername(username)).thenReturn(user_id);
        Mockito.when(productsRepo.createCartForNewUserId(user_id)).thenReturn(true);

      
        assertEquals(true, usersSvc.insertNewUser(user));
    }
}
