package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class product_list extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<ProductModel> productList = new ArrayList<>();
    private ProductAdapter productAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        // Fetch product data from the server and populate the productList
        fetchDataFromServer(); // Corrected function name

        FloatingActionButton addProductButton = findViewById(R.id.addProductButton);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the activity to add a new product
                Intent intent = new Intent(product_list.this, addproduct.class);
                startActivity(intent);
            }
        });
    }

    private void fetchDataFromServer() {
        new FetchProductsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    private class FetchProductsTask extends AsyncTask<Void, Void, List<ProductModel>> {
        @Override
        protected List<ProductModel> doInBackground(Void... voids) {
            List<ProductModel> productList = new ArrayList<>();

            try {
                URL url = new URL("http://172.30.0.1:8084/trial/product");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }

                    JSONArray jsonArray = new JSONArray(responseBuilder.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject productJson = jsonArray.getJSONObject(i);
                        ProductModel product = new ProductModel(productJson);
                        productList.add(product);
                    }
                } else {
                    throw new RuntimeException("HTTP response code: " + connection.getResponseCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return productList;
        }

        @Override
        protected void onPostExecute(List<ProductModel> productList) {
            super.onPostExecute(productList);
            productAdapter.setProducts(productList);
        }
    }




}