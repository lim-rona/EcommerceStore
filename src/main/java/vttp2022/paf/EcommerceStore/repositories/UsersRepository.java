package vttp2022.paf.EcommerceStore.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.paf.EcommerceStore.model.User;

import static vttp2022.paf.EcommerceStore.repositories.Queries.*;


@Repository
public class UsersRepository {

    @Autowired
    private JdbcTemplate template;

    public int countUsersByNameAndPassword(String username, String password) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_AND_COUNT_USERS_BY_NAME_AND_PASSWORD, username, password);
        if (!rs.next())
            return 0;
        return rs.getInt("user_count");
    }

    public int countUsersByName(String username) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_AND_COUNT_USERS_BY_NAME, username);
        if (!rs.next())
            return 0;
        return rs.getInt("user_count");
    }

    public boolean insertNewUser(User user) {
        int count = template.update(SQL_INSERT_USER, user.getName(),user.getEmail(),user.getUsername(),user.getPassword());
        return 1==count; 
    }

    
    public int countUsersByEmail(String email) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_AND_COUNT_USERS_BY_EMAIL, email);
        if (!rs.next())
            return 0;
        return rs.getInt("user_count");
    }


}
