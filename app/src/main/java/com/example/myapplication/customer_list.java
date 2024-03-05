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

public class customer_list extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<CustomerModel> customerList = new ArrayList<>();
    private CustomerAdapter customerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        customerAdapter = new CustomerAdapter(customerList);
        recyclerView.setAdapter(customerAdapter);

        // Fetch customer data from the server and populate the customerList
        fetchDataFromServer(); // Corrected function name

        FloatingActionButton addCustomerButton = findViewById(R.id.addCustomerButton);
        addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the activity to add a new customer
                Intent intent = new Intent(customer_list.this, addcustomer.class);
                startActivity(intent);
            }
        });
    }

    private void fetchDataFromServer() {
        new FetchCustomerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    private class FetchCustomerTask extends AsyncTask<Void, Void, List<CustomerModel>>{
        @Override
        protected List<CustomerModel> doInBackground(Void... voids) {
            List<CustomerModel> customerList = new ArrayList<>();

            try {
                URL url = new URL("http://172.30.0.1:8084/trial/Customer");
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
                        JSONObject customerJson = jsonArray.getJSONObject(i);
                        CustomerModel customer = new CustomerModel(customerJson);
                        customerList.add(customer);
                    }
                } else {
                    throw new RuntimeException("HTTP response code: " + connection.getResponseCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return customerList;
        }

        @Override
        protected void onPostExecute(List<CustomerModel> customerList) {
            super.onPostExecute(customerList);
            customerAdapter.setCustomer(customerList);
        }
    }
}