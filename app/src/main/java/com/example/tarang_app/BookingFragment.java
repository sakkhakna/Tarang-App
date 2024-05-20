package com.example.tarang_app;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tarang_app.adapters.ReservationAdapter;
import com.example.tarang_app.databinding.FragmentBookingBinding;
import com.example.tarang_app.models.Reservation;
import com.example.tarang_app.services.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookingFragment extends Fragment {
    String accessToken;

    private Button backHome;
    private FragmentBookingBinding binding;

    private ReservationAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentBookingBinding.inflate(inflater, container, false);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user", MODE_PRIVATE);
        String retrievedAccessToken = sharedPreferences.getString("accessToken", null);

        if(retrievedAccessToken != null && !retrievedAccessToken.isEmpty()){
            Log.d("accessToken", retrievedAccessToken);
        }else{
            Log.d("accessToken", "No access token found");
        }

        accessToken = retrievedAccessToken;

        if (accessToken != null) {
            loadReservationList();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        adapter = new ReservationAdapter();
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void loadReservationList() {
        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("https://api.tarang.site")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = httpClient.create(ApiService.class);
        Call<List<Reservation>> task = apiService.loadReservationList("Bearer " + accessToken);
        task.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    List<Reservation> reservations = response.body();
                    Log.d("BookingFragment", "Reservations loaded: " + reservations);
                    adapter.submitList(reservations);
                } else {
                    Toast.makeText(getContext(), "Failed to load card list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable throwable) {
                Log.e("BookingFragment", "Error loading reservations", throwable);
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
