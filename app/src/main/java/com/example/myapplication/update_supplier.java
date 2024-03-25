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

public class update_supplier extends AppCompatActivity {

    private EditText supname;

    private EditText mobilecnt;

    private EditText email;

    private EditText address;

    private EditText gst;

    private MaterialButton update;

    private  String supplierID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_supplier);
        getSupportActionBar().hide();

        supname=findViewById(R.id.supname);
        mobilecnt=findViewById(R.id.mobilecnt);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        gst=findViewById(R.id.gst);
        update = findViewById(R.id.update);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            supplierID = extras.getString("supplier_id");
            String existingSupplierName = extras.getString("supplier_name");
            String existingmobilecnt = extras.getString("phone_number");
            String existingemail = extras.getString("email");
            String existingaddress = extras.getString("address");
            String existinggst = extras.getString("gstin");

            // Set the existing product details in the EditText fields for editing
            supname.setText(existingSupplierName);
            mobilecnt.setText(existingmobilecnt);
            email.setText(existingemail);
            address.setText(existingaddress);
            gst.setText(existinggst);


        }
        update.setOnClickListener(v -> updateButtonClicked());
    }
    private void updateButtonClicked() {
        String Supplier_Name = supname.getText().toString();
        String Supplier_Phone_Number = mobilecnt.getText().toString();
        String Supplier_Email = email.getText().toString();
        String Supplier_Address = address.getText().toString();
        String Supplier_gstin = gst.getText().toString();

        if (Supplier_Name.isEmpty() || Supplier_Phone_Number.isEmpty() || Supplier_Email.isEmpty() || Supplier_Address.isEmpty() || Supplier_gstin.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isUpdateSupplierSuccessful = updateSupplier(Supplier_Name,Supplier_Phone_Number, Supplier_Email, Supplier_Address,Supplier_gstin, supplierID);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isUpdateSupplierSuccessful) {
                            Intent i = new Intent(getApplicationContext(), Supplierlist.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(update_supplier.this, "Error while updating data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
    private boolean updateSupplier(String Supplier_Name, String Supplier_Phone_Number, String Supplier_Email, String Supplier_Address, String Supplier_Gstin_Number, String supplierID) {
        try {
            URL url = new URL("http://172.30.0.1:8084/trial/Supplier");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ID", supplierID);
            jsonObject.put("Name",Supplier_Name);
            jsonObject.put("Phone_Number", Supplier_Phone_Number);
            jsonObject.put("Email", Supplier_Email);
            jsonObject.put("Address", Supplier_Address);
            jsonObject.put("Gstin_Number", Supplier_Gstin_Number);


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