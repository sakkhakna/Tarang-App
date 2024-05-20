package com.example.tarang_app.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarang_app.databinding.ViewHolderItemBinding;
import com.example.tarang_app.models.Reservation;
import com.squareup.picasso.Picasso;

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
        ViewHolderItemBinding binding = ViewHolderItemBinding.inflate(inflater);
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
            Picasso.get().load(reservation.getMyvenue().getPhoto()).into(binding.venueImage);
            this.binding.venueName.setText(reservation.getMyvenue().getName());
        }
    }
}
