package vttp2022.paf.EcommerceStore.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.protocol.Resultset;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public class Category {
    private int category_id;
    private String categoryName;
    private String description;
    private byte[] image;
    private boolean active;

    //Getters and setters
    public int getCategory_id() {
        return category_id;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
    
    // Method to populate Category object from sqlrowset
    public static Category populate(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setActive(rs.getBoolean("active"));
        category.setCategoryName(rs.getString("CategoryName"));
        category.setCategory_id(rs.getInt("Category_Id"));
        category.setDescription(rs.getString("Description"));
        category.setImage(rs.getBytes("Picture"));

        return category;

        //got problem with resultset.
    }

    // public static Category populate(SqlRowSet rs) {
    //     Category category = new Category();
    //     category.setActive(rs.getBoolean("active"));
    //     category.setCategoryName(rs.getString("CategoryName"));
    //     category.setCategory_id(rs.getInt("Category_Id"));
    //     category.setDescription(rs.getString("Description"));
    //     // category.setImage(rs.getByte("Picture"));

    //     return category;

    //     //got problem with resultset.
    // }
}

