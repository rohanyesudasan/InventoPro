package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

public class SupplierModel {
    private String ID;
    private String Supplier_Name;
    private String Supplier_Phone_Number;
    private String Supplier_Email;
    private  String Supplier_Address;
    private  String Supplier_gstin;

    public SupplierModel(JSONObject jsonObject) {
        try {
            ID = jsonObject.getString("ID");
            Supplier_Name = jsonObject.getString("Name");
            Supplier_Phone_Number = jsonObject.getString("Phone_Number");
            Supplier_Email = jsonObject.getString("Email");
            Supplier_Address = jsonObject.getString("Address");
            Supplier_gstin = jsonObject.getString("Gstin_Number");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getID() { return ID; }
    public String getSupplier_Name() {
        return Supplier_Name;
    }

    public String getSupplier_Phone_Number() {
        return Supplier_Phone_Number;
    }

    public String getSupplier_Email() {
        return Supplier_Email;
    }

    public String getSupplier_Address() {
        return Supplier_Address;
    }

    public String getSupplier_gstin() {
        return Supplier_gstin;
    }

    // Add other attributes and methods as needed

    // Constructors, getters, setters
}
