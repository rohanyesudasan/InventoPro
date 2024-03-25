//package com.example.myapplication;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.json.JSONObject;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import android.view.View;
//
//import com.google.android.material.button.MaterialButton;
//
//import java.io.DataOutputStream;
//import java.io.InputStream;
//
//public class update_product extends AppCompatActivity {
//
//        private EditText productname;
//        private EditText productdesc;
//        private EditText sellprice;
//        private EditText quantity;
//
//        @SuppressLint("MissingInflatedId")
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_update_product);
//            productname = findViewById(R.id.productname);
//            productdesc = findViewById(R.id.productdesc);
//            sellprice = findViewById(R.id.sellprice);
//            quantity =findViewById(R.id.quantity);
//
//            findViewById(R.id.btnupdate).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    sendProductDetails();
//                }
//            });
//        }
//
//        private void sendProductDetails() {
//            Intent i = new Intent();
//            String productId = i.getStringExtra("productID");
//            String productName = productname.getText().toString();
//            String productDesc = productdesc.getText().toString();
//            String productSellingPrice = sellprice.getText().toString();
//            String productQuantity = quantity.getText().toString();
//
//            try {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("ID", productId);
//                jsonObject.put("Product_Name", productName);
//                jsonObject.put("Product_Description", productDesc);
//                jsonObject.put("Selling_Price", productSellingPrice);
//                jsonObject.put("Quantity", productQuantity);
//                String jsonInputString = jsonObject.toString();
//
//                new SendProductDetailsTask().execute(jsonInputString);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Error sending product details", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        private class SendProductDetailsTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//                try {
//                    URL url = new URL("http://172.30.0.1:8084/trial/product");
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("PUT");
//                    connection.setDoInput(true);
//                    connection.setDoOutput(true);
//                    connection.setRequestProperty("Content-Type", "application/json");
//
//                    DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
//                    dataOutputStream.writeBytes(params[0]);
//                    dataOutputStream.flush();
//                    dataOutputStream.close();
//
//                    int responseCode = connection.getResponseCode();
//
//                    if (responseCode == HttpURLConnection.HTTP_OK) {
//                        InputStream inputStream = connection.getInputStream();
//                        // Read and handle the response as needed
//                        String response = convertStreamToString(inputStream);
//                        return "Product details sent successfully";
//                    } else {
//                        InputStream errorStream = connection.getErrorStream();
//                        // Handle the error response data as needed
//                        return "Error: " + responseCode;
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return "Error: " + e.getMessage();
//                }
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                Toast.makeText(update_product.this, result, Toast.LENGTH_SHORT).show();
//            }
//            private String convertStreamToString(InputStream stream) {
//                java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
//                return s.hasNext() ? s.next() : "";
//            }
//        }
//    }





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

public class update_product extends AppCompatActivity {

    private EditText productname;
    private EditText productdesc;
    private EditText sellprice;
    private EditText quantity;
    private MaterialButton update;

    // You might want to add an extra field to store the product ID
    private String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        getSupportActionBar().hide();

        productname = findViewById(R.id.productname);
        productdesc = findViewById(R.id.productdesc);
        sellprice = findViewById(R.id.productsp);
        quantity = findViewById(R.id.productquantity);
        update = findViewById(R.id.btnupdate);

        // Retrieve the product details from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productID = extras.getString("product_id");
            String existingProductName = extras.getString("product_name");
            String existingProductDesc = extras.getString("product_description");
            String existingSellPrice = extras.getString("selling_price");
            String existingQuantity = extras.getString("quantity");

            // Set the existing product details in the EditText fields for editing
            productname.setText(existingProductName);
            productdesc.setText(existingProductDesc);
            sellprice.setText(existingSellPrice);
            quantity.setText(existingQuantity);
        }

        update.setOnClickListener(v -> updateButtonClicked());
    }

    private void updateButtonClicked() {
        String Product_Name = productname.getText().toString();
        String Product_Description = productdesc.getText().toString();
        String Selling_Price = sellprice.getText().toString();
        String Quantity = quantity.getText().toString();

        if (Product_Name.isEmpty() || Product_Description.isEmpty() || Selling_Price.isEmpty() || Quantity.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isUpdateProductSuccessful = updateProduct(Product_Name, Product_Description, Selling_Price, Quantity, productID);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isUpdateProductSuccessful) {
                            Intent i = new Intent(getApplicationContext(), product_list.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(update_product.this, "Error while updating data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    private boolean updateProduct(String Product_Name, String Product_Description, String Selling_Price, String Quantity, String productID) {
        try {
            URL url = new URL("http://172.30.0.1:8084/trial/product");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ID", productID);
            jsonObject.put("Product_Name", Product_Name);
            jsonObject.put("Product_Description", Product_Description);
            jsonObject.put("Selling_Price", Selling_Price);
            jsonObject.put("Quantity", Quantity);

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
