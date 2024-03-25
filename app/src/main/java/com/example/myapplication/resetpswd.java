package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class resetpswd extends AppCompatActivity {

    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button resetButton;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpswd);

        newPasswordEditText = findViewById(R.id.editTextNewPassword); // Replace with the actual ID
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword); // Replace with the actual ID
        resetButton = findViewById(R.id.buttonResetPassword);

        // Retrieve the phoneNumber from the previous activity
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        resetButton.setOnClickListener(v -> resetButtonClicked());
    }

    private void resetButtonClicked() {
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please enter both new password and confirm password.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New password and confirm password do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        // If passwords match, send the new password to the server
        new Thread(() -> {
            boolean isResetSuccessful = sendNewPasswordToServer(newPassword);

            runOnUiThread(() -> {
                if (isResetSuccessful) {
                    Toast.makeText(resetpswd.this, "Password reset successful.", Toast.LENGTH_SHORT).show();
                    // You can navigate to another activity or perform further actions here.
                } else {
                    Toast.makeText(resetpswd.this, "Failed to reset password. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private boolean sendNewPasswordToServer(String newPassword) {
        try {
            // Replace with the actual URL of your server endpoint
            URL url = new URL("http://172.30.0.1:8084/trial/forgotpassword");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");











             

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phoneNumber", phoneNumber); // Send the phoneNumber
                jsonObject.put("newPassword", newPassword);
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

            String jsonInputString = jsonObject.toString();

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}