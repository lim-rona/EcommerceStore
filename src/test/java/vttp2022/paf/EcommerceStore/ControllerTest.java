package vttp2022.paf.EcommerceStore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import vttp2022.paf.EcommerceStore.controllers.EcommerceController;
import vttp2022.paf.EcommerceStore.model.User;
import vttp2022.paf.EcommerceStore.repositories.OrderHistoryRepository;
import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;
import vttp2022.paf.EcommerceStore.repositories.UsersRepository;
import vttp2022.paf.EcommerceStore.services.OrderHistoryService;
import vttp2022.paf.EcommerceStore.services.ProductsService;
import vttp2022.paf.EcommerceStore.services.UsersService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private EcommerceController ecommerceController;

    @MockBean
    private UsersRepository usersRepo;

    @MockBean
    private UsersService usersSvc;

    @MockBean
    private ProductsRepository productsRepo;

    @MockBean
    private ProductsService productsSvc;

    @MockBean
    private OrderHistoryService orderhistSvc;

    @MockBean
    private OrderHistoryRepository orderhistRepo;

    @Test
    void testHomePage(){
        MockHttpSession sess = new MockHttpSession();
        String username="username";

        User user = new User();
        user.setUsername(username);
        sess.setAttribute("user",user);
        

        // Mockito.when(usersRepo.countUsersByName(username)).thenReturn(1);
        // Mockito.when(usersSvc.authenticate(username)).thenReturn(true);
        ModelAndView mvc1 = ecommerceController.homePage(sess);
        doReturn(true).when(usersSvc).authenticate(username);

        
        assertEquals("index", mvc1.getViewName());
    }

    
    
}
