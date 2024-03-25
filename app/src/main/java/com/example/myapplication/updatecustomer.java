package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class updatecustomer extends AppCompatActivity {

    private EditText custname;
    private EditText CustPhno;
    private EditText CustEmail;
    private EditText CustAddress;
    private MaterialButton btnsave;

    private String customerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatecustomer);
        getSupportActionBar().hide();
        custname = findViewById(R.id.custname);
        CustPhno = findViewById(R.id.CustPhno);
        CustEmail = findViewById(R.id.CustEmail);
        CustAddress = findViewById(R.id.CustAddress);
        btnsave = findViewById(R.id.btnsave);

        // Retrieve the product details from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            customerID = extras.getString("customer_id");
            String existingCustomerName = extras.getString("customer_name");
            String existingPhoneNo = extras.getString("phone_number");
            String existingEmail = extras.getString("email");
            String existingAddress = extras.getString("address");

            // Set the existing product details in the EditText fields for editing
            custname.setText(existingCustomerName);
            CustPhno.setText(existingPhoneNo);
            CustEmail.setText(existingEmail);
            CustAddress.setText(existingAddress);
        }

        btnsave.setOnClickListener(v -> updateButtonClicked());
    }
    private void updateButtonClicked() {
        String Customer_Name = custname.getText().toString();
        String Phone_Number = CustPhno.getText().toString();
        String Email = CustEmail.getText().toString();
        String  Address = CustAddress.getText().toString();

        if (Customer_Name.isEmpty() || Phone_Number.isEmpty() || Email.isEmpty() ||  Address.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isUpdateCustomerSuccessful = updateCustomer(Customer_Name, Phone_Number, Email,  Address, customerID);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isUpdateCustomerSuccessful) {
                            Intent i = new Intent(getApplicationContext(), customer_list.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(updatecustomer.this, "Error while updating data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }


    private boolean updateCustomer(String customer_name, String Phone_Number, String Email, String Address, String customerID) {
        try {
            URL url = new URL("http://172.30.0.1:8084/trial/Customer");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ID", customerID);
            jsonObject.put("Name",customer_name);
            jsonObject.put("Phone_Number", Phone_Number);
            jsonObject.put("Email", Email);
            jsonObject.put("Address", Address);

            String jsoninputstring = jsonObject.toString();

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsoninputstring.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Check if the request was successful
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    String jsonResponseString = reader.readLine();
                    JSONObject responseJson = new JSONObject(jsonResponseString);
                    return responseJson.getBoolean("status");
                }
            } else {
                // Handle error cases here
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}


