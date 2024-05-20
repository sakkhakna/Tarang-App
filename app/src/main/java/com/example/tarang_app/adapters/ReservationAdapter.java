package com.example.tarang_app.adapters;

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
    private OnClickListener onClickListener;
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
        holder.bind(reservation);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, reservation);
                }
            }
        });
    }

    public class ReservationViewHolder extends RecyclerView.ViewHolder{
        private final ViewHolderItemBinding binding;
        public ReservationViewHolder(ViewHolderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Reservation reservation){
            Picasso.get().load(reservation.getImageUrl()).into(binding.venueImage);
            this.binding.venueName.setText(reservation.getVenueName());
            this.binding.sportType.setText(reservation.getSportType());
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public interface OnClickListener {
        void onClick(int position, Reservation reservation);
    }
}
