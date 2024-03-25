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

public class sales_list extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<SalesModel> salesList = new ArrayList<>();
    private SalesAdapter salesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_list);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        salesAdapter = new SalesAdapter(salesList);
        recyclerView.setAdapter(salesAdapter);

        // Fetch product data from the server and populate the productList
        fetchDataFromServer(); // Corrected function name

        FloatingActionButton addSalesButton = findViewById(R.id.addsalesbtn);
        addSalesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the activity to add a new product
                Intent intent = new Intent(sales_list.this, addsales.class);
                startActivity(intent);
            }
        });
    }

    private void fetchDataFromServer() {
        new FetchSalesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    private class FetchSalesTask extends AsyncTask<Void, Void, List<SalesModel>> {
        @Override
        protected List<SalesModel> doInBackground(Void... voids) {
            List<SalesModel> salesList = new ArrayList<>();

            try {
                URL url = new URL("http://172.30.0.1:8084/trial/sales");
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
                        JSONObject salesJson = jsonArray.getJSONObject(i);
                        SalesModel sales = new SalesModel(salesJson);
                        salesList.add(sales);
                    }
                } else {
                    throw new RuntimeException("HTTP response code: " + connection.getResponseCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return salesList;
        }

        @Override
        protected void onPostExecute(List<SalesModel> salesList) {
            super.onPostExecute(salesList);
            salesAdapter.setSales(salesList);
        }
    }




}