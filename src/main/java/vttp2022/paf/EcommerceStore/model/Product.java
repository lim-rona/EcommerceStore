package vttp2022.paf.EcommerceStore.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Base64;
import org.apache.commons.lang3.ArrayUtils;

public class Product {
    private int product_id;
    private String productName;
    private String brand;
    private double price;
    private int unitsInStock;
    private int quantityPurchased;
    private byte[] image;
    private int category_id;
    private String imageBase64;

    //Getters and setters
    public int getProduct_id() {
        return product_id;
    }
    public int getQuantityPurchased() {
        return quantityPurchased;
    }
    public void setQuantityPurchased(int quantityPurchased) {
        this.quantityPurchased = quantityPurchased;
    }
    public String getImageBase64() {
        return imageBase64;
    }
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    public int getCategory_id() {
        return category_id;
    }
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
    public int getUnitsInStock() {
        return unitsInStock;
    }
    public void setUnitsInStock(int unitsInStock) {
        this.unitsInStock = unitsInStock;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
    
    // Method to populate Product object from sqlrowset
    public static Product populate(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setBrand(rs.getString("Brand"));
        product.setCategory_id(rs.getInt("Category_Id"));
        product.setImage(rs.getBytes("Picture"));
        product.setPrice(rs.getDouble("Price"));
        product.setProductName(rs.getString("ProductName"));
        product.setProduct_id(rs.getInt("Product_id"));
        product.setUnitsInStock(rs.getInt("UnitsInStock"));

        if(ArrayUtils.isNotEmpty(product.getImage())) {
            String imageBase64 = Base64.getEncoder().encodeToString(product.getImage());
            product.setImageBase64(imageBase64);
        }

        return product;

    }
}
