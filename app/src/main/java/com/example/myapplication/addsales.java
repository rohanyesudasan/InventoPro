package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class addsales extends AppCompatActivity {

    private AutoCompleteTextView productSpinner;
    private AutoCompleteTextView cusname;
    private EditText salprice;
    private EditText salquantity;

    private MaterialButton save;
    private MaterialButton reset;

    private List<String> customerNames = new ArrayList<>();
    private List<String> productNames = new ArrayList<>();

    private Map<String, String> customerNameToIdMap = new HashMap<>();
    private Map<String, String> productNameToIdMap = new HashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsales);

        getSupportActionBar().hide();
        productSpinner = findViewById(R.id.productSpinner);
        cusname = findViewById(R.id.cusname);
        salprice = findViewById(R.id.salprice);
        salquantity = findViewById(R.id.salquantity);



        save = findViewById(R.id.save);
        save.setOnClickListener(v -> saveButtonClicked());
        fetchCustomerData(); // Fetch and populate supplier data
        fetchProductData(); // Fetch and populate product data
    }
    //    private void populateSpinner(Spinner spinner, List<String> data) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//    }
    private void populateAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView, List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, data);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);

    }
    private void fetchCustomerData() {
        Log.d("SpinnerPopulate", "Fetching Supplier Data...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("http://172.30.0.1:8084/trial/Supplier");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json");

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        StringBuilder response = new StringBuilder();
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                        }
                        final String jsonResponse = response.toString();

                        runOnUiThread(() -> {
                            try {
                                JSONArray jsonArray = new JSONArray(jsonResponse);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject customerObject = jsonArray.getJSONObject(i);
                                    String customerId = customerObject.getString("ID");
                                    String customerName = customerObject.getString("Name");

                                    customerNameToIdMap.put(customerName, customerId);
                                    customerNames.add(customerName);
                                }
                                Log.d("CustomerPopulate", "Customer Names: " + customerNames);

                                runOnUiThread(() -> populateAutoCompleteTextView(cusname, customerNames));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        runOnUiThread(() -> {
                            try {
                                Log.e("fetchCustomerData", "HTTP Request failed with response code: " + connection.getResponseCode());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Toast.makeText(addsales.this, "Error fetching customer data", Toast.LENGTH_SHORT).show();

                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Log.e("fetchCustomerData", "Network error: " + e.getMessage());
                        Toast.makeText(addsales.this, "Network error", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }).start();
    }

    private void fetchProductData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://172.30.0.1:8084/trial/product");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json");

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        StringBuilder response = new StringBuilder();
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                        }
                        final String jsonResponse = response.toString();

                        runOnUiThread(() -> {
                            try {
                                JSONArray jsonArray = new JSONArray(jsonResponse);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject productObject = jsonArray.getJSONObject(i);
                                    String productId = productObject.getString("ID");
                                    String productName = productObject.getString("Product_Name");

                                    productNameToIdMap.put(productName, productId);
                                    productNames.add(productName);
                                }
                                runOnUiThread(() -> populateAutoCompleteTextView(productSpinner, productNames));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(addsales.this, "Error fetching product data", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(addsales.this, "Network error", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }).start();
    }

    private void saveButtonClicked() {

        String Customer_Name = cusname.getText().toString();
        String Product_Name = productSpinner.getText().toString();

        String Amount = salprice.getText().toString();
        String Quantity = salquantity.getText().toString();

        if ( Customer_Name.isEmpty() || Product_Name.isEmpty()|| Amount.isEmpty()  || Quantity.isEmpty() ) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        String Customer_ID = customerNameToIdMap.get(Customer_Name);
        String Product_ID = productNameToIdMap.get(Product_Name);

        if (Customer_ID == null) {
            showToastMessage("Invalid Customer Name");
            return;
        }

        if (Product_ID == null) {
            showToastMessage("Invalid Product Name");
            return;
        }


        // Get the IDs for selected Supplier and Product from the maps
        Log.d("addSales", "Attempting to post data");


        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isAddSalesSuccessful = authenticate(Product_ID, Customer_ID, Amount, Quantity);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isAddSalesSuccessful) {
                            Intent i = new Intent(getApplicationContext(), purchase_list.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(addsales.this, "Error while inserting data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
    private void showToastMessage(String message) {
        runOnUiThread(() -> {
            Toast.makeText(addsales.this, message, Toast.LENGTH_SHORT).show();
        });
    }
    private boolean authenticate(String Product_ID, String Customer_ID, String Amount,String Quantity) {
        try {
            URL url = new URL("http://172.30.0.1:8084/trial/sales");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Product_ID", Product_ID);
            jsonObject.put("Customer_ID", Customer_ID);

            jsonObject.put("Quantity", Quantity);
            jsonObject.put("Amount", Amount);

            String jsonInputString = jsonObject.toString();

            Log.d("JSON Data", jsonInputString); // Log JSON data for debugging

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
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
                // Log the response code and response message for debugging
                Log.e("Server Response", "Code: " + connection.getResponseCode() + ", Message: " + connection.getResponseMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
