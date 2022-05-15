package vttp2022.paf.EcommerceStore.repositories;

public interface Queries {
    
    //For Users
    public static final String SQL_SELECT_AND_COUNT_USERS_BY_NAME_AND_PASSWORD =
        "select count(*) as user_count from user where username = ? and password = sha1(?)";
    
    public static final String SQL_SELECT_AND_COUNT_USERS_BY_NAME=
        "select count(*) as user_count from user where username = ?";
    
    public static final String SQL_SELECT_AND_COUNT_USERS_BY_EMAIL = 
        "select count(*) as user_count from user where email = ?";

    public static final String SQL_INSERT_USER=
        "insert into user(name, email, username, password) values(?,?,?,sha1(?))";

    
    //For Products
    public static final String SQL_RETRIEVE_CATEGORY=
        "select * from Category";
    
    public static final String SQL_RETRIEVE_ALL_PRODUCTS=
        "select * from Products";
}
