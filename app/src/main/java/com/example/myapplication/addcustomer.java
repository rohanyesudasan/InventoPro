package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class addcustomer extends navDrawerActivity {

    private EditText custname;
    private EditText mobilecnt;
    private EditText email;
    private EditText address;
    private MaterialButton reset;
    private  MaterialButton save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcustomer);
        getSupportActionBar().hide();

        custname = findViewById(R.id.custname);
        mobilecnt = findViewById(R.id.mobilecnt);
        email = findViewById(R.id.email);
        address =findViewById(R.id.address);
        save = findViewById(R.id.save);


        save.setOnClickListener(v -> saveButtonClicked());




    }
    private void saveButtonClicked() {
        String Name = custname.getText().toString();
        String Phone_Number = mobilecnt.getText().toString();
        String Email = email.getText().toString();
        String Address = address.getText().toString();

        if (Name.isEmpty() || Phone_Number.isEmpty()  || Email.isEmpty() || Address.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isAddCustomerSuccessful = authenticate(Name, Phone_Number,Email,Address);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isAddCustomerSuccessful) {
                            Intent i = new Intent(getApplicationContext(), customer_list.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(addcustomer.this, "Error while inserting data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();

    }
    private boolean authenticate(String Name, String Phone_Number, String Email, String Address) {
        try {
            URL url = new URL("http://172.30.0.1:8084/trial/Customer");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);

            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Name",Name);
            jsonObject.put("Phone_Number", Phone_Number);
            jsonObject.put("Email", Email);
            jsonObject.put("Address", Address);
            String jsoninputstring = jsonObject.toString();
            try(OutputStream os = connection.getOutputStream()) {
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
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

}