package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText username;
    private EditText email;
    private EditText password;
    private  EditText mobilecnt;

    private  TextView backtologin;
    private AutoCompleteTextView type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mobilecnt = findViewById(R.id.contact);
        backtologin = findViewById(R.id.backtologin);
        type = findViewById(R.id.type);
        String[] suggestions = {"Manager", "Staff"}; // Replace with your suggestions
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        type.setAdapter(adapter);
        type.setThreshold(1);
        MaterialButton signupbtn = findViewById(R.id.btnsignup);

        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, login.class);
                startActivity(i);
                finish();
            }
        });
        signupbtn.setOnClickListener(v -> signupButtonClicked());
    }

    private void signupButtonClicked() {
        String Name = username.getText().toString();
        String Password = password.getText().toString();
        String Phone_Number = mobilecnt.getText().toString();
        String Email = email.getText().toString();
        String Type = type.getText().toString();

        Log.d("Debug", "Name: " + Name);
        Log.d("Debug", "Password: " + Password);
        Log.d("Debug", "Phone_Number: " + Phone_Number);
        Log.d("Debug", "Email: " + Email);
        Log.d("Debug", "Type: " + Type);

        if (Name.isEmpty() || Password.isEmpty() || Phone_Number.isEmpty() || Email.isEmpty() || Type.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSignupSuccessful = authenticate(Name, Password, Phone_Number, Email, Type);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSignupSuccessful) {
//                            Toast.makeText(MainActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), login.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(MainActivity.this, "Error while inserting data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();

    }
    private boolean authenticate(String Name, String Password, String Phone_Number, String Email, String Type) {
        try {
            URL url = new URL("http://172.30.0.1:8084/trial/signup");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Name", Name);
            jsonObject.put("Email", Email);
            jsonObject.put("Password", Password);
            jsonObject.put("Phone_Number", Phone_Number);
            jsonObject.put("Type", Type);
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