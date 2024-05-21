package com.example.tarang_app.services;

import com.example.tarang_app.models.Reservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiService {
    @GET("/api/reservation")
    Call<Reservation> loadReservationList(@Header("Authorization") String authorization);
}
