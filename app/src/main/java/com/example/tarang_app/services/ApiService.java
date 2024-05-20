package com.example.tarang_app.services;

import com.example.tarang_app.models.Reservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/api/reservation")
    Call<List<Reservation>> loadReservationList();
}
