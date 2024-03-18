package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

public class SalesModel {
    private int ID;
    private String Customer_ID;
    private String Product_ID;
    private String Amount;



    private  String Quantity;

    public SalesModel(JSONObject jsonObject) {
        try {

            Customer_ID = jsonObject.getString("Name");
            Product_ID = jsonObject.getString("Product_Name");
            Quantity = jsonObject.getString("Quantity");
            Amount = jsonObject.getString("Amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public int getID() {
        return ID;
    }

    public String getCustomer_ID() {
        return Customer_ID;
    }

    public String getProduct_ID() {
        return Product_ID;
    }

    public String getQuantity() {
        return Quantity;
    }


    public String getAmount() {
        return Amount;
    }

    // Add other attributes and methods as needed

    // Constructors, getters, setters


}