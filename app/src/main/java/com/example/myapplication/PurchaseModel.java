package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseModel {
    private int ID;
    private String Invoice_No;
    private String Supplier_ID;
    private String Product_ID;
    private String Cost_Price;
    private String Amount;



    private  String Quantity;

    public PurchaseModel(JSONObject jsonObject) {
        try {
            Invoice_No = jsonObject.getString("Invoice_No");
            Supplier_ID = jsonObject.getString("Name");
            Product_ID = jsonObject.getString("Product_Name");
            Quantity = jsonObject.getString("Quantity");
            Cost_Price = jsonObject.getString("Cost_Price");
            Amount = jsonObject.getString("Amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public int getID() {
        return ID;
    }
    public String getInvoice_No() {
        return Invoice_No;
    }

    public String getSupplier_ID() {
        return Supplier_ID;
    }

    public String getProduct_ID() {
        return Product_ID;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getCost_Price() {
        return Cost_Price;
    }
    public String getAmount() {
        return Amount;
    }

    // Add other attributes and methods as needed

    // Constructors, getters, setters


}
