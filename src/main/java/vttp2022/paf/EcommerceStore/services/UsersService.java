package vttp2022.paf.EcommerceStore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2022.paf.EcommerceStore.model.User;
import vttp2022.paf.EcommerceStore.repositories.UsersRepository;

@Service
public class UsersService {
    
    @Autowired
    private UsersRepository usersRepo;

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
        return usersRepo.insertNewUser(user);
    }
    

}
