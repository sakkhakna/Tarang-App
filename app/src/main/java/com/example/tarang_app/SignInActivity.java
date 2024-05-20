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

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

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

        AuthRequest authRequest = new AuthRequest(phoneNumber, password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.tarang.site/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<AuthResponse> call = apiService.login(authRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    String accessToken = authResponse.getAccessToken();
                    Toast.makeText(SignInActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("accessToken", accessToken);
                    editor.apply();

                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Close the sign-in activity
                } else {
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
        public String phoneNumber;
        public String password;

        public AuthRequest(String phoneNumber, String password) {
            this.phoneNumber = phoneNumber;
            this.password = password;
        }
    }

    public static class AuthResponse {
        public String token;

        public String getAccessToken() {
            return token;
        }
    }

    public interface ApiService {
        @POST("/api/login")
        Call<AuthResponse> login(@Body AuthRequest authRequest);
    }
}
