package com.example.project;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.ViewHolder> {
    private List<DataModel> transferDataList;

    public TransferAdapter(List<DataModel> transferDataList) {
        this.transferDataList = transferDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel data = transferDataList.get(position);
        // Set the data to the views in the ViewHolder
        holder.titleTextView.setText(data.getDataTitle());
        holder.yearTextView.setText(data.getDataYear());
        holder.priceTextView.setText(String.valueOf(data.getDataPrice()));

        // Use a library like Picasso to load the image into the ImageView
        Picasso.get().load(data.getDataImage()).into(holder.imageView);

        String bidder = data.getHighestBidder();
        if (bidder != null) {
            // Set a special marker or highlight for the highest bidder
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));

            // Set the highest bidder name
            holder.highestBidderTextView.setVisibility(View.VISIBLE);
            holder.highestBidderTextView.setText("Highest Bidder: " + bidder);
        } else {
            // Reset the background color for other items
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));

            // Hide the highest bidder name for other items
            holder.highestBidderTextView.setVisibility(View.VISIBLE);
            holder.highestBidderTextView.setText("Bid Failed: No Bidder Found !");
        }
    }

    @Override
    public int getItemCount() {
        return transferDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView yearTextView;
        private TextView priceTextView;
        private ImageView imageView;
        private TextView highestBidderTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            imageView = itemView.findViewById(R.id.imageView);
            highestBidderTextView = itemView.findViewById(R.id.highestBidderTextView);
        }
    }
}
