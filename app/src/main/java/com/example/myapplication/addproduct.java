package com.example.myapplication;

import android.app.Activity;
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


public class addproduct extends AppCompatActivity {

    private EditText productname;
    private EditText productdesc;
    private EditText sellprice;
    private EditText quantity;
    private MaterialButton reset;
    private  MaterialButton save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);
        getSupportActionBar().hide();


        productname = findViewById(R.id.productname);
        productdesc = findViewById(R.id.productdesc);
        sellprice = findViewById(R.id.sellprice);
        quantity =findViewById(R.id.quantity);
        save = findViewById(R.id.save);


        save.setOnClickListener(v -> saveButtonClicked());




    }
    private void saveButtonClicked() {
        String Product_Name = productname.getText().toString();
        String Product_Description = productdesc.getText().toString();
        String Selling_Price = sellprice.getText().toString();
        String Quantity = quantity.getText().toString();

        if (Product_Name.isEmpty() || Product_Description.isEmpty()  || Selling_Price.isEmpty() || Quantity.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isAddProductSuccessful = authenticate(Product_Name, Product_Description,Selling_Price, Quantity);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isAddProductSuccessful) {
                            Intent i = new Intent(getApplicationContext(), product_list.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(addproduct.this, "Error while inserting data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();

    }
    private boolean authenticate(String Product_Name, String Product_Description, String Selling_Price, String Quantity) {
        try {
            URL url = new URL("http://172.30.0.1:8084/trial/product");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);

            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Product_Name", Product_Name);
            jsonObject.put("Product_Description", Product_Description);
            jsonObject.put("Selling_Price", Selling_Price);
            jsonObject.put("Quantity", Quantity);
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