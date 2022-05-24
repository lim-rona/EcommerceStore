package vttp2022.paf.EcommerceStore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import vttp2022.paf.EcommerceStore.controllers.EcommerceController;
import vttp2022.paf.EcommerceStore.repositories.OrderHistoryRepository;
import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;
import vttp2022.paf.EcommerceStore.repositories.UsersRepository;
import vttp2022.paf.EcommerceStore.services.OrderHistoryService;
import vttp2022.paf.EcommerceStore.services.ProductsService;
import vttp2022.paf.EcommerceStore.services.UsersService;

@SpringBootTest
public class ControllerTest {

    // @Autowired
    // private MockMvc mockMvc;

    @InjectMocks
    private EcommerceController ecommerceController;

    @MockBean
    private UsersRepository usersRepo;

    @Autowired
    private UsersService usersSvc;

    @MockBean
    private ProductsRepository productsRepo;

    @MockBean
    private ProductsService productsSvc;

    @MockBean
    private OrderHistoryService orderhistSvc;

    @MockBean
    private OrderHistoryRepository orderhistRepo;

    // @Test
    // void testHomePage(){
    //     MockHttpSession sess = new MockHttpSession();
    //     ModelAndView mvc1 = ecommerceController.homePage(sess);
        
    //     assertEquals("index", mvc1.getViewName());
    // }
    
}
