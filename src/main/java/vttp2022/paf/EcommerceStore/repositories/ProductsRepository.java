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

} 

