package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends AppCompatActivity {

    EditText email, password;
    Button btnLogin;
    TextView txtInfo, creatacn;

    TextView forgotpass;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        txtInfo = findViewById(R.id.txtInfo);
        creatacn = findViewById(R.id.creatacn);
        forgotpass = findViewById(R.id.forgotpass);
        sharedPreferences = getSharedPreferences("shelfiq", MODE_PRIVATE);

        // Check if shelfiq exists, if yes, redirect to the dashboard
//        if (sharedPreferences.contains("email") && sharedPreferences.contains("password") && sharedPreferences.contains("username")) {
//            Intent i = new Intent(this, dashboard.class);
//            startActivity(i);
//            finish();  // Finish the current activity to prevent going back to the login screen
//        }
        if (sharedPreferences.contains("email") && sharedPreferences.contains("password") && sharedPreferences.contains("username")) {
            String userType = sharedPreferences.getString("userType", "");
            Intent i;
            if ("Admin".equals(userType)) {
                i = new Intent(this, dashboard.class);
            } else if ("Manager".equals(userType)) {
                i = new Intent(this, managerdashboard.class);
            }
                else if ("Staff".equals(userType)) {
                    i = new Intent(this, staffdashboard.class);
            }else {
//                // If user type is not "Admin" or "Manager," you can handle it as needed
//                // For example, you can show a message or redirect to a default activity
                i = new Intent(this, MainActivity.class);
            }
            startActivity(i);
            finish();  // Finish the current activity to prevent going back to the login screen
        }

        creatacn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this, frgpass.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLoginClicked();
            }
        });
    }

    private void btnLoginClicked() {
        String Email = email.getText().toString();
        String Password = password.getText().toString();

        if (Email.isEmpty() || Password.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new AuthenticationTask().execute(Email, Password);
    }

    private class AuthenticationTask extends AsyncTask<String, Void, String> {
        private String email;
        private String password;

        @Override
        protected String doInBackground(String... params) {
            email = params[0];
            password = params[1];
            return authenticate(email, password);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject responseJson = new JSONObject(result);

                    // Retrieve the user type
                    String userType = responseJson.getString("type");
                    Log.d("UserType", "User Type: " + userType);

                    if ("Admin".equals(userType)) {
                        Intent i = new Intent(getApplicationContext(), dashboard.class);
                        startActivity(i);
                    } else if ("Manager".equals(userType)) {
                        Intent i = new Intent(getApplicationContext(), managerdashboard.class);
                        startActivity(i);
                    } else if ("Staff".equals(userType)) {
                        Intent i = new Intent(getApplicationContext(), staffdashboard.class);
                        startActivity(i);
                    }

                    // Display a toast message with the logged-in username
                    String username = responseJson.getString("username");
                    Toast.makeText(login.this, "Login Successful. Welcome, " + username, Toast.LENGTH_SHORT).show();

                    // Store email, password, username, and user type in SharedPreferences
                    saveLoginInfo(email, password, username, userType);

                    finish();  // Finish the current activity to prevent going back to the login screen
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(login.this, "Login Failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
            }
        }
    }

        private void saveLoginInfo(String Email, String Password, String Username, String UserType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", Email);
        editor.putString("password", Password);
        editor.putString("username", Username);
        editor.putString("userType", UserType);
        editor.apply();
    }

    private String authenticate(String Email, String Password) {
        try {
            URL url = new URL("http://172.30.0.1:8084/trial/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Email", Email);
            jsonObject.put("Password", Password);

            String jsonInputString = jsonObject.toString();

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Check if the request was successful
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    String jsonResponseString = reader.readLine();
                    return jsonResponseString;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
