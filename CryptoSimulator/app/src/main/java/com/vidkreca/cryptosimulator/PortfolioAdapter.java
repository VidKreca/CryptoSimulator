package com.vidkreca.cryptosimulator;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vidkreca.data.Cryptocurrency;
import com.vidkreca.data.PortfolioItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.ViewHolder> {

    private App app;
    private PortfolioAdapter.OnItemClickListener listener;

    public PortfolioAdapter(App app,  PortfolioAdapter.OnItemClickListener listener) {
        this.app = app;
        this.listener = listener;
    }


    // Create view object from layout file
    @NonNull
    @Override
    public PortfolioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get context from parent
        Context context = parent.getContext();

        // Create an LayoutInflater object from parent context
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.portfolio_item, parent, false);

        PortfolioAdapter.ViewHolder viewHolder = new PortfolioAdapter.ViewHolder(view);

        return viewHolder;
    }

    // Bind Component object to UI
    @Override
    public void onBindViewHolder(@NonNull PortfolioAdapter.ViewHolder holder, int position) {

        PortfolioItem tmp = app.GetUser().GetPortfolio()[position];

        // Set portfolio_item views values
        if (tmp != null) {

            holder.symbol.setText(tmp.crypto_symbol);
            holder.amount.setText(String.format("%.10f", tmp.crypto_amount));
            holder.worth.setText(String.format("%.2f€", tmp.fiat_worth));

            double percentageChange = tmp.GetPercentageChange();
            holder.percentage.setText(String.format("%.2f%%", percentageChange));
            if (percentageChange < 100) {
                holder.percentage.setTextColor(Color.RED);
            } else {
                holder.percentage.setTextColor(Color.GREEN);
            }

            // Load icon image into ImageView using Picasso
            String imageUrl = API.url + "/img/" + tmp.crypto_symbol.toLowerCase() + ".png";
            Picasso.with(holder.icon.getContext()).load(imageUrl)
                    .centerCrop()
                    .fit()
                    .into(holder.icon);

        } else {
            Toast.makeText(app.getApplicationContext(), "null"+position, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        if (app.GetUser().GetPortfolio() == null)
            return 0;
        return app.GetUser().GetPortfolio().length;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView symbol;
        public TextView amount;
        public TextView worth;
        public TextView percentage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Get UI elements
            icon = itemView.findViewById(R.id.crypto_icon);
            symbol = itemView.findViewById(R.id.crypto_symbol);
            amount = itemView.findViewById(R.id.crypto_amount);
            worth = itemView.findViewById(R.id.crypto_worth);
            percentage = itemView.findViewById(R.id.crypto_percentage);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemView, position);
                    }
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemLongClick(itemView, position);
                    }
                }
                return true;
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
        void onItemLongClick(View itemView, int position);
    }
}
