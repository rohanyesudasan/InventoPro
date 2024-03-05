package com.example.myapplication;


import org.json.JSONException;
import org.json.JSONObject;

public class ProductModel {
    private String ID;
    private String Product_Name;
    private String Product_Description;
    private String Selling_Price;
    private  String Quantity;

    public ProductModel(JSONObject jsonObject) {
        try {
            ID = jsonObject.getString("ID");
            Product_Name = jsonObject.getString("Product_Name");
            Product_Description = jsonObject.getString("Product_Description");
            Selling_Price = jsonObject.getString("Selling_Price");
            Quantity = jsonObject.getString("Quantity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getID() { return ID; }
    public String getProduct_Name() {
        return Product_Name;
    }
    public String getProduct_Description() {
        return Product_Description;
    }
    public String getSelling_Price() {
        return Selling_Price;
    }

    public String getQuantity() {
        return Quantity;
    }

    // Add other attributes and methods as needed

    // Constructors, getters, setters


}
