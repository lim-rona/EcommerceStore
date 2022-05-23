package vttp2022.paf.EcommerceStore.services;

import java.lang.reflect.Executable;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2022.paf.EcommerceStore.model.User;
import vttp2022.paf.EcommerceStore.repositories.ProductsRepository;
import vttp2022.paf.EcommerceStore.repositories.UsersRepository;

@Service
public class UsersService {
    
    @Autowired
    private UsersRepository usersRepo;

    @Autowired
    private ProductsRepository productsRepo;

    public boolean authenticate(String username, String password) {
        return 1 == usersRepo.countUsersByNameAndPassword(username, password);
    }

    public boolean authenticate(String username) {
        return 1 == usersRepo.countUsersByName(username);
    }

    public boolean authenticateByEmail(String email) {
        return 1 == usersRepo.countUsersByEmail(email);
    }


    public boolean insertNewUser(User user) {
        //When inserting user, need to add a cart_id, user_id to Cart.
        String username = user.getUsername();
        Boolean insert = usersRepo.insertNewUser(user);
        int user_id = usersRepo.getUserIdByUsername(username);
        Boolean createCart = productsRepo.createCartForNewUserId(user_id);
        return insert&&createCart;
        
        
    }

    public String getUserNameByEmail(String email){
        Optional<String> username = usersRepo.getUserNameByEmail(email);
        if (username.isPresent()){
            return username.get();
        } else{
            //I don't know I anyhow put this error
            throw new IllegalAccessError("Username don't exists for the matched email");
        }
        
    }

    
    

}
