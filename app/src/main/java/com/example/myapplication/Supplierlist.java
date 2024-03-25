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

public class Supplierlist extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<SupplierModel> SupplierList = new ArrayList<>();
    private SupplierAdapter SupplierAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplierlist);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        SupplierAdapter = new SupplierAdapter(SupplierList);
        recyclerView.setAdapter(SupplierAdapter);

        // Fetch supplier data from the server and populate the supplierList
        fetchDataFromServer(); // Corrected function name

        FloatingActionButton addSupplierButton = findViewById(R.id.addSupplierButton);
        addSupplierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the activity to add a new customer
                Intent intent = new Intent(Supplierlist.this, addsupplier.class);
                startActivity(intent);
            }
        });
    }

    private void fetchDataFromServer() {
        new FetchSupplierTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    private class FetchSupplierTask extends AsyncTask<Void, Void, List<SupplierModel>>{
        @Override
        protected List<SupplierModel> doInBackground(Void... voids) {
            List<SupplierModel> SupplierList = new ArrayList<>();

            try {
                URL url = new URL("http://172.30.0.1:8084/trial/Supplier");
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
                        JSONObject supplierJson = jsonArray.getJSONObject(i);
                        SupplierModel supplier = new SupplierModel(supplierJson);
                        SupplierList.add(supplier);
                    }
                } else {
                    throw new RuntimeException("HTTP response code: " + connection.getResponseCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return SupplierList;
        }

        @Override
        protected void onPostExecute(List<SupplierModel> SupplierList) {
            super.onPostExecute(SupplierList);
            SupplierAdapter.setSupplier(SupplierList);
        }
    }
}
