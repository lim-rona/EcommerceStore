package vttp2022.paf.EcommerceStore.repositories;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.protocol.Resultset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.paf.EcommerceStore.model.Category;
import vttp2022.paf.EcommerceStore.model.Product;

import static vttp2022.paf.EcommerceStore.repositories.Queries.*;


@Repository
public class ProductsRepository {
    
    @Autowired
    private JdbcTemplate template;

    public List<Category> retrieveCategoryList() {
        
        List<Category> list = new ArrayList<Category>();

        return template.query(SQL_RETRIEVE_CATEGORY,
            (ResultSet rs) -> {
                
                while(rs.next()){
                final Category category = Category.populate(rs);
                System.out.println(category.getCategoryName());
                list.add(category);
                }
                
                return list;
            });
    
    }

    public int getCategoryIdWithCategoryName(String categoryName){
        SqlRowSet rs = template.queryForRowSet(SQL_GET_CATEGORYID_WITH_CATEGORYNAME, categoryName);
        if (!rs.next())
            return -1;
        return rs.getInt("Category_id");
    }

    public List<Product> getProductsWithCategoryId(int category_id){
        List<Product> list = new ArrayList<Product>();



        return template.query(SQL_GET_PRODUCTS_WITH_CATEGORYID,
            (ResultSet rs) -> {
                
                while(rs.next()){
                final Product product = Product.populate(rs);
                System.out.println(product.getProductName());
                list.add(product);
                }
                
                return list;
            }, category_id);
    }

    public List<Product> retrieveAllProductsList() {
        
        List<Product> list = new ArrayList<Product>();

        return template.query(SQL_RETRIEVE_ALL_PRODUCTS,
            (ResultSet rs) -> {
                
                while(rs.next()){
                final Product product = Product.populate(rs);
                System.out.println(product.getProductName());
                list.add(product);
                }
                
                return list;
            });

    }

    public List<Product> searchByProductName(String productName){
        List<Product> list = new ArrayList<Product>();

        return template.query(SQL_SEARCH_BY_PRODUCT_NAME,
            (ResultSet rs) -> {
                
                while(rs.next()){
                final Product product = Product.populate(rs);
                System.out.println(product.getProductName());
                list.add(product);
                }
                
                return list;
            }, "%" + productName + "%");
    }

    public Product returnIndividualProductByName(String productName){
        
        return template.query(SQL_SEARCH_BY_PRODUCT_NAME,
            (ResultSet rs) -> {
                Product product = new Product();
                while(rs.next()){
                product = Product.populate(rs);
                System.out.println(product.getProductName());
                }
                
                return product;
            }, productName);
    }

    public int getUserIdWithUsername(String username){
        SqlRowSet rs = template.queryForRowSet(SQL_GET_USERID_WITH_USERNAME, username);
        if (!rs.next())
            return 0;
        return rs.getInt("user_id");
    }

    public int getCartIdWithUserId(int user_id){
        SqlRowSet rs = template.queryForRowSet(SQL_GET_CARTID_WITH_USERID, user_id);
        if (!rs.next())
            return 0;
        return rs.getInt("cart_id");
    }

    public List<Product> getCartItemswithCartId(int cart_id){
        List<Product> userCartList = new ArrayList<>();
        

        //We just need product name and quantity purchased for cartItem, the rest can find later
        SqlRowSet rs = template.queryForRowSet(SQL_GET_CARTITEM_WITH_CARTID, cart_id);
        while(rs.next()){
            Product product = new Product();
            String productName = rs.getString("ProductName");
            // product.setProductName(rs.getString("ProductName"));
            // product.setQuantityPurchased(rs.getInt("QuantityPurchased"));
            // product.setPrice(rs.getDouble("Price"));
            product = returnIndividualProductByName(productName);
            product.setQuantityPurchased(rs.getInt("QuantityPurchased"));

            
            
            
            userCartList.add(product);
        }

        // //new way to do to get the image:
        // return template.query(SQL_GET_CARTITEM_WITH_CARTID,
        //     (ResultSet rs) -> {
        //         Product product = new Product();
        //         while(rs.next()){
        //         product = Product.populate(rs);
        //         userCartList.add(product);
        //         }
                
        //         return userCartList;
        //     }, cart_id);

        return userCartList;

    }



    public boolean addItemIntoUserCart(String productName, int cart_id, int product_id, 
    double price, int quantityPurchased){
        int count = template.update(SQL_ADD_ITEM_INTO_USER_CART, cart_id, product_id,
        productName, price, quantityPurchased);

        return 1==count;

    }

    public boolean deleteItemFromUserCart(int cart_id, String productName, int quantityPurchased){
        int count = template.update(SQL_DELETE_ITEM_FROM_USER_CART, cart_id, productName, quantityPurchased);

        return 1==count;
    }

    public boolean createCartForNewUserId(int user_id){
        int count = template.update(SQL_CREATE_CART_FOR_NEW_USERID,user_id);

        return 1==count;
    }
} 

