package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class purchase_list extends AppCompatActivity {


    private RecyclerView recyclerView;
    private List<PurchaseModel> purchaseList = new ArrayList<>();
    private PurchaseAdapter purchaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_list);


        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        purchaseAdapter = new PurchaseAdapter(purchaseList);
        recyclerView.setAdapter(purchaseAdapter);

        // Fetch product data from the server and populate the productList
        fetchDataFromServer(); // Corrected function name

        FloatingActionButton addPurchaseButton = findViewById(R.id.addpurchbtn);
        addPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the activity to add a new product
                Intent intent = new Intent(purchase_list.this, addpurchase.class);
                startActivity(intent);
            }
        });
    }

    private void fetchDataFromServer() {
        new FetchPurchasesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    private class FetchPurchasesTask extends AsyncTask<Void, Void, List<PurchaseModel>> {
        @Override
        protected List<PurchaseModel> doInBackground(Void... voids) {
            List<PurchaseModel> purchaseList = new ArrayList<>();

            try {
                URL url = new URL("http://172.30.0.1:8084/trial/purchase");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    Log.d("PurchaseList", "Server Response: " + responseBuilder.toString());

                    JSONArray jsonArray = new JSONArray(responseBuilder.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject purchaseJson = jsonArray.getJSONObject(i);
                        PurchaseModel purchase = new PurchaseModel(purchaseJson);
                        purchaseList.add(purchase);
                    }
                } else {
                    throw new RuntimeException("HTTP response code: " + connection.getResponseCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return purchaseList;
        }

        @Override
        protected void onPostExecute(List<PurchaseModel> purchaseList) {
            super.onPostExecute(purchaseList);
            purchaseAdapter.setPurchase(purchaseList);
        }
    }




}