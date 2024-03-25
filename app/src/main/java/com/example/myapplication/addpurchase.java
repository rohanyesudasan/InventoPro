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

public class addpurchase extends navDrawerActivity {

    private EditText invnum;
    private EditText quant;
    private EditText costprice;
    private EditText amt;
    private AutoCompleteTextView productSpinner;
    private AutoCompleteTextView supplierSpinner;
    private MaterialButton save;
    private MaterialButton reset;

    private List<String> supplierNames = new ArrayList<>();
    private List<String> productNames = new ArrayList<>();

    // Maps to store Supplier and Product names and IDs
    private Map<String, String> supplierNameToIdMap = new HashMap<>();
    private Map<String, String> productNameToIdMap = new HashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpurchase);
        setupNavDrawer(findViewById(R.id.drawerLayout));

        getSupportActionBar().hide();

        invnum = findViewById(R.id.invnum);
        amt = findViewById(R.id.amt);
        quant = findViewById(R.id.quant);
        costprice = findViewById(R.id.costprice);
        productSpinner = findViewById(R.id.productSpinner);
        supplierSpinner = findViewById(R.id.supplierSpinner);


        save = findViewById(R.id.save);
        save.setOnClickListener(v -> saveButtonClicked());

        fetchSupplierData(); // Fetch and populate supplier data
        fetchProductData(); // Fetch and populate product data
    }

    private void populateAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView, List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, data);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
    }
    private void fetchSupplierData() {
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
                                    JSONObject supplierObject = jsonArray.getJSONObject(i);
                                    String supplierId = supplierObject.getString("ID");
                                    String supplierName = supplierObject.getString("Name");

                                    supplierNameToIdMap.put(supplierName, supplierId);
                                    supplierNames.add(supplierName);
                                }
                                populateAutoCompleteTextView(supplierSpinner, supplierNames);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        showToastMessage("Error fetching supplier data");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToastMessage("Network error");
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

                                populateAutoCompleteTextView(productSpinner, productNames);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(addpurchase.this, "Error fetching product data", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(addpurchase.this, "Network error", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }).start();
    }

    private void saveButtonClicked() {
        String Invoice_No = invnum.getText().toString();
        String Quantity = quant.getText().toString();
        String Amount = amt.getText().toString();
        String Cost_Price = costprice.getText().toString();
        String Supplier_Name = supplierSpinner.getText().toString();
        String Product_Name = productSpinner.getText().toString();

        if (Invoice_No.isEmpty() || Amount.isEmpty() || Quantity.isEmpty() || Cost_Price.isEmpty() || Supplier_Name.isEmpty() || Product_Name.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        String Supplier_ID = supplierNameToIdMap.get(Supplier_Name);
        String Product_ID = productNameToIdMap.get(Product_Name);

        if (Supplier_ID == null) {
            showToastMessage("Invalid Supplier Name");
            return;
        }

        if (Product_ID == null) {
            showToastMessage("Invalid Product Name");
            return;
        }


        // Get the IDs for selected Supplier and Product from the maps
        Log.d("addPurchase", "Attempting to post data");

        // Make a POST request to add the purchase data
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isAddPurchaseSuccessful = authenticate(Invoice_No, Cost_Price, Quantity, Amount, Supplier_ID, Product_ID);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isAddPurchaseSuccessful) {
                            Intent i = new Intent(getApplicationContext(), purchase_list.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(addpurchase.this, "Error while inserting data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
    private void showToastMessage(String message) {
        runOnUiThread(() -> {
            Toast.makeText(addpurchase.this, message, Toast.LENGTH_SHORT).show();
        });
    }
    private boolean authenticate(String Invoice_No, String Cost_Price, String Quantity, String Amount, String Supplier_ID, String Product_ID) {
        try {
            Log.d("addPurchase", "Attempting to post data");
            URL url = new URL("http://172.30.0.1:8084/trial/purchase");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Invoice_No", Invoice_No);
            jsonObject.put("Cost_Price", Cost_Price);
            jsonObject.put("Product_ID", Product_ID);
            jsonObject.put("Supplier_ID", Supplier_ID);
            jsonObject.put("Quantity", Quantity);
            jsonObject.put("Amount", Amount);

            String jsonInputString = jsonObject.toString();

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Handle the response if needed
                Log.d("addPurchase", "Data posted successfully");
                return true;
            } else {
                Log.e("addPurchase", "Error while posting data: HTTP response code " + responseCode);
                runOnUiThread(() -> {
                    Toast.makeText(addpurchase.this, "Error while posting data", Toast.LENGTH_SHORT).show();
                });
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("addPurchase", "Error while posting data: " + e.getMessage());
            runOnUiThread(() -> {
                Toast.makeText(addpurchase.this, "Network error", Toast.LENGTH_SHORT).show();
            });
            return false;
        }
    }
}
