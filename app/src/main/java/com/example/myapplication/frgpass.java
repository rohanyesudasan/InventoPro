package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class frgpass extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private MaterialButton requestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frgpass);

        phoneNumberEditText = findViewById(R.id.phonenumber);
        requestButton = findViewById(R.id.btnreq);

        requestButton.setOnClickListener(v -> requestButtonClicked());
    }

    private void requestButtonClicked() {
        String phoneNumber = phoneNumberEditText.getText().toString();

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            boolean isRequestSuccessful = sendPhoneNumberToServer(phoneNumber);

            runOnUiThread(() -> {
                if (isRequestSuccessful) {
                    Intent intent = new Intent(frgpass.this, resetpswd.class);
                    intent.putExtra("phoneNumber", phoneNumber); // Pass the phone number to the next activity
                    startActivity(intent);
                } else {
                    Toast.makeText(frgpass.this, "Failed to send the request. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private boolean sendPhoneNumberToServer(String phoneNumber) {
        try {
            URL url = new URL("http://172.30.0.1:8084/trial/forgotpassword");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phoneNumber", phoneNumber);
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

            String jsonInputString = jsonObject.toString();

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    String jsonResponseString = reader.readLine();
                    JSONObject responseJson = new JSONObject(jsonResponseString);
                    return responseJson.getBoolean("status");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}