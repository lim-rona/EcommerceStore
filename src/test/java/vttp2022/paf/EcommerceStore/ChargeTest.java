package vttp2022.paf.EcommerceStore;

import java.util.HashMap;
import java.util.Map;

import com.stripe.exception.InvalidRequestException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import vttp2022.paf.EcommerceStore.model.ChargeRequest;
import vttp2022.paf.EcommerceStore.model.ChargeRequest.Currency;
import vttp2022.paf.EcommerceStore.services.StripeService;

@SpringBootTest
public class ChargeTest {
    
    @Autowired
    private StripeService chargeSvc;
    @Test
    void testCharge(){
        Map<String, Object> chargeParams = new HashMap<>();
        ChargeRequest chargeRequest = new ChargeRequest();
        chargeRequest.setAmount(100);
        chargeRequest.setCurrency(Currency.SGD);
        chargeRequest.setDescription("test");

        Assertions.assertThrows(InvalidRequestException.class, ()-> {
            chargeSvc.charge(chargeRequest);}
        );
        
       
        
    }
}
