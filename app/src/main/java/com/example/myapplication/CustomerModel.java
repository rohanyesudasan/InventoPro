package com.example.myapplication;


import org.json.JSONException;
import org.json.JSONObject;

public class CustomerModel {
    private String ID;
    private String Name;
    private String Phone_Number;
    private String Email;
    private  String Address;

    public CustomerModel(JSONObject jsonObject) {
        try {
            ID = jsonObject.getString("ID");

            Name = jsonObject.getString("Name");
            Phone_Number = jsonObject.getString("Phone_Number");
            Email = jsonObject.getString("Email");
            Address = jsonObject.getString("Address");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getID() { return ID; }
    public String getCustomer_Name() {
        return Name;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public String getEmail() {
        return Email;
    }

    public String getAddress() {
        return Address;
    }

    // Add other attributes and methods as needed

    // Constructors, getters, setters
}
