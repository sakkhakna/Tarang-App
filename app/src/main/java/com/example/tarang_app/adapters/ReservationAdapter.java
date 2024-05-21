package com.example.tarang_app.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarang_app.ReservationInformation;
import com.example.tarang_app.databinding.ViewHolderItemBinding;
import com.example.tarang_app.models.Reservation;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ReservationAdapter extends ListAdapter<Reservation, ReservationAdapter.ReservationViewHolder> {
    //private OnClickListener onClickListener;
    public ReservationAdapter() {
        super(new DiffUtil.ItemCallback<Reservation>() {
            @Override
            public boolean areItemsTheSame(@NonNull Reservation oldItem, @NonNull Reservation newItem) {
                return newItem == oldItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Reservation oldItem, @NonNull Reservation newItem) {
                return newItem.getId() == oldItem.getId();
            }
        });
    }

    public ReservationAdapter(AsyncDifferConfig<Reservation> config) {
        super(config);
    }
    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolderItemBinding binding = ViewHolderItemBinding.inflate(inflater, parent, false);
        return new ReservationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = getItem(position);
        Log.d("ReservationAdapter", "Binding reservation at position " + position + ": " + reservation);
        holder.bind(reservation);
    }

    public class ReservationViewHolder extends RecyclerView.ViewHolder{
        private final ViewHolderItemBinding binding;
        public ReservationViewHolder(ViewHolderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Reservation reservation){
//            Picasso.get().load(reservation.getMyvenue().getPhoto()).into(binding.venueImage);
            this.binding.venueName.setText(reservation.getVenue_name());
            this.binding.date.setText(formatDate(reservation.getDate()));
            this.binding.startTime.setText(formatTime(reservation.getStart_time()));
            this.binding.endTime.setText(formatTime(reservation.getEnd_time()));


            binding.cardId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ReservationInformation.class);
                    intent.putExtra("venue_name", reservation.getVenue_name());
                    intent.putExtra("date", reservation.getDate());
                    intent.putExtra("start_time", reservation.getStart_time());
                    intent.putExtra("end_time", reservation.getEnd_time());
                    v.getContext().startActivity(intent);
                }
            });
            };
        }

        private String formatDate(String dateString) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd MMM", Locale.getDefault());
            try {
                Date date = inputFormat.parse(dateString);
                if (date != null) {
                    return outputFormat.format(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dateString; // return the original string if parsing fails
        }
        private String formatTime(String timeString) {
            // Input format for the time string
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            // Output format to convert to AM/PM
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            try {
                Date date = inputFormat.parse(timeString);
                if (date != null) {
                    String formattedTime = outputFormat.format(date);
                    return formattedTime;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return timeString;
        }
}
