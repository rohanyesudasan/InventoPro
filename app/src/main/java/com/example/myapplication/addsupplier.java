package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class addsupplier extends AppCompatActivity {

    private EditText supname;
    private EditText mobilecnt;
    private EditText email;
    private EditText address;
    private EditText gst;

    private MaterialButton reset;
    private  MaterialButton save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsupplier);
        getSupportActionBar().hide();

        supname = findViewById(R.id.editTextaddsupname);
        mobilecnt = findViewById(R.id.editTextaddphno);
        email = findViewById(R.id.editTextaddsupemail);
        address =findViewById(R.id.editTextaddsupaddress);


        gst =findViewById(R.id.editTextaddsupgst);
        save = findViewById(R.id.btnsave);


        save.setOnClickListener(v -> saveButtonClicked());




    }
    private void saveButtonClicked() {
        String Name = supname.getText().toString();
        String Phone_Number = mobilecnt.getText().toString();
        String Email = email.getText().toString();
        String Address = address.getText().toString();
        String Gstin_Number = gst.getText().toString();

        if (Name.isEmpty() || Phone_Number.isEmpty()  || Email.isEmpty() || Address.isEmpty() || Gstin_Number.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isAddSupplierSuccessful = authenticate(Name, Phone_Number,Email,Address,Gstin_Number);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isAddSupplierSuccessful) {
                            Intent i = new Intent(getApplicationContext(), product_list.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(addsupplier.this, "Error while inserting data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();

    }
    private boolean authenticate(String Name, String Phone_Number, String Email, String Address,String Gstin_Number) {
        try {
            URL url = new URL("http://172.30.0.1:8084/trial/Supplier");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);

            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Name",Name);
            jsonObject.put("Phone_Number", Phone_Number);
            jsonObject.put("Email", Email);
            jsonObject.put("Address", Address);
            jsonObject.put("Gstin_Number", Gstin_Number);

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