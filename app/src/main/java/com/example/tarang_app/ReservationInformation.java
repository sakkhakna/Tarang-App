package com.example.tarang_app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReservationInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_information);

        ImageView imageView = findViewById(R.id.backIcon);

        String venueName = getIntent().getStringExtra("venue_name");
        String date = getIntent().getStringExtra("date");
        String startTime = getIntent().getStringExtra("start_time");
        String endTime = getIntent().getStringExtra("end_time");

        TextView venueNameTextView = findViewById(R.id.venue); // Make sure this ID matches your layout
        TextView dateTextView = findViewById(R.id.date); // Make sure this ID matches your layout
        TextView startTimeTextView = findViewById(R.id.start_time); // Make sure this ID matches your layout
        TextView endTimeTextView = findViewById(R.id.end_time); // Make sure this ID matches your layout

        venueNameTextView.setText(venueName);
        dateTextView.setText(date);
        startTimeTextView.setText(startTime);
        endTimeTextView.setText(endTime);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                goBack();
            }
        });

    }

//    private void goBack() {
//        BookingFragment bookingFragment = new BookingFragment();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragmentContainer, bookingFragment)
//                .addToBackStack(null)
//                .commit();
//        finish();
//    }
}