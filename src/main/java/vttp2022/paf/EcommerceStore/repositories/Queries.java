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

    public static final String SQL_GET_USERNAME_BY_EMAIL=
        "select username from user where email = ?";
    
    public static final String SQL_GET_USERID_BY_USERNAME=
        "select user_id from user where username = ?";
        
    public static final String SQL_CREATE_CART_FOR_NEW_USERID=
        "insert into Cart(user_id) values(?)";

    
    //For Products
    public static final String SQL_RETRIEVE_CATEGORY=
        "select * from Category";
    
    public static final String SQL_GET_CATEGORYID_WITH_CATEGORYNAME=
        "select Category_id from Category where CategoryName = ?";
    
    public static final String SQL_GET_PRODUCTS_WITH_CATEGORYID=
        "select * from Products where Category_id = ?";
        
    public static final String SQL_RETRIEVE_ALL_PRODUCTS=
        "select * from Products";

    public static final String SQL_SEARCH_BY_PRODUCT_NAME=
        "select * from Products where ProductName like ?";

    //For Cart
    public static final String SQL_GET_USERID_WITH_USERNAME=
        "select user_id from user where username like ?";
    public static final String SQL_GET_CARTID_WITH_USERID=
        "select cart_id from Cart where user_id like ?";
    public static final String SQL_GET_CARTITEM_WITH_CARTID=
        "select ProductName, QuantityPurchased, Price from CartItem where cart_id like ?";
    public static final String SQL_ADD_ITEM_INTO_USER_CART=
        "insert into CartItem(cart_id, Product_id, ProductName, Price, QuantityPurchased) values(?,?,?,?,?)";
    public static final String SQL_DELETE_ITEM_FROM_USER_CART=
        "delete from CartItem where cart_id=? and productName=? and quantityPurchased=?";
}
