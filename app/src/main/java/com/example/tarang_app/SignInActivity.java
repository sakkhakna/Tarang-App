package com.example.tarang_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class SignInActivity extends AppCompatActivity {

    private Button signup_btn;
    private Button signIn_btn;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        signIn_btn = findViewById(R.id.signIn_btn);
        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });

        signup_btn = findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void authenticateUser() {
        EditText phoneNumber_input = findViewById(R.id.phone_number);
        EditText password_input = findViewById(R.id.password);

        String phoneNumber = phoneNumber_input.getText().toString();
        String password = password_input.getText().toString();

        if (phoneNumber.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter phone number and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log phone number and password as a JSON object
        String logMessage = new Gson().toJson(new AuthRequest(phoneNumber, password));
        Log.d("SignInActivity", logMessage);

        AuthRequest authRequest = new AuthRequest(phoneNumber, password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.tarang.site")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<AuthResponse> call = apiService.login(authRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    Log.d("SignInActivity", "Response Code: " + response.code());
                    if (authResponse != null) {
                        String accessToken = authResponse.getToken();
                        Log.d("SignInActivity", "Response Body: " + new Gson().toJson(authResponse));
                        if (accessToken != null) {
                            // Token is not null, proceed to store it
                            Toast.makeText(SignInActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                            Log.d("SignInActivity", "Stored token: " + accessToken);

                            // Store token in SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", accessToken);
                            editor.apply();

                            // Retrieve stored token
                            String storedToken = sharedPreferences.getString("token", null);
                            Log.d("SignInActivity", "Retrieved token: " + storedToken);

                            // Proceed to the next activity
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Token is null
                            Log.e("SignInActivity", "Access token is null");
                            Toast.makeText(SignInActivity.this, "Token is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Response body is null
                        Log.e("SignInActivity", "Response body is null");
                        Toast.makeText(SignInActivity.this, "Response body is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Response is not successful
                    Log.e("SignInActivity", "Response is not successful. Code: " + response.code());
                    Toast.makeText(SignInActivity.this, "Invalid credentials or server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable throwable) {
                Log.e("SignInActivity", "onFailure: ", throwable);
                Toast.makeText(SignInActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class AuthRequest {
        public String phone;
        public String password;

        public AuthRequest(String phoneNumber, String password) {
            this.phone = phoneNumber;
            this.password = password;
        }
    }

    public static class AuthResponse {
        public String token;

        public String getToken() {
            return token;
        }
    }

    public interface ApiService {
        @POST("/api/login")
        Call<AuthResponse> login(@Body AuthRequest authRequest);
    }
}
